package com.amhsrobotics.tko2019.Climber;

import edu.wpi.first.wpilibj.XboxController;

import static com.amhsrobotics.tko2019.Climber.Climber.*;

public class ClimberControls {
	XboxController controller = new XboxController(0);
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
