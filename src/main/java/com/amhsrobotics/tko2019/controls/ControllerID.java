package com.amhsrobotics.tko2019.controls;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

public enum ControllerID {
	XboxController(new XboxController(0)),
	Joystick1(new Joystick(1)),
	Joystick2(new Joystick(2));

	private final GenericHID controller;

	ControllerID(final GenericHID controller) {
		this.controller = controller;
	}

	public final int getId() {
		return ordinal();
	}

	public final GenericHID getController(int id) {
		return ControllerID.values()[id].controller;
	}
}
