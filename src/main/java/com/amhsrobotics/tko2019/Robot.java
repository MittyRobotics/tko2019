package com.amhsrobotics.tko2019;

import com.amhsrobotics.tko2019.controls.*;
import com.amhsrobotics.tko2019.hatchpanel.HatchPanel;
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

//	private final Compressor compressor = new Compressor();

	@Override
	protected void robotInit() {
//		CameraServer.getInstance().startAutomaticCapture(0);
//		CameraServer.getInstance().startAutomaticCapture(1);
//		compressor.start();
//		compressor.setClosedLoopControl(true);

//		TalonSRXConfiguration t_config = new TalonSRXConfiguration();
//		t_config.closedloopRamp = 0.0;
//		t_config.openloopRamp = 0.0;
//		hatchPanel.slideTalon.configAllSettings(t_config);


		hatchPanel.init();
		Controls.getInstance().registerDigitalCommand(1, DigitalInput.Joystick4, DigitalType.DigitalPress, () -> {
			System.out.println("start error: " + hatchPanel.slideTalon.getClosedLoopError());
			hatchPanel.slide(16);
		});
		Controls.getInstance().registerAnalogCommand(1, AnalogInput.JoystickY, AnalogType.OutOfThreshold, value -> {
			hatchPanel.slideTalon.set(value / 2);
		});
		Controls.getInstance().registerDigitalCommand(1, DigitalInput.Joystick3, DigitalType.DigitalPress, () -> {
			hatchPanel.slideTalon.setSelectedSensorPosition(0);
		});
	}

	@Override
	public void operatorControl() {
//		Controls.getInstance().enable();
//		currentTime = 0;
//		lastMessage = 0;
//		flag = false;
		while (isEnabled()) {
//			currentTime = System.currentTimeMillis();
//			if ((currentTime-lastMessage) > 50) {
//				System.out.println("Position " + hatchPanel.slideTalon.getSelectedSensorPosition() + " Error " + hatchPanel.slideTalon.getClosedLoopError() + " Setpoint " + hatchPanel.slideTalon.getClosedLoopTarget() + " Speed " + hatchPanel.slideTalon.getMotorOutputPercent());
//				lastMessage = System.currentTimeMillis();
//			}
			System.out.println("switch: " + hatchPanel.sliderSwitches[0].get());
			Timer.delay(1);
// 	if (hatchPanel.sliderSwitches[0].get()) {
//				//if (!flag) {
////					hatchPanel.slideTalon.setSelectedSensorPosition(0);
////					System.out.println("reset");
					while (hatchPanel.sliderSwitches[0].get()) {
						System.out.println("pressed");
					}
////						hatchPanel.slideTalon.set(ControlMode.PercentOutput, 0.4);
////						System.out.println("in loop");
////					}
////					System.out.println("right out of loop");
////					hatchPanel.slideTalon.set(ControlMode.PercentOutput, 0);
////					Timer.delay(5);
////					System.out.println("Position " + hatchPanel.slideTalon.getSelectedSensorPosition() + " Error " + hatchPanel.slideTalon.getClosedLoopError() + " Setpoint " + hatchPanel.slideTalon.getClosedLoopTarget() + " Speed " + hatchPanel.slideTalon.getMotorOutputPercent());
//					System.out.println("switch " + hatchPanel.sliderSwitches[0].get() + " flag " + flag);
////					while (hatchPanel.sliderSwitches[0].get() && !flag) {
////						hatchPanel.slideTalon.set(ControlMode.PercentOutput, -0.15);
////						System.out.println("slow");
//					//	Timer.delay(0.55);
////						hatchPanel.slideTalon.setSelectedSensorPosition(0);
////						flag = true;
////						System.out.println(hatchPanel.slideTalon.getSelectedSensorPosition());
//					}
//					System.out.println("out of loop");
//					hatchPanel.slideTalon.setSelectedSensorPosition(0);
//					flag = true;
//				//} else {
////					hatchPanel.slideTalon.set(ControlMode.PercentOutput, 0);
////					hatchPanel.slideTalon.set(ControlMode.Position, 1.5* hatchPanel.ticksPerInch);
//				//}
//			}
//			if (hatchPanel.sliderSwitches[1].get()) {
//				hatchPanel.slideTalon.set(ControlMode.PercentOutput, 0);
//				Timer.delay(0.2);
//			}
////			try {
////				Thread.sleep(20);
////			} catch (InterruptedException e) {
////				e.printStackTrace();
////			}
//		}

		}
//
//	@Override
//	public void autonomous() {
//
//	}
//
//	@Override
//	public void test() {
//		Controls.getInstance().registerAnalogCommand(1, AnalogInput.JoystickY, AnalogType.OutOfThreshold, value -> {
//
//		});
//	}
	}
}