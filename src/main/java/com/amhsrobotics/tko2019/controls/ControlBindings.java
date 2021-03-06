package com.amhsrobotics.tko2019.controls;

import com.amhsrobotics.tko2019.controls.commands.AnalogType;
import com.amhsrobotics.tko2019.controls.commands.DigitalType;
import com.amhsrobotics.tko2019.hardware.subsystems.Cargo;
import com.amhsrobotics.tko2019.hardware.subsystems.Climber;
import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.hardware.subsystems.HatchPanel;
import com.amhsrobotics.tko2019.vision.Vision;
import edu.wpi.first.wpilibj.DriverStation;

import java.util.concurrent.atomic.AtomicBoolean;

public final class ControlBindings {
	public static void setupControls() {
		setupDrive();
		setupCargo();
		setupHatch();
//		setupClimber(); // FIXME IDK If I Should Still be Disabled
//		setupVision(); //Don't forget about me! (DW I Remembered You)
//		setupAuton();
	}


	///////////////////////////////////////////////////////////////////////////
	// Drive
	///////////////////////////////////////////////////////////////////////////

	private static void setupDrive() {
		Controls.getInstance()
				// Gear Shifting
				.registerDigitalCommand(Controller.XboxController, ControlsConfig.HIGH_GEAR_SWITCH,
						DigitalType.DigitalPress, () -> Drive.getInstance().shiftHigh())
				.registerDigitalCommand(Controller.XboxController, ControlsConfig.LOW_GEAR_SWITCH,
						DigitalType.DigitalPress, () -> Drive.getInstance().shiftLow())
				// Reversing
				.registerDigitalCommand(Controller.XboxController, ControlsConfig.REVERSE_DIRECTION,
						DigitalType.DigitalPress, () -> Drive.getInstance().toggleReverser())
				// Left Wheels
				.registerAnalogCommand(Controller.XboxController, ControlsConfig.LEFT_WHEELS,
						AnalogType.OutOfThresholdMinor, value -> {
					Drive.getInstance().setLeft(value);
					//System.out.println("L Desired: " + value);
					//System.out.println("R Output: " + Drive.getInstance().leftTalons[0].getMotorOutputPercent());
						})
				.registerAnalogCommand(Controller.XboxController, ControlsConfig.LEFT_WHEELS,
						AnalogType.InThresholdMinor, value -> Drive.getInstance().setLeft(0))
				// Right Wheels
				.registerAnalogCommand(Controller.XboxController, ControlsConfig.RIGHT_WHEELS,
						AnalogType.OutOfThresholdMinor, value -> {
					Drive.getInstance().setRight(value);
					//System.out.println("R: Desired: " + value);
					//System.out.println("R: Output: " + Drive.getInstance().rightTalons[0].getMotorOutputPercent());
						})
				.registerAnalogCommand(Controller.XboxController, ControlsConfig.RIGHT_WHEELS,
						AnalogType.InThresholdMinor, value -> Drive.getInstance().setRight(0));
	}


	///////////////////////////////////////////////////////////////////////////
	// Hatch Panel
	///////////////////////////////////////////////////////////////////////////

	private static void setupHatch() {
		Controls.getInstance()
				// Hatch
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.CHECK_GRAB_HATCH, DigitalType.DigitalHold, () -> {
//					HatchPanel.getInstance().forward();
//					if (Switches.getInstance().hasHatch()) {
						HatchPanel.getInstance().release();
//						HatchPanel.getInstance().forward();
//					}
//					else {
//						HatchPanel.getInstance().grab();
//					}
				})
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.MANUAL_GRAB_HATCH, DigitalType.DigitalPress, () -> {
//					if(Switches.getInstance().hasHatch()){
					HatchPanel.getInstance().grab();
//					} else {
//						HatchPanel.getInstance().release();
//					}
				})
				// PID Setpoint Slider Movement
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.SLIDE_MIDDLE,
						DigitalType.DigitalPress, () -> HatchPanel.getInstance().slideMiddle())
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.SLIDE_RIGHT,
						DigitalType.DigitalPress, () -> HatchPanel.getInstance().slideLeft())
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.SLIDE_LEFT,
						DigitalType.DigitalPress, () -> HatchPanel.getInstance().slideRight())
				// Manual PID Setpoint Slider Movement
//				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.CHECK_SLIDE,
//						DigitalType.DigitalPress, () -> HatchPanel.getInstance().canSlide(true))
//				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.CHECK_SLIDE,
//						DigitalType.DigitalRelease, () -> HatchPanel.getInstance().canSlide(false))
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
//				 Cargo Heights
				.registerDigitalCommand(Controller.Joystick2, ControlsConfig.CARGO_HEIGHT,
						DigitalType.DigitalPress, () -> Cargo.getInstance().cargoConveyor())
				.registerDigitalCommand(Controller.Joystick2, ControlsConfig.ROCKET_HEIGHT,
						DigitalType.DigitalPress, () -> Cargo.getInstance().rocketConveyor())
//				.registerDigitalCommand(Controller.Joystick2, ControlsConfig.GROUND_HEIGHT,
//						DigitalType.DigitalPress, () -> Cargo.getInstance().groundConveyor())
				// Manual Cargo Height
				.registerAnalogCommand(Controller.Joystick2, ControlsConfig.MOVE_ANGLE,
						AnalogType.OutOfThresholdMinor, value -> {
							if (Math.abs(value) > 0.15){
								Cargo.getInstance().moveConveyor(Cargo.getInstance().conveyorTalons[0].getSelectedSensorPosition() - value * 1000);
							}
						});
	}


	///////////////////////////////////////////////////////////////////////////
	// Climber
	///////////////////////////////////////////////////////////////////////////

	private static void setupClimber() {
		Controls.getInstance()
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.RELEASE_CLIMBER_10,
						DigitalType.DigitalPress, () -> {
							if (

									DriverStation.getInstance().getMatchTime() < 30 &&
									DriverStation.getInstance().isOperatorControl() &&
											Climber.getInstance().isElevenPressed()) {
								HatchPanel.getInstance().slideMiddle();
								HatchPanel.getInstance().forward();
								try {
									Thread.sleep(1000); // TODO Need to See if is Enough Time
								} catch (final InterruptedException e) {
									e.printStackTrace();
								}
								Climber.getInstance().release();
							}
						})
				.registerDigitalCommand(Controller.Joystick1, ControlsConfig.RELEASE_CLIMBER_11,
						DigitalType.DigitalPress, () -> Climber.getInstance().setElevenPressed(true))
				.registerDigitalCommand(Controller.Joystick2, ControlsConfig.RELEASE_CLIMBER_11,
						DigitalType.DigitalRelease, () -> Climber.getInstance().setElevenPressed(false));
	}


	///////////////////////////////////////////////////////////////////////////
	// Vision which Nobody Cares About :(
	///////////////////////////////////////////////////////////////////////////

	private static void setupVision() {
		Controls.getInstance()
				.registerAnalogCommand(Controller.XboxController, ControlsConfig.START_VISION, AnalogType.OutOfThresholdMajor, value -> Vision.enable())
				.registerAnalogCommand(Controller.XboxController, ControlsConfig.STOP_VISION, AnalogType.OutOfThresholdMajor, value -> Vision.disable());
	}

	private static void setupAuton(){
		Controls.getInstance()
				.registerDigitalCommand(Controller.XboxController, ControlsConfig.RUN_AUTON, DigitalType.DigitalPress, () ->{
					if(DriverStation.getInstance().isAutonomous()){
						Drive.getInstance().enablePID();
						//in negative inches
						Drive.getInstance().moveStraight(-50); //TODO find value
					}
				}).registerDigitalCommand(Controller.XboxController, ControlsConfig.CANCEL_AUTON, DigitalType.DigitalPress, ()-> Drive.getInstance().disablePID());
	}
}