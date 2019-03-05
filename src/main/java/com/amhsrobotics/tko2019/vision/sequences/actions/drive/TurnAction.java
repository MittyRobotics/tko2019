package com.amhsrobotics.tko2019.vision.sequences.actions.drive;

import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.vision.sequences.actions.Action;

public final class TurnAction implements Action {
	private final double angle;

	public TurnAction(final double angle) {
		this.angle = angle;
	}

	@Override
	public final void doAction(final boolean continuing) {
		Drive.getInstance().turn(angle);
	}

	@Override
	public final boolean isContinuable() {
		return false;
	}
}
