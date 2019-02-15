package com.amhsrobotics.tko2019;

import com.amhsrobotics.tko2019.controls.AnalogInput;
import com.amhsrobotics.tko2019.controls.AnalogType;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.DigitalInput;
import com.amhsrobotics.tko2019.controls.DigitalType;
import com.amhsrobotics.tko2019.hatchpanel.HatchPanel;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.*;

@SuppressWarnings("deprecation")
public final class Robot extends SampleRobot {
//	private final Compressor compressor = new Compressor();
//	public static Joystick joystick = new Joystick(1);

	public static void main(final String... args) {
		RobotBase.startRobot(Robot::new);
	}

	HatchPanel hp = new HatchPanel();


	@Override
	protected final void robotInit() {
		hp.init();
//		Controls.getInstance().registerDigitalCommand(1, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick3, DigitalType.DigitalPress, () -> {
//			System.out.println("Button 3");
//			hp.goHatchForward();
//		});
//
//		Controls.getInstance().registerDigitalCommand(1, com.amhsrobotics.tko2019.controls.DigitalInput.Joystick2, DigitalType.DigitalPress, () -> {
//			System.out.println("Button 2");
//			hp.goHatchBackward();
//		});
		Controls.getInstance().registerAnalogCommand(1, AnalogInput.JoystickY, AnalogType.OutOfThresholdMinor, value -> {
			if(Math.abs(value) > 0.1){
				hp.slideTalon.config_kP(0, 0.4, 0);
				hp.slide(hp.slideTalon.getSelectedSensorPosition() / hp.ticksPerInch + value);		//50% speed
				System.out.println("Here");
			}
//			else {
//				hp.slideTalon.set(ControlMode.PercentOutput, 0);
//			}
		});
		Controls.getInstance().registerDigitalCommand(1, DigitalInput.Joystick3, DigitalType.DigitalPress, ()-> {
			hp.slideTalon.config_kP(0, 0.2, 1);
			hp.slide(8);
		});
		Controls.getInstance().registerDigitalCommand(1, DigitalInput.Joystick2, DigitalType.DigitalPress, ()->{
			hp.slideTalon.setSelectedSensorPosition(0);
		});
	}

	@Override
	public final void operatorControl() {
		Controls.getInstance().enable();
		while (isEnabled()){
			if(hp.slideTalon.getSensorCollection().isRevLimitSwitchClosed()){
//				//System.out.println("Here");
				hp.slideTalon.setSelectedSensorPosition(0);
			}
			System.out.println(hp.slideTalon.getClosedLoopTarget() + " " + hp.slideTalon.getSelectedSensorPosition());
//////			try {
//////				Thread.sleep(20);
//////			} catch (InterruptedException e) {
//////				e.printStackTrace();
//////			}
		}
	}

	@Override
	public final void autonomous() {
		enabled();
	}

	@Override
	public final void test() {
		while(isEnabled()) {

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

		}
	}

	@Override
	protected final void disabled() {
//		compressor.stop();
	}

	private void enabled() {
//		compressor.start();
//		compressor.setClosedLoopControl(true);
	}
}
