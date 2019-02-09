package com.amhsrobotics.tko2019.sequences;


import com.amhsrobotics.tko2019.cargo.Cargo;
import com.amhsrobotics.tko2019.drive.Drive;
import com.amhsrobotics.tko2019.hatchpanel.HatchPanel;
import com.amhsrobotics.tko2019.sequences.States.Check;
import com.amhsrobotics.tko2019.sequences.States.State;

public class ScoreHatchLeft {
    static HatchPanel hatch = new HatchPanel();
    static Drive drive = new Drive();

    public static State lastState;

    public static  void Idle(){

    }

    public static void FollowingTarget() {
        System.out.println(Sequence.state);
        Sequence.Transition(State.FOLLOWING_TARGET, State.SCORING, new Check[]{Check.HAS_HATCH, Check.RELATIVE_POS});
    }

    public static  void Scoring(){
        System.out.println(Sequence.state);
        double moveDist = 0; //TODO
        hatch.slideLeft();
        drive.move(moveDist);
        hatch.rocketDrop();
        drive.move(-moveDist);
        Sequence.Transition(State.SCORING, State.EXIT_VISION,new Check[]{Check.HAS_NO_HATCH});
    }


    public static void ExitVision(){
        System.out.println(Sequence.state);
        Sequence.state = State.IDLE;
        lastState = State.IDLE;

    }
}
