package com.amhsrobotics.tko2019.networktables;

import edu.wpi.first.networktables.EntryListenerFlags;

public enum NetworkTableFlags {
	Update(EntryListenerFlags.kUpdate),
	LocalChanges(EntryListenerFlags.kLocal),

	New(EntryListenerFlags.kNew),
	ImmediateListenting(EntryListenerFlags.kImmediate),

	Delete(EntryListenerFlags.kDelete);


	private final int value;

	NetworkTableFlags(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
