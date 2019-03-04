package com.amhsrobotics.tko2019.sequences.hatch;

import com.amhsrobotics.tko2019.sequences.actions.hatch.MoveHatchCenter;

public final class HatchGrabCenter extends HatchScore {
	public HatchGrabCenter() {
		super(new MoveHatchCenter());
	}
}