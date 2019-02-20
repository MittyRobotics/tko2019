package com.amhsrobotics.tko2019.subsystems.climber;

import com.amhsrobotics.tko2019.controls.ControllerID;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.DigitalType;
import com.amhsrobotics.tko2019.settings.ControlsConfig;
import com.amhsrobotics.tko2019.settings.subsystems.SolenoidIds;
import com.amhsrobotics.tko2019.subsystems.Subsystem;
import com.amhsrobotics.tko2019.subsystems.hatchpanel.HatchPanel;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;

public class Climber implements Subsystem {
	private final static Climber INSTANCE = new Climber();
	private DoubleSolenoid climberSolenoid;
	private HatchPanel hatchPanel = HatchPanel.getInstance();

	private Climber() {

	}

	public static Climber getInstance() {
		return INSTANCE;
	}

	@Override
	public void init() {
		climberSolenoid = new DoubleSolenoid(SolenoidIds.CLIMBER[0], SolenoidIds.CLIMBER[1]);
	}

	@Override
	public void initControls() {
		Controls.getInstance().registerDigitalCommand(ControllerID.XboxController.getId(), ControlsConfig.RELEASE_CLIMBER, DigitalType.DigitalPress, () -> {
			if (DriverStation.getInstance().getMatchTime() < 30 && DriverStation.getInstance().isOperatorControl()) {
				hatchPanel.slideMiddle();
				release();
			}
		});
	}

	private void release() {
		climberSolenoid.set(DoubleSolenoid.Value.kForward);
	}
}