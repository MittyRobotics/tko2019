package com.amhsrobotics.tko2019.sequences;

import com.amhsrobotics.datatypes.MicromovementData;
import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.networking.DataPort;
import com.amhsrobotics.tko2019.networking.NetworkRequests;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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

	public void request(String camName, String pos) {
		final NetworkTableInstance nt = NetworkTableInstance.getDefault();
		nt.getEntry("pos").setString(pos);
		final Mat frame = new Mat();
		CameraServer.getInstance().getVideo(camName).grabFrame(frame);
		final byte[] raw = matToBytes(frame);
		nt.getEntry("frame").setRaw(raw);
	}

	public static byte[] matToBytes(final Mat matrix) {
		MatOfByte mob=new MatOfByte();
		Imgcodecs.imencode(".jpg", matrix, mob);
		return new ByteArrayInputStream(mob.toArray()).readAllBytes();
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
