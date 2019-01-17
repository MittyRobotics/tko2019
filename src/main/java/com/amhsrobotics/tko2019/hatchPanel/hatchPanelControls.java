package com.amhsrobotics.tko2019.hatchPanel;

import edu.wpi.first.wpilibj.*;

public class hatchPanelControls extends hatchPanel {
    public static Joystick leftJoy = new Joystick(0);

    public static void run(){
        //limitswitch();
        if (leftJoy.getRawButton(10)){
            openHatch();
        }
        else if (leftJoy.getRawButton(11)){
            closeHatch();
        }
        else if (leftJoy.getY() < -0.1){
            goHatchBackward();
        }
        else if (leftJoy.getY() > 0.1){
            goHatchForward();
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
