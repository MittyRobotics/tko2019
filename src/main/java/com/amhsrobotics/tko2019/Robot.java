package com.amhsrobotics.tko2019;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SampleRobot;

@SuppressWarnings("deprecation")
public class Robot extends SampleRobot {
	public static void main(final String... args) {
		RobotBase.startRobot(Robot::new);
	}

	private final Compressor compressor;

	private Robot() {
		compressor = new Compressor();
	}

	@Override
	protected void robotInit() {

	}

	@Override
	public void operatorControl() {
		enabled();
	}

	@Override
	public void autonomous() {
		enabled();
	}

	@Override
	public void test() {
		CameraServer.getInstance().startAutomaticCapture(0);
		CameraServer.getInstance().startAutomaticCapture(1);
	}

	@Override
	protected void disabled() {
		compressor.stop();
	}

	private void enabled() {
		compressor.start();
		compressor.setClosedLoopControl(true);
	}
}
