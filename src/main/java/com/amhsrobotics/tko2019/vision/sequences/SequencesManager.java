package com.amhsrobotics.tko2019.vision.sequences;

public class SequencesManager {
	static volatile boolean manual = true;

	private Sequence currentSequence = null;

	public synchronized void startSequence(final Sequence sequence) {
		currentSequence = sequence;
		currentSequence.start();
	}

	public static void setManual() {
		manual = true;
	}

	public synchronized void continueSequence() {
		if (currentSequence != null && !currentSequence.isFinished()) {
			currentSequence.start();
		}
	}
}
