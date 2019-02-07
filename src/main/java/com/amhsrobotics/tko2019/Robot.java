package com.amhsrobotics.tko2019;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SampleRobot;

@SuppressWarnings("deprecation")
public final class Robot extends SampleRobot {
	public static void main(final String... args) {
		RobotBase.startRobot(Robot::new);
	}

	@Override
	protected final void robotInit() {

	}

	@Override
	public final void operatorControl() {
		enabled();
	}

	@Override
	public final void autonomous() {
		enabled();
	}

	@Override
	public final void test() {

	}

	@Override
	protected final void disabled() {

	}

	private void enabled() {
		
	}
}
