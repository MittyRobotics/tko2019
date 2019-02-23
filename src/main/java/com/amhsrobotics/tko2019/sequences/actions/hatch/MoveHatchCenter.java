package com.amhsrobotics.tko2019.sequences.actions.hatch;

import com.amhsrobotics.tko2019.hardware.subsystems.HatchPanel;
import com.amhsrobotics.tko2019.sequences.Action;

public final class MoveHatchCenter implements Action {
	@Override
	public final void doAction(boolean continuing) {
		HatchPanel.getInstance().slideMiddle();
	}
}
