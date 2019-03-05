package com.amhsrobotics.tko2019.vision.sequences;

public class SequencesManager {
	static volatile boolean manual = true;

	private Sequence currentSequence = null;

	public void startSequence(final Sequence sequence) {
		currentSequence = sequence;
	}

	public static void setManual() {
		manual = true;
	}

	void continueSequence() {

	}
}
