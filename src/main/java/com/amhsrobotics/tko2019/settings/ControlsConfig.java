package com.amhsrobotics.tko2019.settings;

import com.amhsrobotics.tko2019.controls.AnalogInput;
import com.amhsrobotics.tko2019.controls.DigitalInput;

public class ControlsConfig {
	// Drive

	public static final AnalogInput LEFT_WHEELS = AnalogInput.XboxLYJoystick;
	public static final AnalogInput RIGHT_WHEELS = AnalogInput.XboxRYJoystick;
	public static final DigitalInput REVERSE_DIRECTION = DigitalInput.XboxRBumper;

	public static final DigitalInput GEAR_SWITCH = DigitalInput.XboxLBumper;
}
