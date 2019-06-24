package com.github.tetrisanalyzer.move.rotation;

public class ClockwiseRotation implements RotationDirection {
    public int getDirection() {
        return 1;
    }

    @Override
    public String toString() {
        return "clockwise";
    }
}
