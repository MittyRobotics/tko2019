package com.amhsrobotics.tko2019.sequences.hatch;

import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.sequences.Action;
import com.amhsrobotics.tko2019.sequences.Sequence;

abstract class HatchScore extends Sequence {
	HatchScore(final Action movementAction) {
		super();
	}

	@Override
	protected boolean shouldContinue() {
		return Switches.getInstance().hasHatch();
	}
}
