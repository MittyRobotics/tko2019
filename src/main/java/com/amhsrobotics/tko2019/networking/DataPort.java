package com.amhsrobotics.tko2019.networking;

public enum DataPort {
	Left(8100), Center(8101), Right(8102);

	private final int port;

	DataPort(final int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}
}
