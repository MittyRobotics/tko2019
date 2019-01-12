package com.amhsrobotics.tko2019.hatchPanel;

import com.amhsrobotics.tko2019.Robot;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DigitalInput;

public class hatchPanel {

    static DoubleSolenoid solSide = new DoubleSolenoid(0, 1);;
    static DoubleSolenoid solForward = new DoubleSolenoid(4, 5);
    static DigitalInput limitSwitch = new DigitalInput(1);
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
    static void sliderLeft(){
        hatchPanelAuton.execute(-8);
    }
    static void sliderMiddle() {
        hatchPanelAuton.execute(0);
    }
    static void sliderRight() {
        hatchPanelAuton.execute(8);
    }
    static void deletethisfunction() {
    }
    static void limitswitch() {
        if (limitSwitch.get()) {
            openHatch();
        }
        else {
            closeHatch();
        }
    }
}