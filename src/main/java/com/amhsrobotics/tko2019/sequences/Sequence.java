package com.amhsrobotics.tko2019.sequences;

import java.util.concurrent.LinkedBlockingQueue;

public abstract class Sequence extends Thread {
	protected final LinkedBlockingQueue<Action> sequence = new LinkedBlockingQueue<>();

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

	public void disable() {
		enabled = false;
	}

	@Override
	public void run() {
		enabled = true;
		while (enabled) {
			if (inProgress) {
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
			action.doAction(false);
			action.cleanup();
			inProgress = false;
		}
	}

	public boolean isFinished() {
		return finished;
	}
}
