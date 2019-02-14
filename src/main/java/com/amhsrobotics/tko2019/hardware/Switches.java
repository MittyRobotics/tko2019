package com.amhsrobotics.tko2019.hardware;

import com.amhsrobotics.tko2019.logging.LogCapable;
import com.amhsrobotics.tko2019.settings.SwitcheIds;
import edu.wpi.first.wpilibj.DigitalInput;

public class Switches implements LogCapable {
	private static Switches ourInstance = new Switches();

	public DigitalInput intakeSensor;
	public DigitalInput hatchSwitch;
	public DigitalInput wallSwitch;

	private Switches() {
		
	}

	public static Switches getInstance() {
		return ourInstance;
	}

	public void init() {
		intakeSensor = new DigitalInput(SwitcheIds.INTAKE_SENSOR_ID);
		hatchSwitch = new DigitalInput(SwitcheIds.HATCH_SWITCH_ID);
		wallSwitch = new DigitalInput(SwitcheIds.WALL_SWITCH_ID);
	}
}
