package com.amhsrobotics.tko2019.hardware.subsystems;

import com.amhsrobotics.tko2019.controls.AnalogType;
import com.amhsrobotics.tko2019.controls.ControllerID;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.DigitalType;
import com.amhsrobotics.tko2019.hardware.Switches;
import com.amhsrobotics.tko2019.settings.ControlsConfig;
import com.amhsrobotics.tko2019.settings.subsystems.PID;
import com.amhsrobotics.tko2019.settings.subsystems.SolenoidIds;
import com.amhsrobotics.tko2019.settings.subsystems.TalonIds;
import com.amhsrobotics.tko2019.settings.subsystems.TalonInversions;
import com.amhsrobotics.tko2019.settings.subsystems.TicksPerInch;
import com.amhsrobotics.tko2019.settings.subsystems.hatchpanel.SliderPositions;
import com.amhsrobotics.tko2019.hardware.Subsystem;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class HatchPanel implements Subsystem {
	private final static HatchPanel INSTANCE = new HatchPanel();
	private boolean manual = false;
	private boolean processDone = false;
	private DoubleSolenoid grabber;
	private DoubleSolenoid pushForward;
	private WPI_TalonSRX slideTalon;
	private HatchPanel() {

	}

	public static HatchPanel getInstance() {
		return INSTANCE;
	}

	@Override
	public void init() {
		slideTalon = new WPI_TalonSRX(TalonIds.SLIDE);
		slideTalon.setNeutralMode(NeutralMode.Coast);
		slideTalon.configFactoryDefault();
		slideTalon.setInverted(TalonInversions.SLIDER);
		slideTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
		slideTalon.config_kP(0, PID.SLIDER[0]);
		slideTalon.config_kI(0, PID.SLIDER[1]);
		slideTalon.config_kD(0, PID.SLIDER[2]);

		grabber = new DoubleSolenoid(SolenoidIds.GRABBER[0], SolenoidIds.GRABBER[1]);
		pushForward = new DoubleSolenoid(SolenoidIds.PUSH_FORWARD[0], SolenoidIds.PUSH_FORWARD[1]);
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
		Controls.getInstance().registerDigitalCommand(ControllerID.Joystick1.getId(), ControlsConfig.SLIDE_LEFT, DigitalType.DigitalRelease, () -> {
			if (!manual) {
				if ((
//						!Switches.getInstance().wallSwitch.get() &&
						!Switches.getInstance().getHatchSwitch()) && (processDone)) {
					slideMiddle();
					processDone = false;
				}
			}
		});
		Controls.getInstance().registerDigitalCommand(ControllerID.Joystick1.getId(), ControlsConfig.SLIDE_RIGHT, DigitalType.DigitalRelease, () -> {
			if (!manual) {
				if ((
//						!Switches.getInstance().wallSwitch.get() &&
						!Switches.getInstance().getHatchSwitch()) && (processDone)) {
					slideMiddle();
					processDone = false;
				}
			}
		});
		Controls.getInstance().registerDigitalCommand(ControllerID.Joystick1.getId(), ControlsConfig.SLIDE_LEFT, DigitalType.DigitalPress, () -> {
			if (!manual) {
				slideLeft();
			}
		});
		Controls.getInstance().registerAnalogCommand(ControllerID.Joystick1.getId(), ControlsConfig.JOYTICK_SLIDE, AnalogType.OutOfThresholdMinor, value -> {
			if (!manual) {
				slide(slideTalon.getSelectedSensorPosition() / TicksPerInch.SLIDER + value);
			} else {
				manualSlide(value * 0.5);
			}
		});
		Controls.getInstance().registerAnalogCommand(ControllerID.Joystick1.getId(), ControlsConfig.JOYTICK_SLIDE, AnalogType.InThresholdMinor, value -> {
			if (manual) {
				manualSlide(0);
			}
		});
		Controls.getInstance().registerDigitalCommand(ControllerID.Joystick1.getId(), ControlsConfig.SLIDE_RIGHT, DigitalType.DigitalPress, () -> {
			if (!manual) {
				slideRight();
			}
		});

		Controls.getInstance().registerDigitalCommand(ControllerID.Joystick1.getId(), ControlsConfig.RELEASE_HATCH, DigitalType.DigitalPress, () -> {
			if (!manual) {
				if (!Switches.getInstance().getHatchSwitch()
//						&& Switches.getInstance().wallSwitch.get()
				) {
					processDone = true;
					outtake();
				}
			} else {
				closeHatch();
			}
		});
		Controls.getInstance().registerDigitalCommand(ControllerID.Joystick1.getId(), ControlsConfig.GRAB_HATCH, DigitalType.DigitalPress, () -> {
			if (!manual) {
				if (!Switches.getInstance().getHatchSwitch()
//						&& Switches.getInstance().wallSwitch.get()
				) {
					intake();
				}
			} else {
				openHatch();
			}
		});
		Controls.getInstance().registerAnalogCommand(ControllerID.Joystick1.getId(), ControlsConfig.PUSH_HATCH_MECHANISM, AnalogType.OutOfThresholdMajor, value -> {
			if (manual) {
				if (value > 0) {
					goHatchForward();
				} else {
					goHatchBackward();
				}
			}
		});
		Controls.getInstance().registerDigitalCommand(ControllerID.Joystick1.getId(), ControlsConfig.CONFIG_ENCODER, DigitalType.DigitalPress, this::resetEncoder);
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
		while (!slideTalon.getSensorCollection().isFwdLimitSwitchClosed()) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		slideTalon.set(ControlMode.PercentOutput, 0);
		slideTalon.setSelectedSensorPosition(0);
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

	private void manualSlide(double percent) {
		slideTalon.set(ControlMode.PercentOutput, percent);
	}

	//take in the hatch panel *has safety measures*
	public void intake() {
		if (!Switches.getInstance().getHatchSwitch() && Switches.getInstance().getWallSwitch()) {
			openHatch();
			if (!Switches.getInstance().getHatchSwitch()) {
				closeHatch();
			}
		}
	}

	public void outtake() {
		if (Switches.getInstance().getHatchSwitch() && Switches.getInstance().getWallSwitch()) {
			goHatchForward();
			closeHatch();
			goHatchBackward();
			if (Switches.getInstance().getHatchSwitch()) {
				openHatch();
			}
		}
	}

	//how far the mechanism has to slide
	private void slide(double position) { //position in inches
		slideTalon.set(ControlMode.Position, (position * TicksPerInch.SLIDER));
		System.out.println("end error =" + slideTalon.getClosedLoopError()); // FIXME: 2019-02-14
		System.out.println(slideTalon.getSelectedSensorPosition()); // FIXME: 2019-02-14
	}
}