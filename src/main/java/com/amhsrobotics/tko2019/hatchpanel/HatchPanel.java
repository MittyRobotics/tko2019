package com.amhsrobotics.tko2019.hatchpanel;

import com.amhsrobotics.tko2019.controls.AnalogInput;
import com.amhsrobotics.tko2019.controls.AnalogType;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.DigitalType;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class HatchPanel {

    /*
    if manual
    4: left extreme
    3: center
    2: right extreme
     */

    private boolean manual = false;
    private final double p = .1; //TODO
    private final double i = 0; //TODO
    private final int d = 0; //TODO
    public final double ticksPerInch = (2500); //TODO 1607.68
    private boolean processDone = false;

    private final int[] solSideId = {4, 5}; //TODO
    private final int[] solForwardId = {2, 3}; //TODO
    private DoubleSolenoid solSide;
    private DoubleSolenoid solForward;

    private final int hatchSwitchId = 4; //TODO
    private final int[] sliderSwitchesIds = {0, 1}; //TODO
    private final int wallSwitchId = 3; //TODO
    private DigitalInput hatchSwitch;
    public DigitalInput[] sliderSwitches = new DigitalInput[2];
    private DigitalInput wallSwitch;

    public final int slideTalonId = 13;
    public WPI_TalonSRX slideTalon;

    public void init(){
        solSide = new DoubleSolenoid(solSideId[0], solSideId[1]);
        solForward = new DoubleSolenoid(solForwardId[0], solForwardId[1]);


//        hatchSwitch = new DigitalInput(hatchSwitchId);
//        wallSwitch = new DigitalInput(wallSwitchId);

//        for(int i = 0; i < 2; i++){
//            sliderSwitches[i] = new DigitalInput(sliderSwitchesIds[i]);
//        }

        slideTalon = new WPI_TalonSRX(slideTalonId);
        slideTalon.configFactoryDefault();
//        slideTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 1000);

//        slideTalon.config_kP(0, p, 0);
        slideTalon.config_kP(0, 0.2, 0);
        slideTalon.config_kI(0,i, 0);
        slideTalon.config_kD(0,d,0);
        slideTalon.setSafetyEnabled(false);
//        slideTalon.setSafetyEnabled(true);
        slideTalon.setInverted(true);
    }
    public void run(){
        System.out.println("running");
        Controls.getInstance().registerDigitalCommand(1, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick11, DigitalType.DigitalPress, () ->{
            manual = !manual;
            if(manual){
                System.out.println("Manual Mode");
            } else {
                System.out.println("Automatic Mode");
            }
        });

        Controls.getInstance().registerDigitalCommand(1, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick4, DigitalType.DigitalPress, () -> {
            if(!manual) {
                slideLeft();
                if (/*!wallSwitch.get() &&*/  !hatchSwitch.get() && (processDone)) {
                    slideMiddle();
                    processDone = false;
                }
            }
//            else{
//                slideTalon.set(ControlMode.PercentOutput, -0.5);
//            }
        });

        Controls.getInstance().registerDigitalCommand(1, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick5, DigitalType.DigitalPress, () -> {
            if(!manual) {
                slideRight();
                if (/*!wallSwitch.get() &&*/  !hatchSwitch.get() && (processDone)) {
                    slideMiddle();
                    processDone = false;
                }
            }
//            else{
//                slideRight();
//            }
        });

        Controls.getInstance().registerDigitalCommand(1, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick2, DigitalType.DigitalPress, () -> {
            if(!manual) {
                if (hatchSwitch.get() /*&& wallSwitch.get()*/) {
                    goHatchForward();
                    closeHatch();
                    goHatchBackward();
                    processDone = true;
                }
            }
            else {
//                slideTalon.config_kP(0, 0.2, 1);
                closeHatch();
            }
        });

        Controls.getInstance().registerDigitalCommand(1, com.amhsrobotics.tko2019.controls.DigitalInput.JoystickTrigger, DigitalType.DigitalPress, () -> {
            if(!manual) {
                if (!hatchSwitch.get() /*&& (wallSwitch.get())*/) {
                    openHatch();
                }
            }
            else {
                openHatch();
            }
        });

        Controls.getInstance().registerDigitalCommand(1, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick3, DigitalType.DigitalPress, ()->{
            if(!manual) {
                slideMiddle();
            }
        });

        Controls.getInstance().registerAnalogCommand(1, AnalogInput.JoystickX, AnalogType.OutOfThresholdMinor, value -> {
            if(manual) {
                if (Math.abs(value) > 0.1) {
//                    slideTalon.config_kP(0, 0.4, 0);
                    slideTalon.set(ControlMode.PercentOutput, value / 2);
                } else {
                    slideTalon.set(ControlMode.PercentOutput, 0);
                }
            } else {
                if(Math.abs(value) > 0.1){
                    slide(slideTalon.getSelectedSensorPosition() / ticksPerInch + value);		//50% speed
                }
            }
        });

        Controls.getInstance().registerAnalogCommand(1, AnalogInput.JoystickY, AnalogType.OutOfThresholdMinor, (value) -> {
            if (manual) {
                if (value < -0.5) {
                    goHatchBackward();
                } else if (value > 0.5) {
                    goHatchForward();
                }
            }
        });


    }

    public void openHatch() { solSide.set(DoubleSolenoid.Value.kReverse); }
    public void closeHatch() { solSide.set(DoubleSolenoid.Value.kForward); }
    public void goHatchForward() {
        solForward.set(DoubleSolenoid.Value.kForward);
    }
    public void goHatchBackward() { solForward.set(DoubleSolenoid.Value.kReverse); }

    //outtake for the rocket
    private void rocketDrop() {
        while (/*wallSwitch.get() && */hatchSwitch.get()) {
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
    public void resetEncoder(){
        slideTalon.set(ControlMode.PercentOutput, 0.1);
        if(sliderSwitches[0].get()){
            slideTalon.setSelectedSensorPosition(0);
            slideTalon.set(ControlMode.PercentOutput, 0);
        }
    }


    public void limitSwitchSafety(){
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
        slide(15);
    }

    //action for cargo outtake
    private void cargoDrop() {
        while (hatchSwitch.get() /*&& wallSwitch.get()*/) {
            closeHatch();
            if (hatchSwitch.get()){
                openHatch();
            }
        }
    }

    //take in the hatch panel *has safety measures*
    private void intake() {
        if (!hatchSwitch.get() /*&& wallSwitch.get()*/){
            openHatch();
            if(!hatchSwitch.get()){
                closeHatch();
            }
        }
    }

    //how far the mechanism has to slide
    public void slide(double position){ //position in inches
        slideTalon.set(ControlMode.Position, (position*ticksPerInch));
//        System.out.println("end error =" + slideTalon.getClosedLoopError());
//        System.out.println(slideTalon.getSelectedSensorPosition());
    }
}