package com.amhsrobotics.tko2019;

import com.amhsrobotics.tko2019.cargo.Cargo;
import com.amhsrobotics.tko2019.climber.Climber;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.drive.Drive;
import com.amhsrobotics.tko2019.hatchpanel.HatchPanel;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SampleRobot;

@SuppressWarnings("deprecation")
public final class Robot extends SampleRobot {
	private final Compressor compressor = new Compressor();

	public static void main(final String... args) {
		RobotBase.startRobot(Robot::new);
	}

	Drive drive = new Drive();
	Cargo cargo = new Cargo();
	HatchPanel hatchPanel = new HatchPanel();
	Climber climber = new Climber();


	@Override
	protected final void robotInit() {
		climber.init();
		drive.init();
		cargo.init();
		hatchPanel.init();
		//drive
		drive.run();
		//Climber
		climber.run();
		//Hatch Panel
		hatchPanel.run();
		//Cargo
		cargo.run();
		CameraServer.getInstance().startAutomaticCapture(0);
		CameraServer.getInstance().startAutomaticCapture(1);
	}

	@Override
	public final void operatorControl() {
		enabled();
	}

	@Override
	public final void autonomous() {
		enabled();
	}

	@Override
	public final void test() {
		cargo.conveyorTalons[0].config_kP(0, 0.2, 0);
		cargo.conveyorTalons[0].config_kI(0, 0, 0);
		cargo.conveyorTalons[0].config_kD(0, 0, 0);
		while (isEnabled()){
			cargo.conveyorTalons[0].set(ControlMode.Position, -10000);
			System.out.println("Position: " + cargo.conveyorTalons[0].getMotorOutputPercent());
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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

	@Override
	protected final void disabled() {
		Controls.getInstance().disable();
		compressor.stop();
	}

	private void enabled() {
		Controls.getInstance().enable();
		compressor.start();
		compressor.setClosedLoopControl(true);
	}
}