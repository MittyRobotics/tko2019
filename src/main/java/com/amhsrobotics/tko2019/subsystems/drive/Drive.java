package com.amhsrobotics.tko2019.subsystems.drive;

import com.amhsrobotics.tko2019.controls.AnalogType;
import com.amhsrobotics.tko2019.controls.ControllerID;
import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.controls.DigitalType;
import com.amhsrobotics.tko2019.settings.ControlsConfig;
import com.amhsrobotics.tko2019.settings.subsystems.SolenoidIds;
import com.amhsrobotics.tko2019.settings.subsystems.TalonIds;
import com.amhsrobotics.tko2019.settings.subsystems.TicksPerInch;
import com.amhsrobotics.tko2019.subsystems.Subsystem;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import java.util.logging.Logger;

public final class Drive implements Subsystem {
	private final WPI_TalonSRX[] lTalons = new WPI_TalonSRX[TalonIds.LEFT_DRIVE.length];
	private final WPI_TalonSRX[] rTalons = new WPI_TalonSRX[TalonIds.RIGHT_DRIVE.length];

	private DoubleSolenoid gearShifter;
	private int gear = 1;
	private long lastSwitch = 0;

	private boolean shouldReverse = false;

	@SuppressWarnings("Duplicates")
	@Override
	public void init() {
		entering("<init>");


		hardware("Initializing Left Talons");
 		for (int talonIdIndex = 0; talonIdIndex < TalonIds.LEFT_DRIVE.length; talonIdIndex++) {
			final WPI_TalonSRX talon = new WPI_TalonSRX(TalonIds.LEFT_DRIVE[talonIdIndex]);
			talon.configFactoryDefault();
			if (talonIdIndex == 0) {
				talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
			} else {
				talon.follow(lTalons[0]);
			}
			lTalons[talonIdIndex] = talon;
			hardwareInit(talon);
		}
		hardware("Initializing Right Talons");
		for (int talonIdIndex = 0; talonIdIndex < TalonIds.RIGHT_DRIVE.length; talonIdIndex++) {
			final WPI_TalonSRX talon = new WPI_TalonSRX(TalonIds.RIGHT_DRIVE[talonIdIndex]);
			talon.configFactoryDefault();
			if (talonIdIndex == 0) {
				talon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
			} else {
				talon.follow(rTalons[0]);
			}
			rTalons[talonIdIndex] = talon;
			hardwareInit(talon);
		}

		gearShifter = new DoubleSolenoid(SolenoidIds.DRIVE_SHIFTER[0], SolenoidIds.DRIVE_SHIFTER[1]);
		gearShifter.setName("Gear Shifting Solenoid");
		hardwareInit(gearShifter);


		exiting("<init>");
	}

	@Override
	public void initControls() {
		entering("initControls");

		Controls.getInstance().registerAnalogCommand(ControllerID.XboxController.getId(), ControlsConfig.LEFT_WHEELS, AnalogType.OutOfThresholdMinor, value -> {
			if (shouldReverse) {
				moveRight(-value);
			} else {
				moveLeft(value);
			}
		});
		Controls.getInstance().registerAnalogCommand(ControllerID.XboxController.getId(), ControlsConfig.RIGHT_WHEELS, AnalogType.OutOfThresholdMinor, value -> {
			if (shouldReverse) {
				moveLeft(-value);
			} else {
				moveRight(value);
			}
		});
        Controls.getInstance().registerDigitalCommand(ControllerID.XboxController.getId(), ControlsConfig.GEAR_SWITCH, DigitalType.DigitalPress, () -> {
        	if (System.currentTimeMillis() - lastSwitch > 1000) {
		        if (gear == 1) {
			        shiftGear(0);
		        } else {
			        shiftGear(1);
		        }
		        lastSwitch = System.currentTimeMillis();
	        } else {
        		getLogger().warning("Shifter is on Cooldown.");
	        }
        });
		Controls.getInstance().registerDigitalCommand(ControllerID.XboxController.getId(), ControlsConfig.REVERSE_DIRECTION, DigitalType.DigitalPress, () -> {
			toggleReverser(!shouldReverse);
		});

		exiting("initControls");
	}

	public void moveStraight(final double inches) {
		entering("moveStraight");


		final double setpoint = inches * TicksPerInch.DRIVE_TPI[gear];
		final double threshold = TicksPerInch.DRIVE_TPI[gear] * 0.25;

		lTalons[0].set(ControlMode.Position, lTalons[0].getSelectedSensorPosition() + setpoint);
		rTalons[0].set(ControlMode.Position, rTalons[0].getSelectedSensorPosition() + setpoint);
		while ((Math.abs(lTalons[0].getClosedLoopError()) > threshold) || (Math.abs(rTalons[0].getClosedLoopError()) > threshold)) {
			Logger.getLogger("drive").finer("(Left) T1:\t" + lTalons[0].getSelectedSensorPosition());
			Logger.getLogger("drive").finer("(Right) T1:\t" + rTalons[0].getSelectedSensorPosition());

			try {
				Thread.sleep(20);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

		lTalons[0].set(ControlMode.PercentOutput, 0);
		rTalons[0].set(ControlMode.PercentOutput, 0);


		exiting("moveStraight");
	}

	public void turn(final double degrees) {
		entering("turn");

		getLogger().severe("TURN NOT IMPLEMENTED!");   // TODO

		exiting("turn");
	}

	private synchronized void moveLeft(final double value) {
		entering("moveLeft");
		talonSet(lTalons[0], ControlMode.PercentOutput, value);
		lTalons[0].set(ControlMode.PercentOutput, value);
		exiting("moveLeft");
	}

	private synchronized void moveRight(final double value) {
		entering("moveRight");
		talonSet(rTalons[0], ControlMode.PercentOutput, value);
		rTalons[0].set(ControlMode.PercentOutput, value);
		exiting("moveRight");
	}

	private void toggleReverser(final boolean shouldReverse) {
		entering("toggleReverser");
		toggleSoftware("Reverse", shouldReverse);
		this.shouldReverse = shouldReverse;
		exiting("toggleReverser");
	}

	private synchronized void shiftGear(final int value) {
		entering("shiftGear");
		if (value == 0) {
			solenoidSet(gearShifter, DoubleSolenoid.Value.kReverse);
			gearShifter.set(DoubleSolenoid.Value.kReverse);
			gear = 0;
		} else if (value == 1) {
			solenoidSet(gearShifter, DoubleSolenoid.Value.kForward);
			gearShifter.set(DoubleSolenoid.Value.kForward);
			gear = 1;
		}
		exiting("shiftGear");
	}
}
