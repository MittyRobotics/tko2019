package com.amhsrobotics.tko2019.drive;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import java.lang.Math;

public class driveControls {
    //Add controller
    XboxController controller;
    boolean aButtonPressed = false; //Stores state of A button on controller

    public void run(){
        //Change port based on computer USB port. Temp nil
        controller = new XboxController(0); //Probably shouldn't declare as new every run of the loop
        aButtonPressed = controller.getAButtonPressed();

        //Get left stick Y pos and call drive function
        if (Math.abs(controller.getY(GenericHID.Hand.kLeft)) > 0.05){
            drive.moveLeft(controller.getY(GenericHID.Hand.kLeft));
        } else {
            drive.moveLeft(0);
        }

        if (Math.abs(controller.getY(GenericHID.Hand.kRight)) > 0.05){
            drive.moveRight(controller.getY(GenericHID.Hand.kRight));
        } else {
            drive.moveRight(0);
        }

        //Get state of A button - note: double check code to make sure it should work
        if (aButtonPressed == true){
            aButtonPressed = !aButtonPressed; //Toggle A button pressed boolean
            drive.toggleReverser(aButtonPressed);
        }

    }
}
