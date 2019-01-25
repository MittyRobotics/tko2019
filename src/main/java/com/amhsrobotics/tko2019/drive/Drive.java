package com.amhsrobotics.tko2019.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

public class Drive {
	private final int[] leftTalonIds = {20, 21};
	private final int[] rightTalonIds = {22, 23};
	private final int[] gearShiftingIds = {0, 1};

	private final double[] ticksPerInch = {0.0, 119.47};
	private final double p = 0.2;
	private final double i = 0;
	private final double d = 0;

	private final WPI_TalonSRX[] leftDriveTalons = new WPI_TalonSRX[leftTalonIds.length];
	private final WPI_TalonSRX[] rightDriveTalons = new WPI_TalonSRX[rightTalonIds.length];

	private DoubleSolenoid gearShift;
	private int gear = 1;

	private boolean shouldReverse = false;

	public void init() {
		for (int talonId = 0; talonId < leftTalonIds.length; talonId++) {
			final WPI_TalonSRX talon = new WPI_TalonSRX(leftTalonIds[talonId]);

			talon.config_kP(0, p);
			talon.config_kI(0, i);
			talon.config_kD(0, d);

			if (talonId == 0) {
				talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
			} else {
				talon.set(ControlMode.Follower, leftTalonIds[0]);
			}
		}

		gearShift = new DoubleSolenoid(gearShiftingIds[0], gearShiftingIds[1]);
	}

	@Deprecated
	public void run() {
		XboxController controller = new XboxController(0);

		if (shouldReverse) {
			if (Math.abs(controller.getY(GenericHID.Hand.kRight)) > 0.05) {
				moveLeft(-controller.getY(GenericHID.Hand.kRight));
			} else {
				moveLeft(0);
			}
			if (Math.abs(controller.getY(GenericHID.Hand.kLeft)) > 0.05) {
				moveRight(-controller.getY(GenericHID.Hand.kLeft));
			} else {
				moveRight(0);
			}
		} else {
			if (Math.abs(controller.getY(GenericHID.Hand.kLeft)) > 0.05) {
				moveLeft(controller.getY(GenericHID.Hand.kLeft));
			} else {
				moveLeft(0);
			}
			if (Math.abs(controller.getY(GenericHID.Hand.kRight)) > 0.05) {
				moveRight(controller.getY(GenericHID.Hand.kRight));
			} else {
				moveRight(0);
			}

		}

		boolean pressed = controller.getAButtonPressed();
		toggleReverser(pressed);
	}

	public void move(final double distance) {
		final double setpoint = distance * ticksPerInch[gear];
		final double threshold = 0.25 * ticksPerInch[gear];

		leftDriveTalons[0].set(ControlMode.Position, leftDriveTalons[0].getSelectedSensorPosition() + setpoint);
		rightDriveTalons[0].set(ControlMode.Position, rightDriveTalons[0].getSelectedSensorPosition() + setpoint);
		while ((Math.abs(leftDriveTalons[0].getClosedLoopError()) > threshold) || (Math.abs(rightDriveTalons[0].getClosedLoopError()) > threshold)) {
			System.out.println("(Left) T1: " + leftDriveTalons[0].getSelectedSensorPosition());
			System.out.println("(Right) T1: " + rightDriveTalons[0].getSelectedSensorPosition());

			try {
				Thread.sleep(20);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

		leftDriveTalons[0].set(ControlMode.PercentOutput, 0);
		rightDriveTalons[0].set(ControlMode.PercentOutput, 0);
	}

	private void moveLeft(final double value) {
		leftDriveTalons[0].set(ControlMode.PercentOutput, value);
	}


	private void moveRight(final double value) {
		rightDriveTalons[0].set(ControlMode.PercentOutput, value);
	}


	private void toggleReverser(boolean shouldReverse) {
		this.shouldReverse = shouldReverse;
	}

	public void shiftGear(int value) {
		if (value == 0) {
			gearShift.set(DoubleSolenoid.Value.kReverse);
		} else if (value == 1) {
			gearShift.set(DoubleSolenoid.Value.kForward);
		}
	}
}
