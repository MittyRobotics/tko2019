package com.amhsrobotics.tko2019.Cargo;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Cargo {
    DigitalInput limitSwitch;


    public void init() {
        limitSwitch = new DigitalInput(0);
        limitSwitch.get();
    }
}