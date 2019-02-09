package com.amhsrobotics.tko2019.sequences;

import com.amhsrobotics.tko2019.sequences.States.pcState;

public class PickupCargo {
	static pcState state = pcState.IDLE;

	private static pcState lastState;

	public static void main(String... args) {
		System.out.println(state);
		VisionCommand();
		while (true) {
			PickupCargoMain();
		}
	}

	public static void PickupCargoMain() {

		switch (state) {
			case IDLE:
				Idle();
				break;
			case FOLLOWING_TARGET:
				FollowingTarget();
				break;
			case CHECK_OBJECT:
				CheckObject();
				break;
			case CHECK_RELATIVE_POSITION:
				CheckRelativePosition();
				break;
			case SCORING:
				Scoring();
				break;
			case EXIT_VISION:
				ExitVision();
				break;
		}
	}


	public static void Idle() {
		//System.out.println(state);
	}

	public static void FollowingTarget() {
		System.out.println(state);
		PathComplete();

	}

	public static void CheckObject() {
		System.out.println(state);
		HasObject();

	}

	public static void CheckRelativePosition() {
		System.out.println(state);
		CorrectPos();

	}

	public static void Scoring() {
		System.out.println(state);
		ScoringDone();

	}

	//Transitions

	public static void VisionCommand() {
		if (state == pcState.IDLE) {
			state = pcState.CHECK_OBJECT;
			lastState = pcState.IDLE;
		}
	}

	public static void ExitVision() {
		System.out.println(state);
		state = pcState.IDLE;
		lastState = pcState.IDLE;

	}

	public static void PathComplete() {
		if (state == pcState.FOLLOWING_TARGET) {
			state = pcState.CHECK_OBJECT;
			lastState = pcState.FOLLOWING_TARGET;
		}
	}

	public static void HasObject() {
		if (state == pcState.CHECK_OBJECT) {
			state = pcState.EXIT_VISION;
		}
	}

	public static void NoObject() {
		if (state == pcState.CHECK_OBJECT) {
			if (lastState == pcState.IDLE) {
				state = pcState.FOLLOWING_TARGET;
			}
			if (lastState == pcState.FOLLOWING_TARGET) {
				state = pcState.CHECK_RELATIVE_POSITION;
			}
			lastState = pcState.CHECK_OBJECT;

		}
	}

	public static void CorrectPos() {
		if (state == pcState.CHECK_RELATIVE_POSITION) {
			state = pcState.SCORING;
		}
	}

	public static void ScoringDone() {
		if (state == pcState.SCORING) {
			state = pcState.EXIT_VISION;
		}
	}

	public static void WrongPos() {
		if (state == pcState.CHECK_RELATIVE_POSITION) {
			state = pcState.EXIT_VISION;
		}
	}


}

