package com.amhsrobotics.tko2019.sequences.cargo;

import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.sequences.Action;
import com.amhsrobotics.tko2019.sequences.Sequence;

abstract class CargoScore extends Sequence {
	CargoScore(final Action changeHeightAction) {
		super();
	}

	@Override
	protected boolean shouldContinue() {
		return Switches.getInstance().hasCargo();
	}
}
