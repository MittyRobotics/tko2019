package com.amhsrobotics.tko2019;

import com.amhsrobotics.tko2019.hardware.Enableable;
import com.amhsrobotics.tko2019.hardware.subsystems.Cargo;
import com.amhsrobotics.tko2019.hardware.subsystems.Climber;
import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.hardware.subsystems.HatchPanel;
import edu.wpi.first.wpilibj.SampleRobot;

@SuppressWarnings("deprecation")
public final class Robot extends SampleRobot {
	public static void main(final String... args) {
		//RobotBase.startRobot(Robot::new);
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Override
	protected final void robotInit() {
		Drive.getInstance();
		Climber.getInstance();
		Cargo.getInstance();
		HatchPanel.getInstance();
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
		for (final Enableable enableable : Enableable.ENABLEABLES) {
			enableable.disable();
		}
	}

	private void enabled() {
		for (final Enableable enableable : Enableable.ENABLEABLES) {
			enableable.enable();
		}
	}
}
