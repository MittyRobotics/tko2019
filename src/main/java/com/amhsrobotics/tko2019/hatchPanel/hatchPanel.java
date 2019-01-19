package com.amhsrobotics.tko2019.hatchPanel;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DigitalInput;

public class hatchPanel {
    static DoubleSolenoid solSide;
    static DoubleSolenoid solForward;
    static DigitalInput limitSwitchFront, limitSwitchRight, limitSwitchLeft;
    static WPI_TalonSRX slideMotor;

    public void init(){
        solSide = new DoubleSolenoid(0, 1);
        solForward = new DoubleSolenoid(4, 5);
        limitSwitchFront = new DigitalInput(1);
        limitSwitchRight = new DigitalInput(8);
        limitSwitchLeft = new DigitalInput(9);
        slideMotor = new WPI_TalonSRX(0);
    }

    public static void openHatch() {
        solSide.set(DoubleSolenoid.Value.kReverse);
    }
    public static void closeHatch() {
        solSide.set(DoubleSolenoid.Value.kForward);
    }
    static void goHatchForward() {
        solForward.set(DoubleSolenoid.Value.kForward);
    }
    static void goHatchBackward() {
        solForward.set(DoubleSolenoid.Value.kReverse);
    }

    static void rocketDrop() {
        if(limitSwitchFront.get()) {
            goHatchForward();
            closeHatch();
            goHatchBackward();
        }
    }

    static void resetEncoder(){
        if(limitSwitchLeft.get()){
            slideMotor.setSelectedSensorPosition(0);
        }
    }
    static void slideLeft(){
        hatchPanelAuton.execute(-8);
    }
    static void slideMiddle() {
        hatchPanelAuton.execute(0);
    }
    static void slideRight() {
        hatchPanelAuton.execute(8);
    }
    static void cargoDrop() {
        if (limitSwitchFront.get()) {
            closeHatch();
        }
        else {
            openHatch();
        }
    }
}