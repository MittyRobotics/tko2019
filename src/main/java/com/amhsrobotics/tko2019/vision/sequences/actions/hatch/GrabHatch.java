package com.amhsrobotics.tko2019.vision.sequences.actions.hatch;

import com.amhsrobotics.tko2019.hardware.subsystems.HatchPanel;
import com.amhsrobotics.tko2019.vision.sequences.actions.Action;

public final class GrabHatch implements Action {
	@Override
	public final void doAction(final boolean continuing) {
		HatchPanel.getInstance().grab();
	}
}
