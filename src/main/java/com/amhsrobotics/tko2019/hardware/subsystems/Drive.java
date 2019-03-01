package com.amhsrobotics.tko2019.hardware.subsystems;

import com.amhsrobotics.tko2019.controls.Controller;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.commands.AnalogType;
import com.amhsrobotics.tko2019.controls.commands.DigitalType;
import com.amhsrobotics.tko2019.hardware.Gyro;
import com.amhsrobotics.tko2019.settings.ControlsConfig;
import com.amhsrobotics.tko2019.settings.subsystems.PID;
import com.amhsrobotics.tko2019.settings.subsystems.SolenoidIds;
import com.amhsrobotics.tko2019.settings.subsystems.TalonIds;
import com.amhsrobotics.tko2019.settings.subsystems.TalonInversions;
import com.amhsrobotics.tko2019.settings.subsystems.TicksPerInch;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;

public final class Drive {
	private final static Drive INSTANCE = new Drive();

	private final WPI_TalonSRX[] leftTalons = new WPI_TalonSRX[TalonIds.LEFT_DRIVE.length];
	private final WPI_TalonSRX[] rightTalons = new WPI_TalonSRX[TalonIds.RIGHT_DRIVE.length];
	private final DoubleSolenoid gearShifter =
			new DoubleSolenoid(SolenoidIds.DRIVE_SHIFTER[0], SolenoidIds.DRIVE_SHIFTER[1]);

	private volatile boolean isReversed = false;

	private int currentGear = 1;
	private long lastSwitch = 0;
	private boolean didTurn = false;

	private Drive() {
		for (int i = 0; i < TalonIds.LEFT_DRIVE.length; i++) {
			final WPI_TalonSRX talon = new WPI_TalonSRX(TalonIds.LEFT_DRIVE[i]);
			talon.configFactoryDefault();
			talon.setInverted(TalonInversions.LEFT_DRIVE[i]);
			if (i == 0) {
				talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
			} else {
				talon.follow(leftTalons[0]);
			}
			leftTalons[i] = talon;
		}
		for (int i = 0; i < TalonIds.RIGHT_DRIVE.length; i++) {
			final WPI_TalonSRX talon = new WPI_TalonSRX(TalonIds.RIGHT_DRIVE[i]);
			talon.configFactoryDefault();
			talon.setInverted(TalonInversions.RIGHT_DRIVE[i]);
			if (i == 0) {
				talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
				talon.config_kP(0, PID.DRIVE[0]);
				talon.config_kI(0, PID.DRIVE[1]);
				talon.config_kD(0, PID.DRIVE[2]);
			} else {
				talon.follow(rightTalons[0]);
			}
			rightTalons[i] = talon;
		}
		rightTalons[0].setSensorPhase(true);


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

	public static Drive getInstance() {
		return INSTANCE;
	}

	public final void moveStraight(final double inches) {
		moveStraight(inches, 500);
	}

	public final void moveStraight(final double inches, final double waitTime) {
		leftTalons[0].setNeutralMode(NeutralMode.Brake);
		leftTalons[1].setNeutralMode(NeutralMode.Brake);
		rightTalons[0].setNeutralMode(NeutralMode.Brake);
		rightTalons[1].setNeutralMode(NeutralMode.Brake);
		if (inches < 13) {
			rightTalons[0].config_kP(0, 0.25, 0);
		} else {
			rightTalons[0].config_kP(0, PID.DRIVE[0], 0);
		}
		final double threshold = 0.5 * 160;

		leftTalons[0].set(ControlMode.Follower, rightTalons[0].getDeviceID());
		leftTalons[1].set(ControlMode.Follower, rightTalons[0].getDeviceID());
		final double setpoint;
//		if (didTurn) {
			setpoint = rightTalons[0].getSelectedSensorPosition() + inches * 160;
//		} else {
//			setpoint = rightTalons[0].getSelectedSensorPosition() + inches * 160;
//		}
		rightTalons[0].set(ControlMode.Position, setpoint);
		long time = System.currentTimeMillis();
		while (DriverStation.getInstance().isEnabled()) {
			if (System.currentTimeMillis() - time > waitTime && rightTalons[0].getClosedLoopError() < threshold) {
				break;
			}
			System.out.println("POS: " + rightTalons[0].getSelectedSensorPosition());
			System.out.println("GOAL: " + rightTalons[0].getClosedLoopTarget());
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Here");
		leftTalons[0].set(ControlMode.PercentOutput, 0);
		rightTalons[0].set(ControlMode.PercentOutput, 0);
		leftTalons[1].set(ControlMode.Follower, leftTalons[0].getDeviceID());
//		leftTalons[0].setNeutralMode(NeutralMode.Coast);
//		leftTalons[1].setNeutralMode(NeutralMode.Coast);
//		rightTalons[0].setNeutralMode(NeutralMode.Coast);
//		rightTalons[1].setNeutralMode(NeutralMode.Coast);
//		System.out.println(setpoint);
//		while (DriverStation.getInstance().isEnabled()) {
////			System.out.println("(Left) T1: " + leftDriveTalons[0].getSelectedSensorPosition());
////			System.out.println("(Right) T1: " + rightDriveTalons[0].getSelectedSensorPosition());
//
//			try {
//				Thread.sleep(20);
//			} catch (final InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		System.out.println("here");
//		leftDriveTalons[0].set(ControlMode.PercentOutput, 0);
//		rightDriveTalons[0].set(ControlMode.PercentOutput, 0);
		didTurn = false;
	}

	public final void turn(final double degrees) {
		int count = 0;
		final PIDController pidController = new PIDController(0.1, 0, 0, Gyro.getInstance(), rightTalons[0]);

		pidController.setInputRange(0, 360);
		pidController.setOutputRange(-0.35, 0.35);
		pidController.setContinuous(true);
		leftTalons[0].set(ControlMode.Follower, rightTalons[0].getDeviceID());
		rightTalons[0].setInverted(!TalonInversions.RIGHT_DRIVE[0]);
		rightTalons[1].setInverted(!TalonInversions.RIGHT_DRIVE[1]);

		double angle = degrees + Gyro.getInstance().getAngle();
		if (angle >= 360) {
			angle -= 360;
		} else if (angle < 0) {
			angle += 360;
		}

		pidController.setSetpoint(angle);
		pidController.enable();
		final long startTime = System.currentTimeMillis();
		while (count < 200 && DriverStation.getInstance().isEnabled()) {
			if (Math.abs(pidController.getError()) < 10) {
				count++;
			} else {
				count = 0;
			}
			if(System.currentTimeMillis() - startTime > 3000){
				break;
			}
			System.out.println(pidController.getError());
			try {
				Thread.sleep(10);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Turn Done");
		pidController.close();


		rightTalons[0].setInverted(TalonInversions.RIGHT_DRIVE[0]);
		rightTalons[1].setInverted(TalonInversions.RIGHT_DRIVE[1]);
		didTurn = true;
	}

	public synchronized void set(final double value) {
		set(ControlMode.PercentOutput, value);
	}

	public synchronized void set(final ControlMode controlMode, final double value) {
		leftTalons[0].set(controlMode, value);
		rightTalons[0].set(controlMode, value);
	}

	public synchronized void setLeft(final double value) {
		setLeft(ControlMode.PercentOutput, value);
	}

	public synchronized void setLeft(final ControlMode controlMode, final double value) {
		leftTalons[0].set(controlMode, value);
	}

	public synchronized void setRight(final double value) {
		setRight(ControlMode.PercentOutput, value);
	}

	public synchronized void setRight(final ControlMode controlMode, final double value) {
		rightTalons[0].set(controlMode, value);
	}

	private synchronized void toggleReverser() {
		isReversed = !isReversed;
	}

	public synchronized void shiftGear(final int value) {
		if (value == 0) {
			gearShifter.set(DoubleSolenoid.Value.kReverse);
			currentGear = 0;
		} else if (value == 1) {
			gearShifter.set(DoubleSolenoid.Value.kForward);
			currentGear = 1;
		}
	}

	private double getTicksPerInch() {
		return TicksPerInch.DRIVE[currentGear];
	}
}
