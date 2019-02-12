package com.amhsrobotics.tko2019.drive;

import com.amhsrobotics.tko2019.controls.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import 

public class Drive {
	private final int[] leftTalonIds = {0, 1};
	private final int[] rightTalonIds = {2,3};
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
			leftDriveTalons[talonId] = new WPI_TalonSRX(leftTalonIds[talonId]);
			rightDriveTalons[talonId] = new WPI_TalonSRX(rightTalonIds[talonId]);

//			talon.config_kP(0, p);
//			talon.config_kI(0, i);
//			talon.config_kD(0, d);
//
//			if (talonId == 0) {
//				talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
//			} else {
//				talon.set(ControlMode.Follower, leftTalonIds[0]);
//			}
		}
		leftDriveTalons[0].setInverted(true);
		leftDriveTalons[1].setInverted(true);

//		gearShift = new DoubleSolenoid(gearShiftingIds[0], gearShiftingIds[1]);
	}

	@Deprecated
	public void run() {

            Controls.getInstance().registerAnalogCommand(0, AnalogInput.XboxLYJoystick, AnalogType.OutOfThreshold, value -> {
            	if(shouldReverse){
					moveLeft(value);
				}
            	else {
					moveRight(-value);
				}
            });
            Controls.getInstance().registerAnalogCommand(0, AnalogInput.XboxRYJoystick, AnalogType.OutOfThreshold, value -> {
                if(shouldReverse){
					moveRight(value);
				}
                else {
                	moveLeft(-value);
				}
            });
//        Controls.getInstance().registerDigitalCommand(0, DigitalInput.XboxLBumper, DigitalType.DigitalPress, ()->{
//            shiftGear(1);
//        });
		Controls.getInstance().registerDigitalCommand(0, DigitalInput.XboxLJoystick, DigitalType.DigitalPress, () ->{
			System.out.println("Here");
			toggleReverser(!shouldReverse);
		});
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


	public void moveLeft(final double value) {leftDriveTalons[0].set(ControlMode.PercentOutput, value); }


	public void moveRight(final double value) {
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
