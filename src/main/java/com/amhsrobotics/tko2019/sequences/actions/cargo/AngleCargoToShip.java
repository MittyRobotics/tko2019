package com.amhsrobotics.tko2019.sequences.actions.cargo;

import com.amhsrobotics.tko2019.hardware.subsystems.Cargo;
import com.amhsrobotics.tko2019.sequences.Action;

public final class AngleCargoToShip implements Action {
	@Override
	public final void doAction(final boolean continuing) {
		Cargo.getInstance().cargoConveyor();
	}
}
