package com.amhsrobotics.tko2019.vision.sequences.hatch;

import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.vision.sequences.Sequence;
import com.amhsrobotics.tko2019.vision.sequences.actions.hatch.GrabHatch;
import com.amhsrobotics.tko2019.vision.sequences.actions.hatch.MoveHatchBack;
import com.amhsrobotics.tko2019.vision.sequences.actions.hatch.MoveHatchForward;

public final class HatchRetrieve extends Sequence {
	public HatchRetrieve() {
		super(new MoveHatchForward(), new GrabHatch(), new MoveHatchBack());
	}

	@Override
	protected boolean shouldContinue() {
		return Switches.getInstance().hasHatch();
	}
}
