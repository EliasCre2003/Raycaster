package eliascregard.game;

import eliascregard.physics.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static eliascregard.main.Settings.*;

public class RayCasting {

    private final Game game;
    private Ray[] rayCastingResults;
    private GameObject[] objectsToRender;
    private final HashMap<String, BufferedImage> textures;

    public RayCasting(Game game) {
        this.game = game;
        this.rayCastingResults = new Ray[NUM_RAYS];
        this.objectsToRender = new GameObject[NUM_RAYS];
        this.textures = this.game.getObjectRenderer().getWallTextures();
    }

    private void updateObjectsToRender() {
        for (int i = 0; i < NUM_RAYS; i++) {
            Ray ray = rayCastingResults[i];
            double depth = ray.depth();
            double projectionHeight = ray.projectionHeight();
            int texture = ray.texture();
            double offset = ray.offset();

            BufferedImage wallColumn;
            Vector2D wallPosition;
            if (projectionHeight < SCREEN_SIZE.height){
                wallColumn = textures.get(String.valueOf(texture)).getSubimage(
                        (int) Math.ceil(offset * (TEXTURE_SIZE - SCALE)), 0 , SCALE, TEXTURE_SIZE
                );
                BufferedImage resizedImage = new BufferedImage(SCALE, (int) projectionHeight, BufferedImage.TYPE_INT_ARGB);
                AffineTransform at = new AffineTransform();
                at.scale((double) SCALE / wallColumn.getWidth(), projectionHeight / wallColumn.getHeight());
                AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                wallColumn = scaleOp.filter(wallColumn, resizedImage);
                wallPosition = new Vector2D(i * SCALE, HALF_HEIGHT - projectionHeight / 2);
            } else {
                double textureHeight = TEXTURE_SIZE * SCREEN_SIZE.height / projectionHeight;
                textureHeight = Math.min(Math.max(textureHeight, 1), TEXTURE_SIZE-1);
                wallColumn = textures.get(String.valueOf(texture)).getSubimage(
                        (int) Math.ceil(offset * (TEXTURE_SIZE - SCALE)),
                        (int) Math.ceil(HALF_TEXTURE_SIZE - textureHeight / 2),
                        SCALE, (int) Math.ceil(textureHeight)
                );
                BufferedImage resizedImage = new BufferedImage(SCALE, SCREEN_SIZE.height, BufferedImage.TYPE_INT_ARGB);
                AffineTransform at = new AffineTransform();
                at.scale((double) SCALE / wallColumn.getWidth(),  (double) SCREEN_SIZE.height / wallColumn.getHeight());
                AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                wallColumn = scaleOp.filter(wallColumn, resizedImage);
                wallPosition = new Vector2D(i * SCALE, 0);

            }
            objectsToRender[i] = new GameObject(depth, wallColumn, wallPosition);

        }
    }

    private void castRays() {
        Ray[] newRayCastingResults = new Ray[NUM_RAYS];
        Vector2D playerPosition = game.getPlayer().getPosition();
        Point playerMapPosition = game.getPlayer().getMapPosition();

        int textureV = 1, textureH = 1;

        double rayAngle = game.getPlayer().getAngle() - HALF_FOV + 0.0001;
        for (int ray = 0; ray < NUM_RAYS; ray++) {
            double sin_a = Math.sin(rayAngle), cos_a = Math.cos(rayAngle);

            Vector2D delta = new Vector2D();

            // horizontals
            Vector2D horizontal = new Vector2D();
            if (sin_a > 0) {
                horizontal.y = playerMapPosition.y + 1;
                delta.y = 1;
            } else {
                horizontal.y = playerMapPosition.y - 1e-6;
                delta.y = -1;
            }
            double depthH = (horizontal.y - playerPosition.y) / sin_a;
            horizontal.x = playerPosition.x + depthH * cos_a;
            double deltaDepthH = delta.y / sin_a;
            delta.x = deltaDepthH * cos_a;
            for (int i = 0; i < MAX_DEPTH; i++) {
                Point tileH = new Point((int) horizontal.x, (int) horizontal.y);
                if (game.getMap().isWall(tileH)) {
                    textureH = game.getMap().get(tileH);
                    break;
                }
                horizontal.add(delta);
                depthH += deltaDepthH;
            }

            // verticals
            Vector2D vertical = new Vector2D();
            if (cos_a > 0) {
                vertical.x = playerMapPosition.x + 1;
                delta.x = 1;
            } else {
                vertical.x = playerMapPosition.x - 1e-6;
                delta.x = -1;
            }
            double depthV = (vertical.x - playerPosition.x) / cos_a;
            vertical.y = playerPosition.y + depthV * sin_a;
            double deltaDepthV = delta.x / cos_a;
            delta.y = deltaDepthV * sin_a;
            for (int i = 0; i < MAX_DEPTH; i++) {
                Point tileV = new Point((int) vertical.x, (int) vertical.y);
                if (game.getMap().isWall(tileV)) {
                    textureV = game.getMap().get(tileV);
                    break;
                }
                vertical.add(delta);
                depthV += deltaDepthV;
            }

            double depth, offset;
            int texture;
            if (depthV < depthH) {
                depth = depthV;
                texture = textureV;
                vertical.y %= 1;
                offset = (cos_a > 0) ? (vertical.y) : (1 - vertical.y);
            } else {
                depth = depthH;
                texture = textureH;
                horizontal.x %= 1;
                offset = (sin_a > 0) ? (1 - horizontal.x) : (horizontal.x);
            }
            depth *= Math.cos(game.getPlayer().getAngle() - rayAngle);



            // projection
            double projectionHeight = SCREEN_DISTANCE / (depth + 0.0001);
            newRayCastingResults[ray] = new Ray(depth, projectionHeight, texture, offset);
            rayAngle += DELTA_ANGLE;
        }
        rayCastingResults = newRayCastingResults;
    }

    public void update() {
        castRays();
        updateObjectsToRender();
    }
    public Ray[] getRayCastingResults() {
        return rayCastingResults;
    }
    public Ray getRay(int index) {
        return rayCastingResults[index];
    }

    public GameObject[] getObjectsToRender() {
        return objectsToRender;
    }

}
