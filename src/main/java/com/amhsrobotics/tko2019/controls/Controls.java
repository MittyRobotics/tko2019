package com.amhsrobotics.tko2019.controls;

import java.util.HashMap;

public class Controls {
	private static Controls ourInstance = new Controls();
	public static Controls getInstance() {
		return ourInstance;
	}

	private final Thread controlsThread = new Thread(this::checkControls);
	private volatile boolean shouldRun = false;

	private final HashMap<Integer, HashMap<DigitalInput, HashMap<Type, ControllCommand>>> buttonControls = new HashMap<>();
	private final HashMap<Integer, HashMap<AnalogInput, HashMap<Type, ControllCommand>>> analogControls = new HashMap<>();

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

	public void registerButtonPress(int id, DigitalInput button, ControllCommand lambda) {
		if (!buttonControls.containsKey(id)) {
			buttonControls.put(id, new HashMap<>());
		}

	}

	public void registerButtonRelease(int id, DigitalInput button, ControllCommand lambda) {

	}

	public void registerButtonHold(int id, DigitalInput button, ControllCommand lambda) {

	}

	private void checkControls() {
		while (shouldRun) {
			// Buttons


			// AnalogInput
			if (true) {

			}
		}
	}

	enum Type {
		DigitalPress, DigitalRelease, DigitalHold
	}
}
