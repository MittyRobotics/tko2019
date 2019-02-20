package com.amhsrobotics.tko2019.hardware;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class Gyro extends ADXRS450_Gyro {
	private static Gyro ourInstance = new Gyro();

	private Gyro() {
		super();
	}

	public static Gyro getInstance() {
		return ourInstance;
	}
}
