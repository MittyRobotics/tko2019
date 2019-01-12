package com.amhsrobotics.tko2019.hatchPanel;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import static com.amhsrobotics.tko2019.hatchPanel.hatchPanel.*;

public class hatchPanelAuton {
    private static final int p = 0,
            i = 0,
            d = 0,
            threshold = 10,
            ticksPerInch = 0;
    public static void execute(double distance){
        slideMotor.config_kP(0, p, 0);
        slideMotor.config_kI(0,i, 0);
        slideMotor.config_kD(0,d,0);

        slideMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 1000);

        double setpoint = distance * ticksPerInch;
        double error = slideMotor.getClosedLoopError();
        slideMotor.set(ControlMode.Position, setpoint + slideMotor.getSelectedSensorPosition() );
        while (Math.abs(error) > threshold) {
            error = slideMotor.getClosedLoopError();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        slideMotor.set(ControlMode.PercentOutput, 0);
    }

}