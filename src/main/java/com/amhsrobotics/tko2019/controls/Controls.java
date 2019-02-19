package com.amhsrobotics.tko2019.controls;

import java.util.ArrayList;
import java.util.HashMap;

public class Controls {
	private static Controls ourInstance = new Controls();
	public static Controls getInstance() {
		return ourInstance;
	}

	private final HashMap<Integer, HashMap<DigitalInput, HashMap<DigitalType, ArrayList<DigitalControlCommand>>>> buttonControls = new HashMap<>();
	private final HashMap<Integer, HashMap<AnalogInput, HashMap<AnalogType, ArrayList<AnalogControlCommand>>>> analogControls = new HashMap<>();

	private final Thread controlsThread = new Thread(this::checkControls);
	private volatile boolean shouldRun = false;

	private Controls() {
		controlsThread.setName("Controls Thread");
		controlsThread.setPriority(Thread.MAX_PRIORITY);
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

	public void registerDigitalCommand(int id, DigitalInput digitalInput, DigitalType digitalType, DigitalControlCommand lambda) {
		buttonControls.putIfAbsent(id, new HashMap<>());
		final HashMap<DigitalInput, HashMap<DigitalType, ArrayList<DigitalControlCommand>>> inputs = buttonControls.get(id);
		inputs.putIfAbsent(digitalInput, new HashMap<>());
		final HashMap<DigitalType, ArrayList<DigitalControlCommand>> commands = inputs.get(digitalInput);
		commands.putIfAbsent(digitalType, new ArrayList<>());
		commands.get(digitalType).add(lambda);
	}

	public void registerAnalogCommand(int id, AnalogInput analogInput, AnalogType analogType, AnalogControlCommand lambda) {
		analogControls.putIfAbsent(id, new HashMap<>());
		final HashMap<AnalogInput, HashMap<AnalogType, ArrayList<AnalogControlCommand>>> inputs = analogControls.get(id);
		inputs.putIfAbsent(analogInput, new HashMap<>());
		final HashMap<AnalogType, ArrayList<AnalogControlCommand>> commands = inputs.get(analogInput);
		commands.putIfAbsent(analogType, new ArrayList<>());
		commands.get(analogType).add(lambda);
	}

	private void checkControls() {
		final HashMap<Integer, HashMap<DigitalInput, Boolean>> cachedDigitalInputs = new HashMap<>();
		final HashMap<Integer, HashMap<AnalogInput, Double>> cachedAnalogInputs = new HashMap<>();
		while (shouldRun) {
			// Buttons
			for (final int deviceID : buttonControls.keySet()) {
				for (final DigitalInput digitalInput : buttonControls.get(deviceID).keySet()) {
					boolean value = digitalInput.getInputRequest().requestDigital(ControllerID.getController(deviceID));
					cachedDigitalInputs.putIfAbsent(deviceID, new HashMap<>());
					cachedDigitalInputs.get(deviceID).putIfAbsent(digitalInput, value);
					boolean cachedValue = cachedDigitalInputs.get(deviceID).get(digitalInput);
					if (value) {
						ArrayList<DigitalControlCommand> pressCommands = buttonControls.get(deviceID).get(digitalInput).get(DigitalType.DigitalPress);
						if (pressCommands != null && !cachedValue) {
							for (DigitalControlCommand controlCommand : pressCommands) {
								controlCommand.action();
							}
						}
						ArrayList<DigitalControlCommand> holdCommands = buttonControls.get(deviceID).get(digitalInput).get(DigitalType.DigitalHold);
						if (holdCommands != null) {
							for (DigitalControlCommand controlCommand : holdCommands) {
								controlCommand.action();
							}
						}
					} else {
						ArrayList<DigitalControlCommand> releaseCommands = buttonControls.get(deviceID).get(digitalInput).get(DigitalType.DigitalRelease);
						if (releaseCommands != null && cachedValue) {
							for (DigitalControlCommand controlCommand : releaseCommands) {
								controlCommand.action();
							}
						}
					}
					cachedDigitalInputs.get(deviceID).put(digitalInput, value);
				}
			}

			// AnalogInput
			for (final int deviceID : analogControls.keySet()) {
				for (final AnalogInput analogInput : analogControls.get(deviceID).keySet()) {
					double value = analogInput.getInputRequest().requestAnalog(ControllerID.getController(deviceID));
					cachedAnalogInputs.putIfAbsent(deviceID, new HashMap<>());
					cachedAnalogInputs.get(deviceID).putIfAbsent(analogInput, value);
					double cachedValue = cachedAnalogInputs.get(deviceID).get(analogInput);
					if (Math.abs(value) > 0.1) {
						ArrayList<AnalogControlCommand> outOfMinorThresholdCommands = analogControls.get(deviceID).get(analogInput).get(AnalogType.OutOfThresholdMinor);
						if (outOfMinorThresholdCommands != null) {
							for (AnalogControlCommand controlCommand : outOfMinorThresholdCommands) {
								controlCommand.action(value);
							}
						}
					} else {
						ArrayList<AnalogControlCommand> commands = analogControls.get(deviceID).get(analogInput).get(AnalogType.InThresholdMinor);
						if (commands != null) {
							for (AnalogControlCommand command : commands) {
								command.action(value);
							}
						}
					}
				}
			}

			// Delay
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
