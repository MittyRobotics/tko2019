package com.amhsrobotics.tko2019.sequences.cargo;

import com.amhsrobotics.tko2019.sequences.actions.cargo.AngleCargoToPlayer;
import com.amhsrobotics.tko2019.sequences.actions.cargo.AngleCargoToRocket;

public class CargoRetrievePlayer extends CargoScore {
	public CargoRetrievePlayer() {
		super(new AngleCargoToPlayer());
	}
}
