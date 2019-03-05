package com.amhsrobotics.tko2019.sequences;

import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;

public class VisionSync {
	private static VisionSync ourInstance = new VisionSync();

	private VisionSync() {
	}

	public static VisionSync getInstance() {
		return ourInstance;
	}

	private static BufferedImage matToBufferedImage(final Mat mat) throws Exception {
		final int type;
		if (mat.channels() == 1) {
			type = BufferedImage.TYPE_BYTE_GRAY;
		} else if (mat.channels() == 3) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		} else if (mat.channels() == 4) {
			type = BufferedImage.TYPE_4BYTE_ABGR;
		} else {
			throw new Exception();
		}
		final BufferedImage img = new BufferedImage(mat.width(), mat.height(), type);

		mat.get(0, 0, ((DataBufferByte) img.getRaster().getDataBuffer()).getData());

		return img;
	}

	public void request(String camName, String pos) throws Exception {
		final NetworkTableInstance nt = NetworkTableInstance.getDefault();
		nt.getEntry("pos").setString(pos);
		final Mat frame = new Mat();
		CameraServer.getInstance().getVideo(camName).grabFrame(frame);
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(matToBufferedImage(frame), "jpg", byteArrayOutputStream);
		final byte[] raw = byteArrayOutputStream.toByteArray();
		nt.getEntry("row").setNumber(frame.rows());
		nt.getEntry("col").setNumber(frame.cols());
		nt.getEntry("frame").setRaw(raw);
	}

	public void confirm() {
		final NetworkTableInstance nt = NetworkTableInstance.getDefault();
		Drive.getInstance().leftTalons[0].setNeutralMode(NeutralMode.Brake);
		Drive.getInstance().leftTalons[1].setNeutralMode(NeutralMode.Brake);
		Drive.getInstance().rightTalons[0].setNeutralMode(NeutralMode.Brake);
		Drive.getInstance().rightTalons[1].setNeutralMode(NeutralMode.Brake);
		final double t1 = nt.getEntry("t1").getDouble(0);
		final double d1 = nt.getEntry("d1").getDouble(0);
		final double t2 = nt.getEntry("t2").getDouble(0);
		final double d2 = nt.getEntry("d2").getDouble(0);
		if (t1 != 0) {
			Drive.getInstance().turn(t1);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		Drive.getInstance().moveStraight(d1);
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (t2 != 0) {
			Drive.getInstance().turn(t2);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (d2 != 0) {
			Drive.getInstance().moveStraight(d2);
		}


		Drive.getInstance().leftTalons[0].setNeutralMode(NeutralMode.Coast);
		Drive.getInstance().leftTalons[1].setNeutralMode(NeutralMode.Coast);
		Drive.getInstance().rightTalons[0].setNeutralMode(NeutralMode.Coast);
		Drive.getInstance().rightTalons[1].setNeutralMode(NeutralMode.Coast);
	}
}
