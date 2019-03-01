package com.amhsrobotics.tko2019.networking;

import com.amhsrobotics.datatypes.MicromovementData;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.cameraserver.CameraServer;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Arrays;

public class NetworkRequests {
	private static Thread thread = new Thread();

	private static volatile Mat mat;

	public static MicromovementData requestData(final DataPort dataPort) throws IOException, ClassNotFoundException {
		final Socket socket = new Socket("tegra-ubuntu.local", dataPort.getPort());
		return (MicromovementData) new ObjectInputStream(socket.getInputStream()).readObject();
	}

	public void main(String... args) {
		try {
			final MicromovementData data = requestData(DataPort.Center);
			System.out.println(Arrays.toString(data.getOverlayImage()));
			stream(data, "t");
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void stream(final MicromovementData data, final String name) throws IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data.getOverlayImage());
		final BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
		final Mat mat = bufferedImageToMat(bufferedImage);
		NetworkRequests.mat = mat;

		if (!thread.isAlive()) {
			CvSource outputStream = CameraServer.getInstance().putVideo(name, bufferedImage.getWidth(), bufferedImage.getHeight());
			thread.setDaemon(true);
			thread = new Thread(() -> {
				while (!Thread.interrupted()) {
					outputStream.putFrame(NetworkRequests.mat);
				}
			});
			thread.start();
		}
	}

	public static Mat bufferedImageToMat(BufferedImage bi) {
		final Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		return mat;
	}
}
