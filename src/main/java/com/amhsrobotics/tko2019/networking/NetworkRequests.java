package com.amhsrobotics.tko2019.networking;

import com.amhsrobotics.datatypes.MicromovementData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class NetworkRequests {
	public static MicromovementData requestData(final DataPort dataPort) throws IOException, ClassNotFoundException {
		final Socket socket = new Socket("localHost", dataPort.getPort());
		return (MicromovementData) new ObjectInputStream(socket.getInputStream()).readObject();
	}
}
