package com.amhsrobotics.tko2019.sequences.hatch;

import com.amhsrobotics.tko2019.sequences.actions.hatch.MoveHatchLeft;

public final class HatchScoreLeft extends HatchScore {
	public HatchScoreLeft() {
		super(new MoveHatchLeft());
	}
}
