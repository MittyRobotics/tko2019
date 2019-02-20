package com.amhsrobotics.tko2019;

import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.hardware.Compressor;
import com.amhsrobotics.tko2019.subsystems.Subsystem;
import com.amhsrobotics.tko2019.subsystems.cargo.Cargo;
import com.amhsrobotics.tko2019.subsystems.climber.Climber;
import com.amhsrobotics.tko2019.subsystems.drive.Drive;
import com.amhsrobotics.tko2019.subsystems.hatchpanel.HatchPanel;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SampleRobot;

@SuppressWarnings("deprecation")
public final class Robot extends SampleRobot {
	private volatile boolean disabled = true;

	private final Subsystem[] subsystems = {
			Drive.getInstance(),
			Climber.getInstance(),
			Cargo.getInstance(),
			HatchPanel.getInstance()
	};

	public static void main(final String... args) {
		RobotBase.startRobot(Robot::new);
	}

	@Override
	protected final void robotInit() {
		final Thread[] subsystemThreads = new Thread[subsystems.length];
		for (int subsystemNumber = 0; subsystemNumber < subsystems.length; subsystemNumber++) {
			final Thread subsystemThread = new Thread(subsystems[subsystemNumber]::init);
			subsystemThread.start();
			subsystemThreads[subsystemNumber] = subsystemThread;
		}

		for (final Subsystem subsystem : subsystems) {
			subsystem.initControls();
		}

		Compressor.getInstance().setClosedLoopControl(true);

		for (final Thread subsystemThread : subsystemThreads) {
			try {
				subsystemThread.join();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public final void autonomous() {
		enabled();
	}

	@Override
	public final void operatorControl() {
		enabled();
	}

	@Override
	public final void test() {

	}

	@Override
	protected final void disabled() {
		while (disabled) {
			Thread.onSpinWait();
		}
		Controls.getInstance().disable();
		for (final Subsystem subsystem : subsystems) {
			subsystem.disable();
		}
		Compressor.getInstance().stop();
		disabled = true;
	}

	private void enabled() {
		while (!disabled) {
			Thread.onSpinWait();
		}
		Controls.getInstance().enable();
		for (final Subsystem subsystem : subsystems) {
			subsystem.enable();
		}
		Compressor.getInstance().start();
		disabled = false;
	}
}
