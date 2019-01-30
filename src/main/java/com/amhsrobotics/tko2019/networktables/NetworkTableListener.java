package com.amhsrobotics.tko2019.networktables;

import com.amhsrobotics.tko2019.networktables.events.NetworkTableDeleteEvent;
import com.amhsrobotics.tko2019.networktables.events.NetworkTableEvent;
import com.amhsrobotics.tko2019.networktables.events.NetworkTableNewEvent;
import com.amhsrobotics.tko2019.networktables.events.NetworkTableUpdateEvent;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.TableEntryListener;

import java.util.ArrayList;

public interface NetworkTableListener extends TableEntryListener {
	@Override
	default void valueChanged(final NetworkTable table, final String key, final NetworkTableEntry entry, final NetworkTableValue value, final int flags) {
		final ArrayList<NetworkTableEvent> events = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			if (flags >> i % 2 == 1) {
				switch (i) {
					case 2:
						events.add(new NetworkTableNewEvent(table, key, entry, value));
						break;
					case 3:
						events.add(new NetworkTableDeleteEvent(table, key, entry, value));
						break;
					case 4:
						events.add(new NetworkTableUpdateEvent(table, key, entry, value));
						break;
				}
			}
		}
		for (final NetworkTableEvent event : events) {
			handleEvent(event);
		}
	}

	void handleEvent(final NetworkTableEvent event);
}
