package com.amhsrobotics.tko2019.sequences.hatch;

import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.sequences.Action;
import com.amhsrobotics.tko2019.sequences.Sequence;
import com.amhsrobotics.tko2019.sequences.actions.hatch.GrabHatch;
import com.amhsrobotics.tko2019.sequences.actions.hatch.MoveHatchBack;
import com.amhsrobotics.tko2019.sequences.actions.hatch.MoveHatchForward;
import com.amhsrobotics.tko2019.sequences.actions.hatch.ReleaseHatch;

class HatchRetrieve extends Sequence {
	HatchRetrieve(final Action movementAction) {
		super(new GrabHatch());
	}

	@Override
	protected boolean shouldContinue() {
		return Switches.getInstance().hasHatch();
	}
}
