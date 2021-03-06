package com.amhsrobotics.tko2019.vision.sequences.actions.cargo;

import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.hardware.subsystems.Cargo;
import com.amhsrobotics.tko2019.vision.sequences.actions.Action;

public final class CargoIntake implements Action {
	@Override
	public final void doAction(final boolean continuing) {
		while (!Switches.getInstance().hasCargo()) {
			Cargo.getInstance().spinIntake();
		}
		Cargo.getInstance().stopIntake();
	}
}
