package com.amhsrobotics.tko2019;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.SampleRobot;

public class Robot extends SampleRobot {
	private final Compressor compressor;

	Robot() {
		compressor = new Compressor();
	}

	@Override
	protected void robotInit() {
		CameraServer.getInstance().startAutomaticCapture(0);
		CameraServer.getInstance().startAutomaticCapture(1);
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
