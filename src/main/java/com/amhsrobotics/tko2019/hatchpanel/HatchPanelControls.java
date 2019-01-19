package com.amhsrobotics.tko2019.hatchpanel;

import edu.wpi.first.wpilibj.*;

public class HatchPanelControls extends HatchPanel {
    public static Joystick leftJoy = new Joystick(0);

    public static void run(){
        //limitswitch();
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
