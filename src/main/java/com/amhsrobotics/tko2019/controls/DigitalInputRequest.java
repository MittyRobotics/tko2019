package com.amhsrobotics.tko2019.controls;

import edu.wpi.first.wpilibj.GenericHID;

public interface DigitalInputRequest {
	boolean requestDigital(final GenericHID genericHID);
}
