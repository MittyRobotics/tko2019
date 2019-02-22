package com.amhsrobotics.tko2019.hardware.subsystems;

import com.amhsrobotics.tko2019.controls.Controller;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.commands.AnalogType;
import com.amhsrobotics.tko2019.controls.commands.DigitalType;
import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.settings.ControlsConfig;
import com.amhsrobotics.tko2019.settings.subsystems.EncoderInversions;
import com.amhsrobotics.tko2019.settings.subsystems.PID;
import com.amhsrobotics.tko2019.settings.subsystems.SolenoidIds;
import com.amhsrobotics.tko2019.settings.subsystems.TalonIds;
import com.amhsrobotics.tko2019.settings.subsystems.TalonInversions;
import com.amhsrobotics.tko2019.settings.subsystems.TicksPerInch;
import com.amhsrobotics.tko2019.settings.subsystems.hatchpanel.SliderPositions;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class HatchPanel {
	private final static HatchPanel INSTANCE = new HatchPanel();

	private final DoubleSolenoid grabber;
	private final DoubleSolenoid pushForward;
	private final WPI_TalonSRX slideTalon;
	private boolean encoderConfig = false;

	private HatchPanel() {
		slideTalon = new WPI_TalonSRX(TalonIds.SLIDE);
		slideTalon.configFactoryDefault();
		slideTalon.setInverted(TalonInversions.SLIDER);
		slideTalon.setSensorPhase(EncoderInversions.SLIDER_ENCODER);
		slideTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
		slideTalon.config_kP(0, PID.SLIDER[0]);
		slideTalon.config_kI(0, PID.SLIDER[1]);
		slideTalon.config_kD(0, PID.SLIDER[2]);

		grabber = new DoubleSolenoid(SolenoidIds.GRABBER[0], SolenoidIds.GRABBER[1]);
		pushForward = new DoubleSolenoid(SolenoidIds.PUSH_FORWARD[0], SolenoidIds.PUSH_FORWARD[1]);

		Controls.getInstance().registerAnalogCommand(Controller.Joystick1, ControlsConfig.JOYTICK_SLIDE, AnalogType.Always, value -> {
			if (Math.abs(value) > 0.2) {
				slide(slideTalon.getSelectedSensorPosition() / TicksPerInch.SLIDER - 0.5 * value);
			}
		}).registerDigitalCommand(Controller.Joystick1, ControlsConfig.RELEASE_HATCH, DigitalType.DigitalPress, this::outtake).registerDigitalCommand(Controller.Joystick1, ControlsConfig.GRAB_HATCH, DigitalType.DigitalPress, this::intake)
				.registerAnalogCommand(Controller.Joystick1, ControlsConfig.PUSH_HATCH_MECHANISM, AnalogType.OutOfThresholdMajor, value -> {
					if (value > 0.5) {
						goHatchForward();
					} else if (value < -0.5) {
						goHatchBackward();
					}
				}).registerDigitalCommand(Controller.Joystick1, ControlsConfig.SLIDE_MIDDLE, DigitalType.DigitalPress, () -> {
			if (encoderConfig) {
				slideMiddle();
			}
		}).registerDigitalCommand(Controller.Joystick1, ControlsConfig.SLIDE_LEFT, DigitalType.DigitalPress, () -> {
			if (encoderConfig) {
				slideLeft();
			}
		}).registerDigitalCommand(Controller.Joystick1, ControlsConfig.SLIDE_RIGHT, DigitalType.DigitalPress, () -> {
			if (encoderConfig) {
				slideRight();
			}
		}).registerDigitalCommand(Controller.Joystick1, ControlsConfig.CONFIG_ENCODER, DigitalType.DigitalPress, this::resetEncoder);
	}

	public static HatchPanel getInstance() {
		return INSTANCE;
	}

	private void openHatch() {
		grabber.set(DoubleSolenoid.Value.kForward);
	}

	private void closeHatch() {
		grabber.set(DoubleSolenoid.Value.kReverse);
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
		while (!slideTalon.getSensorCollection().isFwdLimitSwitchClosed()) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		slideTalon.set(ControlMode.PercentOutput, 0);
		slideTalon.setSelectedSensorPosition(0);
		encoderConfig = true;
	}


	//work on position numbers
	public void slideLeft() {
		slide(SliderPositions.SLIDE_LEFT);
	}

	public void slideMiddle() {
		slide(SliderPositions.SLIDE_MIDDLE);
	}

	public void slideRight() {
		slide(SliderPositions.SLIDE_RIGHT);
	}

	//take in the hatch panel *has safety measures*
	public void intake() {
		if (!Switches.getInstance().getHatchSwitch()) { //Switch is inverted
			openHatch();
		}
	}

	public void outtake() {
		closeHatch();
	}

	//how far the mechanism has to slide
	private void slide(double position) { //position in negative inches
		slideTalon.set(ControlMode.Position, (position * TicksPerInch.SLIDER));
	}
}