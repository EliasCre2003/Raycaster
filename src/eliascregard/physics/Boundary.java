package eliascregard.physics;

public class Boundary extends StaticBody {
    Vector2D position;
    double width;
    double height;
    double thickness;

    public Boundary(Vector2D position, double width, double height, double thickness, double restitution, double friction) {
        super(new Vector2D[] {
                new Vector2D(position.x + width / 2, position.y - thickness),
                new Vector2D(position.x + width + thickness, position.y - thickness),
                new Vector2D(position.x + width + thickness, position.y + height + thickness),
                new Vector2D(position.x - thickness, position.y + height + thickness),
                new Vector2D(position.x - thickness, position.y - thickness),
                new Vector2D(position.x + width / 2, position.y - thickness),
                new Vector2D(position.x +  width / 2, position.y),
                new Vector2D(position.x, position.y),
                new Vector2D(position.x, height),
                new Vector2D(position.x + width, height),
                new Vector2D(position.x + width, position.y),
                new Vector2D(position.x + width / 2, position.y)
        }, restitution, friction);
        this.position = position;
        this.width = width;
        this.height = height;
        this.thickness = thickness;
    }

}
