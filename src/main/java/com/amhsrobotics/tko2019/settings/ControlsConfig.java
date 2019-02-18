package com.amhsrobotics.tko2019.settings;

import com.amhsrobotics.tko2019.controls.AnalogInput;
import com.amhsrobotics.tko2019.controls.DigitalInput;

public class ControlsConfig {
	public static final DigitalInput SWITCH_MODE = DigitalInput.Joystick11;
	public static final DigitalInput CONFIG_ENCODER = DigitalInput.Joystick10;


	// Drive

	public static final AnalogInput LEFT_WHEELS = AnalogInput.XboxLYJoystick;
	public static final AnalogInput RIGHT_WHEELS = AnalogInput.XboxRYJoystick;
	public static final DigitalInput REVERSE_DIRECTION = DigitalInput.XboxRJoystick;
	public static final DigitalInput GEAR_SWITCH = DigitalInput.XboxRBumper;


	//Cargo

	public static final AnalogInput SPIN_INTAKE = AnalogInput.JoystickY;
	public static final DigitalInput ANGLE_UP = DigitalInput.Joystick3;
	public static final DigitalInput ANGLE_DOWN = DigitalInput.Joystick2;


	//Hatch Panel

	public static final DigitalInput SLIDE_LEFT = DigitalInput.Joystick4;
	public static final DigitalInput SLIDE_RIGHT = DigitalInput.Joystick5;
	public static final DigitalInput RELEASE_HATCH = DigitalInput.Joystick2;
	public static final DigitalInput GRAB_HATCH = DigitalInput.JoystickTrigger;
	public static final AnalogInput JOYTICK_SLIDE = AnalogInput.JoystickX;


	//Climber

	public static final DigitalInput RELEASE_CLIMBER = DigitalInput.XboxA;
}
