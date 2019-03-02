package com.amhsrobotics.tko2019.sequences.cargo;

import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.sequences.Action;
import com.amhsrobotics.tko2019.sequences.Sequence;
import com.amhsrobotics.tko2019.sequences.actions.cargo.CargoOuttake;

abstract class CargoScore extends Sequence {
	CargoScore(final Action changeHeightAction) {
		super(new CargoOuttake(0.5));
	}

	@Override
	protected boolean shouldContinue() {
		return Switches.getInstance().hasCargo();
	}
}
