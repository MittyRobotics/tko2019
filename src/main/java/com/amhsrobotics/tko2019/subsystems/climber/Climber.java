package com.amhsrobotics.tko2019.subsystems.climber;

import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.DigitalInput;
import com.amhsrobotics.tko2019.controls.DigitalType;
import com.amhsrobotics.tko2019.settings.subsystems.SolenoidIds;
import com.amhsrobotics.tko2019.subsystems.Subsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;

public class Climber implements Subsystem {
	private DoubleSolenoid climberSolenoid;

	@Override
	public void init() {
		climberSolenoid = new DoubleSolenoid(SolenoidIds.CLIMBER[0], SolenoidIds.CLIMBER[1]);
	}

	@Override
	public void initControls() {
		Controls.getInstance().registerDigitalCommand(0, DigitalInput.XboxA, DigitalType.DigitalPress, () -> {
			if (DriverStation.getInstance().getMatchTime() < 30) {
				release();
			}
		});
	}

	private void release() {
		climberSolenoid.set(DoubleSolenoid.Value.kForward);
	}
}