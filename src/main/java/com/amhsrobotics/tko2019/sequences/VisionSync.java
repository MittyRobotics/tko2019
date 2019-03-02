package com.amhsrobotics.tko2019.sequences;

import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;

public class VisionSync {
	private static VisionSync ourInstance = new VisionSync();

	public static VisionSync getInstance() {
		return ourInstance;
	}

	private VisionSync() {
	}

	public void request(String camName, String pos) {
		final NetworkTableInstance nt = NetworkTableInstance.getDefault();
		nt.getEntry("pos").setString(pos);
		final Mat frame = new Mat();
		CameraServer.getInstance().getVideo(camName).grabFrame(frame);
		final byte[] raw = matToBytes(frame);
		nt.getEntry("row").setNumber(frame.rows());
		nt.getEntry("col").setNumber(frame.cols());
		nt.getEntry("frame").setRaw(raw);
	}

	private static byte[] matToBytes(final Mat matrix) {
		byte[] b = new byte[matrix.channels() * matrix.cols() * matrix.rows()];
		matrix.data().get(b);
		return b;
	}

	public void confirm() {
		final NetworkTableInstance nt = NetworkTableInstance.getDefault();
		final double t1 = nt.getEntry("t1").getDouble(0);
		final double d1 = nt.getEntry("d1").getDouble(0);
		final double t2 = nt.getEntry("t2").getDouble(0);
		final double d2 = nt.getEntry("d2").getDouble(0);
		Drive.getInstance().turn(t1);
		Drive.getInstance().moveStraight(d1);
		Drive.getInstance().turn(t2);
		Drive.getInstance().moveStraight(d2);
	}
}
