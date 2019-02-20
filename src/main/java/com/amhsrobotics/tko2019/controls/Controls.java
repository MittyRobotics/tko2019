package com.amhsrobotics.tko2019.controls;

import com.amhsrobotics.tko2019.controls.commands.AnalogControlCommand;
import com.amhsrobotics.tko2019.controls.commands.AnalogType;
import com.amhsrobotics.tko2019.controls.commands.DigitalControlCommand;
import com.amhsrobotics.tko2019.controls.commands.DigitalType;
import com.amhsrobotics.tko2019.controls.input.AnalogInput;
import com.amhsrobotics.tko2019.controls.input.DigitalInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Controls {
	private final static Controls ourInstance = new Controls();

	private final ConcurrentHashMap<Integer, ConcurrentHashMap<DigitalInput, ConcurrentHashMap<DigitalType, ArrayList<DigitalControlCommand>>>> buttonControls = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<Integer, ConcurrentHashMap<AnalogInput, ConcurrentHashMap<AnalogType, ArrayList<AnalogControlCommand>>>> analogControls = new ConcurrentHashMap<>();

	private final HashMap<String, DigitalControlCommand> registeredDigitalControls = new HashMap<>();
	private final HashMap<String, AnalogControlCommand> registeredAnalogControls = new HashMap<>();
	private volatile boolean shouldRun = false;
	private final Thread controlsThread = new Thread(this::checkControls);

	private Controls() {
		controlsThread.setName("Controls Thread");
		controlsThread.setPriority(Thread.MAX_PRIORITY);
	}

	public static Controls getInstance() {
		return ourInstance;
	}

	public synchronized void enable() {
		if (!shouldRun) {
			//noinspection LoopConditionNotUpdatedInsideLoop
			while (controlsThread.isAlive()) {
				Thread.onSpinWait();
			}
			shouldRun = true;
			controlsThread.start();
		}
	}

	public void disable() {
		shouldRun = false;
	}

	@SuppressWarnings("Duplicates")
	public void registerDigitalCommand(int id, DigitalInput digitalInput, DigitalType digitalType, DigitalControlCommand lambda, String... commandName) {
		buttonControls.putIfAbsent(id, new ConcurrentHashMap<>());
		final ConcurrentHashMap<DigitalInput, ConcurrentHashMap<DigitalType, ArrayList<DigitalControlCommand>>> inputs = buttonControls.get(id);
		inputs.putIfAbsent(digitalInput, new ConcurrentHashMap<>());
		final ConcurrentHashMap<DigitalType, ArrayList<DigitalControlCommand>> commands = inputs.get(digitalInput);
		commands.putIfAbsent(digitalType, new ArrayList<>());
		commands.get(digitalType).add(lambda);

		if (commandName.length != 0) {
			registeredDigitalControls.put(commandName[0], lambda);
		}
	}

	@SuppressWarnings("Duplicates")
	public void registerAnalogCommand(int id, AnalogInput analogInput, AnalogType analogType, AnalogControlCommand lambda, String... commandName) {
		analogControls.putIfAbsent(id, new ConcurrentHashMap<>());
		final ConcurrentHashMap<AnalogInput, ConcurrentHashMap<AnalogType, ArrayList<AnalogControlCommand>>> inputs = analogControls.get(id);
		inputs.putIfAbsent(analogInput, new ConcurrentHashMap<>());
		final ConcurrentHashMap<AnalogType, ArrayList<AnalogControlCommand>> commands = inputs.get(analogInput);
		commands.putIfAbsent(analogType, new ArrayList<>());
		commands.get(analogType).add(lambda);

		if (commandName.length != 0) {
			registeredAnalogControls.put(commandName[0], lambda);
		}
	}

	public void unregisterDigitalCommand(int id, DigitalInput digitalInput, DigitalType digitalType, final String commandName) {
		buttonControls.get(id).get(digitalInput).get(digitalType).remove(registeredDigitalControls.get(commandName));
	}

	public void unregisterAnalogCommand(int id, AnalogInput analogInput, AnalogType analogType, final String commandName) {
		analogControls.get(id).get(analogInput).get(analogType).remove(registeredAnalogControls.get(commandName));
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
					ArrayList<AnalogControlCommand> alwaysCommands = analogControls.get(deviceID).get(analogInput).get(AnalogType.Always);
					if (alwaysCommands != null) {
						for (AnalogControlCommand controlCommand : alwaysCommands) {
							controlCommand.action(value);
						}
					}
					if (Math.abs(value) > 0.05) {
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
					if (Math.abs(value) > 0.5) {
						ArrayList<AnalogControlCommand> outOfMajorThresholdCommands = analogControls.get(deviceID).get(analogInput).get(AnalogType.OutOfThresholdMajor);
						if (outOfMajorThresholdCommands != null) {
							for (AnalogControlCommand controlCommand : outOfMajorThresholdCommands) {
								controlCommand.action(value);
							}
						}
					} else {
						ArrayList<AnalogControlCommand> outOfMinorThresholdCommands = analogControls.get(deviceID).get(analogInput).get(AnalogType.InThresholdMajor);
						if (outOfMinorThresholdCommands != null) {
							for (AnalogControlCommand controlCommand : outOfMinorThresholdCommands) {
								controlCommand.action(value);
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
