package com.amhsrobotics.tko2019.hatchPanel;

import edu.wpi.first.wpilibj.*;

public class hatchPanelControls extends hatchPanel {
    public static Joystick leftJoy = new Joystick(0);

    public static void run(){
        //limitswitch();
        if (leftJoy.getRawButton(10)){
            cargoDrop();
        }
        else if (leftJoy.getRawButton(11)) {
            rocketDrop();
        }
        else if (leftJoy.getRawButton(4)) {
            slideLeft();
        }
        else if (leftJoy.getRawButton(3)) {
            slideMiddle();
        }
        else if (leftJoy.getRawButton(5)){
            slideRight();
        }
    }

}
