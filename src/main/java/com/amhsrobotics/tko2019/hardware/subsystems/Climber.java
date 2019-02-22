package com.amhsrobotics.tko2019.hardware.subsystems;

import com.amhsrobotics.tko2019.controls.Controller;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.commands.DigitalType;
import com.amhsrobotics.tko2019.settings.ControlsConfig;
import com.amhsrobotics.tko2019.settings.subsystems.SolenoidIds;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;

public class Climber {
	private final static Climber INSTANCE = new Climber();
	private final DoubleSolenoid climberSolenoid;
	private volatile boolean xPressed = false;

	private Climber() {
		climberSolenoid = new DoubleSolenoid(SolenoidIds.CLIMBER[0], SolenoidIds.CLIMBER[1]);

		Controls.getInstance().registerDigitalCommand(Controller.XboxController, ControlsConfig.RELEASE_CLIMBER_2, DigitalType.DigitalPress, () -> {
			if (DriverStation.getInstance().getMatchTime() < 30 && DriverStation.getInstance().isOperatorControl() && xPressed) {
				HatchPanel.getInstance().slideMiddle();
				release();
			}
		}).registerDigitalCommand(Controller.XboxController, ControlsConfig.RELEASE_CLIMBER_1, DigitalType.DigitalHold, () -> xPressed = true)
				.registerDigitalCommand(Controller.XboxController, ControlsConfig.RELEASE_CLIMBER_1, DigitalType.DigitalRelease, () -> xPressed = false);
	}

	public static Climber getInstance() {
		return INSTANCE;
	}

	private void release() {
		climberSolenoid.set(DoubleSolenoid.Value.kReverse);
	}
}
