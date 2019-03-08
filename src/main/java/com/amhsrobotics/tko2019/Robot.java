package com.amhsrobotics.tko2019;

import com.amhsrobotics.tko2019.controls.ControlBindings;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.hardware.Compressor;
import com.amhsrobotics.tko2019.hardware.Gyro;
import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.hardware.subsystems.Cargo;
import com.amhsrobotics.tko2019.hardware.subsystems.Climber;
import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.hardware.subsystems.HatchPanel;
import com.amhsrobotics.tko2019.vision.VisionSync;
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
		//CameraServer.getInstance().startAutomaticCapture(0);

	//	CameraServer.getInstance().startAutomaticCapture(1);
		final UsbCamera[] CAMERAS = new UsbCamera[3]; //0 is vision
		CAMERAS[0] = CameraServer.getInstance().startAutomaticCapture("0", 0);
		CAMERAS[0].setResolution(640, 360);
		CAMERAS[1] = CameraServer.getInstance().startAutomaticCapture("1", 1);
		CAMERAS[1].setResolution(640, 360);
		CAMERAS[2] = CameraServer.getInstance().startAutomaticCapture("2", 2);

		CAMERAS[0].setBrightness(0);
		CAMERAS[0].setExposureManual(0);
		CAMERAS[2].setBrightness(50);
		CAMERAS[2].setExposureManual(50);
		CAMERAS[2].setResolution(640, 360);


		// Init Hardware
//		Gyro.getInstance();
		Switches.getInstance();

		Compressor.getInstance();

		Drive.getInstance();
		HatchPanel.getInstance();
		Cargo.getInstance();
//		Climber.getInstance();


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
//		VisionSync.SEQUENCES_MANAGER.continueSequence();
	}

	@Override
	public final void test() { // Zero Encoders
		HatchPanel.getInstance().zeroEncoder();
//		Cargo.getInstance().zeroEncoder();
//		while (isEnabled()){
//			if(Switches.getInstance().hasHatch()){
//				System.out.println("HERE");
//			}
//		}
//		while (isEnabled()){
//			if(
//					Cargo.getInstance().conveyorTalons[0].getSensorCollection().isFwdLimitSwitchClosed()
//					||
//					Cargo.getInstance().conveyorTalons[0].getSensorCollection().isRevLimitSwitchClosed()
//			){
//				System.out.println("HERE");
//			}
//			else {
//				System.out.println("NOT");
//			}
//			try {
//				Thread.sleep(20);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		Drive.getInstance().setLeft(0.5);
//		Drive.getInstance().setRight(0.5);
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
