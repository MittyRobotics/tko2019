package com.amhsrobotics.tko2019.hardware;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public final class Gyro extends ADXRS450_Gyro {
	private static final Gyro INSTANCE = new Gyro();

	private Gyro() {
		super();
	}

	public static Gyro getInstance() {
		return INSTANCE;
	}
}
