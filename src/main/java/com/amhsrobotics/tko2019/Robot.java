package com.amhsrobotics.tko2019;

import com.amhsrobotics.tko2019.controls.Controls;
import com.amhsrobotics.tko2019.hardware.Compressor;
import com.amhsrobotics.tko2019.logging.LogCapable;
import com.amhsrobotics.tko2019.subsystems.Subsystem;
import com.amhsrobotics.tko2019.subsystems.cargo.Cargo;
import com.amhsrobotics.tko2019.subsystems.climber.Climber;
import com.amhsrobotics.tko2019.subsystems.drive.Drive;
import com.amhsrobotics.tko2019.subsystems.hatchpanel.HatchPanel;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SampleRobot;

@SuppressWarnings("deprecation")
public final class Robot extends SampleRobot implements LogCapable {
	private final Subsystem[] subsystems = {
			new Drive(),
			new Climber(),
			new Cargo(),
			new HatchPanel()
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
				throwing("robotInit", e);
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
		Controls.getInstance().disable();
		Compressor.getInstance().stop();
	}

	private void enabled() {
		Controls.getInstance().enable();
		Compressor.getInstance().start();
	}
}