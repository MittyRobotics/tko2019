package com.amhsrobotics.tko2019.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class DriveAuton {
    private final double ticksPerInch = 119.47; //Note: double check true TPS
    double p = 0.2;
    double i = 0;
    double d = 0;
    double distance = 36;

    public void Execute() {
        for (int J = 0; J < 2; J++) {
            Drive.leftDriveTalons[J].config_kP(0, p, 0);
            Drive.leftDriveTalons[J].config_kI(0, i, 0);
            Drive.leftDriveTalons[J].config_kD(0, d, 0);

            Drive.rightDriveTalons[J].config_kP(0, p, 0);
            Drive.rightDriveTalons[J].config_kI(0, i, 0);
            Drive.rightDriveTalons[J].config_kD(0, d, 0);
        }

        double setpoint = distance * ticksPerInch;
        double neededPos1 = Drive.leftDriveTalons[0].getSelectedSensorPosition() + setpoint;
        double neededPos1r = Drive.rightDriveTalons[0].getSelectedSensorPosition() + setpoint;
        double threshold = 0.25 * ticksPerInch;
        double error1 = Drive.leftDriveTalons[0].getClosedLoopError();
        double error1r = Drive.rightDriveTalons[0].getClosedLoopError();
        Drive.leftDriveTalons[0].set(ControlMode.Position, neededPos1);
        Drive.rightDriveTalons[0].set(ControlMode.Position, neededPos1r);

        while ((Math.abs(error1) > threshold) && (Math.abs(error1r) > threshold)) {
            error1 = Drive.leftDriveTalons[0].getClosedLoopError();
            error1r = Drive.rightDriveTalons[0].getClosedLoopError();

            System.out.println("(Left) T1: " + Drive.leftDriveTalons[0].getSelectedSensorPosition());
            System.out.println("(Right) T1: " + Drive.rightDriveTalons[0].getSelectedSensorPosition());
            //error2 = Hardware.talons[2].getClosedLoopError();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Drive.leftDriveTalons[0].set(ControlMode.PercentOutput, 0);
        Drive.rightDriveTalons[0].set(ControlMode.PercentOutput, 0);
    }

}