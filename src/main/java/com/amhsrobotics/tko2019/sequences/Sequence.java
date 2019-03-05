package com.amhsrobotics.tko2019.sequences;

import edu.wpi.first.wpilibj.DriverStation;

import java.util.concurrent.LinkedBlockingQueue;

public abstract class Sequence extends Thread {
	private final LinkedBlockingQueue<Action> sequence = new LinkedBlockingQueue<>();

	private volatile boolean enabled = false;
	private volatile boolean finished = false;

	private Action currentAction = null;
	private volatile boolean inProgress = false;

	protected Sequence(final Action... actions) {
		for (final Action action : actions) {
			sequence.offer(action);
		}
	}

	public synchronized void enable() throws InterruptedException {
		if (!enabled) {
			join();
			start();
		}
	}

	public void cancel() {
		enabled = false;
	}

	@Override
	public void run() {
		enabled = true;
		SequencesManager.manual = false;
		while (enabled) {
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
				return;
			}

			if (inProgress) {
				if (!currentAction.isContinuable()) {
					return;
				}
				currentAction.prepare();
				currentAction.doAction(true);
				currentAction.cleanup();
				inProgress = false;
				continue;
			}

			final Action action = sequence.poll();
			if (action == null) {
				finished = true;
				break;
			}

			currentAction = action;
			inProgress = true;
			action.prepare();
			currentAction.doAction(true);
			action.cleanup();
			inProgress = false;
		}
	}

	protected abstract boolean shouldContinue();

	public boolean isFinished() {
		return finished;
	}
}
