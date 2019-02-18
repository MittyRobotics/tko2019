package com.amhsrobotics.tko2019.sequences;


import com.amhsrobotics.tko2019.cargo.Cargo;
import com.amhsrobotics.tko2019.drive.Drive;
import com.amhsrobotics.tko2019.drive.PathFollower;
import com.amhsrobotics.tko2019.sequences.States.Check;
import com.amhsrobotics.tko2019.sequences.States.State;
import com.amhsrobotics.tko2019.serializeddata.Coordinate;

public class ScoreCargoCS {
    static Cargo cargo = new Cargo();
    static Drive drive = new Drive();
    static Sequence sequence = new Sequence();
    static PathFollower pathFollower = new PathFollower();

    public static State lastState;

    public static  void Idle(){

    }

    public static void VisionRequest(){
        //RECIEVE VISION STUFF

        Sequence.Transition(State.VISION_REQUEST, State.FOLLOWING_TARGET, new Check[]{Check.HAS_CARGO});
    }

    public static void FollowingTarget() {
        System.out.println(Sequence.state);
        while(sequence.completedPath == false){
            pathFollower.FollowPath(sequence.currCoord);
        }
        sequence.completedPath = false;
        Sequence.Transition(State.FOLLOWING_TARGET, State.SCORING, new Check[]{Check.HAS_CARGO, Check.RELATIVE_POS});
    }

    public static  void Scoring(){
        System.out.println(Sequence.state);
        double moveDist = 0; //TODO
        cargo.cargoConveyor();
        drive.move(moveDist);
        cargo.intakeOuttakeMacro();
        drive.move(-moveDist);
        cargo.stopIntake();
        Sequence.Transition(State.SCORING, State.EXIT_VISION,new Check[]{Check.HAS_NO_CARGO});
    }


    public static void ExitVision(){
        System.out.println(Sequence.state);
        Sequence.state = State.IDLE;
        lastState = State.IDLE;

    }
}
