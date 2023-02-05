package eliascregard.main;

import eliascregard.game.*;
import eliascregard.input.*;
import eliascregard.physics.*;
import static eliascregard.main.Settings.*;

import javax.swing.*;
import java.awt.*;


import static java.lang.Math.PI;


public class GamePanel extends JPanel implements Runnable {
    private int MAX_FRAME_RATE = FPS;
    private Thread gameThread;
    int ticks = 0;
    private final GameTime time = new GameTime();
    private final KeyHandler keys = new KeyHandler();
    private final MouseButtonHandler mouseButtons = new MouseButtonHandler();
    private final MouseMovementHandler mouseMovement = new MouseMovementHandler();
    private double deltaTime;
    private int tickSpeed;
    private double renderDeltaT = 0;
    private int fps;

    private final Game game = new Game(
            new Map(new int[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                {1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 1},
                {1, 0, 2, 0, 0, 0, 0, 2, 0, 0, 0, 1},
                {1, 2, 2, 2, 0, 2, 2, 2, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    }));

    private void sleep(int nanoseconds) {
        try {
            Thread.sleep((long) GameTime.nanoSecondsToMilliSeconds(nanoseconds), nanoseconds % 1_000_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public GamePanel() {
        this.setPreferredSize(SCREEN_SIZE);
        this.setBackground(new Color(0, 0, 0));
        this.setDoubleBuffered(true);
        this.addKeyListener(keys);
        this.addMouseListener(mouseButtons);
        this.addMouseMotionListener(mouseMovement);
        this.setFocusable(true);
    }


    @Override
    public void run() {

        while (gameThread != null) {
            deltaTime = time.getDeltaTime();
            if (ticks == 0) {
                deltaTime = 0;
            }
            tickSpeed = time.getFPS(deltaTime);
            renderDeltaT += deltaTime;
            if (MAX_FRAME_RATE == 0) {
                fps = time.getFPS(deltaTime);
            }

            if (keys.escapePressed) {
                System.exit(0);
            }
            game.update(deltaTime, keys);
            ticks++;
            if (MAX_FRAME_RATE > 0 && renderDeltaT >= 1.0 / MAX_FRAME_RATE) {
                fps = time.getFPS(renderDeltaT);
                repaint();
                renderDeltaT -= 1.0 / MAX_FRAME_RATE;
            } else {
                repaint();
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(new Color(54, 54, 54));
        g2.fillRect(0, 0, SCREEN_SIZE.width, SCREEN_SIZE.height);

        game.draw(g2);

        g2.dispose();
        System.out.println(fps);
    }
}