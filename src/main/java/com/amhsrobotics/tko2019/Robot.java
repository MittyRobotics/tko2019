package com.amhsrobotics.tko2019;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.SampleRobot;

public class Robot extends SampleRobot {
	private final Compressor compressor;

	Robot() {
		compressor = new Compressor();
	}

	@Override
	protected void robotInit() {
		compressor.start();
		compressor.setClosedLoopControl(true);
	}

	@Override
	public void operatorControl() {
	}

	@Override
	public void autonomous() {

	}

	@Override
	public void test() {

	}
}
