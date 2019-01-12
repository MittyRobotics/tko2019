package com.amhsrobotics.tko2019.hatchPanel;

import com.amhsrobotics.tko2019.Robot;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DigitalInput;

public class hatchPanel {

    static DoubleSolenoid solSide = new DoubleSolenoid(0, 1);;
    static DoubleSolenoid solForward = new DoubleSolenoid(4, 5);
    static DigitalInput limitSwitchFront = new DigitalInput(1);
    static DigitalInput limitSwitchRight = new DigitalInput(8);
    static DigitalInput limitSwitchLeft = new DigitalInput(9);
    static WPI_TalonSRX slideMotor = new WPI_TalonSRX(0);


    static void openHatch() {
        solSide.set(DoubleSolenoid.Value.kReverse);
    }
    static void closeHatch() {
        solSide.set(DoubleSolenoid.Value.kForward);
    }
    static void goHatchForward() {
        solForward.set(DoubleSolenoid.Value.kForward);
    }
    static void goHatchBackward() {
        solForward.set(DoubleSolenoid.Value.kReverse);
    }
    static void slideEnd() {
        if (limitSwitchLeft.get()) {
            hatchPanelAuton.execute(1);
        }
        if (limitSwitchRight.get()) {
            hatchPanelAuton.execute(-1);
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
    static void limitswitch() {
        if (limitSwitchFront.get()) {
            openHatch();
        }
        else {
            closeHatch();
        }
    }
}