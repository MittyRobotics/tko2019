package com.amhsrobotics.tko2019.sequences.hatch;

import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.sequences.Action;
import com.amhsrobotics.tko2019.sequences.Sequence;
import com.amhsrobotics.tko2019.sequences.actions.hatch.MoveHatchBack;
import com.amhsrobotics.tko2019.sequences.actions.hatch.MoveHatchForward;
import com.amhsrobotics.tko2019.sequences.actions.hatch.ReleaseHatch;

abstract class HatchScore extends Sequence {
	HatchScore(final Action movementAction) {
		super(new MoveHatchForward(), new ReleaseHatch(), new MoveHatchBack());
	}

	@Override
	protected boolean shouldContinue() {
		return Switches.getInstance().hasHatch();
	}
}
