package com.amhsrobotics.tko2019.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class drive {
    // creates talons
    WPI_TalonSRX[] leftDriveTalons = new WPI_TalonSRX[2];
    WPI_TalonSRX[] rightDriveTalons = new WPI_TalonSRX[2];
    DoubleSolenoid gearShift;
    int x = 0;
    //initializes talons
    public void init(){
        leftDriveTalons[0] = new WPI_TalonSRX(20);
        leftDriveTalons[1] = new WPI_TalonSRX(21);
        rightDriveTalons[0] = new WPI_TalonSRX(22);
        rightDriveTalons[1] = new WPI_TalonSRX(23);
        gearShift = new DoubleSolenoid(0, 1);
        leftDriveTalons[1].set(ControlMode.Follower, leftDriveTalons[0].getDeviceID());
        rightDriveTalons[1].set(ControlMode.Follower, rightDriveTalons[0].getDeviceID());

    }
    // move left function
    public void moveLeft(double value){
        leftDriveTalons[0].set(ControlMode.PercentOutput, value);

    }
    //move right function
    public void moveRight(double value){
        rightDriveTalons[0].set(ControlMode.PercentOutput, value);
    }
    //shift gear function 
    public void shiftGear(int value){
        if (value == 0) {
            gearShift.set(DoubleSolenoid.Value.kReverse);
        }
        else {
            gearShift.set(DoubleSolenoid.Value.kForward);
        }
    }
}
