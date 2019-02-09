package com.amhsrobotics.tko2019.logging;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import java.util.logging.Level;
import java.util.logging.Logger;

public interface LogCapable {
	Level HARDWARE_LEVEL = Level.FINE;
	Level HARDWARE_INIT_LEVEL = Level.INFO;
	Level TALON_LEVEL = Level.FINE;
	Level SOLENOID_LEVEL = Level.FINE;

	Level SOFTWARE_TOGGLE = Level.FINER;

	Level TRACE = Level.FINEST;

	default Logger getLogger() {
		return Logger.getLogger(getClass().getSimpleName());
	}

	default void hardware(final String message) {
		getLogger().log(HARDWARE_LEVEL, String.format("Hardware: %s", message));
	}

	default void hardwareInit(final WPI_TalonSRX talon) {
		getLogger().log(HARDWARE_INIT_LEVEL, String.format("Hardware Init: %s", talon.getName()));
	}

	default void hardwareInit(final DoubleSolenoid solenoid) {
		getLogger().log(HARDWARE_INIT_LEVEL, String.format("Hardware Init: %s", solenoid.getName()));
	}

	default void talonSet(final WPI_TalonSRX talon, final ControlMode controlMode, final double value) {
		getLogger().log(TALON_LEVEL, String.format("Talon[%s]: %s @ %s", talon.getName(), value, controlMode));
	}

	default void solenoidSet(final DoubleSolenoid solenoid, final DoubleSolenoid.Value value) {
		getLogger().log(SOLENOID_LEVEL, String.format("Solenoid[%s]: %s", solenoid.getName(), value));
	}

	default void toggleSoftware(final String name, final boolean value) {
		getLogger().log(SOFTWARE_TOGGLE, String.format("%s Toggled to %s", name, value));
	}

	default void throwing(final String methodName, final Throwable throwable) {
		getLogger().throwing(getClass().getName(), methodName, throwable);
	}

	default void entering(final String methodName) {
		getLogger().entering(getClass().getName(), methodName);
	}

	default void exiting(final String methodName) {
		getLogger().exiting(getClass().getName(), methodName);
	}
}
