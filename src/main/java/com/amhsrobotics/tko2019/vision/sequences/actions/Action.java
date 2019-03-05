package com.amhsrobotics.tko2019.vision.sequences.actions;

public interface Action {
	void doAction(boolean continuing);

	default boolean isContinuable() {
		return true;
	}
}
