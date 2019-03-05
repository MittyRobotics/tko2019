package com.amhsrobotics.tko2019.hardware;

public final class Compressor extends edu.wpi.first.wpilibj.Compressor {
	private static final Compressor INSTANCE = new Compressor();

	private Compressor() {
		super();
	}

	public static Compressor getInstance() {
		return INSTANCE;
	}
}
