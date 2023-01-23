package eliascregard.physics;

public class Line {
    public Vector2D point1;
    public Vector2D point2;

    public Line(Vector2D point1, Vector2D point2) {
        this.point1 = point1;
        this.point2 = point2;
    }
    public Line(double x1, double y1, double x2, double y2) {
        this.point1 = new Vector2D(x1, y1);
        this.point2 = new Vector2D(x2, y2);
    }
    public double length() {
        return this.point1.distance(this.point2);
    }

    public static Vector2D intersection(Line line1, Line line2) {
        Vector2D s1 = Vector2D.difference(line1.point2, line1.point1);
        Vector2D s2 = Vector2D.difference(line2.point2, line2.point1);
        double denominator = (-s2.x * s1.y + s1.x * s2.y);
        if (denominator == 0) return null;
        double s = (-s1.y * (line1.point1.x - line2.point1.x) + s1.x * (line1.point1.y - line2.point1.y)) / denominator;
        double t = ( s2.x * (line1.point1.y - line2.point1.y) - s2.y * (line1.point1.x - line2.point1.x)) / denominator;
        if (s >= 0 && s<= 1 && t >= 0 && t <= 1) {
            return new Vector2D(line1.point1.x + (t * s1.x), line1.point1.y + (t * s1.y));
        }
        return null;
    }

    public static Vector2D lineLineIntersection(Line line1, Line line2) {
        double x1 = line1.point1.x, y1 = line1.point1.y;
        double x2 = line1.point2.x, y2 = line1.point2.y;
        double x3 = line2.point1.x, y3 = line2.point1.y;
        double x4 = line2.point2.x, y4 = line2.point2.y;

        double denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (denominator == 0) {
            return null;
        }
        double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / denominator;
        double u = ((x1 - x2) * (y1 - y2) - (y1 - y3) * (x1 - x2)) / denominator;
        if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
            return new Vector2D(x1 + t * (x2 - x1), y1 + t * (y2 - y1));
        }
        return null;
    }
    public static Vector2D lineLineIntersection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        return Line.lineLineIntersection(new Line(x1, y1, x2, y2), new Line(x3, y3, x4, y4));
    }
    public static Vector2D lineLineIntersection(Vector2D point1, Vector2D point2, Vector2D point3, Vector2D point4) {
        return Line.lineLineIntersection(new Line(point1, point2), new Line(point3, point4));
    }
    public Vector2D lineLineIntersection(Line otherLine) {
        return Line.lineLineIntersection(this, otherLine);
    }

    public static boolean linePoint (Line line, Vector2D point) {
        double d1 = point.distance(line.point1);
        double d2 = point.distance(line.point2);
        double lineLength = line.length();
        double buffer = 0.1;
        return d1 + d2 >= lineLength - buffer && d1 + d2 <= lineLength + buffer;
    }

    public static Vector2D closestPointOnLineToPoint(Line line, Vector2D point) {
        Vector2D P = line.point1;
        Vector2D Q = line.point2;
        Vector2D v = Vector2D.difference(Q, P);
        double lambdaS = v.dotProduct(Vector2D.difference(point, P)) / v.dotProduct(v);
        if (lambdaS <= 0) {
            return P.makeCopy();
        }
        if (lambdaS >= 1) {
            return Q.makeCopy();
        }
        return P.sum(v.scaled(lambdaS));
    }

}
