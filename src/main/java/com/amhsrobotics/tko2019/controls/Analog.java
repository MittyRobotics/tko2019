package com.amhsrobotics.tko2019.controls;

public enum Analog {
	XboxLTrigger(2), XboxRTrigger(3),
	XboxLXJoystick(0), XboxRXJoystick(4),
	XboxLYJoystick(1), XboxRYJoystick(5),
	JoystickX(0), JoystickY(1), JoystickZ(2);

	private final int rawId;
	Analog(int rawId){
		this.rawId = rawId;
	}
	public int getRawId() {
		return rawId;
	}
}
