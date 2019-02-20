package com.amhsrobotics.tko2019.hardware.subsystems;

import com.amhsrobotics.tko2019.controls.ControllerID;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.commands.DigitalType;
import com.amhsrobotics.tko2019.settings.ControlsConfig;
import com.amhsrobotics.tko2019.settings.subsystems.SolenoidIds;
import com.amhsrobotics.tko2019.hardware.Subsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;

public class Climber implements Subsystem {
	private final static Climber INSTANCE = new Climber();
	private final DoubleSolenoid climberSolenoid;

	private Climber() {
		climberSolenoid = new DoubleSolenoid(SolenoidIds.CLIMBER[0], SolenoidIds.CLIMBER[1]);
	}

	public static Climber getInstance() {
		return INSTANCE;
	}

	@Override
	public void initControls() {
		Controls.getInstance().registerDigitalCommand(ControllerID.XboxController.getId(), ControlsConfig.RELEASE_CLIMBER, DigitalType.DigitalPress, () -> {
			if (DriverStation.getInstance().getMatchTime() < 30 && DriverStation.getInstance().isOperatorControl()) {
				HatchPanel.getInstance().slideMiddle();
				release();
			}
		});
	}

	private void release() {
		climberSolenoid.set(DoubleSolenoid.Value.kForward);
	}
}