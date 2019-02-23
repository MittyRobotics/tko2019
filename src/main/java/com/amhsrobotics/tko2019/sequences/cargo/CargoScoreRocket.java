package com.amhsrobotics.tko2019.sequences.cargo;

import com.amhsrobotics.tko2019.sequences.actions.cargo.AngleCargoToRocket;

public class CargoScoreRocket extends CargoScore {
	public CargoScoreRocket() {
		super(new AngleCargoToRocket());
	}
}
