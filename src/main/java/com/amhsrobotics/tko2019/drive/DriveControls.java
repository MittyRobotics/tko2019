package com.amhsrobotics.tko2019.drive;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import java.lang.Math;

public class DriveControls {

    XboxController controller;
    boolean aButtonPressed = false;

    public void run(){

        controller = new XboxController(0);
        aButtonPressed = controller.getAButtonPressed();


        if (Math.abs(controller.getY(GenericHID.Hand.kLeft)) > 0.05){
            Drive.moveLeft(controller.getY(GenericHID.Hand.kLeft));
        } else {
            Drive.moveLeft(0.5);
        }

        if (Math.abs(controller.getY(GenericHID.Hand.kRight)) > 0.05){
            Drive.moveRight(controller.getY(GenericHID.Hand.kRight));
        } else {
            Drive.moveRight(0);
        }


        if (aButtonPressed == true){
            aButtonPressed = !aButtonPressed;
            Drive.toggleReverser(aButtonPressed);
        }

    }
}
