package com.amhsrobotics.tko2019.vision.sequences;

import com.amhsrobotics.tko2019.vision.sequences.actions.Action;
import com.amhsrobotics.tko2019.vision.sequences.actions.drive.MoveAction;
import com.amhsrobotics.tko2019.vision.sequences.actions.drive.TurnAction;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;

import java.util.concurrent.LinkedBlockingQueue;

public abstract class Sequence extends Thread {
	private final LinkedBlockingQueue<Action> sequence = new LinkedBlockingQueue<>();

	private volatile boolean active = false;
	private volatile boolean finished = false;

	private Action currentAction = null;
	private volatile boolean inProgress = false;

	protected Sequence(final Action... actions) {
		final double t1 = NetworkTableInstance.getDefault().getEntry("t1").getDouble(0);
		if (t1 != 0) {
			sequence.offer(new TurnAction(t1));
		}
		final double d1 = NetworkTableInstance.getDefault().getEntry("d1").getDouble(0);
		if (d1 != 0) {
			sequence.offer(new MoveAction(d1));
		}
		final double t2 = NetworkTableInstance.getDefault().getEntry("t2").getDouble(0);
		if (t2 != 0) {
			sequence.offer(new TurnAction(t2));
		}
		final double d2 = NetworkTableInstance.getDefault().getEntry("d2").getDouble(0);
		if (d2 != 0) {
			sequence.offer(new MoveAction(d2));
		}

		for (final Action action : actions) {
			sequence.offer(action);
		}
	}

	public boolean isFinished() {
		return finished;
	}

	public void cancel() {
		active = false;
	}

	@Override
	public void run() {
		SequencesManager.manual = false;
		active = true;
		while (active && !SequencesManager.manual) {
			while (!DriverStation.getInstance().isEnabled()) {
				try {
					Thread.sleep(100);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (SequencesManager.manual) {
				cancel();
				break;
			}

			if (!shouldContinue()) {
				break;
			}

			if (inProgress) {
				if (!currentAction.isContinuable()) {
					cancel();
					break;
				}
				currentAction.doAction(true);
				if (DriverStation.getInstance().isEnabled()) {
					inProgress = false;
				}
				continue;
			}

			currentAction = sequence.poll();
			if (currentAction == null) {
				finished = true;
				break;
			}

			inProgress = true;
			currentAction.doAction(false);
			if (DriverStation.getInstance().isEnabled()) {
				inProgress = false;
			}
		}
	}

	protected abstract boolean shouldContinue();
}
