package com.amhsrobotics.tko2019;

import com.amhsrobotics.tko2019.controls.Controller;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.commands.DigitalType;
import com.amhsrobotics.tko2019.controls.input.DigitalInput;
import com.amhsrobotics.tko2019.hardware.Compressor;
import com.amhsrobotics.tko2019.hardware.Enableable;
import com.amhsrobotics.tko2019.hardware.Gyro;
import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.sequences.VisionSync;
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
		CameraServer.getInstance().startAutomaticCapture(0);

//		Compressor.getInstance();
		//Switches.getInstance();
//		Gyro.getInstance();

		Drive.getInstance();

		//Climber.getInstance();
		//Cargo.getInstance();
		//HatchPanel.getInstance();

		
//		Controls.getInstance().registerDigitalCommand(Controller.XboxController, DigitalInput.XboxA, DigitalType.DigitalPress, () -> {
//			VisionSync.getInstance().request();
//		}).registerDigitalCommand(Controller.XboxController, DigitalInput.XboxB, DigitalType.DigitalPress, () -> {
//			VisionSync.getInstance().confirm();
//			Drive.getInstance().turn(180 +15);
//			Drive.getInstance().moveStraight(24);
//			Drive.getInstance().turn(-90);
//		});
	}

	@Override
	public final void autonomous() {
		enabled();
	}

	@Override
	public final void operatorControl() {
		Controls.getInstance().enable();
		enabled();
	}

	@Override
	public final void test() {

	}

	@Override
	protected final void disabled() {
		Controls.getInstance().disable();
		for (final Enableable enableable : Enableable.ENABLEABLES) {
			enableable.disable();
		}
	}

	private void enabled() {
		for (final Enableable enableable : Enableable.ENABLEABLES) {
			enableable.enable();
		}
	}
}
