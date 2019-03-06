package com.amhsrobotics.tko2019.controls;

import com.amhsrobotics.tko2019.controls.commands.AnalogType;
import com.amhsrobotics.tko2019.controls.commands.DigitalType;
import com.amhsrobotics.tko2019.hardware.subsystems.Cargo;
import com.amhsrobotics.tko2019.hardware.subsystems.Climber;
import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.hardware.subsystems.HatchPanel;
import edu.wpi.first.wpilibj.DriverStation;

public class ControlBindings {
	public static void setupControls() {
		setupDrive();
		setupCargo();
		setupHatch();
		setupClimber();
	}

	private static void setupDrive() {
		Controls.getInstance()
				// Gear Shifting
				.registerDigitalCommand(Controller.XboxController, ControlsConfig.GEAR_SWITCH,
						DigitalType.DigitalPress, () -> Drive.getInstance().shiftGear())
				// Reversing
				.registerDigitalCommand(Controller.XboxController, ControlsConfig.REVERSE_DIRECTION,
						DigitalType.DigitalPress, () -> Drive.getInstance().toggleReverser())
				// Left Wheels
				.registerAnalogCommand(Controller.XboxController, ControlsConfig.LEFT_WHEELS,
						AnalogType.OutOfThresholdMinor, value -> Drive.getInstance().setLeft(value))
				.registerAnalogCommand(Controller.XboxController, ControlsConfig.LEFT_WHEELS,
						AnalogType.InThresholdMinor, value -> Drive.getInstance().setLeft(0))
				// Right Wheels
				.registerAnalogCommand(Controller.XboxController, ControlsConfig.RIGHT_WHEELS,
						AnalogType.OutOfThresholdMinor, value -> Drive.getInstance().setRight(value))
				.registerAnalogCommand(Controller.XboxController, ControlsConfig.RIGHT_WHEELS,
						AnalogType.InThresholdMinor, value -> Drive.getInstance().setRight(0));
	}

	private static void setupHatch() {
		Controls.getInstance()
				// Hatch
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.GRAB_HATCH,
						DigitalType.DigitalPress, () -> HatchPanel.getInstance().grab())
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.RELEASE_HATCH,
						DigitalType.DigitalPress, () -> HatchPanel.getInstance().release())
				// PID Setpoint Slider Movement
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.SLIDE_MIDDLE,
						DigitalType.DigitalPress, () -> HatchPanel.getInstance().slideMiddle())
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.SLIDE_LEFT,
						DigitalType.DigitalPress, () -> HatchPanel.getInstance().slideLeft())
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.SLIDE_RIGHT,
						DigitalType.DigitalPress, () -> HatchPanel.getInstance().slideRight())
				// Manual PID Setpoint Slider Movement
				.registerAnalogCommand(Controller.Joystick1, ControlsConfig.JOYSTICK_SLIDE,
						AnalogType.OutOfThresholdMajor, value -> HatchPanel.getInstance().slideManual(value))
				.registerAnalogCommand(Controller.Joystick1, ControlsConfig.PUSH_HATCH_MECHANISM, AnalogType.OutOfThresholdMajor, value -> {
					if (value > 0) {
						HatchPanel.getInstance().forward();
					} else {
						HatchPanel.getInstance().back();
					}
				});
	}

	private static void setupCargo() {
		Controls.getInstance()
				// Rollers
				.registerDigitalCommand(Controller.Joystick2, ControlsConfig.SPIN_INTAKE,
						DigitalType.DigitalHold, () -> Cargo.getInstance().spinIntake())
				.registerDigitalCommand(Controller.Joystick2, ControlsConfig.SPIN_INTAKE,
						DigitalType.DigitalRelease, () -> Cargo.getInstance().stopIntake())
				.registerDigitalCommand(Controller.Joystick2, ControlsConfig.SPIN_OUTTAKE,
						DigitalType.DigitalHold, () -> Cargo.getInstance().spinOuttake())
				.registerDigitalCommand(Controller.Joystick2, ControlsConfig.SPIN_OUTTAKE,
						DigitalType.DigitalRelease, () -> Cargo.getInstance().stopIntake())
				// Cargo Heights
				.registerDigitalCommand(Controller.Joystick2, ControlsConfig.CARGO_HEIGHT,
						DigitalType.DigitalPress, () -> Cargo.getInstance().cargoConveyor())
				.registerDigitalCommand(Controller.Joystick2, ControlsConfig.ROCKET_HEIGHT,
						DigitalType.DigitalPress, () -> Cargo.getInstance().rocketConveyor())
				.registerDigitalCommand(Controller.Joystick2, ControlsConfig.GROUND_HEIGHT,
						DigitalType.DigitalPress, () -> Cargo.getInstance().groundConveyor())
				// Manual Cargo Height
				.registerAnalogCommand(Controller.Joystick2, ControlsConfig.MOVE_ANGLE,
						AnalogType.OutOfThresholdMinor, value -> Cargo.getInstance().manualConveyor(value));
	}

	private static void setupClimber() {
		Controls.getInstance()
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.RELEASE_CLIMBER,
						DigitalType.DigitalPress, () -> {
							if (DriverStation.getInstance().getMatchTime() < 30 && DriverStation.getInstance().isOperatorControl()) {
								HatchPanel.getInstance().slideMiddle();
								HatchPanel.getInstance().forward();
								try {
									Thread.sleep(1000); // TODO Need to See if is Enough Time
								} catch (final InterruptedException e) {
									e.printStackTrace();
								}
								Climber.getInstance().release();
							}
						});
	}
}
