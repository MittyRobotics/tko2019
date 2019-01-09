package com.amhsrobotics.tko2019.drive;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import java.lang.Math;

public class driveControls {
    XboxController controller;
    public void run(){
        controller = new XboxController(0);
        if (Math.abs(controller.getY(GenericHID.Hand.kLeft)) > 0.05) {
            drive.moveLeft(controller.getY(GenericHID.Hand.kLeft));
        } else {
            drive.moveLeft(0);
        }


    }
}
