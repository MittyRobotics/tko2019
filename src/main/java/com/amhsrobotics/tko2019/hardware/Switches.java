package com.amhsrobotics.tko2019.hardware;

import com.amhsrobotics.tko2019.hardware.settings.SwitchIds;
import edu.wpi.first.wpilibj.DigitalInput;

public final class Switches {
	private static final Switches INSTANCE = new Switches();

	private final DigitalInput intakeSensor = new DigitalInput(SwitchIds.INTAKE_SENSOR_ID);
	private final DigitalInput hatchSwitch = new DigitalInput(SwitchIds.HATCH_SWITCH_ID);
	private final DigitalInput wallSwitch = new DigitalInput(SwitchIds.WALL_SWITCH_ID);

	private Switches() {
	}

	public static Switches getInstance() {
		return INSTANCE;
	}

	public final boolean hasCargo() {
		return !intakeSensor.get();
	}

	public final boolean hasHatch() {
		return !hatchSwitch.get();
	}

	public final boolean isTouchingWall() {
		return wallSwitch.get();
	}
}
