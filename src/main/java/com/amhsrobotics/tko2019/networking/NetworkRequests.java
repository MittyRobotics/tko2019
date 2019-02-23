package com.amhsrobotics.tko2019.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class NetworkRequests {
	public static MicromovementData requestData(final DataPort dataPort) throws IOException, ClassNotFoundException {
		final Socket socket = new Socket("tegra-ubuntu.local", dataPort.getPort());
		return (MicromovementData) new ObjectInputStream(socket.getInputStream()).readObject();
	}
}
