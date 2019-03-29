package com.amhsrobotics.tko2019.vision;

import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.vision.networking.Connection;
import edu.wpi.first.wpilibj.DriverStation;

public final class Vision {
	private static final double dP = 0.0;
	private static final double dT = 0.0;

	private static volatile boolean shouldRun = false;

	public static void enable() {
		shouldRun = true;
		while (DriverStation.getInstance().isEnabled() && shouldRun
				&& (Connection.getInstance().getDistance() > 40 || Connection.getInstance().getDistance() == 0)) {
			double d = Connection.getInstance().getDistance() * dP;
			double t = Connection.getInstance().getAngle() * dT;

			Drive.getInstance().setLeft(d - t);
			Drive.getInstance().setRight(d + t);

			try {
				Thread.sleep(25);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void distable() {
		shouldRun = false;
	}
}
