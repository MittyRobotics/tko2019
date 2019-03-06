package com.amhsrobotics.tko2019.vision.sequences;

public final class SequencesManager {
	static volatile boolean manual = true;

	private Sequence currentSequence = null;

	public static void setManual() {
		manual = true;
	}

	public synchronized void startSequence(final Sequence sequence) {
		currentSequence = sequence;
		currentSequence.start();
	}

	public synchronized void continueSequence() {
		if (currentSequence != null && !currentSequence.isFinished()) {
			currentSequence.start();
		}
	}
}
