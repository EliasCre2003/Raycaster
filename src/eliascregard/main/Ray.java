package eliascregard.main;

import java.awt.*;

public class Ray {
    private final double distance;
    private final Color color;

    public Ray(double distance, Color color) {
        this.distance = distance;
        this.color = color;
    }

    public double getDistance() {
        return distance;
    }

    public Color getColor() {
        return color;
    }
}
