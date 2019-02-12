package com.amhsrobotics.tko2019;

import com.amhsrobotics.tko2019.controls.AnalogInput;
import com.amhsrobotics.tko2019.controls.AnalogType;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.DigitalType;
import com.amhsrobotics.tko2019.drive.Drive;
import com.amhsrobotics.tko2019.hatchpanel.HatchPanel;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Compressor;

@SuppressWarnings("deprecation")
public final class Robot extends SampleRobot {
//	private final Compressor compressor = new Compressor();
//	public static Joystick joystick = new Joystick(1);
	Drive drive = new Drive();
	public static void main(final String... args) {
		RobotBase.startRobot(Robot::new);
	}

//	HatchPanel hp = new HatchPanel();


	@Override
	protected final void robotInit() {
//		hp.init();
		drive.init();
		drive.run();

	}

	@Override
	public final void operatorControl() {
		Controls.getInstance().enable();

	}

	@Override
	public final void autonomous() {
		enabled();
	}

	@Override
	public final void test() {
		while(isEnabled()) {
//			hp.goHatchForward();


			//Controls.getInstance().registerAnalogCommand(1, AnalogInput.JoystickY, AnalogType.OutOfThreshold, (value) -> {
			/////////////////////////////////////////////////////
//				hp.slideTalon.set(ControlMode.PercentOutput, joystick.getY());
//				System.out.println(joystick.getY());
			/////////////////////////////////////////////////////
			//if(value < -0.2) {

					//if (hp.sliderSwitches[1].get()) {
					//	System.out.println("1: Pressed");
					//	hp.slideTalon.set(ControlMode.PercentOutput, 0);
					//}
					//else {
//					System.out.println("back");
//						hp.slideTalon.set(ControlMode.PercentOutput, value);
					//}
				//}

				//else if (value > 0.2){

					//if (hp.sliderSwitches[0].get()) {
					//	System.out.println("0: Pressed");
					//	hp.slideTalon.set(ControlMode.PercentOutput, 0);
					//}
					//else {
//					System.out.println("forward");
//						hp.slideTalon.set(ControlMode.PercentOutput, value);
					//}
				//}
				//else {
					//hp.slideTalon.set(ControlMode.PercentOutput, 0);
				//}



				//hp.limitSwitchSafety();

			//});

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected final void disabled() {
//		compressor.stop();
	}

	private void enabled() {
	}
}