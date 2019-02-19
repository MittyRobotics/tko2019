package com.amhsrobotics.tko2019;

import com.amhsrobotics.tko2019.cargo.Cargo;
import com.amhsrobotics.tko2019.climber.Climber;
import com.amhsrobotics.tko2019.controls.AnalogInput;
import com.amhsrobotics.tko2019.controls.AnalogType;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.DigitalInput;
import com.amhsrobotics.tko2019.controls.DigitalType;
import com.amhsrobotics.tko2019.drive.Drive;
import com.amhsrobotics.tko2019.hatchpanel.HatchPanel;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.*;

@SuppressWarnings("deprecation")
public final class Robot extends SampleRobot {
	private final Compressor compressor = new Compressor();
//	public static Joystick joystick = new Joystick(1);

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
		drive.run();
//		Controls.getInstance().registerDigitalCommand(0, DigitalInput.XboxB, DigitalType.DigitalPress, ()->{
//			climber.climber.set(DoubleSolenoid.Value.kForward);
//		});
//		Controls.getInstance().registerDigitalCommand(0, DigitalInput.XboxA, DigitalType.DigitalPress, ()->{
//			climber.climber.set(DoubleSolenoid.Value.kReverse);
//		});
		Controls.getInstance().registerAnalogCommand(1, AnalogInput.JoystickX, AnalogType.OutOfThresholdMinor, value -> {
			hatchPanel.slideTalon.set(ControlMode.PercentOutput, value);
		});
		Controls.getInstance().registerAnalogCommand(1, AnalogInput.JoystickX, AnalogType.InThresholdMinor, value -> {
			hatchPanel.slideTalon.set(ControlMode.PercentOutput, 0);
		});
		Controls.getInstance().registerDigitalCommand(1, DigitalInput.Joystick3, DigitalType.DigitalPress, ()->{
			hatchPanel.openHatch();
		});
		Controls.getInstance().registerDigitalCommand(1, DigitalInput.Joystick2, DigitalType.DigitalPress, ()->{
			hatchPanel.closeHatch();
		});
		Controls.getInstance().registerDigitalCommand(1, DigitalInput.Joystick4, DigitalType.DigitalPress, ()->{
			hatchPanel.goHatchBackward();
		});
		Controls.getInstance().registerDigitalCommand(1, DigitalInput.Joystick5, DigitalType.DigitalPress, ()->{
			hatchPanel.goHatchForward();
		});
//		hatchPanel.run();
//		Controls.getInstance().registerAnalogCommand(1, AnalogInput.JoystickY, AnalogType.OutOfThresholdMinor, value -> {
//			if(Math.abs(value) > 0.1){
////				hp.slideTalon.config_kP(0, 0.4, 0);
//				hp.slide(hp.slideTalon.getSelectedSensorPosition() / hp.ticksPerInch + value);		//50% speed
//				System.out.println("Here");
//			}
//			else {
//				hp.slide(hp.slideTalon.getSelectedSensorPosition() / hp.ticksPerInch);
//			}
//			else {
//				hp.slideTalon.set(ControlMode.PercentOutput, 0);
//			}
//		});
//		Controls.getInstance().registerDigitalCommand(1, DigitalInput.Joystick3, DigitalType.DigitalPress, ()-> {
////			hp.slideTalon.config_kP(0, 0.2, 0); // value: 0.2
//			hp.slide(8);
//		});
//		Controls.getInstance().registerDigitalCommand(1, DigitalInput.Joystick2, DigitalType.DigitalPress, ()->{
//			hp.slideTalon.setSelectedSensorPosition(0);
//		});
//		Controls.getInstance().registerAnalogCommand(1, AnalogInput.JoystickY, AnalogType.OutOfThresholdMinor, value ->{
//			if(!cargo.intakeSensor.get() && value > 0){
//				cargo.spinOuttake(0, 0);
//			}
//			else {
//				cargo.spinOuttake(value, -value);
//			}
//		});
//		Controls.getInstance().registerAnalogCommand(1, AnalogInput.JoystickY, AnalogType.InThresholdMinor, value -> {
//			cargo.spinOuttake(0, 0);
//		});
//		Controls.getInstance().registerAnalogCommand(2, AnalogInput.JoystickY, AnalogType.OutOfThresholdMinor, value -> {
//			cargo.conveyorTalons[0].set(ControlMode.PercentOutput, value * 0.25);
//		});
//		Controls.getInstance().registerAnalogCommand(2, AnalogInput.JoystickY, AnalogType.InThresholdMinor, value -> {
//			cargo.conveyorTalons[0].set(ControlMode.PercentOutput, 0);
//		});
	}

	@Override
	public final void operatorControl() {
		Controls.getInstance().enable();
		compressor.start();
		compressor.setClosedLoopControl(true);
		while (isEnabled()){
			if(hatchPanel.slideTalon.getSensorCollection().isFwdLimitSwitchClosed()){
				System.out.println("FWD");
			}
			if(hatchPanel.slideTalon.getSensorCollection().isRevLimitSwitchClosed()){
				System.out.println("REV");
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//		disabled();
//		System.out.println("hp.run");
//			System.out.println(hp.slideTalon.getClosedLoopTarget() + " " + hp.slideTalon.getSelectedSensorPosition());
//			try {
//				Thread.sleep(20);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//
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
		Controls.getInstance().disable();
		compressor.stop();
	}

	private void enabled() {

	}
}
