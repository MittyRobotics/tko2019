package com.amhsrobotics.tko2019.settings.subsystems;

public class TalonInversions {
	//Drive
	public static final boolean[] LEFT_DRIVE_INVERSIONS = {false, false};
	public static final boolean[] RIGHT_DRIVE_INVERSIONS = {false, false};

	//Hatch Panel
	public static final boolean SLIDER_INVERSION = false;

	//CARGO
	public static final boolean[] CONVEYOR_TALON_INVERSIONS = {false, false};
	public static final boolean[] INTAKE_TALON_INVERSIONS = {false, true};
}
