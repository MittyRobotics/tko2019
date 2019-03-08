package com.amhsrobotics.tko2019.vision;

import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.hardware.subsystems.HatchPanel;
import com.amhsrobotics.tko2019.util.Conversions;
import com.amhsrobotics.tko2019.vision.sequences.SequencesManager;
import com.amhsrobotics.tko2019.vision.sequences.drive.DriveSequence;
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
		final SequencesManager manager = new SequencesManager();
		manager.startSequence(new DriveSequence());
	}
}
