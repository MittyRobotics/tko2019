package com.amhsrobotics.tko2019.climber;

import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.DigitalInput;
import com.amhsrobotics.tko2019.controls.DigitalType;
import com.amhsrobotics.tko2019.hatchpanel.HatchPanel;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Climber {
	private final int[] climberId = {6, 7}; //TODO
	public DoubleSolenoid climber;
	private HatchPanel hatchPanel =  new HatchPanel();

	public void init(){
		climber = new DoubleSolenoid(climberId[0], climberId[1]);
	}
	public void run(){
		Controls.getInstance().registerDigitalCommand(2, DigitalInput.Joystick6, DigitalType.DigitalPress, ()->{
			hatchPanel.slide(-8);
				openClimber();
		});
		Controls.getInstance().registerDigitalCommand(2, DigitalInput.Joystick7, DigitalType.DigitalPress, ()->{
			climber.set(DoubleSolenoid.Value.kForward);
		});
	}
	private void openClimber(){
		climber.set(DoubleSolenoid.Value.kReverse);
	}
}