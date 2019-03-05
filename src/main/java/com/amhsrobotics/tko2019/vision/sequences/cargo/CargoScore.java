package com.amhsrobotics.tko2019.vision.sequences.cargo;

import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.vision.sequences.actions.Action;
import com.amhsrobotics.tko2019.vision.sequences.Sequence;
import com.amhsrobotics.tko2019.vision.sequences.actions.cargo.CargoOuttake;

public class CargoScore extends Sequence {
	CargoScore(final Action changeHeightAction) {
		super(new CargoOuttake(0.5));
	}

	@Override
	protected boolean shouldContinue() {
		return Switches.getInstance().hasCargo();
	}
}
