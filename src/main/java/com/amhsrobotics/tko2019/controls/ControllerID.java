package com.amhsrobotics.tko2019.controls;

public enum ControllerID {
	XboxController(0), Joystick1(1), Joystick2(2);

	private final int id;

	ControllerID(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
