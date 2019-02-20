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
    private final double cargoSpeed = 5; //TODO
    private final double rocketSpeed = 0.8; //TODO  these speeds will change be positive or negative
    private final double intakeSpeed = 0.5; //TODO
    private final double groundSpeed = 0.5; //TODO
    private final double rocketHeight = 0; //TODO
    private final double visionHeight = 0; //TODO
    private final double intakeHeight = 0; //TODO
    private final double cargoHeight = 0; //TODO
    private final double groundHeight = 0; //TODO
    private final double p = 20; //TODO
    private final double i = 0; //TODO
    private final double d = 0; //TODO
    private final double threshold = 1 * ticksPerDegree; //TODO
    boolean groundThreshold = false;
    boolean rocketThreshold = false;
    boolean cargoThreshold = false;
    boolean intakeThreshold = false;
    private boolean manual = false;
    private int level = 1; //1 = ground, 2 = rocket, 3 = cargo, 4 = human player height

    public final int intakeSensorId = 0; //TODO
    private final int[] conveyorLimitIds = {1, 2}; //TODO
    public DigitalInput intakeSensor;
    private DigitalInput[] conveyorLimits = new DigitalInput[conveyorLimitIds.length];

    private final int[] intakeTalonsIds = {30, 31};
    private final int[] conveyorTalonIds = {32, 33};
    private WPI_TalonSRX[] intakeTalons = new WPI_TalonSRX[intakeTalonsIds.length];
    public WPI_TalonSRX[] conveyorTalons = new WPI_TalonSRX[conveyorTalonIds.length];

    public void init() {
        for (int i = 0; i < intakeTalonsIds.length; i++) {
            intakeTalons[i] = new WPI_TalonSRX(intakeTalonsIds[i]);
        }
        for (int i = 0; i < conveyorTalonIds.length; i++) {
            conveyorTalons[i] = new WPI_TalonSRX(conveyorTalonIds[i]);
            conveyorTalons[i].configFactoryDefault();
        }

        intakeSensor = new DigitalInput(intakeSensorId);
//        for (int i = 0; i < conveyorLimits.length; i++) {
//            conveyorLimits[i] = new DigitalInput(conveyorLimitIds[i]);
//        }

        conveyorTalons[0].configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);

        conveyorTalons[1].set(ControlMode.Follower, conveyorTalons[0].getDeviceID());
        conveyorTalons[1].setInverted(true);
        conveyorTalons[0].setSensorPhase(true);
        conveyorTalons[0].config_kP(0, 0.1, 0);
        conveyorTalons[0].config_kI(0, 0, 0);
        conveyorTalons[0].config_kD(0, 0, 0);
    }

    public void run() {

        //heights need to be fixed
        Controls.getInstance().registerDigitalCommand(2, com.amhsrobotics.tko2019.controls.DigitalInput.JoystickTrigger, DigitalType.DigitalHold, () -> {
            if(!intakeSensor.get()){
                spinOuttake(0, 0);
            }
            else {
                spinOuttake(0.5, -0.5);
            }
        });
        Controls.getInstance().registerDigitalCommand(2, com.amhsrobotics.tko2019.controls.DigitalInput.JoystickTrigger, DigitalType.DigitalRelease, () ->{
            spinOuttake(0, 0);
        });
        Controls.getInstance().registerDigitalCommand(2, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick2, DigitalType.DigitalHold, () -> {
            spinOuttake(-0.5, 0.5);
        });
        Controls.getInstance().registerDigitalCommand(2, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick2, DigitalType.DigitalRelease, () ->{
            spinOuttake(0, 0);
        });

        Controls.getInstance().registerAnalogCommand(2, AnalogInput.JoystickY, AnalogType.OutOfThresholdMinor, value -> {
            conveyorTalons[0].set(ControlMode.Position, conveyorTalons[0].getSelectedSensorPosition() + value * 1000);

        });
        Controls.getInstance().registerDigitalCommand(2, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick8, DigitalType.DigitalPress, ()->{
            calibrateConveyor();
        });
        Controls.getInstance().registerDigitalCommand(2, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick4, DigitalType.DigitalPress, ()->{
            conveyorTalons[0].set(ControlMode.Position, -26200); //cargo height
        });
        Controls.getInstance().registerDigitalCommand(2, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick5, DigitalType.DigitalPress, ()->{
           conveyorTalons[0].set(ControlMode.Position, -18800); //rocket height
        });
        Controls.getInstance().registerDigitalCommand(2, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick6, DigitalType.DigitalPress, ()->{
            conveyorTalons[0].set(ControlMode.Position, -200); //intake height
        });
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
        if (intakeSensor.get()){
            visionConveyor();
            stopIntake();
        }

        intakeTalons[0].set(ControlMode.PercentOutput, topSpeed);
        intakeTalons[1].set(ControlMode.PercentOutput, bottomSpeed);
    }

    public void spinOuttake(double topSpeed, double bottomSpeed){
        intakeTalons[0].set(ControlMode.PercentOutput, topSpeed);
        intakeTalons[1].set(ControlMode.PercentOutput, bottomSpeed);
    }

    private void stopIntake () {
        intakeTalons[0].set(ControlMode.PercentOutput, 0);
        intakeTalons[1].set(ControlMode.PercentOutput, 0);
    }

    private void moveConveyor ( double neededPos){
        conveyorTalons[0].set(ControlMode.Position, neededPos);
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

    public void calibrateConveyor() {
        while (!conveyorTalons[0].getSensorCollection().isFwdLimitSwitchClosed()){
            conveyorTalons[0].set(ControlMode.PercentOutput, 0.1);
        }
        conveyorTalons[0].set(ControlMode.PercentOutput, 0);
        conveyorTalons[0].setSelectedSensorPosition(0);
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