package com.amhsrobotics.tko2019.networktables;

public enum NetworkTableKey {
	;

	private final String key;

	NetworkTableKey(final String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return key;
	}
}
