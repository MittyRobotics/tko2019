package com.amhsrobotics.tko2019.vision.sequences.actions.cargo;

import com.amhsrobotics.tko2019.hardware.subsystems.Cargo;
import com.amhsrobotics.tko2019.vision.sequences.actions.Action;

public final class AngleCargoToRocket implements Action {
	@Override
	public final void doAction(final boolean continuing) {
		Cargo.getInstance().rocketConveyor();
	}
}
