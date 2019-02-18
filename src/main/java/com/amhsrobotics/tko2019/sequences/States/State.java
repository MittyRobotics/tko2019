package com.amhsrobotics.tko2019.sequences.States;

public enum State {
    IDLE,
    VISION_REQUEST,
    FOLLOWING_TARGET,
    SCORING,
    EXIT_VISION;

    public int getId() {
        return ordinal();
    }

    public static State getModeFromId(final int id) {
        return State.values()[id];
    }

}
