package eliascregard.main;

import eliascregard.physics.Line;
import eliascregard.physics.Vector2D;

import java.awt.*;

public class Cell {
    private final Vector2D position;
    private final double size;
    private final Line[] lines;
    private final Line[] hitLines;
    private Color color;

    public Cell(Vector2D position, double size, Color color) {
        this.position = position;
        this.size = size;
        this.lines = generateLines();
        this.hitLines = generateHitLines();
        this.color = color;
    }

    public Cell(double x, double y, double size, Color color) {
        this(new Vector2D(x, y), size, color);
    }

    public Cell(double x, double y, double size) {
        this(new Vector2D(x, y), size, Color.WHITE);
    }

    private Line[] generateLines() {
        return new Line[]{
                new Line(position, new Vector2D(position.x + size, position.y)),
                new Line(position, new Vector2D(position.x, position.y + size)),
                new Line(new Vector2D(position.x + size, position.y), new Vector2D(position.x + size, position.y + size)),
                new Line(new Vector2D(position.x, position.y + size), new Vector2D(position.x + size, position.y + size))
        };
    }

    private Line[] generateHitLines() {
        return new Line[]{
                new Line(new Vector2D(position.x-1, position.y-1), new Vector2D(position.x + size + 1, position.y - 1)),
                new Line(new Vector2D(position.x-1, position.y-1), new Vector2D(position.x - 1, position.y + size + 1)),
                new Line(new Vector2D(position.x + size + 1, position.y - 1), new Vector2D(position.x + size + 1, position.y + size + 1)),
                new Line(new Vector2D(position.x - 1, position.y + size + 1), new Vector2D(position.x + size + 1, position.y + size + 1))
        };
    }

    public Vector2D getPosition() {
        return position;
    }

    public double getSize() {
        return size;
    }

    public Line[] getLines() {
        return lines;
    }

    public Line[] getHitLines() {
        return hitLines;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
