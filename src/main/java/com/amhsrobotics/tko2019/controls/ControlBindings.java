package com.amhsrobotics.tko2019.controls;

import com.amhsrobotics.tko2019.controls.commands.AnalogType;
import com.amhsrobotics.tko2019.controls.commands.DigitalType;
import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.hardware.subsystems.Cargo;
import com.amhsrobotics.tko2019.hardware.subsystems.Climber;
import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.hardware.subsystems.HatchPanel;
import com.amhsrobotics.tko2019.vision.VisionSync;
import edu.wpi.first.wpilibj.DriverStation;

public final class ControlBindings {
	public static void setupControls() {
		setupDrive();
		setupCargo();
		setupHatch();
		setupClimber();
		setupVision();
	}


	///////////////////////////////////////////////////////////////////////////
	// Drive
	///////////////////////////////////////////////////////////////////////////

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


	///////////////////////////////////////////////////////////////////////////
	// Hatch Panel
	///////////////////////////////////////////////////////////////////////////

	private static void setupHatch() {
		Controls.getInstance()
				// Hatch
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.CHECK_GRAB_HATCH, DigitalType.DigitalPress, () -> {
					if (Switches.getInstance().hasHatch()) {
						HatchPanel.getInstance().grab();
					} else {
						HatchPanel.getInstance().release();
					}
				})
				// PID Setpoint Slider Movement
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.SLIDE_MIDDLE,
						DigitalType.DigitalPress, () -> HatchPanel.getInstance().slideMiddle())
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.SLIDE_LEFT,
						DigitalType.DigitalPress, () -> HatchPanel.getInstance().slideLeft())
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.SLIDE_RIGHT,
						DigitalType.DigitalPress, () -> HatchPanel.getInstance().slideRight())
				// Manual PID Setpoint Slider Movement
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.CHECK_SLIDE,
						DigitalType.DigitalPress, () -> HatchPanel.getInstance().canSlide(true))
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.CHECK_SLIDE,
						DigitalType.DigitalRelease, () -> HatchPanel.getInstance().canSlide(false))
				.registerAnalogCommand(Controller.Joystick1, ControlsConfig.JOYSTICK_SLIDE,
						AnalogType.OutOfThresholdMinor, value -> HatchPanel.getInstance().slideManual(value))
				.registerAnalogCommand(Controller.Joystick1, ControlsConfig.PUSH_HATCH_MECHANISM, AnalogType.OutOfThresholdMajor, value -> {
					if (value > 0) {
						HatchPanel.getInstance().forward();
					} else {
						HatchPanel.getInstance().back();
					}
				});
	}


	///////////////////////////////////////////////////////////////////////////
	// Cargo
	///////////////////////////////////////////////////////////////////////////

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


	///////////////////////////////////////////////////////////////////////////
	// Climber
	///////////////////////////////////////////////////////////////////////////

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


	///////////////////////////////////////////////////////////////////////////
	// Vision
	///////////////////////////////////////////////////////////////////////////

	private static void setupVision() {
		final VisionSync visionSync = new VisionSync();
		Controls.getInstance()
				.registerDigitalCommand(Controller.XboxController, ControlsConfig.CONFIRM_VISION,
						DigitalType.DigitalPress, () -> new Thread(() -> {
							try {
								visionSync.request();
							} catch (final Exception e) {
								e.printStackTrace();
							}
						}).start())
				.registerDigitalCommand(Controller.XboxController, ControlsConfig.CONFIRM_VISION,
						DigitalType.DigitalRelease, () -> new Thread(visionSync::confirm).start());
	}
}