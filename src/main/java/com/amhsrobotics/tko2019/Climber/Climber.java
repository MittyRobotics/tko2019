package com.amhsrobotics.tko2019.Climber;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.XboxController;

public class Climber {
	private boolean isOpen = false;
	private DoubleSolenoid climber;
	public void init(){
		climber = new DoubleSolenoid(0, 1);
	}
	private void openClimber(){
		climber.set(DoubleSolenoid.Value.kForward);
		isOpen = false;
	}
	private void closeClimber(){
		climber.set(DoubleSolenoid.Value.kReverse);
		isOpen = true;
	}
	public void run(){
		XboxController controller = new XboxController(0);
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
