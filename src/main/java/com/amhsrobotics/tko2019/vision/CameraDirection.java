package com.amhsrobotics.tko2019.vision;

public enum CameraDirection { // TODO Replace -1 to Correct Value
	Hatch("-1"),
	Cargo("-1");

	private final String cameraName;

	CameraDirection(final String cameraName) {
		this.cameraName = cameraName;
	}

	@Override
	public final String toString() {
		return cameraName;
	}
}
