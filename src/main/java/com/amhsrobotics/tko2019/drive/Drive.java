package com.amhsrobotics.tko2019.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Drive {

    static WPI_TalonSRX[] leftDriveTalons = new WPI_TalonSRX[2];
    static WPI_TalonSRX[] rightDriveTalons = new WPI_TalonSRX[2];
    static DoubleSolenoid gearShift;
    int x = 0;
    public static int reverseFactor;


    public static void init(){
        leftDriveTalons[0] = new WPI_TalonSRX(20);
        leftDriveTalons[1] = new WPI_TalonSRX(21);
        rightDriveTalons[0] = new WPI_TalonSRX(22);
        rightDriveTalons[1] = new WPI_TalonSRX(23);
        gearShift = new DoubleSolenoid(0, 1);
        leftDriveTalons[1].set(ControlMode.Follower, leftDriveTalons[0].getDeviceID());
        rightDriveTalons[1].set(ControlMode.Follower, rightDriveTalons[0].getDeviceID());
        reverseFactor = 1;
    }


    public static void moveLeft(double value){
        leftDriveTalons[0].set(ControlMode.PercentOutput, reverseFactor*value);
    }


    public static void moveRight(double value){
        rightDriveTalons[0].set(ControlMode.PercentOutput, reverseFactor*value);
    }


    public static void  toggleReverser(boolean aButtonPressed){
        if (aButtonPressed == true){
            reverseFactor = -1;
        }
        if (aButtonPressed == false){
            reverseFactor = 1;
        }
    }



    public static void shiftGear(int value){
        if (value == 0) {
            gearShift.set(DoubleSolenoid.Value.kReverse);
        }
        else {
            gearShift.set(DoubleSolenoid.Value.kForward);
        }
    }


}
