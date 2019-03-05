package com.amhsrobotics.tko2019;

import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.hardware.Compressor;
import com.amhsrobotics.tko2019.hardware.Gyro;
import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.hardware.subsystems.Cargo;
import com.amhsrobotics.tko2019.hardware.subsystems.Climber;
import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.hardware.subsystems.HatchPanel;
import com.amhsrobotics.tko2019.controls.ControlBindings;
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
		final UsbCamera[] CAMERAS = new UsbCamera[4];
		CAMERAS[0] = CameraServer.getInstance().startAutomaticCapture("0", 0);
		CAMERAS[1] = CameraServer.getInstance().startAutomaticCapture("1", 1);
		CAMERAS[2] = CameraServer.getInstance().startAutomaticCapture("2", 2);
		CAMERAS[3] = CameraServer.getInstance().startAutomaticCapture("3", 3);

		CAMERAS[2].setBrightness(-100);
		CAMERAS[2].setExposureManual(-100);
		CAMERAS[2].setResolution(640, 360);


		// Init Hardware
		Gyro.getInstance();
		Switches.getInstance();

		Compressor.getInstance();

		Drive.getInstance();
		HatchPanel.getInstance();
		Cargo.getInstance();
		Climber.getInstance();


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
