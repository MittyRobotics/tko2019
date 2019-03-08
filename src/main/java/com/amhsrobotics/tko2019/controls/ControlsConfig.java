package com.amhsrobotics.tko2019.controls;

import com.amhsrobotics.tko2019.controls.input.AnalogInput;
import com.amhsrobotics.tko2019.controls.input.DigitalInput;

final class ControlsConfig {
	///////////////////////////////////////////////////////////////////////////
	// Drive
	///////////////////////////////////////////////////////////////////////////

	static final DigitalInput REVERSE_DIRECTION = DigitalInput.XboxRJoystick;
	static final DigitalInput GEAR_SWITCH = DigitalInput.XboxRBumper;

	static final AnalogInput LEFT_WHEELS = AnalogInput.XboxLYJoystick;
	static final AnalogInput RIGHT_WHEELS = AnalogInput.XboxRYJoystick;


	///////////////////////////////////////////////////////////////////////////
	// Hatch Panel
	///////////////////////////////////////////////////////////////////////////

	static final DigitalInput SLIDE_LEFT = DigitalInput.Joystick4;
	static final DigitalInput SLIDE_RIGHT = DigitalInput.Joystick5;
	static final DigitalInput SLIDE_MIDDLE = DigitalInput.Joystick3;
	static final DigitalInput CHECK_GRAB_HATCH = DigitalInput.JoystickTrigger;
	//	static final DigitalInput CHECK_SLIDE = DigitalInput.Joystick2;
	static final DigitalInput MANUAL_GRAB_HATCH = DigitalInput.Joystick2;

	static final AnalogInput JOYSTICK_SLIDE = AnalogInput.JoystickX;
	static final AnalogInput PUSH_HATCH_MECHANISM = AnalogInput.JoystickY;


	///////////////////////////////////////////////////////////////////////////
	// Cargo
	///////////////////////////////////////////////////////////////////////////

	static final DigitalInput SPIN_INTAKE = DigitalInput.JoystickTrigger;
	static final DigitalInput SPIN_OUTTAKE = DigitalInput.Joystick2;
	static final DigitalInput CARGO_HEIGHT = DigitalInput.Joystick5;
	static final DigitalInput ROCKET_HEIGHT = DigitalInput.Joystick4;
	static final DigitalInput GROUND_HEIGHT = DigitalInput.Joystick3;
	static final DigitalInput LOCK_CARGO = DigitalInput.Joystick6;

	static final AnalogInput MOVE_ANGLE = AnalogInput.JoystickY;


	///////////////////////////////////////////////////////////////////////////
	// Climber
	///////////////////////////////////////////////////////////////////////////

	static final DigitalInput RELEASE_CLIMBER_10 = DigitalInput.Joystick10;
	static final DigitalInput RELEASE_CLIMBER_11 = DigitalInput.Joystick11;


	///////////////////////////////////////////////////////////////////////////
	// Vision
	///////////////////////////////////////////////////////////////////////////

	static final DigitalInput REQUEST_VISION = DigitalInput.XboxA;
	static final DigitalInput CONFIRM_VISION = DigitalInput.XboxB;
}
