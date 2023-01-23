package eliascregard.physics;

public class Collisions {

    public static boolean isColliding(Circle circle1, Circle circle2) {
        double distance = Vector2D.distance(circle1.position, circle2.position);
        return distance < (circle1.radius + circle2.radius);
    }

    public static boolean isColliding(Line line, Vector2D point, double buffer) {
        double d1 = point.distance(line.point1);
        double d2 = point.distance(line.point2);
        double lineLength = line.length();
        return d1 + d2 >= lineLength - buffer && d1 + d2 <= lineLength + buffer;
    }

    public static boolean isColliding(Line line, Vector2D point) {
        return isColliding(line, point, 0.1);
    }

    public static boolean isColliding(Line line1, Line line2) {
        Vector2D p1 = line1.point1;
        Vector2D p2 = line1.point2;
        Vector2D p3 = line2.point1;
        Vector2D p4 = line2.point2;

        double denominator = (p1.x - p2.x) * (p3.y - p4.y) - (p1.y - p2.y) * (p3.x - p4.x);
        if (denominator == 0) {
            return false;
        }
        double t = ((p1.x - p3.x) * (p3.y - p4.y) - (p1.y - p3.y) * (p3.x - p4.x)) / denominator;
        double u = ((p1.x - p2.x) * (p1.y - p2.y) - (p1.y - p3.y) * (p1.x - p2.x)) / denominator;
        return t >= 0 && t <= 1 && u >= 0 && u <= 1;
    }

    public static boolean isColliding(Line line, Circle circle) {
        Vector2D closestPoint = Line.closestPointOnLineToPoint(line, circle.position);
        return circle.position.distance(closestPoint) < circle.radius;
    }

}
