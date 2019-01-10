package com.amhsrobotics.tko2019;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SampleRobot;

public class Robot extends SampleRobot {
	public static void main(final String... args) {
		RobotBase.startRobot(Robot::new);
	}

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
