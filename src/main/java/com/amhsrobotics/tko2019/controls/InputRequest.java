package com.amhsrobotics.tko2019.controls;

import edu.wpi.first.wpilibj.GenericHID;

public interface InputRequest extends DigitalInputRequest, AnalogInputRequest {
	default double requestAnalog(final GenericHID genericHID) {
		return 0;
	}

	default boolean requestDigital(final GenericHID genericHID) {
		return false;
	}
}
