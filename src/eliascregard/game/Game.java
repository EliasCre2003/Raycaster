package eliascregard.game;

import eliascregard.input.KeyHandler;
import eliascregard.main.ObjectRenderer;

import java.awt.*;
import static eliascregard.main.Settings.*;

public class Game {

    private final Map map;
    private final Player player;
    private final RayCasting rayCasting;
    private final ObjectRenderer objectRenderer;

    public Game(Map map) {
        this.map = map;
        this.player = new Player(this);
        this.objectRenderer = new ObjectRenderer(this);
        this.rayCasting = new RayCasting(this);
    }

    public void update(double deltaTime, KeyHandler input) {
        player.update(deltaTime, input);
        rayCasting.update();
    }

    public Map getMap() {
        return map;
    }
    public Player getPlayer() {
        return player;
    }
    public Ray[] getRayCastingResults() {
        return rayCasting.getRayCastingResults();
    }
    public ObjectRenderer getObjectRenderer() {
        return objectRenderer;
    }
    public RayCasting getRayCasting() {
        return rayCasting;
    }

    public void draw(Graphics2D g2) {
//        double[] heights = new double[NUM_RAYS];
//        for (int i = 0; i < NUM_RAYS; i++) {
//            heights[i] = rayCasting.getRay(i).projectionHeight();
//        }
//        for (int i = 0; i < NUM_RAYS; i++) {
//            g2.setColor(new Color(255, 255, 255));
//            g2.fillRect(i * SCALE, (int) (HALF_HEIGHT - heights[i] / 2), SCALE, (int) heights[i]);
//        }
        objectRenderer.draw(g2);
    }
}
