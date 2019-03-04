package com.amhsrobotics.tko2019.sequences.cargo;

import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.sequences.Action;
import com.amhsrobotics.tko2019.sequences.Sequence;
import com.amhsrobotics.tko2019.sequences.actions.cargo.CargoIntake;

abstract class CargoGrab extends Sequence {
	CargoGrab(final Action changeHeightAction) {
		super(new CargoIntake(0.5));
	}

	@Override
	protected boolean shouldContinue() {
		return Switches.getInstance().hasCargo();
	}
}
