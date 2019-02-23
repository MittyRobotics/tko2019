package com.amhsrobotics.tko2019.sequences.actions.drive;

import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.sequences.Action;

public final class MoveAction implements Action {
	private final double inches;

	public MoveAction(final double inches) {
		this.inches = inches;
	}

	@Override
	public final void doAction(final boolean continuing) {
		Drive.getInstance().moveStraight(inches);
	}

	@Override
	public final boolean isContinuable() {
		return false;
	}
}
