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
	public static Climber getInstance() {
		return INSTANCE;
	}

	private Climber() {

	}

	private DoubleSolenoid climberSolenoid;
	private HatchPanel hatchPanel;
	@Override
	public void init() {
		entering("init");

		climberSolenoid = new DoubleSolenoid(SolenoidIds.CLIMBER[0], SolenoidIds.CLIMBER[1]);
		hatchPanel = new HatchPanel();
		exiting("init");
	}

	@Override
	public void initControls() {
		entering("initControls");

		Controls.getInstance().registerDigitalCommand(ControllerID.XboxController.getId(), ControlsConfig.RELEASE_CLIMBER, DigitalType.DigitalPress, () -> {
			if (DriverStation.getInstance().getMatchTime() < 30 && DriverStation.getInstance().isOperatorControl()) {
				hatchPanel.slideMiddle();
				release();
			}
		});

		exiting("initControls");
	}

	private void release() {
		entering("release");

		solenoidSet(climberSolenoid, DoubleSolenoid.Value.kForward);
		climberSolenoid.set(DoubleSolenoid.Value.kForward);

		exiting("release");
	}
}