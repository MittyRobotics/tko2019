package com.amhsrobotics.tko2019.controls;

import com.amhsrobotics.tko2019.controls.commands.AnalogType;
import com.amhsrobotics.tko2019.controls.commands.DigitalType;
import com.amhsrobotics.tko2019.hardware.subsystems.HatchPanel;
import com.amhsrobotics.tko2019.hardware.settings.subsystems.TicksPerInch;
import edu.wpi.first.wpilibj.DriverStation;

public class ControlBindings {
	public static void setupControls() {
		setupDrive();
		setupCargo();
		setupHatch();
		setupClimber();
	}

	private static void setupDrive() {
		Controls.getInstance().registerAnalogCommand(Controller.XboxController, ControlsConfig.LEFT_WHEELS,
				AnalogType.OutOfThresholdMinor, value -> {
					if (isReversed) {
						setRight(-value);
					} else {
						setLeft(value);
					}
				}).registerAnalogCommand(Controller.XboxController, ControlsConfig.LEFT_WHEELS,
				AnalogType.InThresholdMinor, value -> {
					if (isReversed) {
						setRight(0);
					} else {
						setLeft(0);
					}
				}).registerAnalogCommand(Controller.XboxController, ControlsConfig.RIGHT_WHEELS,
				AnalogType.OutOfThresholdMinor, value -> {
					if (isReversed) {
						setLeft(-value);
					} else {
						setRight(value);
					}
				}).registerAnalogCommand(Controller.XboxController, ControlsConfig.RIGHT_WHEELS,
				AnalogType.InThresholdMinor, value -> {
					if (isReversed) {
						setLeft(0);
					} else {
						setRight(0);
					}
//				}).registerDigitalCommand(Controller.XboxController, ControlsConfig.GEAR_SWITCH,
//				DigitalType.DigitalPress, () -> {
//					if (System.currentTimeMillis() - lastSwitch > Restrictions.DRIVE_GEAR_SHIFT_COOLDOWN_MILLIS) {
//						if (currentGear == 1) {
//							shiftGear(0);
//						} else {
//							shiftGear(1);
//						}
//						lastSwitch = System.currentTimeMillis();
//					} else {
//						System.err.println("Shifter is on Cooldown.");
//					}
				}).registerDigitalCommand(Controller.XboxController, ControlsConfig.REVERSE_DIRECTION,
				DigitalType.DigitalPress, this::toggleReverser);
	}

	private static void setupHatch() {
		Controls.getInstance().registerAnalogCommand(Controller.Joystick1, ControlsConfig.JOYSTICK_SLIDE, AnalogType.Always, value -> {
			if (Math.abs(value) > 0.2) {
				slide(slideTalon.getSelectedSensorPosition() / TicksPerInch.SLIDER - 0.5 * value);
			}
		}).registerDigitalCommand(Controller.Joystick1, ControlsConfig.RELEASE_HATCH, DigitalType.DigitalPress, this::outtake).registerDigitalCommand(Controller.Joystick1, ControlsConfig.GRAB_HATCH, DigitalType.DigitalPress, this::intake)
				.registerAnalogCommand(Controller.Joystick1, ControlsConfig.PUSH_HATCH_MECHANISM, AnalogType.OutOfThresholdMajor, value -> {
					if (value > 0.5) {
						goHatchForward();
					} else if (value < -0.5) {
						goHatchBackward();
					}
				}).registerDigitalCommand(Controller.Joystick1, ControlsConfig.SLIDE_MIDDLE, DigitalType.DigitalPress, () -> {
			if (encoderConfig) {
				slideMiddle();
			}
		}).registerDigitalCommand(Controller.Joystick1, ControlsConfig.SLIDE_LEFT, DigitalType.DigitalPress, () -> {
			if (encoderConfig) {
				slideLeft();
			}
		}).registerDigitalCommand(Controller.Joystick1, ControlsConfig.SLIDE_RIGHT, DigitalType.DigitalPress, () -> {
			if (encoderConfig) {
				slideRight();
			}
		}).registerDigitalCommand(Controller.Joystick1, ControlsConfig.CONFIG_ENCODER_HATCH, DigitalType.DigitalPress, this::resetEncoder);

	}

	private static void setupCargo() {
		Controls.getInstance().registerDigitalCommand(Controller.Joystick2, ControlsConfig.SPIN_INTAKE, DigitalType.DigitalHold, () -> spinIntake(0.5));
		Controls.getInstance().registerDigitalCommand(Controller.Joystick2, ControlsConfig.SPIN_INTAKE, DigitalType.DigitalRelease, this::stopIntake);
		Controls.getInstance().registerDigitalCommand(Controller.Joystick2, ControlsConfig.SPIN_OUTTAKE, DigitalType.DigitalHold, () -> spinOuttake(0.5));
		Controls.getInstance().registerDigitalCommand(Controller.Joystick2, ControlsConfig.SPIN_OUTTAKE, DigitalType.DigitalRelease, this::stopIntake);

		Controls.getInstance().registerAnalogCommand(Controller.Joystick2, ControlsConfig.MOVE_ANGLE, AnalogType.OutOfThresholdMinor, value -> {
			moveConveyor(conveyorTalons[0].getSelectedSensorPosition() + value * 1000);
		}).registerDigitalCommand(Controller.Joystick2, ControlsConfig.CARGO_HEIGHT, DigitalType.DigitalPress, () -> {
			if (configEncoder) {
				cargoConveyor();
			}
		}).registerDigitalCommand(Controller.Joystick2, ControlsConfig.ROCKET_HEIGHT, DigitalType.DigitalPress, () -> {
			if (configEncoder) {
				rocketConveyor();
			}
		}).registerDigitalCommand(Controller.Joystick2, ControlsConfig.GROUND_HEIGHT, DigitalType.DigitalPress, () -> {
			if (configEncoder) {
				groundConveyor();
			}
		}).registerDigitalCommand(Controller.Joystick2, ControlsConfig.CONFIG_ENCODER_CARGO, DigitalType.DigitalPress, this::resetEncoder);

	}

	private static void setupClimber() {
		boolean elevenPressed = false; // FIXME
		Controls.getInstance().registerDigitalCommand(Controller.Joystick1, ControlsConfig.RELEASE_CLIMBER_2, DigitalType.DigitalPress, () -> {
			if (DriverStation.getInstance().getMatchTime() < 30 && DriverStation.getInstance().isOperatorControl() && elevenPressed) {
				HatchPanel.getInstance().slideMiddle();
				release();
			}
		}).registerDigitalCommand(Controller.Joystick1, ControlsConfig.RELEASE_CLIMBER_1, DigitalType.DigitalHold, () -> elevenPressed = true)
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.RELEASE_CLIMBER_1, DigitalType.DigitalRelease, () -> elevenPressed = false);
	}
}
