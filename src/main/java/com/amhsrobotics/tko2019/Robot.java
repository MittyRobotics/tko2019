package com.amhsrobotics.tko2019;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SampleRobot;

@SuppressWarnings("deprecation")
public final class Robot extends SampleRobot {
	public static void main(final String... args) {
		RobotBase.startRobot(Robot::new);
	}

	private final Compressor compressor = new Compressor();
	
	@Override
	protected final void robotInit() {

	}

	@Override
	public final void operatorControl() {
		enabled();
	}

	@Override
	public final void autonomous() {
		enabled();
	}

	@Override
	public final void test() {
		CameraServer.getInstance().startAutomaticCapture(0);
		CameraServer.getInstance().startAutomaticCapture(1);
	}

	@Override
	protected final void disabled() {
		compressor.stop();
	}

	private void enabled() {
		compressor.start();
		compressor.setClosedLoopControl(true);
	}
}
