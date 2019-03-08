package com.amhsrobotics.tko2019.vision;

import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.hardware.subsystems.HatchPanel;
import com.amhsrobotics.tko2019.util.Conversions;
import com.amhsrobotics.tko2019.vision.sequences.SequencesManager;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

public final class VisionSync {
	public static final SequencesManager SEQUENCES_MANAGER = new SequencesManager();

	private final CameraDirection cam;
	private final HatchPanel.HatchPosition pos;

	public VisionSync() {
		if (Drive.getInstance().isReversed()) {
			cam = CameraDirection.Cargo;
			pos = HatchPanel.HatchPosition.Center;
		} else {
			cam = CameraDirection.Hatch;
			pos = HatchPanel.getInstance().getHatchPosition();
		}
	}

	public final synchronized void request() throws Exception {
		NetworkTableInstance.getDefault().getEntry("pos").setString(pos.toString());
		final Mat frame = new Mat();
		CameraServer.getInstance().getVideo(cam.toString()).grabFrame(frame);
		NetworkTableInstance.getDefault().getEntry("row").setNumber(frame.rows());
		NetworkTableInstance.getDefault().getEntry("col").setNumber(frame.cols());
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(Objects.requireNonNull(Conversions.matToBufferedImage(frame)),
				"jpg", byteArrayOutputStream);
		final byte[] raw = byteArrayOutputStream.toByteArray();
		NetworkTableInstance.getDefault().getEntry("frame").setRaw(raw);
	}

	public final synchronized void confirm() {
		final double t1 = NetworkTableInstance.getDefault().getEntry("t1").getDouble(0);
		if (t1 != 0) {
			Drive.getInstance().turn(t1);
		}
		final double d1 = NetworkTableInstance.getDefault().getEntry("d1").getDouble(0);
		if (d1 != 0) {
			Drive.getInstance().moveStraight(d1);
		}
		final double t2 = NetworkTableInstance.getDefault().getEntry("t2").getDouble(0);
		if (t2 != 0) {
			Drive.getInstance().turn(t2);
		}
		final double d2 = NetworkTableInstance.getDefault().getEntry("d2").getDouble(0);
		if (d2 != 0) {
			Drive.getInstance().moveStraight(d2);
		}
	}
}
