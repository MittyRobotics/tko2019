package com.amhsrobotics.tko2019.hardware.subsystems;

import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.hardware.settings.subsystems.EncoderInversions;
import com.amhsrobotics.tko2019.hardware.settings.subsystems.NeutralModes;
import com.amhsrobotics.tko2019.hardware.settings.subsystems.PID;
import com.amhsrobotics.tko2019.hardware.settings.subsystems.TalonIds;
import com.amhsrobotics.tko2019.hardware.settings.subsystems.TalonInversions;
import com.amhsrobotics.tko2019.hardware.settings.subsystems.cargo.IntakeHeights;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DriverStation;

public final class Cargo {
	private static final Cargo INSTANCE = new Cargo();

	public final WPI_TalonSRX[] conveyorTalons = new WPI_TalonSRX[TalonIds.CONVEYOR.length];
	private final WPI_TalonSRX[] intakeTalons = new WPI_TalonSRX[TalonIds.INTAKE.length];

	//	private final DoubleSolenoid holdIntake = new DoubleSolenoid(SolenoidIds.HOLD_INTAKE[0], SolenoidIds.HOLD_INTAKE[1]);
	private boolean intakeLocked = true;

	private Cargo() {
		// Intake Talons
		for (int i = 0; i < TalonIds.INTAKE.length; i++) {
			final WPI_TalonSRX talon = new WPI_TalonSRX(TalonIds.INTAKE[i]);
			talon.configFactoryDefault();
			talon.setInverted(TalonInversions.INTAKE[i]);
			talon.setNeutralMode(NeutralModes.INTAKE);
			intakeTalons[i] = talon;
		}

		// Conveyor Talons
		for (int i = 0; i < TalonIds.CONVEYOR.length; i++) {
			final WPI_TalonSRX talon = new WPI_TalonSRX(TalonIds.CONVEYOR[i]);
			talon.configFactoryDefault();
			talon.setInverted(TalonInversions.CONVEYOR[i]);
			talon.configClosedLoopPeakOutput(0, 0.47);
			if (i == 0) {
				talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
				talon.setSensorPhase(EncoderInversions.CONVEYOR_ENCODER);
				talon.setNeutralMode(NeutralModes.CONVEYOR);
				talon.config_kP(0, PID.CARGO[0]);
				talon.config_kI(0, PID.CARGO[1]);
				talon.config_kD(0, PID.CARGO[2]);
			} else {
				talon.set(ControlMode.Follower, conveyorTalons[0].getDeviceID());
			}
			conveyorTalons[i] = talon;
		}
	}

	public static Cargo getInstance() {
		return INSTANCE;
	}


	///////////////////////////////////////////////////////////////////////////
	// Intake
	///////////////////////////////////////////////////////////////////////////

	public final void spinIntake() {
		if (!Switches.getInstance().hasCargo()) {
			intakeTalons[0].set(ControlMode.PercentOutput, 0.8);
			intakeTalons[1].set(ControlMode.PercentOutput, 0.8);
		} else {
//			visionConveyor();
			stopIntake();
		}
	}

	public final void spinOuttake() {
		intakeTalons[0].set(ControlMode.PercentOutput, -0.8);
		intakeTalons[1].set(ControlMode.PercentOutput, 0.3);
	}

	public final void stopIntake() {
		intakeTalons[0].set(ControlMode.PercentOutput, 0);
		intakeTalons[1].set(ControlMode.PercentOutput, 0);
	}

//	public final void holdIntake(){
//		if(intakeLocked){
//			holdIntake.set(DoubleSolenoid.Value.kForward);
//		}
//		else{
//			holdIntake.set(DoubleSolenoid.Value.kReverse);
//		}
//		intakeLocked = !intakeLocked;
//	}


	///////////////////////////////////////////////////////////////////////////
	// Conveyor
	///////////////////////////////////////////////////////////////////////////

	public final void stationConveyor() {
		moveConveyor(IntakeHeights.STATION_HEIGHT);
	}

	public final void cargoConveyor() {
		moveConveyor(IntakeHeights.CARGO_HEIGHT);
	}

	public final void rocketConveyor() {
		moveConveyor(IntakeHeights.ROCKET_HEIGHT);
	}

	public final void visionConveyor() {
		moveConveyor(IntakeHeights.VISION_HEIGHT);
	}

	public final void groundConveyor() {
		moveConveyor(IntakeHeights.GROUND_HEIGHT);
	}

	public final void manualConveyor(final double value) {
		conveyorTalons[0].set(ControlMode.PercentOutput, value);
	}

	public void moveConveyor(final double neededPos) { // In negative ticks (more negative moving up) FIXME wtf? just invert the sensor and the output???
		conveyorTalons[0].set(ControlMode.Position, neededPos);
	}


	///////////////////////////////////////////////////////////////////////////
	// Reset Encoders
	///////////////////////////////////////////////////////////////////////////

	public final void zeroEncoder() {
		conveyorTalons[0].set(ControlMode.PercentOutput, -0.3);
		while (!conveyorTalons[0].getSensorCollection().isRevLimitSwitchClosed() && DriverStation.getInstance().isTest()) {
//			System.out.println(conveyorTalons[0].getSelectedSensorPosition());
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		conveyorTalons[0].set(ControlMode.PercentOutput, 0);
		System.out.println("Here");
		conveyorTalons[0].setSelectedSensorPosition(0);
	}
}
