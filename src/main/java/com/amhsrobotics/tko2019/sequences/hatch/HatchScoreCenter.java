package com.amhsrobotics.tko2019.sequences.hatch;

import com.amhsrobotics.tko2019.sequences.actions.hatch.MoveHatchCenter;

public final class HatchScoreCenter extends HatchScore {
	public HatchScoreCenter() {
		super(new MoveHatchCenter());
	}
}
