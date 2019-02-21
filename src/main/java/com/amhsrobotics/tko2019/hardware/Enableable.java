package com.amhsrobotics.tko2019.hardware;

import java.util.ArrayList;

public interface Enableable {
	ArrayList<Enableable> ENABLEABLES = new ArrayList<>();


	void enable();

	void disable();
}
