package com.amhsrobotics.tko2019.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PeerSync {
	private static List<Socket> peers = new CopyOnWriteArrayList<>();

	private static volatile int data = 0;
	private static volatile boolean isRunning = false;

	static void run() {
		isRunning = true;
		new Thread(() -> {
			while (isRunning) {
				for (final Socket peer : peers) {
					if (!peer.isClosed()) {
						try {
							data = new ObjectInputStream(peer.getInputStream()).readInt();
						} catch (final IOException e) {
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

	static void updateData(final int value) {
		data = value;
		for (final Socket peer : peers) {
			try {
				new ObjectOutputStream(peer.getOutputStream()).writeObject(data);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	static int retrieveData() {
		return data;
	}
}
