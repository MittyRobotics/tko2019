package com.amhsrobotics.tko2019.networking;

import com.amhsrobotics.datatypes.MicromovementData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class NetworkRequests {
	public static MicromovementData requestData(final DataPort dataPort) throws IOException, ClassNotFoundException {
		final Socket socket = new Socket("tegra-ubuntu.local", dataPort.getPort());
		return (MicromovementData) new ObjectInputStream(socket.getInputStream()).readObject();
	}

	public static void main(String... args) {
		try {
			while (true) {
				System.out.println(requestData(DataPort.Left).getTurn1());
				System.out.println(requestData(DataPort.Center).getTurn1());
				System.out.println(requestData(DataPort.Right).getTurn1());
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
