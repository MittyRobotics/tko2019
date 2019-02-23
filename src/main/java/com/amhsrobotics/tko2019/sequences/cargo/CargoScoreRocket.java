package com.amhsrobotics.tko2019.sequences.cargo;

import com.amhsrobotics.tko2019.sequences.actions.MoveHatchForward;

public class CargoScoreRocket extends CargoScore {
	public CargoScoreRocket(final double inches) {
		super(new MoveHatchForward(), new MoveHatchForward(), new MoveHatchForward());
	}
}
