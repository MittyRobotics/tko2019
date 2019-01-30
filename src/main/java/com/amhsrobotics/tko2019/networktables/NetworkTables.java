package com.amhsrobotics.tko2019.networktables;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.TableEntryListener;

public class NetworkTables {
	private static NetworkTables ourInstance = new NetworkTables();

	public static NetworkTables getInstance() {
		return ourInstance;
	}

	private final NetworkTable primaryTable;

	private NetworkTables() {
		final NetworkTableInstance networkTableInstance = NetworkTableInstance.getDefault();
		primaryTable = networkTableInstance.getTable("/SmartDashboard");
	}

	void registerToTableChange(NetworkTableKey key, TableEntryListener listener, NetworkTableFlags... flags) {
		int flagsValue = 0;
		for (NetworkTableFlags flag: flags) {

		}
		primaryTable.addEntryListener(key.toString(), listener, flagsValue);
	}
}
