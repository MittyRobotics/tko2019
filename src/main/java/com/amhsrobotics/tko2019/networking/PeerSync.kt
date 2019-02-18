package com.amhsrobotics.tko2019.networking

import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.net.Socket
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.concurrent.thread

object PeerSync : Serializable {
	private val peers = CopyOnWriteArrayList<Socket>()

	@Volatile
	private var data = Triple(0, 0, 0)
	@Volatile
	private var isRunning = false

	internal fun run() {
		isRunning = true
		thread {
			while (isRunning) {
				peers.forEach {
					if (!it.isClosed) {
						data = ObjectInputStream(it.getInputStream()).readObject() as Triple<Int, Int, Int>
					}
				}
				Thread.sleep(1)
			}
		}
	}

	private fun stop() {
		isRunning = false
	}

	internal fun addPeer(peer: Socket) {
		peers.add(peer)
	}

	fun updateData(value: Triple<Int, Int, Int>) {
		data = value
		for (peer in peers) {
			ObjectOutputStream(peer.getOutputStream()).writeObject(data)
		}
	}

	fun retrieveData(): Triple<Int, Int, Int> {
		return data
	}
}