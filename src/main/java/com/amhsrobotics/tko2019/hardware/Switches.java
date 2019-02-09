package com.amhsrobotics.tko2019.hardware;

import com.amhsrobotics.tko2019.logging.LogCapable;
import edu.wpi.first.wpilibj.DigitalInput;

public class Switches implements LogCapable {
	private static Switches ourInstance = new Switches();

	public static Switches getInstance() {
		return ourInstance;
	}

	private final int INTAKE_SENSOR_ID = 0;
	private final int[] CONVEYOR_LIMIT_IDS = {1, 2};

	public DigitalInput intakeSensor;
	public DigitalInput[] conveyorLimits = new DigitalInput[CONVEYOR_LIMIT_IDS.length];

	private Switches() {
		intakeSensor = new DigitalInput(INTAKE_SENSOR_ID);
		for(int limitSwitchIds = 0; limitSwitchIds < conveyorLimits.length; limitSwitchIds++){
			final DigitalInput digitalInput = new DigitalInput(CONVEYOR_LIMIT_IDS[limitSwitchIds]);
			conveyorLimits[limitSwitchIds] = digitalInput;
		}
	}

	public void init() {

	}
}
