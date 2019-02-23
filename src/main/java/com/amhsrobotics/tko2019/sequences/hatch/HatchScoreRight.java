package com.amhsrobotics.tko2019.sequences.hatch;

import com.amhsrobotics.tko2019.sequences.actions.hatch.MoveHatchRight;

public final class HatchScoreRight extends HatchScore {
	public HatchScoreRight() {
		super(new MoveHatchRight());
	}
}
