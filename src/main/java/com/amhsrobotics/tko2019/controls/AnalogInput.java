package com.amhsrobotics.tko2019.controls;

public enum AnalogInput {
	XboxLTrigger(genericHID -> {
		return genericHID.getRawAxis(2);
	}),
	XboxRTrigger(genericHID -> {
		return genericHID.getRawAxis(3);
	}),
	XboxLXJoystick(genericHID -> {
		return genericHID.getRawAxis(0);
	}),
	XboxRXJoystick(genericHID -> {
		return genericHID.getRawAxis(4);
	}),
	XboxLYJoystick(genericHID -> {
		return -genericHID.getRawAxis(1); // XBox Controller is Reversed
	}),
	XboxRYJoystick(genericHID -> {
		return -genericHID.getRawAxis(5); // XBox Controller is Reversed
	}),
	JoystickX(genericHID -> {
		return genericHID.getRawAxis(0);
	}),
	JoystickY(genericHID -> {
		return genericHID.getRawAxis(1);
	}),
	JoystickZ(genericHID -> {
		return genericHID.getRawAxis(2);
	});

	private final AnalogInputRequest inputRequest;

	AnalogInput(AnalogInputRequest inputRequest) {
		this.inputRequest = inputRequest;
	}

	public final AnalogInputRequest getInputRequest() {
		return inputRequest;
	}
}
