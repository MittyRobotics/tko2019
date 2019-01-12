package com.amhsrobotics.tko2019.Cargo;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

//////////////////////////////////////////////////////////////////////////
// CHECK THIS CODE. IT MAY OR MAY NOT WORK !!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
//////////////////////////////////////////////////////////////////////////

public class Cargo {
    static DigitalInput intakeSensor;
    static DigitalInput conveyorLimit1;
    static DigitalInput conveyorLimit2;
    static DigitalInput conveyorLimit3;
    static DigitalInput groundLimit1;
    private static final double ticksPerInch = 0; //TODO
    public static final double cargoSpeed = 0.8;
    public static final double rocketSpeed = 0.8;
    public static final double baseSpeed = 0.5;
    public static double cargoHeight = 0;
    public static double baseHeight = 0;


    public static WPI_TalonSRX[] intakeTalons = new WPI_TalonSRX[2];
    public static WPI_TalonSRX[] conveyorTalons = new WPI_TalonSRX[2];

    public void init() {

        intakeTalons[0] = new WPI_TalonSRX(0);
        intakeTalons[1] = new WPI_TalonSRX(1);

        conveyorTalons[0] = new WPI_TalonSRX(3);
        conveyorTalons[1] = new WPI_TalonSRX(4);

        intakeSensor = new DigitalInput(0);
        conveyorLimit1 = new DigitalInput(1);
        conveyorLimit1 = new DigitalInput(2);
        conveyorLimit1 = new DigitalInput(3);
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

    public static void spinIntake(double topSpeed, double bottomSpeed){
        intakeTalons[0].set(ControlMode.PercentOutput, topSpeed); // or -1
        intakeTalons[1].set(ControlMode.PercentOutput, bottomSpeed);
    }

    public static void spinouttake(){
        intakeTalons[0].set(ControlMode.PercentOutput, -.5); //TODO
        intakeTalons[1].set(ControlMode.PercentOutput, -.5);
    }

    public static void stopIntake(){
        intakeTalons[0].set(ControlMode.PercentOutput, 0);
    }

    public static void moveConveyor(double neededPos){
        double threshold = 1;   //TODO
        double error1 = conveyorTalons[0].getClosedLoopError();
        neededPos += conveyorTalons[0].getSelectedSensorPosition();
        conveyorTalons[0].set(ControlMode.Position, neededPos*ticksPerInch);

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

    public static void raiseConveyor(){
        final double highSetpoint = 0; //TODO
        moveConveyor(highSetpoint);
    }

    public static void rocketConveyor(){
        final double highSetpoint = 150; //TODO
        moveConveyor(highSetpoint);
    }

    public static void dropConveyor(){
        final double lowSetpoint = 300;    //TODO
        moveConveyor(lowSetpoint);
    }

    public static void intakeAuton (){
        if (!intakeSensor.get()){
            spinIntake(baseSpeed, baseSpeed);
        }
        else{
            stopIntake();
            raiseConveyor();
        }
    }
    public static void calibrateConveyor(){
        while(!conveyorLimit1.get()){
            conveyorTalons[0].set(ControlMode.Position, conveyorTalons[0].getSelectedSensorPosition() + 1);
        }
        cargoHeight = conveyorTalons[0].getSelectedSensorPosition();
        while(!conveyorLimit1.get()){
            conveyorTalons[0].set(ControlMode.Position, conveyorTalons[0].getSelectedSensorPosition() - 1);
        }
        baseHeight = conveyorTalons[0].getSelectedSensorPosition();
    }
}