package com.amhsrobotics.tko2019.cargo;

import com.amhsrobotics.tko2019.controls.AnalogInput;
import com.amhsrobotics.tko2019.controls.AnalogType;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.DigitalType;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

//////////////////////////////////////////////////////////////////////////
// CHECK THIS CODE. IT MAY OR MAY NOT WORK !!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
//////////////////////////////////////////////////////////////////////////

public class Cargo {
    private final double ticksPerDegree = 0; //TODO
    private final double cargoSpeed = 0.8; //TODO
    private final double rocketSpeed = 0.8; //TODO  these speeds will change be positive or negative
    private final double intakeSpeed = 0.5; //TODO
    private final double groundSpeed = 0.5; //TODO
    private final double rocketHeight = 0; //TODO
    private final double visionHeight = 0; //TODO
    private final double intakeHeight = 0; //TODO
    private final double cargoHeight = 0; //TODO
    private final double groundHeight = 0; //TODO
    private final double p = 0.2; //TODO
    private final double i = 0; //TODO
    private final double d = 0; //TODO
    private final double threshold = 1 * ticksPerDegree; //TODO
    boolean groundThreshold = false;
    boolean rocketThreshold = false;
    boolean cargoThreshold = false;
    boolean intakeThreshold = false;
    private boolean manual = false;
    private int level = 1; //1 = ground, 2 = rocket, 3 = cargo, 4 = human player height

    private final int intakeSensorId = 0; //TODO
    private final int[] conveyorLimitIds = {1, 2}; //TODO
    private DigitalInput intakeSensor;
    private DigitalInput[] conveyorLimits = new DigitalInput[conveyorLimitIds.length];

    private final int[] intakeTalonsIds = {26, 27};
    private final int[] conveyorTalonIds = {24, 25};
    private WPI_TalonSRX[] intakeTalons = new WPI_TalonSRX[intakeTalonsIds.length];
    private WPI_TalonSRX[] conveyorTalons = new WPI_TalonSRX[conveyorTalonIds.length];

    public void init() {
        for (int i = 0; i < intakeTalonsIds.length; i++) {
            intakeTalons[i] = new WPI_TalonSRX(intakeTalonsIds[i]);
        }
        for (int i = 0; i < conveyorTalonIds.length; i++) {
            conveyorTalons[i] = new WPI_TalonSRX(conveyorTalonIds[i]);
        }

        intakeSensor = new DigitalInput(intakeSensorId);
        for (int i = 0; i < conveyorLimits.length; i++) {
            conveyorLimits[i] = new DigitalInput(conveyorLimitIds[i]);
        }

        conveyorTalons[0].configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);

        conveyorTalons[1].set(ControlMode.Follower, conveyorTalons[0].getDeviceID());

        conveyorTalons[0].config_kP(0, p, 0);
        conveyorTalons[0].config_kP(0, i, 0);
        conveyorTalons[0].config_kP(0, d, 0);
    }

    public void run() {

        if (conveyorTalons[0].getSelectedSensorPosition() > (groundHeight*ticksPerDegree-threshold) && (conveyorTalons[0].getSelectedSensorPosition() < (groundHeight*ticksPerDegree+threshold))){
            groundThreshold = true;
        }
        else {groundThreshold = false;}
        if (conveyorTalons[0].getSelectedSensorPosition() > (rocketHeight*ticksPerDegree-threshold) && (conveyorTalons[0].getSelectedSensorPosition() < (rocketHeight*ticksPerDegree+threshold))){
            rocketThreshold = true;
        }
        else {rocketThreshold = false;}
        if (conveyorTalons[0].getSelectedSensorPosition() > (cargoHeight*ticksPerDegree-threshold) && (conveyorTalons[0].getSelectedSensorPosition() < (cargoHeight*ticksPerDegree+threshold))){
            cargoThreshold = true;
        }
        else {rocketThreshold = false;}
        if (conveyorTalons[0].getSelectedSensorPosition() > (intakeHeight*ticksPerDegree-threshold) && (conveyorTalons[0].getSelectedSensorPosition() < (intakeHeight*ticksPerDegree+threshold))){
            intakeThreshold = true;
        }
        else {intakeThreshold = false;}

        Controls.getInstance().registerDigitalCommand(2, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick11, DigitalType.DigitalRelease, () -> {
            manual = !manual;
            if (manual) {
                System.out.println("Manual Mode");
            } else {
                System.out.println("Auton Mode");
            }
        });
        if (manual) {
            Controls.getInstance().registerAnalogCommand(2, AnalogInput.JoystickY, AnalogType.OutOfThreshold, (value) -> {
                spinIntake(value, value);
            });
            Controls.getInstance().registerDigitalCommand(2, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick3, DigitalType.DigitalHold, () -> {
                conveyorTalons[0].set(ControlMode.Position, conveyorTalons[1].getSelectedSensorPosition() + 1);;
            });
            Controls.getInstance().registerDigitalCommand(2, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick2, DigitalType.DigitalHold, () -> {
                conveyorTalons[0].set(ControlMode.Position, conveyorTalons[1].getSelectedSensorPosition() - 1);;
            });
        } else if (!manual) {
            Controls.getInstance().registerDigitalCommand(2, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick3, DigitalType.DigitalRelease, () -> {
                if ((groundThreshold)){
                    rocketConveyor();
                    groundThreshold = false;
                    System.out.println("Rocket Height");;
                }
                else if (rocketThreshold){
                    cargoConveyor();
                    rocketThreshold = false;
                    System.out.println("Cargo Height");
                }
                else if (cargoThreshold){
                    intakeConveyor();
                    cargoThreshold = false;
                    System.out.println("Intake Height");
                }
                else {stopIntake();}
                //1 = ground, 2 = rocket, 3 = cargo 4 = human player height
            });
            Controls.getInstance().registerDigitalCommand(2, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick2, DigitalType.DigitalRelease, () -> {
                if ((intakeThreshold)){
                    cargoConveyor();
                    groundThreshold = false;
                    System.out.println("Cargo Height");;
                }
                else if (cargoThreshold){
                    rocketConveyor();
                    rocketThreshold = false;
                    System.out.println("Rocket Height");
                }
                else if (rocketThreshold){
                    intakeConveyor();
                    cargoThreshold = false;
                    System.out.println("Intake Height");
                }
                //1 = ground, 2 = rocket, 3 = cargo 4 = human player height
            });
            Controls.getInstance().registerAnalogCommand(2, AnalogInput.JoystickY, AnalogType.OutOfThreshold, (value) -> {
                if (intakeSensor.get()){
                    visionConveyor();
                    stopIntake();
                }
                else if ((intakeThreshold) && (value < -0.5)){
                    spinIntake(intakeSpeed, intakeSpeed);  //TODO
                }
                else if ((groundThreshold) && (value < -0.5)){
                    spinIntake(groundSpeed, groundSpeed);  //TODO
                }
                else if ((rocketThreshold) && (value > 0.5)){
                    spinIntake(rocketSpeed, rocketSpeed);  //TODO
                }
                else if ((cargoThreshold) && (value > 0.5)){
                    spinIntake(cargoSpeed, cargoSpeed);  //TODO
                }
                else {stopIntake();}
            });
        }
    }

    private void intakeOuttakeMacro(){
        if (level == 0){
            spinIntake(groundSpeed, groundSpeed);
        }
        else if (level == 1){
            spinIntake(intakeSpeed, intakeSpeed);
        }
        else if (level == 2){
            spinIntake(-rocketSpeed, -rocketSpeed);
        }
        else if (level == 3){
            spinIntake(-cargoSpeed, -cargoSpeed);
        }
        Timer.delay(1); //TODO
        stopIntake();
    }


    private void spinIntake ( double topSpeed, double bottomSpeed){
        intakeTalons[0].set(ControlMode.PercentOutput, topSpeed);
        intakeTalons[1].set(ControlMode.PercentOutput, bottomSpeed);
    }

    private void stopIntake () {
        intakeTalons[0].set(ControlMode.PercentOutput, 0);
        intakeTalons[1].set(ControlMode.PercentOutput, 0);
    }

    private void moveConveyor ( double neededPos){
        double error1 = conveyorTalons[0].getClosedLoopError();
        neededPos += conveyorTalons[0].getSelectedSensorPosition();
        conveyorTalons[0].set(ControlMode.Position, neededPos * ticksPerDegree);

        while ((Math.abs(error1) > threshold)) {
            error1 = conveyorTalons[0].getClosedLoopError();
            System.out.println("T1: " + conveyorTalons[0].getSelectedSensorPosition());
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        conveyorTalons[0].set(ControlMode.PercentOutput, 0);
    }

    private void rocketConveyor () {
        moveConveyor(rocketHeight);
    }

    private void cargoConveyor () {
        moveConveyor(cargoHeight);
    }

    private void intakeConveyor () {
        moveConveyor(intakeHeight);
    }

    private void visionConveyor () {
        moveConveyor(visionHeight);
    }

    private void groundConveyor () {
        moveConveyor(groundHeight);
    }

    private void calibrateConveyor () {
        while (!conveyorLimits[0].get()) {
            conveyorTalons[0].set(ControlMode.PercentOutput, 0.5);
        }
        conveyorTalons[0].setSelectedSensorPosition(90);
    }

    private void cargoLimitSafety () {
        while (conveyorLimits[0].get()) {
            conveyorTalons[0].set(ControlMode.PercentOutput, -0.5);
        }
        conveyorTalons[0].set(ControlMode.PercentOutput, 0);
        while (conveyorLimits[1].get()) {
            conveyorTalons[0].set(ControlMode.PercentOutput, 0.5);
        }
        conveyorTalons[0].set(ControlMode.PercentOutput, 0);
    }
}