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
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SampleRobot;

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
		//drive
		drive.run();
		//Climber
		climber.run();
		cargo.conveyorTalons[0].setSensorPhase(true);
		cargo.conveyorTalons[0].config_kP(0, 0.1, 0);
		cargo.conveyorTalons[0].config_kI(0, 0, 0);
		cargo.conveyorTalons[0].config_kD(0, 0, 0);
		//Hatch Panel
		Controls.getInstance().registerAnalogCommand(1, AnalogInput.JoystickX, AnalogType.OutOfThresholdMinor, value -> {
			if (Math.abs(value) > 0.2) {
				hatchPanel.slide(hatchPanel.slideTalon.getSelectedSensorPosition() / hatchPanel.ticksPerInch - 0.5 *value);
//				hatchPanel.slideTalon.set(ControlMode.PercentOutput, value * 0.5);
			}
//			else {
//				hatchPanel.slideTalon.set(ControlModens[0].getSelectedSensorPosition());
			System.out.println("Setpoint: " + cargo.conveyorTalons[0].getClosedLoopTarget());
//			System.out.println("Voltage: " + cargo.conveyorTalons[0].PercentOutput, 0);
//			}
		});
//		Controls.getInstance().registerAnalogCommand(1, AnalogInput.JoystickX, AnalogType.InThresholdMinor, value -> {
//			hatchPanel.slideTalon.set(ControlMode.PercentOutput, 0);
//		});
		Controls.getInstance().registerDigitalCommand(1, DigitalInput.Joystick2, DigitalType.DigitalPress, ()->{
			hatchPanel.openHatch();
		});
		Controls.getInstance().registerDigitalCommand(1, DigitalInput.JoystickTrigger, DigitalType.DigitalPress, ()->{
			hatchPanel.closeHatch();
		});
		Controls.getInstance().registerAnalogCommand(1, AnalogInput.JoystickY, AnalogType.OutOfThresholdMinor, value ->{
//			System.out.println("Here1");
			if(value > 0.5){
//				System.out.println("Here2");
				hatchPanel.goHatchForward();
			}
			else if(value < -0.5){
//				System.out.println("here3");
				hatchPanel.goHatchBackward();
			}
		});
		Controls.getInstance().registerDigitalCommand(1, DigitalInput.Joystick8, DigitalType.DigitalPress, ()->{
			hatchPanel.resetEncoder();
		});
		Controls.getInstance().registerDigitalCommand(1, DigitalInput.Joystick3, DigitalType.DigitalPress, ()->{
			hatchPanel.slide(-8);
		});
		Controls.getInstance().registerDigitalCommand(1, DigitalInput.Joystick4, DigitalType.DigitalPress, ()->{
			hatchPanel.slide(-13);
		});
		Controls.getInstance().registerDigitalCommand(1, DigitalInput.Joystick5, DigitalType.DigitalPress, ()->{
			hatchPanel.slide(-3);
		});

		//Cargo
		Controls.getInstance().registerDigitalCommand(2, DigitalInput.JoystickTrigger, DigitalType.DigitalHold, () -> {
			if(!cargo.intakeSensor.get()){
				cargo.spinOuttake(0, 0);
			}
			else {
				cargo.spinOuttake(0.5, -0.5);
			}
		});
		Controls.getInstance().registerDigitalCommand(2, DigitalInput.JoystickTrigger, DigitalType.DigitalRelease, () ->{
			cargo.spinOuttake(0, 0);
		});
		Controls.getInstance().registerDigitalCommand(2, DigitalInput.Joystick2, DigitalType.DigitalHold, () -> {
			cargo.spinOuttake(-0.5, 0.5);
		});
		Controls.getInstance().registerDigitalCommand(2, DigitalInput.Joystick2, DigitalType.DigitalRelease, () ->{
			cargo.spinOuttake(0, 0);
		});

		Controls.getInstance().registerAnalogCommand(2, AnalogInput.JoystickY, AnalogType.OutOfThresholdMinor, value -> {
			cargo.conveyorTalons[0].set(ControlMode.Position, cargo.conveyorTalons[0].getSelectedSensorPosition() + value * 1000);

		});
//		Controls.getInstance().registerAnalogCommand(2, AnalogInput.JoystickY, AnalogType.InThresholdMinor, value -> {
//			cargo.conveyorTalons[0].set(ControlMode.PercentOutput, 0);
//		});
		Controls.getInstance().registerDigitalCommand(2, DigitalInput.Joystick8, DigitalType.DigitalPress, ()->{
			cargo.calibrateConveyor();
		});
		Controls.getInstance().registerDigitalCommand(2, DigitalInput.Joystick4, DigitalType.DigitalPress, ()->{
			cargo.conveyorTalons[0].set(ControlMode.Position, -26200);
		});
		Controls.getInstance().registerDigitalCommand(2, DigitalInput.Joystick5, DigitalType.DigitalPress, ()->{
			cargo.conveyorTalons[0].set(ControlMode.Position, -18800);
		});
		Controls.getInstance().registerDigitalCommand(2, DigitalInput.Joystick6, DigitalType.DigitalPress, ()->{
			cargo.conveyorTalons[0].set(ControlMode.Position, -200);
		});
	}

	@Override
	public final void operatorControl() {
		Controls.getInstance().enable();
		compressor.start();
		compressor.setClosedLoopControl(true);
//		hatchPanel.slideTalon.setSelectedSensorPosition(0);
		while (isEnabled()) {
			System.out.println(cargo.conveyorTalons[0].getSelectedSensorPosition());
			if(cargo.conveyorTalons[0].getSensorCollection().isFwdLimitSwitchClosed()){
				System.out.println("FWD");
			}
			if(cargo.conveyorTalons[0].getSensorCollection().isRevLimitSwitchClosed()){
				System.out.println("REV");
			}
//		disabled();
//		System.out.println("hp.run");
//			System.out.println(hp.slideTalon.getClosedLoopTarget() + " " + hp.slideTalon.getSelectedSensorPosition());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//
	}

	@Override
	public final void autonomous() {
//		enabled();
//		cargo.conveyorTalons[0].setSensorPhase(true);
		cargo.conveyorTalons[0].config_kP(0, 0.2, 0);
		cargo.conveyorTalons[0].config_kI(0, 0, 0);
		cargo.conveyorTalons[0].config_kD(0, 0, 0);
		while (isEnabled()){
			cargo.conveyorTalons[0].set(ControlMode.Position, -20000);
			System.out.println("Position: " + cargo.conveyorTalons[0].getMotorOutputPercent());
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//			cargo.conveyorTalons[0].set(ControlMode.PercentOutput, 0);
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

	}
}