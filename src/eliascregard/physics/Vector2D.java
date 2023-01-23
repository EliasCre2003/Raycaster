package eliascregard.physics;

public class Vector2D {

    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D() {
        this(0, 0);
    }

    public void set(Vector2D vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D makeCopy() {
        return new Vector2D(this.x, this.y);
    }

    public void scale(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    public Vector2D scaled(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    public void negate() {
        this.scale(-1);
    }
    public Vector2D negated() {
        return this.scaled(-1);
    }

    public void add(Vector2D otherVector, double scalar) {
        this.x += otherVector.x * scalar;
        this.y += otherVector.y * scalar;
    }

    public void add(Vector2D otherVector) {
        this.add(otherVector, 1);
    }

    public Vector2D sum(Vector2D otherVector) {
        Vector2D newVector = this.makeCopy();
        newVector.add(otherVector, 1);
        return newVector;
    }

    public void subtract(Vector2D otherVector, double scalar) {
        this.x -= otherVector.x * scalar;
        this.y -= otherVector.y * scalar;
    }

    public void subtract(Vector2D otherVector) {
        this.subtract(otherVector, 1);
    }

    public static Vector2D difference(Vector2D vector1, Vector2D vector2) {
        Vector2D newVector = new Vector2D(vector1.x, vector1.y);
        newVector.subtract(vector2);
        return newVector;
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vector2D normalized() {
        if (this.x == 0 && this.y == 0) {
            return this;
        }
        double length = this.length();
        return new Vector2D(this.x / length, this.y / length);
    }
    public void normalize() {
        this.set(this.normalized());
    }

    public void setDirection(double angle) {
        this.set(angleToVector(angle, this.length()));
    }
    public double getDirection() {
        return vectorToAngle(this);
    }
    public void rotate(double angle) {
        double newAngle = angle + this.getDirection();
        this.setDirection(newAngle);
    }

    public double dotProduct(Vector2D otherVector) {
        return this.x * otherVector.x + this.y * otherVector.y;
    }

    public double crossProduct(Vector2D otherVector) {
        return this.x * otherVector.y - this.y * otherVector.x;
    }

    public static double vectorToAngle(Vector2D vector) {
        return Math.atan2(vector.y, vector.x);
    }

    public static Vector2D angleToVector(double angle, double length) {
        Vector2D vector = new Vector2D(Math.cos(angle), Math.sin(angle));
        return vector.scaled(length);
    }
    public static Vector2D angleToVector(double angle) {
        return angleToVector(angle, 1);
    }

    public static double distance(Vector2D vector1, Vector2D vector2) {
        return difference(vector1, vector2).length();
    }
    public double distance(Vector2D vector2) {
        return distance(this, vector2);
    }

    public boolean equals(Vector2D otherVector) {
        System.out.println("HI");
        return this.x == otherVector.x && this.y == otherVector.y;
    }
    public String toString() {
        return "x: " + this.x + ", y: " + this.y;
    }
}
