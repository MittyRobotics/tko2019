package com.amhsrobotics.tko2019.sequences.actions.cargo;

import com.amhsrobotics.tko2019.hardware.subsystems.Cargo;
import com.amhsrobotics.tko2019.sequences.Action;

public final class CargoOuttake implements Action {
	private final double speed;

	public CargoOuttake(final double speed) {
		this.speed = speed;
	}

	@Override
	public final void doAction(final boolean continuing) {
		Cargo.getInstance().spinOuttake(speed);
	}
}
