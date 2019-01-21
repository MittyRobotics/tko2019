package com.amhsrobotics.tko2019.controls;

import edu.wpi.first.wpilibj.GenericHID;

public enum DigitalInput {
	XboxA(genericHID -> {
		return genericHID.getRawButton(1);
	}),
	XboxB(genericHID -> {
		return genericHID.getRawButton(2);
	}),
	XboxX(genericHID -> {
		return genericHID.getRawButton(3);
	}),
	XboxY(genericHID -> {
		return genericHID.getRawButton(4);
	}),
	XboxLBumper(genericHID -> {
		return genericHID.getRawButton(5);
	}),
	XboxRBumper(genericHID -> {
		return genericHID.getRawButton(6);
	}),
	Dpad0(genericHID -> {
		return genericHID.getPOV(0) == 0;
	}),
	Dpad45(genericHID -> {
		return genericHID.getPOV(0) == 45;
	}),
	Dpad90(genericHID -> {
		return genericHID.getPOV(0) == 90;
	}),
	Dpad135(genericHID -> {
		return genericHID.getPOV(0) == 135;
	}),
	Dpad180(genericHID -> {
		return genericHID.getPOV(0) == 180;
	}),
	Dpad225(genericHID -> {
		return genericHID.getPOV(0) == 225;
	}),
	Dpad270(genericHID -> {
		return genericHID.getPOV(0) == 270;
	}),
	Dpad315(genericHID -> {
		return genericHID.getPOV(0) == 315;
	}),
	XboxLJoystick(genericHID -> {
		return genericHID.getRawButton(9);
	}),
	XboxRJoystick(genericHID -> {
		return genericHID.getRawButton(10);
	}),
	JoystickTrigger(genericHID -> {
		return genericHID.getRawButton(1);
	}),
	Joystick2(genericHID -> {
		return genericHID.getRawButton(2);
	}),
	Joystick3(genericHID -> {
		return genericHID.getRawButton(3);
	}),
	Joystick4(genericHID -> {
		return genericHID.getRawButton(4);
	}),
	Joystick5(genericHID -> {
		return genericHID.getRawButton(5);
	}),
	Joystick6(genericHID -> {
		return genericHID.getRawButton(6);
	}),
	Joystick7(genericHID -> {
		return genericHID.getRawButton(7);
	}),
	Joystick8(genericHID -> {
		return genericHID.getRawButton(8);
	}),
	Joystick9(genericHID -> {
		return genericHID.getRawButton(9);
	}),
	Joystick10(genericHID -> {
		return genericHID.getRawButton(10);
	}),
	Joystick11(genericHID -> {
		return genericHID.getRawButton(11);
	});

	DigitalInput(DigitalInputRequest inputRequest){

	}
}
