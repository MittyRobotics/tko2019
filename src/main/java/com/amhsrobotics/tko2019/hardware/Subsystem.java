package com.amhsrobotics.tko2019.hardware;

public interface Subsystem extends Enableable {
	@Override
	default void enable() {
	}

	@Override
	default void disable() {
	}
}
