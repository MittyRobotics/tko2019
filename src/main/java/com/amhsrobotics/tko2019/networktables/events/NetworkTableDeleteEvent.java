package com.amhsrobotics.tko2019.networktables.events;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableValue;

public class NetworkTableDeleteEvent extends NetworkTableEvent {
	public NetworkTableDeleteEvent(NetworkTable table, String key, NetworkTableEntry entry, NetworkTableValue value) {
		super(table, key, entry, value);
	}
}
