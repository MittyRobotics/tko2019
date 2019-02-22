package com.amhsrobotics.tko2019.hardware.subsystems;

import com.amhsrobotics.tko2019.controls.Controller;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.commands.AnalogType;
import com.amhsrobotics.tko2019.controls.commands.DigitalType;
import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.settings.ControlsConfig;
import com.amhsrobotics.tko2019.settings.subsystems.EncoderInversions;
import com.amhsrobotics.tko2019.settings.subsystems.PID;
import com.amhsrobotics.tko2019.settings.subsystems.TalonIds;
import com.amhsrobotics.tko2019.settings.subsystems.TalonInversions;
import com.amhsrobotics.tko2019.settings.subsystems.cargo.IntakeHeights;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Cargo {
	private final static Cargo INSTANCE = new Cargo();
	private final WPI_TalonSRX[] intakeTalons = new WPI_TalonSRX[TalonIds.INTAKE.length];
	private final WPI_TalonSRX[] conveyorTalons = new WPI_TalonSRX[TalonIds.CONVEYOR.length];
	private boolean manual = false;
	private boolean configEncoder = false;

	private Cargo() {
		for (int talonIdIndex = 0; talonIdIndex < TalonIds.INTAKE.length; talonIdIndex++) {
			final WPI_TalonSRX talon = new WPI_TalonSRX(TalonIds.INTAKE[talonIdIndex]);
			talon.configFactoryDefault();
			talon.setInverted(TalonInversions.INTAKE[talonIdIndex]);
			intakeTalons[talonIdIndex] = talon;
		}
		for (int talonIdIndex = 0; talonIdIndex < TalonIds.CONVEYOR.length; talonIdIndex++) {
			final WPI_TalonSRX talon = new WPI_TalonSRX(TalonIds.CONVEYOR[talonIdIndex]);
			talon.configFactoryDefault();
			talon.setInverted(TalonInversions.CONVEYOR[talonIdIndex]);
			if (talonIdIndex == 0) {
				talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
				talon.setSensorPhase(EncoderInversions.CONVEYOR_ENCODER);
				talon.config_kP(0, PID.CARGO[0]);
				talon.config_kI(0, PID.CARGO[1]);
				talon.config_kD(0, PID.CARGO[2]);
			} else {
				talon.follow(conveyorTalons[0]);
			}
			conveyorTalons[talonIdIndex] = talon;
		}


		Controls.getInstance().registerDigitalCommand(Controller.Joystick2, ControlsConfig.SWITCH_MODE, DigitalType.DigitalPress, () -> {
			manual = !manual;
			if(manual){
				System.out.println("Manual Mode");
			}
			else {
				System.out.println("Automatic Mode");
			}
		});
		Controls.getInstance().registerDigitalCommand(Controller.Joystick2, ControlsConfig.SPIN_INTAKE, DigitalType.DigitalHold, () -> spinIntake(0.5, 0.5));
		Controls.getInstance().registerDigitalCommand(Controller.Joystick2, ControlsConfig.SPIN_INTAKE, DigitalType.DigitalRelease, this::stopIntake);
		Controls.getInstance().registerDigitalCommand(Controller.Joystick2, ControlsConfig.SPIN_OUTTAKE, DigitalType.DigitalHold, () -> spinOuttake(0.5, 0.5));
		Controls.getInstance().registerDigitalCommand(Controller.Joystick2, ControlsConfig.SPIN_OUTTAKE, DigitalType.DigitalRelease, this::stopIntake);

		Controls.getInstance().registerAnalogCommand(Controller.Joystick2, ControlsConfig.MOVE_ANGLE, AnalogType.OutOfThresholdMinor, value -> {
				moveConveyor(conveyorTalons[0].getSelectedSensorPosition() + value * 1000);
		}).registerDigitalCommand(Controller.Joystick2, ControlsConfig.CARGO_HEIGHT, DigitalType.DigitalPress, ()->{
			if(configEncoder){
				cargoConveyor();
			}
		}).registerDigitalCommand(Controller.Joystick2, ControlsConfig.ROCKET_HEIGHT, DigitalType.DigitalPress, ()->{
			if(configEncoder){
				rocketConveyor();
			}
		}).registerDigitalCommand(Controller.Joystick2, ControlsConfig.GROUND_HEIGHT, DigitalType.DigitalPress, ()->{
			if(configEncoder){
				groundConveyor();
			}
		}).registerDigitalCommand(Controller.Joystick2, ControlsConfig.STATION_HEIGHT, DigitalType.DigitalPress, ()->{
			if(configEncoder){
				stationConveyor();
			}
		}).registerDigitalCommand(Controller.Joystick2, ControlsConfig.CONFIG_ENCODER, DigitalType.DigitalPress, this::resetEncoder);
	}

	public static Cargo getInstance() {
		return INSTANCE;
	}

	public void spinIntake(double topSpeed, double bottomSpeed) {
		if (!Switches.getInstance().getIntakeSensor()) { //Switch is inverted
			visionConveyor();
			stopIntake();
		}
		else {
			intakeTalons[0].set(ControlMode.PercentOutput, topSpeed);
			intakeTalons[1].set(ControlMode.PercentOutput, -bottomSpeed);
		}
	}

	public void spinOuttake(double topSpeed, double bottomSpeed) {
		intakeTalons[0].set(ControlMode.PercentOutput, -topSpeed);
		intakeTalons[1].set(ControlMode.PercentOutput, bottomSpeed);
	}

	public void stopIntake() {
		intakeTalons[0].set(ControlMode.PercentOutput, 0);
		intakeTalons[1].set(ControlMode.PercentOutput, 0);
	}

	private void moveConveyor(double neededPos) { // In negative ticks (more negative moving up)
		conveyorTalons[0].set(ControlMode.Position, neededPos);
	}

	public void rocketConveyor() {
		moveConveyor(IntakeHeights.ROCKET_HEIGHT);
	}

	public void cargoConveyor() {
		moveConveyor(IntakeHeights.CARGO_HEIGHT);
	}

	public void stationConveyor() {
		moveConveyor(IntakeHeights.STATION_HEIGHT);
	}

	private void visionConveyor() {
		moveConveyor(IntakeHeights.VISION_HEIGHT);
	}

	private void groundConveyor() {
		moveConveyor(IntakeHeights.GROUND_HEIGHT);
	}

	private void resetEncoder() {
		conveyorTalons[0].set(ControlMode.PercentOutput, 0.1);
		while (!conveyorTalons[0].getSensorCollection().isFwdLimitSwitchClosed()) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		conveyorTalons[0].set(ControlMode.PercentOutput, 0);
		conveyorTalons[0].setSelectedSensorPosition(0);
		configEncoder = true;
	}

}