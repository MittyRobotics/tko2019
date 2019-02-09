package com.amhsrobotics.tko2019.sequences.States;

public enum VisionMode {
    NO_STATE,
    PICKUP_CARGO,
    PICKUP_HATCH,
    SCORE_CARGO_CS, //Cargo Ship
    SCORE_CARGO_RS, //Rocket Ship
    SCORE_HATCH_CS, //Cargo Ship
    SCORE_HATCH_LEFT, //Rocket Ship
    SCORE_HATCH_RIGHT; //Rocket Ship




    public int getId() {
        return ordinal();
    }

    public static VisionMode getModeFromId(final int id) {
        return VisionMode.values()[id];
    }


}
