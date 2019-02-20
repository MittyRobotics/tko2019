package com.amhsrobotics.tko2019.subsystems;

import com.amhsrobotics.tko2019.logging.LogCapable;

public interface Subsystem extends LogCapable {
	default void init() {
	}

	default void initControls() {
	}


	default void enable() {
	}

	default void disable() {
	}
}
