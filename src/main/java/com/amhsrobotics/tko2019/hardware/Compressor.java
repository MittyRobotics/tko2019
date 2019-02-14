package com.amhsrobotics.tko2019.hardware;

public class Compressor extends edu.wpi.first.wpilibj.Compressor {
	private static Compressor ourInstance = new Compressor();

	private Compressor() {
		super();
	}

	public static Compressor getInstance() {
		return ourInstance;
	}
}
