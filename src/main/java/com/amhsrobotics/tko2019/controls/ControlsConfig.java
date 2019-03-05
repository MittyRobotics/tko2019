package com.amhsrobotics.tko2019.controls;

import com.amhsrobotics.tko2019.controls.input.AnalogInput;
import com.amhsrobotics.tko2019.controls.input.DigitalInput;

public class ControlsConfig {
	///////////////////////////////////////////////////////////////////////////
	// Drive
	///////////////////////////////////////////////////////////////////////////

	public static final DigitalInput REVERSE_DIRECTION = DigitalInput.XboxRJoystick;
	public static final DigitalInput GEAR_SWITCH = DigitalInput.XboxRBumper;

	public static final AnalogInput LEFT_WHEELS = AnalogInput.XboxLYJoystick;
	public static final AnalogInput RIGHT_WHEELS = AnalogInput.XboxRYJoystick;


	///////////////////////////////////////////////////////////////////////////
	// Hatch Panel
	///////////////////////////////////////////////////////////////////////////

	public static final DigitalInput SLIDE_LEFT = DigitalInput.Joystick4;
	public static final DigitalInput SLIDE_RIGHT = DigitalInput.Joystick5;
	public static final DigitalInput SLIDE_MIDDLE = DigitalInput.Joystick3;
	public static final DigitalInput RELEASE_HATCH = DigitalInput.Joystick2;
	public static final DigitalInput GRAB_HATCH = DigitalInput.JoystickTrigger;

	public static final AnalogInput JOYSTICK_SLIDE = AnalogInput.JoystickX;
	public static final AnalogInput PUSH_HATCH_MECHANISM = AnalogInput.JoystickY;


	///////////////////////////////////////////////////////////////////////////
	// Cargo
	///////////////////////////////////////////////////////////////////////////

	public static final DigitalInput SPIN_INTAKE = DigitalInput.JoystickTrigger;
	public static final DigitalInput SPIN_OUTTAKE = DigitalInput.Joystick2;
	public static final DigitalInput CARGO_HEIGHT = DigitalInput.Joystick5;
	public static final DigitalInput ROCKET_HEIGHT = DigitalInput.Joystick4;
	public static final DigitalInput GROUND_HEIGHT = DigitalInput.Joystick3;

	public static final AnalogInput MOVE_ANGLE = AnalogInput.JoystickY;


	///////////////////////////////////////////////////////////////////////////
	// Climber
	///////////////////////////////////////////////////////////////////////////

	public static final DigitalInput RELEASE_CLIMBER = DigitalInput.Joystick10;
}
