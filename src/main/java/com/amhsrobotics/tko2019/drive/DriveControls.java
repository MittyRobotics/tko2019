package com.amhsrobotics.tko2019.drive;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import java.lang.Math;

public class DriveControls {
    //Add controller
    XboxController controller;
    boolean aButtonPressed = false; //Stores state of A button on controller

    public void run(){
        //Change port based on computer USB port. Temp nil
        controller = new XboxController(0); //Probably shouldn't declare as new every run of the loop
        aButtonPressed = controller.getAButtonPressed();

        //Get left stick Y pos and call Drive function
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

        //Get state of A button - note: double check code to make sure it should work
        if (aButtonPressed == true){
            aButtonPressed = !aButtonPressed; //Toggle A button pressed boolean
            Drive.toggleReverser(aButtonPressed);
        }

    }
}
