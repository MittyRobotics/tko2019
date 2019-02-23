package com.amhsrobotics.tko2019.sequences.drive;

import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.sequences.Action;

public final class MoveAction implements Action {
	private final double inches;

	public MoveAction(final double inches) {
		this.inches = inches;
	}

	@Override
	public void doAction(final boolean continuing) {
		Drive.getInstance().moveStraight(inches);
	}

	@Override
	public boolean isContinuable() {
		return false;
	}
}
