package com.amhsrobotics.tko2019;

import com.amhsrobotics.tko2019.controls.ControlBindings;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.hardware.Compressor;
import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.hardware.subsystems.Cargo;
import com.amhsrobotics.tko2019.hardware.subsystems.Climber;
import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.hardware.subsystems.HatchPanel;
import com.amhsrobotics.tko2019.vision.networking.Connection;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SampleRobot;

@SuppressWarnings("deprecation")
public final class Robot extends SampleRobot {
	public static void main(final String... args) {
		RobotBase.startRobot(Robot::new);
	}
//	Robot(){
//		Connection.getInstance();
//	}

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
//		Gyro.getInstance();
		Switches.getInstance();

		Compressor.getInstance();

		Drive.getInstance();
		HatchPanel.getInstance();
		Cargo.getInstance();
//		Connection.getInstance();
//		Climber.getInstance();


		// Init Controls
		ControlBindings.setupControls();
	}

	@Override
	public final void autonomous() {
//		Drive.getInstance().moveStraight(-36);
		//Cargo 6390
		enabled();
//		Cargo.getInstance().moveConveyor(15734);
//		while (isEnabled()) {
//////			System.out.println(Drive.getInstance().leftTalons[0].getSelectedSensorPosition());
//////			System.out.println(Drive.getInstance().leftTalons[0].getClosedLoopTarget());
//////			try {
//////				Thread.sleep(10);
//////			} catch (InterruptedException e) {
//////				e.printStackTrace();
//////			}
//////		}
	}

	@Override
	public final void operatorControl() {
//		Climber.getInstance().push();
		enabled();
//		while (isEnabled()){
//			System.out.println(Drive.getInstance().leftTalons[0].getSelectedSensorPosition());
//			System.out.println(Drive.getInstance().rightTalons[0].getSelectedSensorPosition());
////			System.out.println("Real: " + Drive.getInstance().leftTalons[0].getMotorOutputPercent());
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		VisionSync.SEQUENCES_MANAGER.continueSequence();
	}

	@Override
	public final void test() { // Zero Encoders
		Compressor.getInstance().start();
//		Cargo.getInstance().moveConveyor(15000);
		HatchPanel.getInstance().zeroEncoder();
		Cargo.getInstance().zeroEncoder();
		//Rockket 15730
//		while (isEnabled()){
//			System.out.println(Cargo.getInstance().conveyorTalons[0].getSelectedSensorPosition());
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		HatchPanel.getInstance().zeroEncoder();
//		Drive.getInstance().moveStraight(-12);
//		enabled();
//		while (isEnabled()){
//			System.out.println(Drive.getInstance().leftTalons[0].getSelectedSensorPosition());
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
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
