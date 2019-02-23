package com.amhsrobotics.tko2019.sequences;

public interface Action {
	default void prepare() {
	}

	void doAction(boolean continuing);

	default void cleanup() {
	}

	default boolean isContinuable() {
		return true;
	}

	default boolean canRun() {
		return true;
	}
}
