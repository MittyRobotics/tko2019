package com.amhsrobotics.tko2019.vision.sequences.actions.hatch;

import com.amhsrobotics.tko2019.hardware.subsystems.HatchPanel;
import com.amhsrobotics.tko2019.vision.sequences.actions.Action;

public final class MoveHatchCenter implements Action {
	@Override
	public final void doAction(boolean continuing) {
		HatchPanel.getInstance().slideMiddle();
	}
}
