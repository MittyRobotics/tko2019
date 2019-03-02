package com.amhsrobotics.tko2019.controls;

import com.amhsrobotics.tko2019.controls.commands.AnalogControlCommand;
import com.amhsrobotics.tko2019.controls.commands.AnalogType;
import com.amhsrobotics.tko2019.controls.commands.DigitalControlCommand;
import com.amhsrobotics.tko2019.controls.commands.DigitalType;
import com.amhsrobotics.tko2019.controls.input.AnalogInput;
import com.amhsrobotics.tko2019.controls.input.DigitalInput;

import java.util.ArrayList;
import java.util.HashMap;

public final class Controls extends Thread {
	private static final Controls INSTANCE = new Controls();

	private final HashMap<Controller, HashMap<DigitalInput, HashMap<DigitalType, ArrayList<DigitalControlCommand>>>> buttonControls = new HashMap<>();
	private final HashMap<Controller, HashMap<AnalogInput, HashMap<AnalogType, ArrayList<AnalogControlCommand>>>> analogControls = new HashMap<>();

	private volatile boolean shouldRun = false;

	private Controls() {
		setName("Controls Thread");
		setPriority(Thread.MAX_PRIORITY);
		start();
	}

	public static Controls getInstance() {
		return INSTANCE;
	}

	public synchronized void enable() {
		shouldRun = true;
	}

	public void disable() {
		shouldRun = false;
	}

	@SuppressWarnings("Duplicates")
	public Controls registerDigitalCommand(Controller controller, DigitalInput digitalInput, DigitalType digitalType, DigitalControlCommand lambda) {
		buttonControls.putIfAbsent(controller, new HashMap<>());
		final HashMap<DigitalInput, HashMap<DigitalType, ArrayList<DigitalControlCommand>>> inputs = buttonControls.get(controller);
		inputs.putIfAbsent(digitalInput, new HashMap<>());
		final HashMap<DigitalType, ArrayList<DigitalControlCommand>> commands = inputs.get(digitalInput);
		commands.putIfAbsent(digitalType, new ArrayList<>());
		commands.get(digitalType).add(lambda);
		return this;
	}

	@SuppressWarnings("Duplicates")
	public Controls registerAnalogCommand(Controller controller, AnalogInput analogInput, AnalogType analogType, AnalogControlCommand lambda) {
		analogControls.putIfAbsent(controller, new HashMap<>());
		final HashMap<AnalogInput, HashMap<AnalogType, ArrayList<AnalogControlCommand>>> inputs = analogControls.get(controller);
		inputs.putIfAbsent(analogInput, new HashMap<>());
		final HashMap<AnalogType, ArrayList<AnalogControlCommand>> commands = inputs.get(analogInput);
		commands.putIfAbsent(analogType, new ArrayList<>());
		commands.get(analogType).add(lambda);
		return this;
	}

	@Override
	public void run() {
		final HashMap<Controller, HashMap<DigitalInput, Boolean>> cachedDigitalInputs = new HashMap<>();
		final HashMap<Controller, HashMap<AnalogInput, Double>> cachedAnalogInputs = new HashMap<>();
		while (true) {
			while (shouldRun) {
				// Buttons
				for (Controller controller : buttonControls.keySet()) {
					for (final DigitalInput digitalInput : buttonControls.get(controller).keySet()) {
						boolean value = digitalInput.getInputRequest().requestDigital(controller.getController());
						cachedDigitalInputs.putIfAbsent(controller, new HashMap<>());
						cachedDigitalInputs.get(controller).putIfAbsent(digitalInput, value);
						boolean cachedValue = cachedDigitalInputs.get(controller).get(digitalInput);
						if (value) {
							ArrayList<DigitalControlCommand> pressCommands = buttonControls.get(controller).get(digitalInput).get(DigitalType.DigitalPress);
							if (pressCommands != null && !cachedValue) {
								for (DigitalControlCommand controlCommand : pressCommands) {
									controlCommand.action();
								}
							}
							ArrayList<DigitalControlCommand> holdCommands = buttonControls.get(controller).get(digitalInput).get(DigitalType.DigitalHold);
							if (holdCommands != null) {
								for (DigitalControlCommand controlCommand : holdCommands) {
									controlCommand.action();
								}
							}
						} else {
							ArrayList<DigitalControlCommand> releaseCommands = buttonControls.get(controller).get(digitalInput).get(DigitalType.DigitalRelease);
							if (releaseCommands != null && cachedValue) {
								for (DigitalControlCommand controlCommand : releaseCommands) {
									controlCommand.action();
								}
							}
						}
						cachedDigitalInputs.get(controller).put(digitalInput, value);
					}
				}

				// AnalogInput
				for (final Controller controller : analogControls.keySet()) {
					for (final AnalogInput analogInput : analogControls.get(controller).keySet()) {
						double value = analogInput.getInputRequest().requestAnalog(controller.getController());
						cachedAnalogInputs.putIfAbsent(controller, new HashMap<>());
						cachedAnalogInputs.get(controller).putIfAbsent(analogInput, value);
						ArrayList<AnalogControlCommand> alwaysCommands = analogControls.get(controller).get(analogInput).get(AnalogType.Always);
						if (alwaysCommands != null) {
							for (AnalogControlCommand controlCommand : alwaysCommands) {
								controlCommand.action(value);
							}
						}
						if (Math.abs(value) > 0.05) {
							ArrayList<AnalogControlCommand> outOfMinorThresholdCommands = analogControls.get(controller).get(analogInput).get(AnalogType.OutOfThresholdMinor);
							if (outOfMinorThresholdCommands != null) {
								for (AnalogControlCommand controlCommand : outOfMinorThresholdCommands) {
									controlCommand.action(value);
								}
							}
						} else {
							ArrayList<AnalogControlCommand> commands = analogControls.get(controller).get(analogInput).get(AnalogType.InThresholdMinor);
							if (commands != null) {
								for (AnalogControlCommand command : commands) {
									command.action(value);
								}
							}
						}
						if (Math.abs(value) > 0.5) {
							ArrayList<AnalogControlCommand> outOfMajorThresholdCommands = analogControls.get(controller).get(analogInput).get(AnalogType.OutOfThresholdMajor);
							if (outOfMajorThresholdCommands != null) {
								for (AnalogControlCommand controlCommand : outOfMajorThresholdCommands) {
									controlCommand.action(value);
								}
							}
						} else {
							ArrayList<AnalogControlCommand> outOfMinorThresholdCommands = analogControls.get(controller).get(analogInput).get(AnalogType.InThresholdMajor);
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
					sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
