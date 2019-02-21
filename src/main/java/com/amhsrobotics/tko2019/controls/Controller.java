package com.amhsrobotics.tko2019.controls;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

public enum Controller {
	XboxController(new XboxController(0)),
	Joystick1(new Joystick(1)),
	Joystick2(new Joystick(2));

	private final GenericHID controller;

	Controller(final GenericHID controller) {
		this.controller = controller;
	}

	public final GenericHID getController() {
		return controller;
	}
}
