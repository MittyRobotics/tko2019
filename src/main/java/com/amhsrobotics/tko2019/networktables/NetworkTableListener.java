package com.amhsrobotics.tko2019.networktables;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;

public interface NetworkTableListener extends TableEntryListener {
	@Override
	default void valueChanged(NetworkTable table, String key, NetworkTableEntry entry, NetworkTableValue value, int flags) {
		handleEvent();
	}

	void handleEvent();
}
