package com.amhsrobotics.tko2019.networktables;

public class NetworkTables {
	private static NetworkTables ourInstance = new NetworkTables();

	public static NetworkTables getInstance() {
		return ourInstance;
	}

	private NetworkTables() {
	}

}
