package com.amhsrobotics.tko2019.subsystems;

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
