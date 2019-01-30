package com.amhsrobotics.tko2019.networktables.events;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableValue;

public class NetworkTableEvent {
	private final NetworkTable table;
	private final String key;
	private final NetworkTableEntry entry;
	private final NetworkTableValue value;

	public NetworkTableEvent(final NetworkTable table, final String key, final NetworkTableEntry entry, final NetworkTableValue value) {
		this.table = table;
		this.key = key;
		this.entry = entry;
		this.value = value;
	}
}
