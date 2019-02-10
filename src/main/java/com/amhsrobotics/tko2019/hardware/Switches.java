package com.amhsrobotics.tko2019.hardware;

import com.amhsrobotics.tko2019.logging.LogCapable;
import edu.wpi.first.wpilibj.DigitalInput;

public class Switches implements LogCapable {
	private static Switches ourInstance = new Switches();

	public static Switches getInstance() {
		return ourInstance;
	}

	private final int INTAKE_SENSOR_ID = 0;
	private final int HATCH_SWITCH_ID = 0;
	private final int WALL_SWITCH_ID = 3;

	public DigitalInput intakeSensor;
	public DigitalInput hatchSwitch;
	public DigitalInput wallSwitch;

	private Switches() {
		intakeSensor = new DigitalInput(INTAKE_SENSOR_ID);
		hatchSwitch = new DigitalInput(HATCH_SWITCH_ID);
		wallSwitch = new DigitalInput(WALL_SWITCH_ID);
	}

	public void init() {

	}
}
