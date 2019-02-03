package com.amhsrobotics.tko2019.sequences;

import com.amhsrobotics.tko2019.sequences.States.pcState;

public class PickupCargo {
    pcState state;

    public void Idle(){

    }

    public void FollowingTarget(){
        PathComplete();
    }

    public void CheckObject(){
        HasObject();
    }

    public void CheckRelativePosition(){
        CorrectPos();
    }

    public void Scoring(){
        ScoringDone();
    }

    //Transitions

    public void VisionCommand(){
        if(state == pcState.IDLE){
            state = pcState.FOLLOWING_TARGET;
        }
    }

    public void ExitVision(){
        state = pcState.IDLE;
    }

    public void PathComplete(){
        if(state == pcState.FOLLOWING_TARGET){
            state = pcState.CHECK_OBJECT;
        }
    }

    public void HasObject(){
        if(state == pcState.CHECK_OBJECT){
            state = pcState.CHECK_RELATIVE_POSITION;
        }
    }

    public void CorrectPos(){
        if(state == pcState.CHECK_RELATIVE_POSITION){
            state = pcState.SCORING;
        }
    }

    public void ScoringDone(){
        if(state == pcState.SCORING){
            state = pcState.IDLE;
        }
    }

    public void WrongPos(){
        if(state == pcState.CHECK_RELATIVE_POSITION){
            state = pcState.IDLE;
        }
    }
}

