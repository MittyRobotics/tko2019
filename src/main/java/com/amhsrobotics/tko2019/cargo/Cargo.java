package com.amhsrobotics.tko2019.cargo;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;

//////////////////////////////////////////////////////////////////////////
// CHECK THIS CODE. IT MAY OR MAY NOT WORK !!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
//////////////////////////////////////////////////////////////////////////

public class Cargo {
    DigitalInput intakeSensor;
    DigitalInput conveyorLimit1;
    DigitalInput conveyorLimit2;
    DigitalInput conveyorLimit3;
    DigitalInput groundLimit1;
    private final double ticksPerDegree = 0; //TODO
    public final double cargoSpeed = 0.8;
    public final double rocketSpeed = 0.8;
    public final double baseSpeed = 0.5;
    //public static double cargoHeight = 0;
    private double rocketHeight = 0;
    private double visionHeight = 0;
    private double intakeHeight = 0;
    private double cargoHeight = 0;
    private Joystick cargoJoystick;



    public WPI_TalonSRX[] intakeTalons = new WPI_TalonSRX[2];
    public WPI_TalonSRX[] conveyorTalons = new WPI_TalonSRX[2];

    public void init() {
        cargoJoystick = new Joystick(1);
        intakeTalons[0] = new WPI_TalonSRX(0);
        intakeTalons[1] = new WPI_TalonSRX(1);

        conveyorTalons[0] = new WPI_TalonSRX(3);
        conveyorTalons[1] = new WPI_TalonSRX(4);

        intakeSensor = new DigitalInput(0);
        conveyorLimit1 = new DigitalInput(1);
       // conveyorLimit2 = new DigitalInput(2);
        //conveyorLimit3 = new DigitalInput(3);
        groundLimit1 = new DigitalInput(4);

        conveyorTalons[0].configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);

        conveyorTalons[1].set(ControlMode.Follower, conveyorTalons[0].getDeviceID());

        double p = 0.2;  //TODO
        double i = 0;
        double d = 0;

        conveyorTalons[0].config_kP(0, p, 0);
        conveyorTalons[0].config_kP(0, i, 0);
        conveyorTalons[0].config_kP(0, d, 0);

    }

    public void run(){
        boolean manual = false;

        while(true){
            if(cargoJoystick.getRawButton(11)){
                manual = true;
            }
            if(cargoJoystick.getRawButton(10)){
                manual = false;
            }
            if (!manual) {
                if (cargoJoystick.getRawButton(2)) {
                    spinIntake(1, 1);
                }
                else if (cargoJoystick.getRawButton(4)) {
                    cargoConveyor();
                }
                else if (cargoJoystick.getRawButton(3)) {
                    rocketConveyor();
                }
                else if (cargoJoystick.getRawButton(2)) {
                    intakeConveyor();
                }
                else if (cargoJoystick.getTrigger()) {
                    spinOuttake(1, 1);
                }
            }
            if ((manual)){
                if (cargoJoystick.getRawButton(2)) {
                    intake(1, 1);
                }
                else if (cargoJoystick.getRawButton(3)) {
                    intake(-1, -1);
                }
                double cargopos = cargoJoystick.getY();
                conveyorTalons[0].set(ControlMode.PercentOutput, cargopos);
                conveyorTalons[1].set(ControlMode.PercentOutput, cargopos);
            }
        }
    }

    public void spinIntake(double topSpeed, double bottomSpeed){
        intakeConveyor();
        if(intakeSensor.get()){
            stopIntake();
        }
        else {
            intakeTalons[0].set(ControlMode.PercentOutput, topSpeed);
            intakeTalons[1].set(ControlMode.PercentOutput, bottomSpeed);
        }
    }

    public void intake(double topSpeed, double bottomSpeed){
        intakeTalons[0].set(ControlMode.PercentOutput, -topSpeed);
        intakeTalons[1].set(ControlMode.PercentOutput, -bottomSpeed);
    }

    public void spinOuttake(double topSpeed, double bottomSpeed){
        if(intakeSensor.get()){
            intakeTalons[0].set(ControlMode.PercentOutput, -topSpeed);
            intakeTalons[1].set(ControlMode.PercentOutput, -bottomSpeed);
        }
        else {
            stopIntake();
        }
    }

    public void stopIntake(){
            intakeTalons[0].set(ControlMode.PercentOutput, 0);
            intakeTalons[1].set(ControlMode.PercentOutput, 0);
    }

    public void moveConveyor(double neededPos){
        double threshold = 1;   //TODO
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

    public void rocketConveyor(){
        rocketHeight = 45; //TODO
        moveConveyor(rocketHeight);
    }

    public void cargoConveyor(){
        cargoHeight = 55; //TODO
        moveConveyor(rocketHeight);
    }

    public void intakeConveyor(){
        intakeHeight = 30;    //TODO
        moveConveyor(intakeHeight);
    }

    public void visionConveyor() {
        visionHeight = 100; //TODO
        moveConveyor(visionHeight);
    }

    public void calibrateConveyor() {
        while (!conveyorLimit1.get()) {
            conveyorTalons[0].set(ControlMode.PercentOutput, 0.5);
        }
        conveyorTalons[0].setSelectedSensorPosition(90);
    }

    public void cargoLimitSafety() {
        while(groundLimit1.get()) {
            conveyorTalons[0].set(ControlMode.PercentOutput, -0.5);
        }
        conveyorTalons[0].set(ControlMode.PercentOutput, 0);
        while(!conveyorLimit1.get()) {
            conveyorTalons[0].set(ControlMode.PercentOutput, 0.5);
        }
    }
}