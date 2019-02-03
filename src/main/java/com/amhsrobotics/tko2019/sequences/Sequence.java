package com.amhsrobotics.tko2019.sequences;

import com.amhsrobotics.tko2019.sequences.States.SequenceState;

public class Sequence {
    static SequenceState currSequence;

    public void StartVision(SequenceState visionMode){
        currSequence = visionMode;
    }

    public static void ExitVision(){
        currSequence = SequenceState.NO_STATE;
    }

    public static boolean isTargetDetected(){
        return(true);
    }

    public static boolean hasHatch(){
        return(true);
    }

    public static boolean hasCargo(){
        return(true);
    }

    public static boolean isVisionDone(){
        return(true);
    }

    public static boolean checkRelativeRot(){
        return(true);
    }

    public static boolean checkRelativePos(int offset){
        return(true);
    }

    public static boolean completedPath(){
        return(true);
    }

    //Common Sequence

    public void Check(){

    }

    public void DriveToTarget(){

    }

    public void Position(){

    }

}
