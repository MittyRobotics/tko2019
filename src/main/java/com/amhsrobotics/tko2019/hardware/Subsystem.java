package com.amhsrobotics.tko2019.hardware;

import com.amhsrobotics.tko2019.controls.Controllable;

public interface Subsystem extends Initable, Controllable, Enableable {
	@Override
	default void enable() {
	}

	@Override
	default void disable() {
	}
}
