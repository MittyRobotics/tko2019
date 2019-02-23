package com.amhsrobotics.tko2019.sequences.drive;

import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.sequences.Action;

public final class TurnAction implements Action {
	private final double angle;

	public TurnAction(final double angle) {
		this.angle = angle;
	}

	@Override
	public void doAction(final boolean continuing) {
		Drive.getInstance().turn(angle);
	}

	@Override
	public boolean isContinuable() {
		return false;
	}
}
