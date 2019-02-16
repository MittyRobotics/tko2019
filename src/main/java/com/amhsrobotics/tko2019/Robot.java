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
	private final Object lock = new Object();

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
		entering("robotInit");


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


		exiting("robotInit");
	}

	@Override
	public final void autonomous() {
		entering("autonomous");
		enabled();
		exiting("autonomous");
	}

	@Override
	public final void operatorControl() {
		entering("operatorControl");
		enabled();
		exiting("operatorControl");
	}

	@Override
	public final void test() {
		entering("test");

		exiting("test");
	}

	@Override
	protected final void disabled() {
		entering("disabled");
		synchronized (lock) {
			Controls.getInstance().disable();
			for (final Subsystem subsystem : subsystems) {
				subsystem.disable();
			}
			Compressor.getInstance().stop();
		}
		exiting("disabled");
	}

	private void enabled() {
		entering("enabled");
		synchronized (lock) {
			Controls.getInstance().enable();
			for (final Subsystem subsystem : subsystems) {
				subsystem.enable();
			}
			Compressor.getInstance().start();
		}
		exiting("disabled");
	}
}