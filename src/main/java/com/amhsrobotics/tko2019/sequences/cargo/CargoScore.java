package com.amhsrobotics.tko2019.sequences.cargo;

import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.sequences.Action;
import com.amhsrobotics.tko2019.sequences.Sequence;

abstract class CargoScore extends Sequence {
	CargoScore(Action... actions) {
		super(actions);
	}

	@Override
	protected boolean shouldContinue() {
		return Switches.getInstance().hasCargo();
	}
}
