package com.amhsrobotics.tko2019;

import com.amhsrobotics.tko2019.controls.AnalogInput;
import com.amhsrobotics.tko2019.controls.AnalogType;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.hatchpanel.HatchPanel;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SampleRobot;

public class Robot extends SampleRobot {
	HatchPanel hatchPanel = new HatchPanel();
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
		hatchPanel.run();
	}

	@Override
	public void operatorControl() {
		Controls.getInstance().enable();
	}

	@Override
	public void autonomous() {

	}

	@Override
	public void test() {
		Controls.getInstance().registerAnalogCommand(1, AnalogInput.JoystickY, AnalogType.OutOfThreshold, value -> {

		});
	}
}