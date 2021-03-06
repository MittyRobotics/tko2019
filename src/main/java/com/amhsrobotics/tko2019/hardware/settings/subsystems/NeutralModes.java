package com.amhsrobotics.tko2019.hardware.settings.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;

public class NeutralModes {
	//Drive
	public static final NeutralMode DRIVE = NeutralMode.Brake;

	//Cargo
	public static final NeutralMode INTAKE = NeutralMode.Coast;
	public static final NeutralMode CONVEYOR = NeutralMode.Coast;

	//Hatch Panel
	public static final NeutralMode SLIDER = NeutralMode.Coast;
}
