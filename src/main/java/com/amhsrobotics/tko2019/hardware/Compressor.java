package com.amhsrobotics.tko2019.hardware;

public class Compressor extends edu.wpi.first.wpilibj.Compressor implements Enableable {
	private static Compressor ourInstance = new Compressor();

	private Compressor() {
		super();
		setClosedLoopControl(true);
		ENABLEABLES.add(this);
	}

	public static Compressor getInstance() {
		return ourInstance;
	}

	@Override
	public void enable() {
		start();
	}

	@Override
	public void disable() {
		stop();
	}
}
