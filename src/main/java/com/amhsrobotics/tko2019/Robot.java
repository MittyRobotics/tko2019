package com.amhsrobotics.tko2019;

import com.amhsrobotics.tko2019.controls.ControlBindings;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.hardware.Compressor;
import com.amhsrobotics.tko2019.hardware.Gyro;
import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.hardware.subsystems.Cargo;
import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.hardware.subsystems.HatchPanel;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SampleRobot;

@SuppressWarnings("deprecation")
public final class Robot extends SampleRobot {
	public static void main(final String... args) {
		RobotBase.startRobot(Robot::new);
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Override
	protected final void robotInit() {
		// Init Cameras
		UsbCamera c = CameraServer.getInstance().startAutomaticCapture(0);
		c.setBrightness(50);
		c.setExposureAuto();
		c.setWhiteBalanceAuto();
		c.setFPS(15);
		UsbCamera m = CameraServer.getInstance().startAutomaticCapture(1);
		m.setBrightness(50);
		m.setExposureAuto();
		m.setWhiteBalanceAuto();
		m.setFPS(15);


		// Init Hardware
		Gyro.getInstance();
		Switches.getInstance();
		Compressor.getInstance();

		Drive.getInstance();
		HatchPanel.getInstance();
		Cargo.getInstance();


		// Init Controls
		ControlBindings.setupControls();
	}

	@Override
	public final void autonomous() {
		enabled();
	}

	@Override
	public final void operatorControl() {
		enabled();
	}

	@Override
	public final void test() { // Zero Encoders
		Compressor.getInstance().start();
		HatchPanel.getInstance().zeroEncoder();
		Cargo.getInstance().zeroEncoder();

	}

	@Override
	protected final void disabled() {
		Controls.getInstance().disable();
		Compressor.getInstance().stop();
	}

	private void enabled() { // Reversed Order of disabled
		Compressor.getInstance().start();
		Controls.getInstance().enable();
	}
}
