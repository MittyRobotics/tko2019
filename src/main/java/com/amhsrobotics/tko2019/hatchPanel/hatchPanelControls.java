package com.amhsrobotics.tko2019.hatchPanel;

import edu.wpi.first.wpilibj.*;

import static com.amhsrobotics.tko2019.hatchPanel.hatchPanel.*;

public class hatchPanelControls{
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
    }

}
