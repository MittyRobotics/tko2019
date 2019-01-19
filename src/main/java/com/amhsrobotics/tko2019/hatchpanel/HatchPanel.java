package com.amhsrobotics.tko2019.hatchpanel;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DigitalInput;

public class HatchPanel {
    static DoubleSolenoid solSide;
    static DoubleSolenoid solForward;
    static DigitalInput limitSwitchFront, limitSwitchRight, limitSwitchLeft, wallSwitch;
    private static final int p = 0,
            i = 0,
            d = 0,
            threshold = 10,
            ticksPerInch = 0;
    static WPI_TalonSRX slideMotor;
    public void init(){
        solSide = new DoubleSolenoid(0, 1);
        solForward = new DoubleSolenoid(4, 5);
        limitSwitchFront = new DigitalInput(1);
        limitSwitchRight = new DigitalInput(8);
        limitSwitchLeft = new DigitalInput(9);
        slideMotor = new WPI_TalonSRX(0);
        slideMotor.config_kP(0, p, 0);
        slideMotor.config_kI(0,i, 0);
        slideMotor.config_kD(0,d,0);

        slideMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 1000);
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
        if(wallSwitch.get() && limitSwitchFront.get()) {
            goHatchForward();
            closeHatch();
            goHatchBackward();
            slideMiddle();
        }
    }
    public static void resetEncoder(){
        if(limitSwitchLeft.get()){
            slideMotor.setSelectedSensorPosition(0);
        }
    }
    static void slideLeft(){
        slide(0);
    }
    static void slideMiddle() {
        slide(8);
    }
    static void slideRight() {
        slide(16);
    }
    static void cargoDrop() {
        if (limitSwitchFront.get() && wallSwitch.get()) {
            closeHatch();
        }
    }
    static void intake() {
        if (!limitSwitchFront.get() && wallSwitch.get()){
            openHatch();
        }
    }
    static void slide(double position){
        double setpoint = position * ticksPerInch;
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