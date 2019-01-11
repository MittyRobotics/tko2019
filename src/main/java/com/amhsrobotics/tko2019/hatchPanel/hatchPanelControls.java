package com.amhsrobotics.tko2019.hatchPanel;

import edu.wpi.first.wpilibj.*;

import static com.amhsrobotics.tko2019.hatchPanel.hatchPanel.*;

public class hatchPanelControls{
    public static Joystick leftJoy = new Joystick(0);
    public void run(){
        if (leftJoy.getRawButton(4)){
            openHatch();
        }
        else if (leftJoy.getRawButton(5)){
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
