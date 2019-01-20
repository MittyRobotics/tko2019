package com.amhsrobotics.tko2019.hatchpanel;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;

public class HatchPanel {

    private boolean manual = false;
    private final int p = 0; //TODO
    private final int i = 0; //TODO
    private final int d = 0; //TODO
    private final int threshold = 10; //TODO
    private final int ticksPerInch = 0; //TODO

    private final int[] solSideId = {0, 1}; //TODO
    private final int[] solForwardId = {2, 3}; //TODO
    private DoubleSolenoid solSide;
    private DoubleSolenoid solForward;

    private final int hatchSwitchId = 0; //TODO
    private final int[] sliderSwitchesIds = {1, 2}; //TODO
    private final int wallSwitchId = 3; //TODO
    private DigitalInput hatchSwitch;
    private DigitalInput[] sliderSwitches = new DigitalInput[2];
    private DigitalInput wallSwitch;

    private final int slideTalonId = 28;
    private WPI_TalonSRX slideTalon;

    private final int joystickId = 1; //TODO
    private Joystick hatchJoystick;

    public void init(){
        hatchJoystick = new Joystick(joystickId);

        solSide = new DoubleSolenoid(solSideId[0], solSideId[1]);
        solForward = new DoubleSolenoid(solForwardId[0], solForwardId[1]);

        hatchSwitch = new DigitalInput(hatchSwitchId);
        wallSwitch = new DigitalInput(wallSwitchId);
        for(int i = 0; i < 2; i++){
            sliderSwitches[i] = new DigitalInput(sliderSwitchesIds[i]);
        }

        slideTalon = new WPI_TalonSRX(slideTalonId);

        slideTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 1000);

        slideTalon.config_kP(0, p, 0);
        slideTalon.config_kI(0,i, 0);
        slideTalon.config_kD(0,d,0);
    }
    public void runs(){
        //all the buttons: manual and automatic
        limitSwitchSafety();
        if(hatchJoystick.getRawButton(11)){
            manual = !manual;
        }
        if(manual){
            if (hatchJoystick.getTriggerPressed()){
                openHatch();
            }
            else if (hatchJoystick.getRawButton(2)){
                closeHatch();
            }
            if (hatchJoystick.getY() > 0.1){
                goHatchForward();
            }
            else if (hatchJoystick.getY() < -0.1){
                goHatchBackward();
            }
            if (hatchJoystick.getRawButton(4)){
                slideLeft();
            }
            else if (hatchJoystick.getRawButton(5)){
                slideRight();
            }
            else if (hatchJoystick.getRawButton(3)) {
                slideMiddle();
            }
        }
        else{
            if (hatchJoystick.getRawButton(3)){
                cargoDrop();
            }
            else if (hatchJoystick.getRawButton(2)) {
                rocketDrop();
            }
            else if (hatchJoystick.getRawButton(4)) {
                slideLeft();
            }
            else if (hatchJoystick.getRawButton(5)){
                slideRight();
            }
            else if(hatchJoystick.getTriggerPressed()){
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

    //outtake for the rocket
    private void rocketDrop() {
        while (wallSwitch.get() && hatchSwitch.get()) {
            goHatchForward();
            closeHatch();
            goHatchBackward();
            if (hatchSwitch.get()){
                openHatch();
            }
            else {
                slideMiddle();
            }
        }
    }

    //resets encoder when slider is to the left
    private void resetEncoder(){
        if(sliderSwitches[0].get()){
            slideTalon.setSelectedSensorPosition(0);
        }
    }


    private void limitSwitchSafety(){
        while (sliderSwitches[0].get()){
            slideTalon.set(ControlMode.PercentOutput, 0.2);
        }
        slideTalon.set(ControlMode.PercentOutput, 0);
        while (sliderSwitches[1].get()){
            slideTalon.set(ControlMode.PercentOutput, -0.2);
        }
        slideTalon.set(ControlMode.PercentOutput, 0);
    }

    //work on position numbers
    private void slideLeft(){
        slide(0);
    }
    private void slideMiddle() {
        slide(8);
    }
    private void slideRight() {
        slide(16);
    }

    //action for cargo outtake
    private void cargoDrop() {
        while (hatchSwitch.get() && wallSwitch.get()) {
            closeHatch();
            if (hatchSwitch.get()){
                openHatch();
            }
        }
    }

    //take in the hatch panel *has safety measures*
    private void intake() {
        if (!hatchSwitch.get() && wallSwitch.get()){
            openHatch();
            if(!hatchSwitch.get()){
                closeHatch();
            }
        }
    }

    //how far the mechanism has to slide
    private void slide(double position){
        double setpoint = position * ticksPerInch;
        double error = slideTalon.getClosedLoopError();
        slideTalon.set(ControlMode.Position, setpoint);
        while (Math.abs(error) > threshold) {
            error = slideTalon.getClosedLoopError();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        slideTalon.set(ControlMode.PercentOutput, 0);
    }
}