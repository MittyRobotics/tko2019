package com.amhsrobotics.tko2019.sequences.old;


import com.amhsrobotics.tko2019.hardware.subsystems.Cargo;
import com.amhsrobotics.tko2019.hardware.subsystems.Drive;
import com.amhsrobotics.tko2019.sequences.old.States.Check;
import com.amhsrobotics.tko2019.sequences.old.States.State;

public class ScoreCargoCS {
	public static State lastState;
	static Cargo cargo = Cargo.getInstance();
	static Drive drive = Drive.getInstance();
	static Sequence sequence = new Sequence();
	static PathFollower pathFollower = new PathFollower();

	public static void Idle() {

	}

	public static void VisionRequest() {
		//////////////////////
		//Recieve Coordinate//
		//////////////////////

		Sequence.Transition(State.VISION_REQUEST, State.FOLLOWING_TARGET, new Check[]{Check.HAS_CARGO});
	}

	public static void FollowingTarget() {
		System.out.println(Sequence.state);
		while (sequence.completedPath == false) {
		}
		sequence.completedPath = false;
		Sequence.Transition(State.FOLLOWING_TARGET, State.SCORING, new Check[]{Check.HAS_CARGO, Check.RELATIVE_POS});
	}

	public static void Scoring() {
		System.out.println(Sequence.state);
		double moveDist = 0; //TODO
		cargo.cargoConveyor();
		drive.moveStraight(moveDist);
		cargo.spinOuttake(0.5, 0.5);
		drive.moveStraight(-moveDist);
		cargo.stopIntake();
		Sequence.Transition(State.SCORING, State.EXIT_VISION, new Check[]{Check.HAS_NO_CARGO});
	}


	public static void ExitVision() {
		System.out.println(Sequence.state);
		Sequence.state = State.IDLE;
		lastState = State.IDLE;

	}
}
