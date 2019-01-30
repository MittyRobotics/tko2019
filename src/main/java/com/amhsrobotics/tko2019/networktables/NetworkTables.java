package com.amhsrobotics.tko2019.networktables;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.TableEntryListener;

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

	void registerToTableChange(NetworkTableKey key, TableEntryListener listener, NetworkTableFlags... flags) {
		int flagsValue = 0;
		for (NetworkTableFlags flag : flags) {
			flagsValue += flag.getValue();
		}
		primaryTable.addEntryListener(key.toString(), listener, flagsValue);
	}
}
