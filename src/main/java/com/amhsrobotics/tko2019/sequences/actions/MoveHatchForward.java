package com.amhsrobotics.tko2019.sequences.actions;

import com.amhsrobotics.tko2019.hardware.subsystems.HatchPanel;
import com.amhsrobotics.tko2019.sequences.Action;

public class MoveHatchForward implements Action {
	@Override
	public void doAction(boolean continuing) {
		HatchPanel.getInstance().goHatchForward();
	}

	@Override
	public boolean isContinuable() {
		return true;
	}
}
