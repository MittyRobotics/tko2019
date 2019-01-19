package com.amhsrobotics.tko2019.Climber;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.XboxController;

public class Climber {
	private boolean isOpen = false;
	private DoubleSolenoid climber;
	private XboxController controller;
	public void init(){
		climber = new DoubleSolenoid(0, 1);
	}
	public void openClimber(){
		climber.set(DoubleSolenoid.Value.kForward);
		isOpen = false;
	}
	public void closeClimber(){
		climber.set(DoubleSolenoid.Value.kReverse);
		isOpen = true;
	}
	public void run(){
		controller = new XboxController(0);
		if(controller.getAButton()){
			if(isOpen){
				closeClimber();
			}
			else {
				openClimber();
			}
		}
	}
}
