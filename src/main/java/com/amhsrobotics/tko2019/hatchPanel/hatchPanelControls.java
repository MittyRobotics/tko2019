package com.amhsrobotics.tko2019.hatchPanel;

import edu.wpi.first.wpilibj.*;

public class hatchPanelControls extends hatchPanel {
    public static Joystick leftJoy = new Joystick(0);

    public void run(){
        limitswitch();
        if (leftJoy.getY() > 0.1){
            openHatch();
        }
        else if (leftJoy.getY() < -0.1){
            closeHatch();
        }
        else if (leftJoy.getRawButton(10)){
            goHatchBackward();
        }
        else if (leftJoy.getRawButton(11)){
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
