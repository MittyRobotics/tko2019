package com.amhsrobotics.tko2019.Cargo;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Cargo {
    DigitalInput limitSwitch;

    public static WPI_TalonSRX[] intakeTalons = new WPI_TalonSRX[2];
    public static WPI_TalonSRX[] conveyorTalons = new WPI_TalonSRX[2];

    public void init() {

        intakeTalons[0] = new WPI_TalonSRX(0);
        intakeTalons[1] = new WPI_TalonSRX(1);

        conveyorTalons[0] = new WPI_TalonSRX(3);
        conveyorTalons[1] = new WPI_TalonSRX(4);

        intakeTalons[1].set(ControlMode.Follower, intakeTalons[0].getDeviceID());
        conveyorTalons[0].configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);

        conveyorTalons[1].set(ControlMode.Follower, conveyorTalons[0].getDeviceID());

        double p = 0.2;
        double i = 0;
        double d = 0;

        conveyorTalons[0].config_kP(0, p, 0);
        conveyorTalons[0].config_kP(0, i, 0);
        conveyorTalons[0].config_kP(0, d, 0);

    }

    public void spinIntake(){
        intakeTalons[0].set(ControlMode.PercentOutput, .8); // or -1
    }

    public void stopIntake(){
        intakeTalons[0].set(ControlMode.PercentOutput, 0);
    }

    public void moveConveyor(double neededPos){
        double threshold = 1;   //TODO
        double error1 = conveyorTalons[0].getClosedLoopError();

        conveyorTalons[0].set(ControlMode.Position, neededPos);

        while((Math.abs(error1) > threshold)){
            error1 = conveyorTalons[0].getClosedLoopError();
            System.out.println("T1: " + conveyorTalons[0].getSelectedSensorPosition());
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void raiseConveyor(){
        double highSetpoint = 100; //TODO
        moveConveyor(highSetpoint);
    }

    public void dropConveyor(){
        double lowSetpoint = 1;    //TODO
        moveConveyor(lowSetpoint);
    }
}