package com.amhsrobotics.tko2019.subsystems.hatchpanel;

import com.amhsrobotics.tko2019.controls.ControllerID;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.DigitalType;
import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.settings.ControlsConfig;
import com.amhsrobotics.tko2019.settings.subsystems.PID;
import com.amhsrobotics.tko2019.settings.subsystems.SolenoidIds;
import com.amhsrobotics.tko2019.settings.subsystems.TalonIds;
import com.amhsrobotics.tko2019.settings.subsystems.TicksPerInch;
import com.amhsrobotics.tko2019.subsystems.Subsystem;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class HatchPanel implements Subsystem {
	private boolean manual = false;
	private boolean processDone = false;
	private DoubleSolenoid grabber;
	private DoubleSolenoid pushForward;
	private WPI_TalonSRX slideTalon;

	@Override
	public void init() {


		grabber = new DoubleSolenoid(SolenoidIds.GRABBER[0], SolenoidIds.GRABBER[1]);
		pushForward = new DoubleSolenoid(SolenoidIds.PUSH_FORWARD[0], SolenoidIds.PUSH_FORWARD[1]);
		slideTalon = new WPI_TalonSRX(TalonIds.SLIDE_TALON);
		slideTalon.configClosedloopRamp(0.0);
		slideTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 1000);

		//slideTalon.configurePID()

		slideTalon.config_kP(0, PID.SLIDER[0], 0);
		slideTalon.config_kI(0, PID.SLIDER[1], 0);
		slideTalon.config_kD(0, PID.SLIDER[2], 0);
	}

	@Override
	public void initControls() {
		Controls.getInstance().registerDigitalCommand(ControllerID.Joystick1.getId(), ControlsConfig.SWITCH_MODE, DigitalType.DigitalPress, () -> {
			manual = !manual;
			if (manual) {
				System.out.println("Manual Mode");
			} else {
				System.out.println("Automatic Mode");
			}
		});
		Controls.getInstance().registerDigitalCommand(ControllerID.Joystick1.getId(), ControlsConfig.SLIDE_LEFT, DigitalType.DigitalRelease, ()->{
			if ((!Switches.getInstance().wallSwitch.get() && !Switches.getInstance().hatchSwitch.get()) && (processDone)) {
				slideMiddle();
				processDone = false;
			}
		});
		Controls.getInstance().registerDigitalCommand(ControllerID.Joystick1.getId(), ControlsConfig.SLIDE_RIGHT, DigitalType.DigitalRelease, ()->{
			if ((!Switches.getInstance().wallSwitch.get() && !Switches.getInstance().hatchSwitch.get()) && (processDone)) {
				slideMiddle();
				processDone = false;
			}
		});
		Controls.getInstance().registerDigitalCommand(ControllerID.Joystick1.getId(), ControlsConfig.SLIDE_LEFT, DigitalType.DigitalPress, () -> {
			if (!manual) {
				slideLeft();
			} else {
				manualSlideLeft();
			}
		});

		Controls.getInstance().registerDigitalCommand(ControllerID.Joystick1.getId(), ControlsConfig.SLIDE_RIGHT, DigitalType.DigitalPress, () -> {
			if (!manual) {
				slideRight();
			} else {
				manualSlideRight();
			}
		});

		Controls.getInstance().registerDigitalCommand(ControllerID.Joystick1.getId(), com.amhsrobotics.tko2019.controls.DigitalInput.Joystick3, DigitalType.DigitalPress, () -> {
			if (Switches.getInstance().hatchSwitch.get() && Switches.getInstance().wallSwitch.get()) {
				if (!manual) {
					processDone = true;
					outtake();
				} else {
					closeHatch();
				}
			}
		});

		Controls.getInstance().registerDigitalCommand(ControllerID.Joystick1.getId(), com.amhsrobotics.tko2019.controls.DigitalInput.JoystickTrigger, DigitalType.DigitalPress, () -> {
			if (!Switches.getInstance().hatchSwitch.get() && Switches.getInstance().wallSwitch.get()) {
				if (!manual) {
					processDone = true;
					intake();
				} else {
					openHatch();
				}
			}
		});
	}

	private void openHatch() {
		grabber.set(DoubleSolenoid.Value.kReverse);
	}

	private void closeHatch() {
		grabber.set(DoubleSolenoid.Value.kForward);
	}

	private void goHatchForward() {
		pushForward.set(DoubleSolenoid.Value.kForward);
	}

	private void goHatchBackward() {
		pushForward.set(DoubleSolenoid.Value.kReverse);
	}

	//outtake for the rocket

	//resets encoder when slider is to the left
	private void resetEncoder() {
		slideTalon.set(ControlMode.PercentOutput, 0.1);
		if (slideTalon.getSensorCollection().isFwdLimitSwitchClosed()) {
			slideTalon.setSelectedSensorPosition(0);
		}
	}


	//work on position numbers
	private void slideLeft() {
		slide(0);
	}

	private void manualSlideLeft(){
		slideTalon.set(ControlMode.PercentOutput, 0.5);
	}

	private void manualSlideRight(){
		slideTalon.set(ControlMode.PercentOutput, -0.5);
	}

	private void slideMiddle() {
		slide(8);
	}

	private void slideRight() {
		slide(16);
	}

	//take in the hatch panel *has safety measures*
	private void intake() {
		if (!Switches.getInstance().hatchSwitch.get() && Switches.getInstance().wallSwitch.get()) {
			openHatch();
			if (!Switches.getInstance().hatchSwitch.get()) {
				closeHatch();
			}
		}
	}
	private void outtake(){
		if(Switches.getInstance().hatchSwitch.get() && Switches.getInstance().wallSwitch.get()){
			goHatchForward();
			closeHatch();
			goHatchBackward();
			if(Switches.getInstance().hatchSwitch.get()){
				openHatch();
			}
		}
	}

	//how far the mechanism has to slide
	private void slide(double position) { //position in inches
		slideTalon.set(ControlMode.Position, (position * TicksPerInch.SLIDER_TPI));
		System.out.println("end error =" + slideTalon.getClosedLoopError());
		System.out.println(slideTalon.getSelectedSensorPosition());
	}
}