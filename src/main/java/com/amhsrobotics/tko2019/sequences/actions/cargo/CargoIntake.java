package com.amhsrobotics.tko2019.sequences.actions.cargo;

import com.amhsrobotics.tko2019.hardware.subsystems.Cargo;
import com.amhsrobotics.tko2019.sequences.Action;

public final class CargoIntake implements Action {
	private final double speed;

	public CargoIntake(final double speed) {
		this.speed = speed;
	}

	@Override
	public final void doAction(boolean continuing) {
		Cargo.getInstance().spinOuttake(-speed); // FIXME: 2019-02-22 Need to Make Spin Intake, Need to Ask Group About Reasoning of Code
	}
}
