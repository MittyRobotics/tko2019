package com.amhsrobotics.tko2019.sequences;

import com.amhsrobotics.tko2019.sequences.States.Check;
import com.amhsrobotics.tko2019.sequences.States.State;
import com.amhsrobotics.tko2019.sequences.States.VisionMode;
import edu.wpi.first.networktables.NetworkTableEntry;

public class Sequence {
	static VisionMode currSequence;
	static State state;
	public boolean completedPath = false;
	VisionMode prevMode;
	NetworkTableEntry networkTableEntry;

	public static void VisionCommand() {
		Transition(State.IDLE, State.FOLLOWING_TARGET, new Check[]{Check.HAS_HATCH, Check.HAS_TARGET});

	}

	public static void Transition(State prev, State newState, Check[] checks) {
		System.out.println("Transition From: " + prev.toString() + " To: " + newState.toString());
		if (state == prev) {
			switch (currSequence) {
				case PICKUP_CARGO:
					PickupCargo.lastState = prev;
					break;
				case PICKUP_HATCH:
					PickupHatch.lastState = prev;
					break;
				case SCORE_CARGO_CS:
					ScoreCargoCS.lastState = prev;
					break;
				case SCORE_CARGO_RS:
					ScoreCargoRS.lastState = prev;
					break;
				case SCORE_HATCH_CS:
					ScoreHatchCS.lastState = prev;
					break;
				case SCORE_HATCH_LEFT:
					ScoreHatchLeft.lastState = prev;
					break;
				case SCORE_HATCH_RIGHT:
					ScoreHatchRight.lastState = prev;
					break;
			}

		}

		boolean continueSequence = true;
		for (int i = 0; i < checks.length; i++) {
			switch (checks[i]) {
				case HAS_CARGO:
					if (false) {
						continueSequence = false;
					}
					break;
				case HAS_HATCH:
					if (false) {
						continueSequence = false;
					}
					break;
				case HAS_TARGET:
					if (false) {
						continueSequence = false;
					}
					break;
				case RELATIVE_POS:
					if (false) {
						continueSequence = false;
					}
			}
		}
		if (continueSequence == true) {
			state = newState;
			System.out.println("Success!");
		} else {
			switch (currSequence) {
				case PICKUP_CARGO:
					PickupCargo.ExitVision();
					break;
				case PICKUP_HATCH:
					PickupHatch.ExitVision();
					break;
				case SCORE_CARGO_CS:
					ScoreCargoCS.ExitVision();
					break;
				case SCORE_CARGO_RS:
					ScoreCargoRS.ExitVision();
					break;
				case SCORE_HATCH_CS:
					ScoreHatchCS.ExitVision();
					break;
				case SCORE_HATCH_LEFT:
					ScoreHatchLeft.ExitVision();
					break;
				case SCORE_HATCH_RIGHT:
					ScoreHatchRight.ExitVision();
					break;
			}
			System.out.println("Failed");
		}
	}

	public void SequenceMain(String... args) {


		state = State.IDLE;
		VisionCommand();

		//TODO: No while true loops, change later
		while (1 == 1) {

			if (prevMode != currSequence) {
				networkTableEntry.setDouble(currSequence.ordinal());
			}

			switch (currSequence) {
				case NO_STATE:
					break;
				case PICKUP_CARGO:
					switch (state) {
						case IDLE:
							PickupCargo.Idle();
							break;
						case FOLLOWING_TARGET:
							PickupCargo.FollowingTarget();
							break;
						case SCORING:
							PickupCargo.Scoring();
							break;
						case EXIT_VISION:
							PickupCargo.ExitVision();
							break;
						case VISION_REQUEST:
							PickupCargo.VisionRequest();
					}
					break;
				case PICKUP_HATCH:
					switch (state) {
						case IDLE:
							PickupHatch.Idle();
							break;
						case FOLLOWING_TARGET:
							PickupHatch.FollowingTarget();
							break;
						case SCORING:
							PickupHatch.Scoring();
							break;
						case EXIT_VISION:
							PickupHatch.ExitVision();
							break;
						case VISION_REQUEST:
							PickupHatch.VisionRequest();
					}
					break;
				case SCORE_CARGO_CS:
					switch (state) {
						case IDLE:
							ScoreCargoCS.Idle();
							break;
						case FOLLOWING_TARGET:
							ScoreCargoCS.FollowingTarget();
							break;
						case SCORING:
							ScoreCargoCS.Scoring();
							break;
						case EXIT_VISION:
							ScoreCargoCS.ExitVision();
							break;
						case VISION_REQUEST:
							ScoreCargoCS.VisionRequest();
					}
					break;
				case SCORE_CARGO_RS:
					switch (state) {
						case IDLE:
							ScoreCargoRS.Idle();
							break;
						case FOLLOWING_TARGET:
							ScoreCargoRS.FollowingTarget();
							break;
						case SCORING:
							ScoreCargoRS.Scoring();
							break;
						case EXIT_VISION:
							ScoreCargoRS.ExitVision();
							break;
						case VISION_REQUEST:
							ScoreCargoRS.VisionRequest();
					}
					break;
				case SCORE_HATCH_CS:
					switch (state) {
						case IDLE:
							ScoreHatchCS.Idle();
							break;
						case FOLLOWING_TARGET:
							ScoreHatchCS.FollowingTarget();
							break;
						case SCORING:
							ScoreHatchCS.Scoring();
							break;
						case EXIT_VISION:
							ScoreHatchCS.ExitVision();
							break;
						case VISION_REQUEST:
							ScoreHatchCS.VisionRequest();
					}
					break;
				case SCORE_HATCH_LEFT:
					switch (state) {
						case IDLE:
							ScoreHatchLeft.Idle();
							break;
						case FOLLOWING_TARGET:
							ScoreHatchLeft.FollowingTarget();
							break;
						case SCORING:
							ScoreHatchLeft.Scoring();
							break;
						case EXIT_VISION:
							ScoreHatchLeft.ExitVision();
							break;
						case VISION_REQUEST:
							ScoreHatchLeft.VisionRequest();
					}
					break;
				case SCORE_HATCH_RIGHT:
					switch (state) {
						case IDLE:
							ScoreHatchRight.Idle();
							break;
						case FOLLOWING_TARGET:
							ScoreHatchRight.FollowingTarget();
							break;
						case SCORING:
							ScoreHatchRight.Scoring();
							break;
						case EXIT_VISION:
							ScoreHatchRight.ExitVision();
							break;
						case VISION_REQUEST:
							ScoreHatchRight.VisionRequest();
					}
					break;
			}

		}
	}

	public void pickupCargo() {
		currSequence = VisionMode.PICKUP_CARGO;
	}

	public void pickupHatch() {
		currSequence = VisionMode.PICKUP_HATCH;
	}

	public void scoreCargoCS() {
		currSequence = VisionMode.SCORE_CARGO_CS;
	}

	public void scoreCargoRS() {
		currSequence = VisionMode.SCORE_CARGO_RS;
	}

	public void scoreHatchCS() {
		currSequence = VisionMode.SCORE_HATCH_CS;
	}

	public void scoreHatchLeft() {
		currSequence = VisionMode.SCORE_HATCH_LEFT;
	}

	public void scoreHatchRight() {
		currSequence = VisionMode.SCORE_HATCH_RIGHT;
	}

}

