package com.amhsrobotics.tko2019.subsystems.cargo;

import com.amhsrobotics.tko2019.controls.AnalogInput;
import com.amhsrobotics.tko2019.controls.AnalogType;
import com.amhsrobotics.tko2019.controls.ControllerID;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.DigitalType;
import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.logging.LogCapable;
import com.amhsrobotics.tko2019.settings.subsystems.PID;
import com.amhsrobotics.tko2019.settings.subsystems.TalonIds;
import com.amhsrobotics.tko2019.settings.subsystems.TicksPerInch;
import com.amhsrobotics.tko2019.subsystems.Subsystem;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import javax.naming.ldap.Control;

public class Cargo implements Subsystem {
	private final double threshold = 1 * TicksPerInch.CARGO_TPD;
	private boolean manual = false;
	private int level = 0; //0 = ground, 1 = rocket, 2 = cargo, 3 = human player height
	private WPI_TalonSRX[] intakeTalons = new WPI_TalonSRX[TalonIds.INTAKE_TALON.length];
	private WPI_TalonSRX[] conveyorTalons = new WPI_TalonSRX[TalonIds.CONVEYOR_TALON.length];

	public void init() {
		for (int talonIdIndex = 0; talonIdIndex < TalonIds.INTAKE_TALON.length; talonIdIndex++) {
			final WPI_TalonSRX talon = new WPI_TalonSRX(TalonIds.INTAKE_TALON[talonIdIndex]);
			talon.configFactoryDefault();
			intakeTalons[talonIdIndex] = talon;
			hardwareInit(talon);
		}
		for (int talonIdIndex = 0; talonIdIndex < TalonIds.CONVEYOR_TALON.length; talonIdIndex++) {
			final WPI_TalonSRX talon = new WPI_TalonSRX(TalonIds.CONVEYOR_TALON[talonIdIndex]);
			talon.configFactoryDefault();
			if (talonIdIndex == 0) {
				talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
			} else {
				talon.follow(conveyorTalons[0]);
			}
			conveyorTalons[talonIdIndex] = talon;
			hardwareInit(talon);
		}
		conveyorTalons[0].config_kP(0, PID.CARGO[0], 0);
		conveyorTalons[0].config_kP(0, PID.CARGO[1], 0);
		conveyorTalons[0].config_kP(0, PID.CARGO[2], 0);
	}

	public void initControls() {

		Controls.getInstance().registerDigitalCommand(ControllerID.Joystick2.getId(), com.amhsrobotics.tko2019.controls.DigitalInput.Joystick11, DigitalType.DigitalRelease, () -> {
			manual = !manual;
			if (manual) {
				System.out.println("Manual Mode");
			} else {
				System.out.println("Auton Mode");
			}
		});
		//manual
			Controls.getInstance().registerAnalogCommand(ControllerID.Joystick2.getId(), AnalogInput.JoystickY, AnalogType.OutOfThresholdMinor, (value) -> {
				if(manual){
					spinIntake(value, value);
				}
				else {
					if(Math.abs(value) < 0.05){
						intakeOuttakeMacro();
					} else {
						stopIntake();
					}
				}
			});
			Controls.getInstance().registerDigitalCommand(ControllerID.Joystick2.getId(), com.amhsrobotics.tko2019.controls.DigitalInput.Joystick3, DigitalType.DigitalHold, () -> {
				if(manual){
					conveyorTalons[0].set(ControlMode.Position, conveyorTalons[1].getSelectedSensorPosition() + 1);
				}
				else {
					if ((conveyorTalons[0].getSelectedSensorPosition() < IntakeHeights.ROCKET_HEIGHT * TicksPerInch.CARGO_TPD - 100)) {
						rocketConveyor();
						level = 1;
						System.out.println("Rocket Height");
					} else if (conveyorTalons[0].getSelectedSensorPosition() < IntakeHeights.CARGO_HEIGHT * TicksPerInch.CARGO_TPD - 100) {
						cargoConveyor();
						level = 2;
						System.out.println("Cargo Height");
					} else {
						intakeConveyor();
						level = 3;
						System.out.println("Human Player Height");
						//0 = ground, 1 = rocket, 2 = cargo 3 = human player height
					}
				}
            });
			Controls.getInstance().registerDigitalCommand(ControllerID.Joystick2.getId(), com.amhsrobotics.tko2019.controls.DigitalInput.Joystick2, DigitalType.DigitalHold, () -> {
				if(manual){
					conveyorTalons[0].set(ControlMode.Position, conveyorTalons[1].getSelectedSensorPosition() - 1);
				}
				else {
					if ((conveyorTalons[0].getSelectedSensorPosition() > IntakeHeights.CARGO_HEIGHT * TicksPerInch.CARGO_TPD + 100)) {
						cargoConveyor();
						level = 2;
						System.out.println("Cargo Height");
					} else if (conveyorTalons[0].getSelectedSensorPosition() > IntakeHeights.ROCKET_HEIGHT * TicksPerInch.CARGO_TPD + 100) {
						rocketConveyor();
						level = 1;
						System.out.println("Rocket Height");
					} else {
						groundConveyor();
						level = 0;
						System.out.println("Intake Height");
					}
						//0 = ground, 1 = rocket, 2 = cargo 3 = human player height
					}

            });
		}

	private void intakeOuttakeMacro() {
		if (level == 0) {
			spinIntake(IntakeSpeeds.GROUND_SPEED, IntakeSpeeds.GROUND_SPEED);
		} else if (level == 1) {
			spinOuttake(IntakeSpeeds.ROCKET_SPEED, IntakeSpeeds.ROCKET_SPEED);
		} else if (level == 2) {
			spinOuttake(-IntakeSpeeds.CARGO_SPEED, IntakeSpeeds.CARGO_SPEED);
		} else if (level == 3) {
			spinIntake(IntakeSpeeds.STATION_SPEED, IntakeSpeeds.STATION_SPEED);
		} else {
			stopIntake();
		}
	}


	private void spinIntake(double topSpeed, double bottomSpeed) {
		if (Switches.getInstance().intakeSensor.get()) {
			visionConveyor();
			stopIntake();
		}

		intakeTalons[0].set(ControlMode.PercentOutput, topSpeed);
		intakeTalons[1].set(ControlMode.PercentOutput, -bottomSpeed);
	}

	private void spinOuttake(double topSpeed, double bottomSpeed) {
		intakeTalons[0].set(ControlMode.PercentOutput, -topSpeed);
		intakeTalons[1].set(ControlMode.PercentOutput, bottomSpeed);
	}

	private void stopIntake() {
		intakeTalons[0].set(ControlMode.PercentOutput, 0);
		intakeTalons[1].set(ControlMode.PercentOutput, 0);
	}

	private void moveConveyor(double neededPos) {
		double error1 = conveyorTalons[0].getClosedLoopError();
		neededPos += conveyorTalons[0].getSelectedSensorPosition();
		conveyorTalons[0].set(ControlMode.Position, neededPos * TicksPerInch.CARGO_TPD);

		while ((Math.abs(error1) > threshold)) {
			error1 = conveyorTalons[0].getClosedLoopError();
			System.out.println("T1: " + conveyorTalons[0].getSelectedSensorPosition());
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		conveyorTalons[0].set(ControlMode.PercentOutput, 0);
	}

	private void rocketConveyor() {
		moveConveyor(IntakeHeights.ROCKET_HEIGHT);
	}

	private void cargoConveyor() {
		moveConveyor(IntakeHeights.CARGO_HEIGHT);
	}

	private void intakeConveyor() {
		moveConveyor(IntakeHeights.STATION_HEIGHT);
	}

	private void visionConveyor() {
		moveConveyor(IntakeHeights.VISION_HEIGHT);
	}

	private void groundConveyor() {
		moveConveyor(IntakeHeights.GROUND_HEIGHT);
	}

	private void calibrateConveyor() {
		while (!Switches.getInstance().conveyorLimits[0].get()) {
			conveyorTalons[0].set(ControlMode.PercentOutput, 0.5);
		}
		conveyorTalons[0].setSelectedSensorPosition(90);
	}
}