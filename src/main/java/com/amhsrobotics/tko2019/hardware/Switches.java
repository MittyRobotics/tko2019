package com.amhsrobotics.tko2019.hardware;

import com.amhsrobotics.tko2019.settings.SwitchIds;
import edu.wpi.first.wpilibj.DigitalInput;

public class Switches implements Initable {
	private static Switches ourInstance = new Switches();

	public DigitalInput intakeSensor;
	public DigitalInput hatchSwitch;
	public DigitalInput wallSwitch;

	private Switches() {

	}

	public static Switches getInstance() {
		return ourInstance;
	}

	@Override
	public void init() {
		intakeSensor = new DigitalInput(SwitchIds.INTAKE_SENSOR_ID);
		hatchSwitch = new DigitalInput(SwitchIds.HATCH_SWITCH_ID);
		wallSwitch = new DigitalInput(SwitchIds.WALL_SWITCH_ID);
	}
}
