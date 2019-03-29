package com.amhsrobotics.tko2019.vision.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public final class Connection {
	private final static Connection INSTANCE = new Connection();

	private volatile double distance;
	private volatile double angle;

	private Connection() {
		new Thread(() -> {
			try {
				final ServerSocket ss = new ServerSocket(1337);
				while (ss.isBound()) { // FIXME Untested, Make (true) if You Are Getting an Error
					final Socket s = ss.accept();
					final BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
					while (s.isConnected()) {
						final String line = br.readLine();
						if (line != null) {
							final String[] split = line.substring(1).split("a");
							distance = Double.parseDouble(split[0]);
							angle = Double.parseDouble(split[1]);
						} else {
							break;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public static Connection getInstance() {
		return INSTANCE;
	}

	public final double getDistance() {
		return distance;
	}

	public final double getAngle() {
		return angle;
	}
}
