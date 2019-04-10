package com.amhsrobotics.tko2019.hardware.subsystems;

import com.amhsrobotics.tko2019.hardware.Gyro;
import com.amhsrobotics.tko2019.hardware.settings.subsystems.EncoderInversions;
import com.amhsrobotics.tko2019.hardware.settings.subsystems.NeutralModes;
import com.amhsrobotics.tko2019.hardware.settings.subsystems.PID;
import com.amhsrobotics.tko2019.hardware.settings.subsystems.SolenoidIds;
import com.amhsrobotics.tko2019.hardware.settings.subsystems.TalonIds;
import com.amhsrobotics.tko2019.hardware.settings.subsystems.TalonInversions;
import com.amhsrobotics.tko2019.hardware.settings.subsystems.TicksPerInch;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;

public final class Drive {
	private boolean runPID = true;
	private static final Drive INSTANCE = new Drive();

	public final WPI_TalonSRX[] leftTalons = new WPI_TalonSRX[TalonIds.LEFT_DRIVE.length];
	public final WPI_TalonSRX[] rightTalons = new WPI_TalonSRX[TalonIds.RIGHT_DRIVE.length];
	public final DoubleSolenoid gearShifter = new DoubleSolenoid(
			SolenoidIds.DRIVE_SHIFTER[0], SolenoidIds.DRIVE_SHIFTER[1]
	);

	private volatile boolean reversed = false;
	private volatile int currentGear = 1;

	private Drive() {
		// Left Drive
		for (int i = 0; i < TalonIds.LEFT_DRIVE.length; i++) {
			final WPI_TalonSRX talon = new WPI_TalonSRX(TalonIds.LEFT_DRIVE[i]);
			talon.configFactoryDefault();
			talon.setInverted(TalonInversions.LEFT_DRIVE[i]);
			talon.setNeutralMode(NeutralModes.DRIVE);
			if (i == 0) {
				talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
				talon.setSensorPhase(EncoderInversions.DRIVE_LEFT);
				talon.config_kP(0, PID.DRIVE[0]);
				talon.config_kI(0, PID.DRIVE[1]);
				talon.config_kD(0, PID.DRIVE[2]);
				talon.configClosedLoopPeakOutput(0, 0.5);
			}
// 			else {
//				talon.set(ControlMode.Follower, TalonIds.LEFT_DRIVE[0]);
//			}
			leftTalons[i] = talon;
		}

		// Right Drive
		for (int i = 0; i < TalonIds.RIGHT_DRIVE.length; i++) {
			final WPI_TalonSRX talon = new WPI_TalonSRX(TalonIds.RIGHT_DRIVE[i]);
			talon.configFactoryDefault();
			talon.setInverted(TalonInversions.RIGHT_DRIVE[i]);
			talon.setNeutralMode(NeutralModes.DRIVE);
			if (i == 0) {
				talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
				talon.setSensorPhase(EncoderInversions.DRIVE_RIGHT);
				talon.config_kP(0, PID.DRIVE[0]);
				talon.config_kI(0, PID.DRIVE[1]);
				talon.config_kD(0, PID.DRIVE[2]);
				talon.configClosedLoopPeakOutput(0, 0.5);
			}
//			else {
//				talon.set(ControlMode.Follower, TalonIds.RIGHT_DRIVE[0]);
//			}
			rightTalons[i] = talon;
		}
	}

	public static Drive getInstance() {
		return INSTANCE;
	}

	public final boolean isReversed() {
		return reversed;
	}


	///////////////////////////////////////////////////////////////////////////
	// Talon Sets
	///////////////////////////////////////////////////////////////////////////

	public final void set(final double value) {
		set(ControlMode.PercentOutput, value);
	}

	public final void set(final ControlMode controlMode, final double value) {
		leftTalons[0].set(controlMode, value);
		rightTalons[0].set(controlMode, value);
	}

	public final void setLeft(final double value) {
		if (!reversed) {
			setLeft(ControlMode.PercentOutput, value);
		} else {
			setRight(ControlMode.PercentOutput, -value);
		}
	}

	public final void setLeft(final ControlMode controlMode, final double value) {
		leftTalons[0].set(controlMode, value);
		leftTalons[1].set(controlMode, value);
	}

	public final void setRight(final double value) {
		if (!reversed) {
			setRight(ControlMode.PercentOutput, value);
		} else {
			setLeft(ControlMode.PercentOutput, -value);
		}
	}

	public final void setRight(final ControlMode controlMode, final double value) {
		rightTalons[0].set(controlMode, value);
		rightTalons[1].set(controlMode, value);
	}

	///////////////////////////////////////////////////////////////////////////
	// PID
	///////////////////////////////////////////////////////////////////////////

	public final void moveStraight(final double inches) {
		moveStraight(inches, 500);
	}

	public final void moveStraight(final double inches, final double waitTime) {
		shiftLow();
		final double threshold = 100;

		leftTalons[0].set(ControlMode.Position, leftTalons[0].getSelectedSensorPosition() + inches * TicksPerInch.DRIVE[currentGear]);
		rightTalons[0].set(ControlMode.Position, rightTalons[0].getSelectedSensorPosition() + inches * TicksPerInch.DRIVE[currentGear]);

		final long startingTime = System.currentTimeMillis();
		while (DriverStation.getInstance().isEnabled()) {
			if(System.currentTimeMillis() - startingTime > waitTime
			&& Math.abs(leftTalons[0].getClosedLoopError()) < threshold
			&& Math.abs(rightTalons[0].getClosedLoopError()) < threshold){
				break;
			}
			if(!runPID){
				break;
			}
			leftTalons[1].set(ControlMode.PercentOutput, leftTalons[0].getMotorOutputPercent());
			rightTalons[1].set(ControlMode.PercentOutput, rightTalons[0].getMotorOutputPercent());
		}
		set(0);
	}

	public final synchronized void turn(final double degrees) {
		turn(degrees, 3000);
	}

	public final synchronized void turn(final double degrees, final int breakoutTime) {
		// Talon Setup
		setRight(ControlMode.Follower, leftTalons[0].getDeviceID());
		rightTalons[0].setInverted(!TalonInversions.RIGHT_DRIVE[0]);
		rightTalons[1].setInverted(!TalonInversions.RIGHT_DRIVE[1]); // TODO Check if Needed

		// PID Controller Setup
		final PIDController pid = new PIDController(PID.TURN[0], PID.TURN[1], PID.TURN[2],
				Gyro.getInstance(), leftTalons[0]);
		pid.setContinuous(true);
		pid.setInputRange(-180, 180);
		double angle = Gyro.getInstance().getAngle() + degrees;
		while (angle < -180) {
			angle += 360;
		}
		while (angle >= 180) {
			angle -= 360;
		}
		pid.setSetpoint(angle);

		// Start PID Loop
		int count = 0;
		pid.enable();
		final long startingTime = System.currentTimeMillis();
		while (DriverStation.getInstance().isEnabled() && System.currentTimeMillis() - startingTime < breakoutTime
				&& count < 200) {
			if (Math.abs(pid.getError()) < 10) {
				count++;
			} else {
				count = 0;
			}
			try {
				Thread.sleep(10);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

		// End PID Control
		pid.disable();
		setLeft(0);
		setRight(0);

		// Reset Talon Configurations
		rightTalons[0].setInverted(TalonInversions.RIGHT_DRIVE[0]);
		rightTalons[1].setInverted(TalonInversions.RIGHT_DRIVE[1]); // FIXME Connected Above
	}


	///////////////////////////////////////////////////////////////////////////
	// Drive Settings
	///////////////////////////////////////////////////////////////////////////

	public final synchronized void toggleReverser() {
		reversed = !reversed;
	}
	public final void shiftLow(){
		gearShifter.set(DoubleSolenoid.Value.kForward);
		currentGear = 0;
	}
	public final void shiftHigh(){
		gearShifter.set(DoubleSolenoid.Value.kReverse);
		currentGear = 1;
	}

	public final synchronized void shiftGear() {
		switch (currentGear) {
			case 0:
				gearShifter.set(DoubleSolenoid.Value.kReverse);
				currentGear = 1;
				break;
			case 1:
				gearShifter.set(DoubleSolenoid.Value.kForward);
				currentGear = 0;
				break;
		}
	}
	public final void disablePID(){
		runPID = false;
	}
	public final void enablePID(){
		runPID = true;
	}
}
