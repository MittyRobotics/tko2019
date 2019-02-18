package com.amhsrobotics.tko2019.drive;

import com.amhsrobotics.tko2019.sequences.Sequence;
import com.amhsrobotics.tko2019.serializeddata.Coordinate;

public class PathFollower {
    Sequence sequence = new Sequence();
    public void FollowPath(Coordinate coordinate){
        //Generate path using Pathfinder library
        //Follow path
        //if done
        sequence.completedPath = true;
    }
}
