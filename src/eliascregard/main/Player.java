package eliascregard.main;

import eliascregard.physics.Line;
import eliascregard.physics.StaticBody;
import eliascregard.physics.Vector2D;

import java.awt.*;

public class Player {
    private final Vector2D position;
    private double angle;
    private double speed = 50;

    public Player(Vector2D position, double speed) {
        this.position = position;
        this.speed = speed;
    }
    public Player(double x, double y, double speed) {
        this(new Vector2D(x, y), speed);
    }

    public Vector2D getPosition() {
        return position;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void move(double direction, double deltaTime) {
        if (direction == 0) return;
        boolean backwards = direction < 0;
        double finalDirection = backwards ? angle + Math.PI : angle;
        Vector2D directionVector = Vector2D.angleToVector(finalDirection);
        position.add(directionVector.scaled(speed * deltaTime));
    }

    public void collide(StaticBody body) {
        Line[] lines = body.getLines();
        Line ray = new Line(position, new Vector2D(position.x + Integer.MAX_VALUE, position.y));
        int intersections = 0;
        Vector2D[] closestPoints = new Vector2D[lines.length];
        for (int i = 0; i < lines.length; i++) {
            Vector2D intersectionPoint = Line.lineLineIntersection(ray, lines[i]);
            if (intersectionPoint != null) {
                intersections++;
            }
            closestPoints[i] = Line.closestPointOnLineToPoint(lines[i], position);
        }
        if (intersections % 2 == 0) return;
        Vector2D closestPoint = closestPoints[0];
        double closestDistance = Vector2D.distance(position, closestPoint);
        for (int i = 1; i < closestPoints.length; i++) {
            double distance = Vector2D.distance(position, closestPoints[i]);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestPoint = closestPoints[i];
            }
        }
        Vector2D direction = Vector2D.difference(position, closestPoint);
        if (direction.length() == 0) {
            return;
        }
        position.set(closestPoint);

    }

    public void draw(Graphics2D g2, double scale) {
        g2.setColor(new Color(255, 255, 0));
        g2.fillRect((int) ((position.x - 10) * scale), (int) ((position.y - 10) * scale), (int) (20 * scale), (int) (20 * scale));
        g2.setStroke(new BasicStroke(3 * (float) scale));
        g2.drawLine(
                (int) (position.x * scale), (int) (position.y * scale),
                (int) ((position.x + 40 * Math.cos(angle)) * scale), (int) ((position.y + 40 * Math.sin(angle)) * scale)
        );

    }
}
