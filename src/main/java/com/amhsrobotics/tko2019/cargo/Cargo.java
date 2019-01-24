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

//////////////////////////////////////////////////////////////////////////
// CHECK THIS CODE. IT MAY OR MAY NOT WORK !!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
//////////////////////////////////////////////////////////////////////////

public class Cargo {
    private final double ticksPerDegree = 0; //TODO
    private final double cargoSpeed = 0.8; //TODO
    private final double rocketSpeed = 0.8; //TODO
    private final double baseSpeed = 0.5; //TODO
    private final double rocketHeight = 0; //TODO
    private final double visionHeight = 0; //TODO
    private final double intakeHeight = 0; //TODO
    private final double cargoHeight = 0; //TODO
    private final double p = 0.2; //TODO
    private final double i = 0; //TODO
    private final double d = 0; //TODO
    private final double threshold = 1; //TODO
    private boolean manual = false;
    private int level = 0; //0 = intake, 1 = rocket, 2 = cargo

    private final int intakeSensorId = 0; //TODO
    private final int[] conveyorLimitIds = {1, 2}; //TODO
    private DigitalInput intakeSensor;
    private DigitalInput[] conveyorLimits = new DigitalInput[conveyorLimitIds.length];

    private final int[] intakeTalonsIds = {26, 27};
    private final int[] conveyorTalonIds  = {24, 25};
    private WPI_TalonSRX[] intakeTalons = new WPI_TalonSRX[intakeTalonsIds.length];
    private WPI_TalonSRX[] conveyorTalons = new WPI_TalonSRX[conveyorTalonIds.length];

    public void init() {
        for(int i = 0; i < intakeTalonsIds.length; i++){
            intakeTalons[i] = new WPI_TalonSRX(intakeTalonsIds[i]);
        }
        for(int i = 0; i < conveyorTalonIds.length; i++){
            conveyorTalons[i] = new WPI_TalonSRX(conveyorTalonIds[i]);
        }

        intakeSensor = new DigitalInput(intakeSensorId);
        for (int i = 0; i < conveyorLimits.length; i++){
            conveyorLimits[i] = new DigitalInput(conveyorLimitIds[i]);
        }

        conveyorTalons[0].configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);

        conveyorTalons[1].set(ControlMode.Follower, conveyorTalons[0].getDeviceID());

        conveyorTalons[0].config_kP(0, p, 0);
        conveyorTalons[0].config_kP(0, i, 0);
        conveyorTalons[0].config_kP(0, d, 0);
    }

    public void run(){
        Controls.getInstance().registerDigitalCommand(2, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick11, DigitalType.DigitalRelease, ()->{
            manual = !manual;
            if(manual){
                System.out.println("Manual Mode");
            }
            else {
                System.out.println("Auton Mode");
            }
        });
        if(manual){

        }
        else {

        }
    }

    private void spinIntake(double topSpeed, double bottomSpeed){
        intakeConveyor();
        while (!intakeSensor.get()) {
            intakeTalons[0].set(ControlMode.PercentOutput, topSpeed);
            intakeTalons[1].set(ControlMode.PercentOutput, bottomSpeed);
        }
        stopIntake();
        visionConveyor();
    }

    private void spinRoller(double topSpeed, double bottomSpeed){
        intakeTalons[0].set(ControlMode.PercentOutput, topSpeed);
        intakeTalons[1].set(ControlMode.PercentOutput, bottomSpeed);
    }

    private void spinOuttake(double topSpeed, double bottomSpeed){
       while (intakeSensor.get()){
            intakeTalons[0].set(ControlMode.PercentOutput, -topSpeed);
            intakeTalons[1].set(ControlMode.PercentOutput, -bottomSpeed);
        }
        stopIntake();
    }

    private void stopIntake(){
            intakeTalons[0].set(ControlMode.PercentOutput, 0);
            intakeTalons[1].set(ControlMode.PercentOutput, 0);
    }

    private void moveConveyor(double neededPos){
        double error1 = conveyorTalons[0].getClosedLoopError();
        neededPos += conveyorTalons[0].getSelectedSensorPosition();
        conveyorTalons[0].set(ControlMode.Position, neededPos*ticksPerDegree);

        while((Math.abs(error1) > threshold)){
            error1 = conveyorTalons[0].getClosedLoopError();
            System.out.println("T1: " + conveyorTalons[0].getSelectedSensorPosition());
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        conveyorTalons[0].set(ControlMode.PercentOutput, 0);
    }

    private void rocketConveyor(){
        moveConveyor(rocketHeight);
    }

    private void cargoConveyor(){
        moveConveyor(cargoHeight);
    }

    private void intakeConveyor(){
        moveConveyor(intakeHeight);
    }

    private void visionConveyor() {
        moveConveyor(visionHeight);
    }

    private void calibrateConveyor() {
        while (!conveyorLimits[0].get()) {
            conveyorTalons[0].set(ControlMode.PercentOutput, 0.5);
        }
        conveyorTalons[0].setSelectedSensorPosition(90);
    }

    private void cargoLimitSafety() {
        while(conveyorLimits[0].get()) {
            conveyorTalons[0].set(ControlMode.PercentOutput, -0.5);
        }
        conveyorTalons[0].set(ControlMode.PercentOutput, 0);
        while(conveyorLimits[1].get()) {
            conveyorTalons[0].set(ControlMode.PercentOutput, 0.5);
        }
        conveyorTalons[0].set(ControlMode.PercentOutput, 0);
    }
}