package com.amhsrobotics.tko2019.serializeddata;

import java.io.Serializable;

public class Coordinate implements Serializable {
    private final double x;
    private final double y;
    private final double a;

    public Coordinate(final double x, final double y, final double a) {
        this.x = x;
        this.y = y;
        this.a = a;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getA() {
        return a;
    }
}
