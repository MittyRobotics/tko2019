package com.amhsrobotics.tko2019.hardware.subsystems;

import com.amhsrobotics.tko2019.controls.ControllerID;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.commands.AnalogType;
import com.amhsrobotics.tko2019.controls.commands.DigitalType;
import com.amhsrobotics.tko2019.hardware.Subsystem;
import com.amhsrobotics.tko2019.settings.ControlsConfig;
import com.amhsrobotics.tko2019.settings.subsystems.PID;
import com.amhsrobotics.tko2019.settings.subsystems.SolenoidIds;
import com.amhsrobotics.tko2019.settings.subsystems.TalonIds;
import com.amhsrobotics.tko2019.settings.subsystems.TalonInversions;
import com.amhsrobotics.tko2019.settings.subsystems.Thresholds;
import com.amhsrobotics.tko2019.settings.subsystems.TicksPerInch;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PIDController;

import java.util.logging.Logger;

public final class Drive implements Subsystem {
	private final static Drive INSTANCE = new Drive();

	private final WPI_TalonSRX[] lTalons = new WPI_TalonSRX[TalonIds.LEFT_DRIVE.length];
	private final WPI_TalonSRX[] rTalons = new WPI_TalonSRX[TalonIds.RIGHT_DRIVE.length];
	private final ADXRS450_Gyro gyro;
	private final DoubleSolenoid gearShifter;

	private int gear = 1;
	private long lastSwitch = 0;
	private boolean shouldReverse = false;

	private Drive() {
		for (int talonIdIndex = 0; talonIdIndex < TalonIds.LEFT_DRIVE.length; talonIdIndex++) {
			final WPI_TalonSRX talon = new WPI_TalonSRX(TalonIds.LEFT_DRIVE[talonIdIndex]);
			talon.setInverted(TalonInversions.LEFT_DRIVE[talonIdIndex]);
			talon.setNeutralMode(NeutralMode.Coast);
			talon.configFactoryDefault();
			if (talonIdIndex == 0) {
				talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
			} else {
				talon.follow(lTalons[0]);
			}
			lTalons[talonIdIndex] = talon;
		}
		for (int talonIdIndex = 0; talonIdIndex < TalonIds.RIGHT_DRIVE.length; talonIdIndex++) {
			final WPI_TalonSRX talon = new WPI_TalonSRX(TalonIds.RIGHT_DRIVE[talonIdIndex]);
			talon.setInverted(TalonInversions.RIGHT_DRIVE[talonIdIndex]);
			talon.setNeutralMode(NeutralMode.Coast);
			talon.configFactoryDefault();
			if (talonIdIndex == 0) {
				talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
			} else {
				talon.follow(rTalons[0]);
			}
			rTalons[talonIdIndex] = talon;
		}

		gearShifter = new DoubleSolenoid(SolenoidIds.DRIVE_SHIFTER[0], SolenoidIds.DRIVE_SHIFTER[1]);
		gearShifter.setName("Gear Shifting Solenoid");
		gyro = new ADXRS450_Gyro();


		Controls.getInstance().registerAnalogCommand(ControllerID.XboxController.getId(), ControlsConfig.LEFT_WHEELS, AnalogType.OutOfThresholdMinor, value -> {
			if (shouldReverse) {
				moveRight(-value);
			} else {
				moveLeft(value);
			}
		});
		Controls.getInstance().registerAnalogCommand(ControllerID.XboxController.getId(), ControlsConfig.LEFT_WHEELS, AnalogType.InThresholdMinor, value -> {
			if (shouldReverse) {
				moveRight(0);
			} else {
				moveLeft(0);
			}
		});
		Controls.getInstance().registerAnalogCommand(ControllerID.XboxController.getId(), ControlsConfig.RIGHT_WHEELS, AnalogType.OutOfThresholdMinor, value -> {
			if (shouldReverse) {
				moveLeft(-value);
			} else {
				moveRight(value);
			}
		});
		Controls.getInstance().registerAnalogCommand(ControllerID.XboxController.getId(), ControlsConfig.RIGHT_WHEELS, AnalogType.InThresholdMinor, value -> {
			if (shouldReverse) {
				moveLeft(0);
			} else {
				moveRight(0);
			}
		});
		Controls.getInstance().registerDigitalCommand(ControllerID.XboxController.getId(), ControlsConfig.GEAR_SWITCH, DigitalType.DigitalPress, () -> {
			if (System.currentTimeMillis() - lastSwitch > 1000) {
				if (gear == 1) {
					shiftGear(0);
				} else {
					shiftGear(1);
				}
				lastSwitch = System.currentTimeMillis();
			} else {
				System.err.println("Shifter is on Cooldown.");
			}
		});
		Controls.getInstance().registerDigitalCommand(ControllerID.XboxController.getId(), ControlsConfig.REVERSE_DIRECTION, DigitalType.DigitalPress, () -> {
			toggleReverser(!shouldReverse);
		});
	}

	public static Drive getInstance() {
		return INSTANCE;
	}

	public void moveStraight(final double inches) {
		moveStraight(inches, 0);
	}

	public void moveStraight(final double inches, final double breakInches) {
		final double setpoint = inches * TicksPerInch.DRIVE[gear];
		final double threshold = TicksPerInch.DRIVE[gear] * 0.25;

		lTalons[0].set(ControlMode.Position, lTalons[0].getSelectedSensorPosition() + setpoint);
		rTalons[0].set(ControlMode.Follower, lTalons[0].getDeviceID());
		while (Math.abs(lTalons[0].getClosedLoopError()) > breakInches + threshold) {
			Logger.getLogger("drive").finer("Error:\t" + lTalons[0].getClosedLoopError());
			try {
				Thread.sleep(20);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

		lTalons[0].set(ControlMode.PercentOutput, 0);
		rTalons[0].set(ControlMode.PercentOutput, 0);
	}

	public void turn(final double degrees) {
		final PIDController pidController = new PIDController(PID.TURN[0], PID.TURN[1], PID.TURN[2], gyro, lTalons[0]);
		pidController.setInputRange(0, 360);
		pidController.setOutputRange(-0.35, 0.35);
		pidController.setContinuous(true);

		rTalons[0].set(ControlMode.Follower, lTalons[0].getDeviceID());
		rTalons[0].setInverted(!TalonInversions.RIGHT_DRIVE[0]);
		rTalons[1].setInverted(!TalonInversions.RIGHT_DRIVE[1]);

		double angle = degrees + gyro.getAngle();
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

		rTalons[0].setInverted(TalonInversions.RIGHT_DRIVE[0]);
		rTalons[1].setInverted(TalonInversions.RIGHT_DRIVE[1]);
	}

	private synchronized void moveLeft(final double value) {
		lTalons[0].set(ControlMode.PercentOutput, value);
	}

	private synchronized void moveRight(final double value) {
		rTalons[0].set(ControlMode.PercentOutput, value);
	}

	private void toggleReverser(final boolean shouldReverse) {
		this.shouldReverse = shouldReverse;
	}

	private synchronized void shiftGear(final int value) {
		if (value == 0) {
			gearShifter.set(DoubleSolenoid.Value.kReverse);
			gear = 0;
		} else if (value == 1) {
			gearShifter.set(DoubleSolenoid.Value.kForward);
			gear = 1;
		}
	}
}
