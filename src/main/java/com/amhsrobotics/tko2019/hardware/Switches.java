package com.amhsrobotics.tko2019.hardware;

public class Switches {
	private static Switches ourInstance = new Switches();

	public static Switches getInstance() {
		return ourInstance;
	}

	private Switches() {
	}
}
