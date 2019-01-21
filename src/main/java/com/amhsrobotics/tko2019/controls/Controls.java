package com.amhsrobotics.tko2019.controls;

import java.util.ArrayList;
import java.util.HashMap;

public class Controls {
	private static Controls ourInstance = new Controls();
	public static Controls getInstance() {
		return ourInstance;
	}

	private final HashMap<Integer, HashMap<DigitalInput, HashMap<DigitalType, ArrayList<ControlCommand>>>> buttonControls = new HashMap<>();
	private final HashMap<Integer, HashMap<AnalogInput, HashMap<AnalogType, ArrayList<ControlCommand>>>> analogControls = new HashMap<>();

	private final Thread controlsThread = new Thread(this::checkControls);
	private volatile boolean shouldRun = false;

	private Controls() {

	}

	public void enable() {
		if (!shouldRun) {
			shouldRun = true;
			controlsThread.start();
		}
	}

	public void disable() {
		shouldRun = false;
	}

	public void registerButtonPress(int id, DigitalInput digitalInput, DigitalType digitalType, ControlCommand lambda) {
		if (!buttonControls.containsKey(id)) {
			buttonControls.put(id, new HashMap<>());
		}
		final HashMap<DigitalInput, HashMap<DigitalType, ArrayList<ControlCommand>>> inputs = buttonControls.get(id);
		if (!inputs.containsKey(digitalInput)) {
			inputs.put(digitalInput, new HashMap<>());
		}
		final HashMap<DigitalType, ArrayList<ControlCommand>> commands = inputs.get(digitalInput);
		if (!commands.containsKey(digitalType)) {
			commands.put(digitalType, new ArrayList<>());
		}
		commands.get(digitalType).add(lambda);
	}

	public void registerButtonRelease(int id, DigitalInput digitalInput, ControlCommand lambda) {

	}

	public void registerButtonHold(int id, DigitalInput digitalInput, ControlCommand lambda) {

	}

	private void checkControls() {
		while (shouldRun) {
			// Buttons


			// AnalogInput

		}
	}

	enum DigitalType {
		DigitalPress, DigitalRelease, DigitalHold
	}

	enum AnalogType {

	}
}
