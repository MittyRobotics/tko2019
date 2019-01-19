package com.amhsrobotics.tko2019.hatchpanel;

import edu.wpi.first.wpilibj.*;

public class HatchPanelControls extends HatchPanel {
    public static Joystick leftJoy = new Joystick(0);
    private static boolean manual = false;
    public static void run(){
        //limitswitch();
        if(leftJoy.getRawButton(11)){
            manual = !manual;
        }
        if(manual){
            if (leftJoy.getTriggerPressed()){
                openHatch();
            }
            else if (leftJoy.getRawButton(2)){
                closeHatch();
            }
            if (leftJoy.getY() > 0.1){
                goHatchForward();
            }
            else if (leftJoy.getY() < -0.1){
                goHatchBackward();
            }
            if (leftJoy.getRawButton(4)){
                slideLeft();
            }
            else if (leftJoy.getRawButton(5)){
                slideRight();
            }
            else if (leftJoy.getRawButton(3)) {
                slideMiddle();
            }
        }
        else{
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

}
