package com.amhsrobotics.tko2019.hatchpanel;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;

public class HatchPanel {
    private Joystick leftJoy;
    private boolean manual = false;
    private DoubleSolenoid solSide;
    private DoubleSolenoid solForward;
    private DigitalInput limitSwitchFront, limitSwitchRight, limitSwitchLeft, wallSwitch;
    private WPI_TalonSRX slideMotor;
    public void init(){
        final int p = 0, i = 0, d = 0;
        //Ids need work
        leftJoy = new Joystick(0);
        solSide = new DoubleSolenoid(0, 1);
        solForward = new DoubleSolenoid(4, 5);
        limitSwitchFront = new DigitalInput(1);
        limitSwitchRight = new DigitalInput(8);
        limitSwitchLeft = new DigitalInput(9);
        wallSwitch = new DigitalInput(7);
        slideMotor = new WPI_TalonSRX(0);
        slideMotor.config_kP(0, p, 0);
        slideMotor.config_kI(0,i, 0);
        slideMotor.config_kD(0,d,0);

        slideMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 1000);
    }
    public void runs(){
        limitSwitchSafety();
        if(leftJoy.getRawButton(11)){
            manual = !manual;
        }
        if(manual){
            if (leftJoy.getTriggerPressed()){
                openHatch();
            }
            else if (leftJoy.getRawButton(2)){
                closeHatch();
            }
            if (leftJoy.getY() > 0.1){
                goHatchForward();
            }
            else if (leftJoy.getY() < -0.1){
                goHatchBackward();
            }
            if (leftJoy.getRawButton(4)){
                slideLeft();
            }
            else if (leftJoy.getRawButton(5)){
                slideRight();
            }
            else if (leftJoy.getRawButton(3)) {
                slideMiddle();
            }
        }
        else{
            if (leftJoy.getRawButton(3)){
                cargoDrop();
            }
            else if (leftJoy.getRawButton(2)) {
                rocketDrop();
            }
            else if (leftJoy.getRawButton(4)) {
                slideLeft();
            }
            else if (leftJoy.getRawButton(5)){
                slideRight();
            }
            else if(leftJoy.getTriggerPressed()){
                intake();
            }
        }
    }

    private void openHatch() {
        solSide.set(DoubleSolenoid.Value.kReverse);
    }
    private void closeHatch() {
        solSide.set(DoubleSolenoid.Value.kForward);
    }
    private void goHatchForward() {
        solForward.set(DoubleSolenoid.Value.kForward);
    }
    private void goHatchBackward() {
        solForward.set(DoubleSolenoid.Value.kReverse);
    }

    private void rocketDrop() {
        while (wallSwitch.get() && limitSwitchFront.get()) {
            goHatchForward();
            closeHatch();
            goHatchBackward();
            if (limitSwitchFront.get()){
                openHatch();
            }
            else {
                slideMiddle();
            }
        }
    }
    private void resetEncoder(){
        if(limitSwitchLeft.get()){
            slideMotor.setSelectedSensorPosition(0);
        }
    }
    private void limitSwitchSafety(){
        while (limitSwitchLeft.get()){
            slideMotor.set(ControlMode.PercentOutput, 0.2);
        }
        while (limitSwitchRight.get()){
            slideMotor.set(ControlMode.PercentOutput, -0.2);
        }
        slideMotor.set(ControlMode.PercentOutput, 0);
    }
    private void slideLeft(){
        slide(0);
    }
    private void slideMiddle() {
        slide(8);
    }
    private void slideRight() {
        slide(16);
    }
    private void cargoDrop() {
        while (limitSwitchFront.get() && wallSwitch.get()) {
            closeHatch();
            if (limitSwitchFront.get()){
                openHatch();
            }
        }
    }
    private void intake() {
        if (!limitSwitchFront.get() && wallSwitch.get()){
            openHatch();
            if(!limitSwitchFront.get()){
                closeHatch();
            }
        }
    }
    private void slide(double position){
        final int threshold = 10, ticksPerInch = 0;
        double setpoint = position * ticksPerInch;
        double error = slideMotor.getClosedLoopError();
        slideMotor.set(ControlMode.Position, setpoint);
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