package com.amhsrobotics.tko2019.networking;

import java.io.IOException;
import java.net.Socket;

public class Client {
	public Client(final String ip, final int port) {
		try {
			PeerSync.addPeer(new Socket(ip, port));
		} catch (final IOException e) {
			e.printStackTrace();
		}
		PeerSync.run();
	}
}
