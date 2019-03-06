package com.amhsrobotics.tko2019.vision.sequences.hatch;

import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.vision.sequences.Sequence;
import com.amhsrobotics.tko2019.vision.sequences.actions.hatch.MoveHatchBack;
import com.amhsrobotics.tko2019.vision.sequences.actions.hatch.MoveHatchForward;
import com.amhsrobotics.tko2019.vision.sequences.actions.hatch.ReleaseHatch;

public final class HatchScore extends Sequence {
	public HatchScore() {
		super(new MoveHatchForward(), new ReleaseHatch(), new MoveHatchBack());
	}

	@Override
	protected boolean shouldContinue() {
		return Switches.getInstance().hasHatch();
	}
}
