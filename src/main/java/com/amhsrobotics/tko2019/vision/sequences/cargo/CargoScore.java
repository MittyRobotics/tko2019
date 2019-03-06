package com.amhsrobotics.tko2019.vision.sequences.cargo;

import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.vision.sequences.Sequence;
import com.amhsrobotics.tko2019.vision.sequences.actions.cargo.AngleCargoToRocket;
import com.amhsrobotics.tko2019.vision.sequences.actions.cargo.CargoOuttake;

public class CargoScore extends Sequence {
	public CargoScore() {
		super(new AngleCargoToRocket(), new CargoOuttake());
	}

	@Override
	protected boolean shouldContinue() {
		return Switches.getInstance().hasCargo();
	}
}
