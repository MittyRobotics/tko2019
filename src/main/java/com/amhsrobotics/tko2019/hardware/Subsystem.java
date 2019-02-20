package com.amhsrobotics.tko2019.hardware;

public interface Subsystem {
	default void init() {
	}

	default void initControls() {
	}


	default void enable() {
	}

	default void disable() {
	}
}
