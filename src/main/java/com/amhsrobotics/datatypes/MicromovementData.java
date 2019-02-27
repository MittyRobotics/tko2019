package com.amhsrobotics.datatypes;

import java.io.Serializable;

public class MicromovementData implements Serializable {
    private final double turn1;
    private final double drive1;
    private final double turn2;
    private final double drive2;
    private final byte[] overlayImage;

    public MicromovementData(final double turn1, final double drive1, final double turn2, final double drive2, byte[] overlayImage) {
        this.turn1 = turn1;
        this.drive1 = drive1;
        this.turn2 = turn2;
        this.drive2 = drive2;
        this.overlayImage = overlayImage;
    }

    public double getTurn1() {
        return turn1;
    }

    public double getDrive1() {
        return drive1;
    }

    public double getTurn2() {
        return turn2;
    }

    public double getDrive2() {
        return drive2;
    }

    public byte[] getOverlayImage(){return overlayImage;}
}
