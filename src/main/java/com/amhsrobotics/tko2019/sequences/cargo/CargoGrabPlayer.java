package com.amhsrobotics.tko2019.sequences.cargo;

import com.amhsrobotics.tko2019.sequences.actions.cargo.AngleCargoToPlayer;

public class CargoGrabPlayer extends CargoGrab {
	public CargoGrabPlayer() {
		super(new AngleCargoToPlayer());
	}
}