package com.amhsrobotics.tko2019.sequences.cargo;

import com.amhsrobotics.tko2019.sequences.actions.cargo.AngleCargoToShip;

public class CargoScoreShip extends CargoScore {
	public CargoScoreShip() {
		super(new AngleCargoToShip());
	}
}
