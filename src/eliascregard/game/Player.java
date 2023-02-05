package eliascregard.game;

import eliascregard.input.KeyHandler;
import eliascregard.physics.Vector2D;
import static eliascregard.main.Settings.*;

import java.awt.*;

public class Player {
    private final Vector2D position = PLAYER_START_POSITION;
    private final Point mapPosition = new Point((int) position.x, (int) position.y);
    private double angle = PLAYER_START_ANGLE;
    private final double speed = PLAYER_SPEED;
    private final double rotationSpeed = PLAYER_ROTATION_SPEED;
    private final Game game;

    public Player(Game game) {
        this.game = game;
    }

    public Vector2D getPosition() {
        return position;
    }
    public Point getMapPosition() {
        return mapPosition;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle % Math.TAU;
    }
    public void rotate(double deltaAngle) {
        angle += deltaAngle;
        angle = angle % Math.TAU;
    }

    public void update(double deltaTime, KeyHandler keys) {
        Vector2D deltaPosition = new Vector2D();
        double deltaAngle = 0;
        if (keys.wPressed) {
            deltaPosition.x += 1;
        }
        if (keys.sPressed) {
            deltaPosition.x -= 1;
        }
        if (keys.aPressed) {
            deltaPosition.y -= 1;
        }
        if (keys.dPressed) {
            deltaPosition.y += 1;
        }
        deltaPosition.normalize();
        if (keys.leftPressed) {
            deltaAngle -= rotationSpeed * deltaTime;
        }
        if (keys.rightPressed) {
            deltaAngle += rotationSpeed * deltaTime;
        }
        rotate(deltaAngle);
        move(deltaPosition, deltaTime);
    }

    public void move(Vector2D direction, double deltaTime) {
        direction.rotate(angle);
        Vector2D deltaPosition = direction.scaled(speed * deltaTime);
        position.add(deltaPosition);
        checkWallCollision(deltaPosition.x, deltaPosition.y);
        mapPosition.setLocation((int) position.x, (int) position.y);
    }

    private boolean checkWall(int x, int y) {
        return game.getMap().isWall(x, y);
    }

    private void checkWallCollision(double dx, double dy) {
        if (checkWall((int) (position.x + dx), (int) (position.y))) {
            position.x -= dx;
        }
        if (checkWall((int) (position.x), (int) (position.y + dy))) {
            position.y -= dy;
        }
    }



}
