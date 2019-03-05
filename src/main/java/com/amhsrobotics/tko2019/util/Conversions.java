package com.amhsrobotics.tko2019.util;

import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public final class Conversions {
	public static BufferedImage matToBufferedImage(final Mat mat) {
		final int type;
		switch (mat.channels()) {
			case 1:
				type = BufferedImage.TYPE_BYTE_GRAY;
				break;
			case 3:
				type = BufferedImage.TYPE_3BYTE_BGR;
				break;
			case 4:
				type = BufferedImage.TYPE_4BYTE_ABGR;
				break;
			default:
				return null;
		}
		final BufferedImage img = new BufferedImage(mat.width(), mat.height(), type);

		mat.get(0, 0, ((DataBufferByte) img.getRaster().getDataBuffer()).getData());

		return img;
	}
}
