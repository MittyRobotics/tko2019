package com.amhsrobotics.tko2019.cargo;

import edu.wpi.first.wpilibj.Joystick;

public class CargoControls {
    //still preliminary
    Joystick cargoControls;

    public void runCargo(){
        while (true){
            if(cargoControls.getRawButton(2)){
                Cargo.raiseConveyor();
            }
            else if(cargoControls.getRawButton(3)){
                Cargo.dropConveyor();
            }
            else if(cargoControls.getTrigger()){
                Cargo.spinIntake(Cargo.cargoSpeed, Cargo.cargoSpeed);
            }
            else if(cargoControls.getRawButton(4)){
                Cargo.stopIntake();
            }
            else{
                Cargo.intakeAuton();
            }
            if(!cargoControls.getTrigger()){
                Cargo.stopIntake();
            }
        }
    }
}
