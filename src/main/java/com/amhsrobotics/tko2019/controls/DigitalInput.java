package com.amhsrobotics.tko2019.controls;

public enum DigitalInput {
	XboxA(1), XboxB(2), XboxX(3), XboxY(4),
	XboxLBumper(5), XboxRBumper(6),
	Dpad0(0), Dpad45(45), Dpad90(90), Dpad135(135),
	Dpad180(180), Dpad225(225), Dpad270(270), Dpad315(315),
	XboxLJoystick(9), XboxRJoystick(10),
	JoystickTrigger(1),
	Joystick2(2), Joystick3(3), Joystick4(4), Joystick5(5),
	Joystick6(6), Joystick7(7),
	Joystick8(8), Joystick9(9),
	Joystick10(10), Joystick11(11);

	private final int rawId;

	DigitalInput(final int rawId) {
		this.rawId = rawId;
	}

	public final int getRawId() {
		return rawId;
	}
}
