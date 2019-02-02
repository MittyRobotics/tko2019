package com.amhsrobotics.tko2019;

import com.amhsrobotics.tko2019.controls.AnalogInput;
import com.amhsrobotics.tko2019.controls.AnalogType;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.drive.Drive;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SampleRobot;

@SuppressWarnings("deprecation")
public final class Robot extends SampleRobot {
  public static void main(final String... args) {
		RobotBase.startRobot(Robot::new);
	}

	private final Drive drive = new Drive();
//
//  private final Compressor compressor = new Compressor();
	
	@Override
	protected final void robotInit() {
		drive.init();
		drive.run();
//    compressor.setClosedLoopControl(true);
	}

	@Override
	public final void operatorControl() {
		Controls.getInstance().enable();
//		enabled();
	}

	@Override
	public final void autonomous() {
//		enabled();
	}

	@Override
	public final void test() {

	}

//	@Override
//	protected final void disabled() {
//		compressor.stop();
//	}
//
//	private void enabled() {
//		compressor.start();
//	}
}
