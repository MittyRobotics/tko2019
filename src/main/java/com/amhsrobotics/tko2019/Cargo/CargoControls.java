package com.amhsrobotics.tko2019.Cargo;

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
                Cargo.spinIntake();
            }
            else if(cargoControls.getRawButton(4)){
                Cargo.stopIntake();
            }

        }
    }
}
