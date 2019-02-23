package com.amhsrobotics.tko2019.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PeerSync {
	private static List<Socket> peers = new CopyOnWriteArrayList<>();

	private static volatile HashMap<String, Double> data = new HashMap<String, Double>();
	private static volatile boolean isRunning = false;

	static void run() {
		isRunning = true;
		new Thread(() -> {
			while (isRunning) {
				for (final Socket peer : peers) {
					if (!peer.isClosed()) {
						try {
							data = (HashMap<String, Double>) new ObjectInputStream(peer.getInputStream()).readObject();
						} catch (final IOException | ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
				try {
					Thread.sleep(1);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	static void addPeer(final Socket peer) {
		peers.add(peer);
	}

	static void stop() {
		isRunning = false;
	}

	static void updateData(final HashMap<String, Double> value) {
		data = value;
		for (final Socket peer : peers) {
			try {
				new ObjectOutputStream(peer.getOutputStream()).writeObject(data);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	static HashMap<String, Double> retrieveData() {
		return data;
	}
}
