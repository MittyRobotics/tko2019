package com.amhsrobotics.tko2019.hardware.subsystems;

import com.amhsrobotics.tko2019.hardware.settings.subsystems.SolenoidIds;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public final class Climber {
	private static final Climber INSTANCE = new Climber();

	private final DoubleSolenoid climberSolenoid = new DoubleSolenoid(SolenoidIds.CLIMBER[0], SolenoidIds.CLIMBER[1]);

	private Climber() {
		climberSolenoid.set(DoubleSolenoid.Value.kForward);
	}

	public static Climber getInstance() {
		return INSTANCE;
	}

	public final void release() {
		climberSolenoid.set(DoubleSolenoid.Value.kReverse);
	}
}
