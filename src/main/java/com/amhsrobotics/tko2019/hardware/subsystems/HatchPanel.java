package com.amhsrobotics.tko2019.hardware.subsystems;

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

public final class HatchPanel {
	private final static HatchPanel INSTANCE = new HatchPanel();

	private final WPI_TalonSRX slideTalon = new WPI_TalonSRX(TalonIds.SLIDE);

	private final DoubleSolenoid grabber = new DoubleSolenoid(SolenoidIds.GRABBER[0], SolenoidIds.GRABBER[1]);
	private final DoubleSolenoid pusher = new DoubleSolenoid(SolenoidIds.PUSH_FORWARD[0], SolenoidIds.PUSH_FORWARD[1]);

	private HatchPanel() {
		slideTalon.configFactoryDefault();
		slideTalon.setInverted(TalonInversions.SLIDER); // FIXME wtf is neg??
		slideTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder); // FIXME wtf is neg??
		slideTalon.setSensorPhase(EncoderInversions.SLIDER_ENCODER);
		slideTalon.config_kP(0, PID.SLIDER[0]);
		slideTalon.config_kI(0, PID.SLIDER[1]);
		slideTalon.config_kD(0, PID.SLIDER[2]);
	}

	public static HatchPanel getInstance() {
		return INSTANCE;
	}


	///////////////////////////////////////////////////////////////////////////
	// Hatch Panel
	///////////////////////////////////////////////////////////////////////////

	public final void grab() {
		grabber.set(DoubleSolenoid.Value.kReverse);
	}

	public final void release() {
		grabber.set(DoubleSolenoid.Value.kForward);
	}

	public final void forward() {
		pusher.set(DoubleSolenoid.Value.kForward);
	}

	public final void back() {
		pusher.set(DoubleSolenoid.Value.kReverse);
	}

	
	///////////////////////////////////////////////////////////////////////////
	// Slider
	///////////////////////////////////////////////////////////////////////////

	public final  void slideLeft() {
		slide(SliderPositions.SLIDE_LEFT);
	}

	public final  void slideMiddle() {
		slide(SliderPositions.SLIDE_MIDDLE);
	}

	public final  void slideRight() {
		slide(SliderPositions.SLIDE_RIGHT);
	}

	public final  void slide(double position) {
		slideTalon.set(ControlMode.Position, (position * TicksPerInch.SLIDER));
	}


	///////////////////////////////////////////////////////////////////////////
	// Reset Encoders
	///////////////////////////////////////////////////////////////////////////

	public final void zeroEncoder() {
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
}
