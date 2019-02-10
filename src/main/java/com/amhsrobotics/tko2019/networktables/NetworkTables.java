package com.amhsrobotics.tko2019.networktables;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NetworkTables {
	private static NetworkTables ourInstance = new NetworkTables();
	private final NetworkTable primaryTable;

	private NetworkTables() {
		final NetworkTableInstance networkTableInstance = NetworkTableInstance.getDefault();
		primaryTable = networkTableInstance.getTable("/SmartDashboard");
	}

	public static NetworkTables getInstance() {
		return ourInstance;
	}

	public void registerToTableChange(final NetworkTableKey key, final NetworkTableListener listener, final NetworkTableFlags... flags) {
		int flagsValue = 0;
		for (final NetworkTableFlags flag : flags) {
			flagsValue += flag.getValue();
		}
		primaryTable.addEntryListener(key.toString(), listener, flagsValue);
	}
}