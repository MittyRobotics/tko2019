package com.amhsrobotics.tko2019.climber;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.XboxController;

public class Climber {
	private boolean isOpen = false;

	private final int[] climberId = {0, 1}; //TODO
	private DoubleSolenoid climber;

	private final int controllerId = 0;
	private XboxController controller;
	public void init(){
		controller = new XboxController(controllerId);

		climber = new DoubleSolenoid(climberId[0], climberId[1]);
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
		if(controller.getAButtonPressed()){
			if(isOpen){
				closeClimber();
			}
			else {
				openClimber();
			}
		}
	}
}
