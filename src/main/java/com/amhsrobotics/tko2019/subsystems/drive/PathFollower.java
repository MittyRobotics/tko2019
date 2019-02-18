package com.amhsrobotics.tko2019.subsystems.drive;

import com.amhsrobotics.tko2019.sequences.Sequence;

public class PathFollower {
    Sequence sequence = new Sequence();
    public void FollowPath(){
        //Generate path using Pathfinder library
        //Follow path
        //if done
        sequence.completedPath = true;
    }
}
