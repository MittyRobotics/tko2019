package com.amhsrobotics.tko2019.hardware.subsystems;

import com.amhsrobotics.tko2019.controls.Controller;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.commands.AnalogType;
import com.amhsrobotics.tko2019.controls.commands.DigitalType;
import com.amhsrobotics.tko2019.hardware.Gyro;
import com.amhsrobotics.tko2019.settings.ControlsConfig;
import com.amhsrobotics.tko2019.settings.Restrictions;
import com.amhsrobotics.tko2019.settings.subsystems.PID;
import com.amhsrobotics.tko2019.settings.subsystems.SolenoidIds;
import com.amhsrobotics.tko2019.settings.subsystems.TalonIds;
import com.amhsrobotics.tko2019.settings.subsystems.TalonInversions;
import com.amhsrobotics.tko2019.settings.subsystems.Thresholds;
import com.amhsrobotics.tko2019.settings.subsystems.TicksPerInch;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;

import java.util.logging.Logger;

public final class Drive {
	private final static Drive INSTANCE = new Drive();

	private final WPI_TalonSRX[] leftTalons = new WPI_TalonSRX[TalonIds.LEFT_DRIVE.length];
	private final WPI_TalonSRX[] rightTalons = new WPI_TalonSRX[TalonIds.RIGHT_DRIVE.length];
	private final DoubleSolenoid gearShifter =
			new DoubleSolenoid(SolenoidIds.DRIVE_SHIFTER[0], SolenoidIds.DRIVE_SHIFTER[1]);

	private volatile boolean isReversed = false;

	private int currentGear = 1;
	private long lastSwitch = 0;

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
				}).registerDigitalCommand(Controller.XboxController, ControlsConfig.GEAR_SWITCH,
				DigitalType.DigitalPress, () -> {
					if (System.currentTimeMillis() - lastSwitch > Restrictions.DRIVE_GEAR_SHIFT_COOLDOWN_MILLIS) {
						if (currentGear == 1) {
							shiftGear(0);
						} else {
							shiftGear(1);
						}
						lastSwitch = System.currentTimeMillis();
					} else {
						System.err.println("Shifter is on Cooldown.");
					}
				}).registerDigitalCommand(Controller.XboxController, ControlsConfig.REVERSE_DIRECTION,
				DigitalType.DigitalPress, this::toggleReverser);
	}

	public static Drive getInstance() {
		return INSTANCE;
	}

	public final void moveStraight(final double inches) {
		moveStraight(inches, 0);
	}

	public final void moveStraight(final double inches, final double breakInches) {
		final double setpoint = inches * getTicksPerInch();
		final double threshold = 0.25 * getTicksPerInch();

		leftTalons[0].set(ControlMode.Position, leftTalons[0].getSelectedSensorPosition() + setpoint);
		rightTalons[0].follow(leftTalons[0]);
		while (Math.abs(leftTalons[0].getClosedLoopError()) > breakInches + threshold) {
			Logger.getLogger("drive").finer("Error:\t" + leftTalons[0].getClosedLoopError());
			try {
				Thread.sleep(20);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

		leftTalons[0].set(ControlMode.PercentOutput, 0);
		rightTalons[0].set(ControlMode.PercentOutput, 0);
	}

	public final void turn(final double degrees) {
		final PIDController pidController = new PIDController(PID.TURN[0], PID.TURN[1], PID.TURN[2], Gyro.getInstance(), leftTalons[0]);
		pidController.setInputRange(0, 360);
		pidController.setOutputRange(-0.35, 0.35);
		pidController.setContinuous(true);

		rightTalons[0].follow(leftTalons[0]);
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
		while (pidController.getError() > Thresholds.TURN) {
			try {
				Thread.sleep(20);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		pidController.disable();

		rightTalons[0].setInverted(TalonInversions.RIGHT_DRIVE[0]);
		rightTalons[1].setInverted(TalonInversions.RIGHT_DRIVE[1]);
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
