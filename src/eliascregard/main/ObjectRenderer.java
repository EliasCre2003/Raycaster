package eliascregard.main;

import eliascregard.game.Game;
import eliascregard.game.GameObject;

import static eliascregard.main.Settings.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ObjectRenderer {

    private final Game game;
    private final HashMap<String, BufferedImage> wallTextures;


    public ObjectRenderer(Game game) {
        this.game = game;
        wallTextures = loadWallTextures();
    }

    public void draw(Graphics2D g2) {
        renderGameObjects(g2);
    }

    private void renderGameObjects(Graphics2D g2) {
        for (GameObject gameObject : game.getRayCasting().getObjectsToRender()) {
            BufferedImage wallColumn = gameObject.wallColumn();
            if (wallColumn != null) {
                g2.drawImage(wallColumn, (int) gameObject.position().x, (int) gameObject.position().y, null);
            }
        }
    }

    public static BufferedImage getTexture(String path, Dimension resolution) {
        BufferedImage image;
        Dimension size;
        try {
            image = ImageIO.read(new File(path));
            size = new Dimension(image.getWidth(), image.getHeight());
        } catch (IOException e) {
            throw new IllegalArgumentException("could not read image: " + path, e);
        }
        if (resolution.width == size.width && resolution.height == size.height) {
            return image;
        }
        BufferedImage resizedImage = new BufferedImage(resolution.width, resolution.height, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale((double) resolution.width / size.width, (double) resolution.height / size.height);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        return scaleOp.filter(image, resizedImage);

    }
    public static BufferedImage getTexture(String path) {
        return getTexture(path, new Dimension(TEXTURE_SIZE, TEXTURE_SIZE));
    }

    public HashMap<String, BufferedImage> getWallTextures() {
        return wallTextures;
    }

    private static HashMap<String, BufferedImage> loadWallTextures() {
        HashMap<String, BufferedImage> textures = new HashMap<>();
        textures.put("1", getTexture("src/eliascregard/resources/textures/1.png"));
        textures.put("2", getTexture("src/eliascregard/resources/textures/2.png"));
        textures.put("3", getTexture("src/eliascregard/resources/textures/3.png"));
        textures.put("4", getTexture("src/eliascregard/resources/textures/4.png"));
        textures.put("5", getTexture("src/eliascregard/resources/textures/5.png"));
        return textures;
    }

}
