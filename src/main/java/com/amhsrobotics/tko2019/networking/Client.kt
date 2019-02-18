package com.amhsrobotics.tko2019.networking

import java.net.Socket

class Client(ip: String, port: Int) {
	init {
		val socket = Socket(ip, port)
		PeerSync.addPeer(socket)
		PeerSync.run()
	}

	companion object {
		fun main() {
			Client("localhost", 8080)
			while (true) {
				val num: Triple<Int, Int, Int> = PeerSync.retrieveData()
				if (num.first < 100) {
					if (num.first % 2 == 0) {
						PeerSync.updateData(Triple(num.first + 1, 0, 0))
						println(num.first)
					}
				} else {
					break
				}
			}
		}
	}
}