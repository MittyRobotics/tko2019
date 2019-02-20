package com.amhsrobotics.tko2019.hardware;

import com.amhsrobotics.tko2019.settings.SwitchIds;
import edu.wpi.first.wpilibj.DigitalInput;

public class Switches {
	private static Switches ourInstance = new Switches();

	private final DigitalInput intakeSensor;
	private final DigitalInput hatchSwitch;
	private final DigitalInput wallSwitch;

	private Switches() {
		intakeSensor = new DigitalInput(SwitchIds.INTAKE_SENSOR_ID);
		hatchSwitch = new DigitalInput(SwitchIds.HATCH_SWITCH_ID);
		wallSwitch = new DigitalInput(SwitchIds.WALL_SWITCH_ID);
	}

	public static Switches getInstance() {
		return ourInstance;
	}

	public boolean getIntakeSensor() {
		return intakeSensor.get();
	}

	public boolean getHatchSwitch() {
		return hatchSwitch.get();
	}

	public boolean getWallSwitch() {
		return wallSwitch.get();
	}
}
