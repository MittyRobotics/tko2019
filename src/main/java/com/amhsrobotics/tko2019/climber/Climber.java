package com.amhsrobotics.tko2019.climber;

import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.DigitalInput;
import com.amhsrobotics.tko2019.controls.DigitalType;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Climber {
	private boolean isOpen = false;

	private final int[] climberId = {0, 1}; //TODO
	private DoubleSolenoid climber;

	public void init(){
		climber = new DoubleSolenoid(climberId[0], climberId[1]);
	}
	public void run(){
		Controls.getInstance().registerDigitalCommand(0, DigitalInput.XboxA, DigitalType.DigitalPress, ()->{
			if(isOpen){
				closeClimber();
			}
			else {
				openClimber();
			}
		});
	}
	private void openClimber(){
		climber.set(DoubleSolenoid.Value.kForward);
		isOpen = true;
	}
	private void closeClimber(){
		climber.set(DoubleSolenoid.Value.kReverse);
		isOpen = false;
	}
}
