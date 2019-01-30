package com.amhsrobotics.tko2019.networktables;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
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

}
