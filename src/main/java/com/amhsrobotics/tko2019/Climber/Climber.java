package com.amhsrobotics.tko2019.Climber;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Climber {
	static boolean isOpen = false;
	static DoubleSolenoid climber;
	public static void init(){
		climber = new DoubleSolenoid(0, 1);
	}
	public static void openClimber(){
		climber.set(DoubleSolenoid.Value.kForward);
		isOpen = false;
	}
	public static void closeClimber(){
		climber.set(DoubleSolenoid.Value.kReverse);
		isOpen = true;
	}
}
