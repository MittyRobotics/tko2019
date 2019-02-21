package com.amhsrobotics.tko2019.settings;

import com.amhsrobotics.tko2019.controls.input.AnalogInput;
import com.amhsrobotics.tko2019.controls.input.DigitalInput;

public class ControlsConfig {
	public static final DigitalInput SWITCH_MODE = DigitalInput.Joystick11;
	public static final DigitalInput CONFIG_ENCODER = DigitalInput.Joystick6;


	// Drive

	public static final AnalogInput LEFT_WHEELS = AnalogInput.XboxLYJoystick;
	public static final AnalogInput RIGHT_WHEELS = AnalogInput.XboxRYJoystick;
	public static final DigitalInput REVERSE_DIRECTION = DigitalInput.XboxRJoystick;
	public static final DigitalInput GEAR_SWITCH = DigitalInput.XboxRBumper;


	//Cargo

	public static final DigitalInput SPIN_INTAKE = DigitalInput.JoystickTrigger;
	public static final DigitalInput SPIN_OUTTAKE = DigitalInput.Joystick2;
	public static final AnalogInput MOVE_ANGLE = AnalogInput.JoystickY;
	public static final DigitalInput CARGO_HEIGHT = DigitalInput.Joystick3;
	public static final DigitalInput ROCKET_HEIGHT = DigitalInput.Joystick4;
	public static final DigitalInput STATION_HEIGHT = DigitalInput.Joystick7; //TODO Figure out place
	public static final DigitalInput GROUND_HEIGHT = DigitalInput.Joystick5;


	//Hatch Panel

	public static final DigitalInput SLIDE_LEFT = DigitalInput.Joystick4;
	public static final DigitalInput SLIDE_RIGHT = DigitalInput.Joystick5;
	public static final DigitalInput SLIDE_MIDDLE = DigitalInput.Joystick3;
	public static final DigitalInput RELEASE_HATCH = DigitalInput.Joystick2;
	public static final DigitalInput GRAB_HATCH = DigitalInput.JoystickTrigger;
	public static final AnalogInput JOYTICK_SLIDE = AnalogInput.JoystickX;
	public static final AnalogInput PUSH_HATCH_MECHANISM = AnalogInput.JoystickY;


	//Climber

	public static final DigitalInput RELEASE_CLIMBER_1 = DigitalInput.Joystick6;
	public static final DigitalInput RELEASE_CLIMBER_2 = DigitalInput.XboxY;
}
