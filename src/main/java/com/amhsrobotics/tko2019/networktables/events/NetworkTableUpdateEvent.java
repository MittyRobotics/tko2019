package com.amhsrobotics.tko2019.networktables.events;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableValue;

public class NetworkTableUpdateEvent extends NetworkTableEvent {
	public NetworkTableUpdateEvent(NetworkTable table, String key, NetworkTableEntry entry, NetworkTableValue value) {
		super(table, key, entry, value);
	}
}
