package com.amhsrobotics.tko2019.controls.input;

import edu.wpi.first.wpilibj.GenericHID;

public interface AnalogInputRequest {
	double requestAnalog(final GenericHID genericHID);
}
