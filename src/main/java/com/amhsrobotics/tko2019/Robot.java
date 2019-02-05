package com.amhsrobotics.tko2019;

import com.amhsrobotics.tko2019.controls.*;
import com.amhsrobotics.tko2019.hatchpanel.HatchPanel;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot {
	//private static final Object AnalogInput = ;
	HatchPanel hatchPanel = new HatchPanel();
	long lastMessage, currentTime;
	boolean flag;

	public static void main(final String... args) {
		RobotBase.startRobot(Robot::new);
	}

	private final Compressor compressor = new Compressor();

	@Override
	protected void robotInit() {
		CameraServer.getInstance().startAutomaticCapture(0);
		CameraServer.getInstance().startAutomaticCapture(1);
		compressor.start();
		compressor.setClosedLoopControl(true);


		hatchPanel.init();
	}

	@Override
	public void operatorControl() {
		}

		@Override
		public void autonomous () {

		}

		@Override
		public void test () {
			Controls.getInstance().registerAnalogCommand(1, AnalogInput.JoystickY, AnalogType.OutOfThreshold, value -> {

			});
		}
}