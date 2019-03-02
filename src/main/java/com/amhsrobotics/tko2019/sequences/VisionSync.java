package com.amhsrobotics.tko2019.sequences;

import com.amhsrobotics.datatypes.MicromovementData;
import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.networking.DataPort;
import com.amhsrobotics.tko2019.networking.NetworkRequests;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.io.IOException;

import static com.amhsrobotics.tko2019.networking.NetworkRequests.stream;

public class VisionSync {
	private static VisionSync ourInstance = new VisionSync();

	private MicromovementData data = null;

	public static VisionSync getInstance() {
		return ourInstance;
	}

	private VisionSync() {
	}

	public void request() {
		try {
			data = NetworkRequests.requestData(DataPort.Center);
			NetworkTableInstance.getDefault().getEntry("t1").setNumber(data.getTurn1());
			NetworkTableInstance.getDefault().getEntry("d1").setNumber(data.getDrive1());
			NetworkTableInstance.getDefault().getEntry("t2").setNumber(data.getTurn2());
			NetworkTableInstance.getDefault().getEntry("d2").setNumber(data.getDrive2());
			stream(data, "t");
		} catch (final IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void confirm() {
		if (data != null) {
			Drive.getInstance().turn(180- (-data.getTurn1()));
			Drive.getInstance().moveStraight(-data.getDrive1());
			Drive.getInstance().turn(180 - (-data.getTurn2()));
			// Drive.getInstance().moveStraight(data.getDrive2());
		}
	}

	public void invalidate() {
		data = null;
	}
}
