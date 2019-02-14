package com.amhsrobotics.tko2019.subsystems;

import com.amhsrobotics.tko2019.logging.LogCapable;

public interface Subsystem extends LogCapable {
	void init();

	void initControls();
}
