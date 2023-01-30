package eliascregard.main;

import eliascregard.input.*;
import eliascregard.physics.*;

import javax.swing.*;
import java.awt.*;


import static java.lang.Math.PI;


public class GamePanel extends JPanel implements Runnable {
    private final Dimension SCREEN_SIZE = Main.SCREEN_SIZE;
    private final Dimension DEFAULT_SCREEN_SIZE = new Dimension(1600, 900);
    private double SCREEN_SCALE = (double) SCREEN_SIZE.width / DEFAULT_SCREEN_SIZE.width;
    private int MAX_FRAME_RATE = 200;
    private Thread gameThread;
    int ticks = 0;
    private final GameTime time = new GameTime();
    private final KeyHandler keys = new KeyHandler();
    private final MouseButtonHandler mouseButtons = new MouseButtonHandler();
    private final MouseMovementHandler mouseMovement = new MouseMovementHandler(SCREEN_SCALE);
    private double deltaTime;
    private int tickSpeed;
    private double renderDeltaT = 0;
    private int fps;
    private final Player player = new Player(300, 300, 200);
    private final Map map = new Map(new int[][]{
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    });

    private final Cell[][] cellStatics = new Cell[map.getWidth()][map.getHeight()];
    private final double cellSize = (double) DEFAULT_SCREEN_SIZE.height / map.getHeight();
    private final double fov = Math.toRadians(90);
    private final int rayCount = DEFAULT_SCREEN_SIZE.width;
    private final double rayLength = 1000;
    private double[] distances = new double[rayCount];
    private Ray[] rays = new Ray[rayCount];

    private void castRays() {
        double angle = player.getAngle() - fov / 2.0;
        double angleIncrement = fov / (double) rayCount;
        double[] newDistances = new double[rayCount];
        Ray[] newRays = new Ray[rayCount];
        for (int i = 0; i < rayCount; i++) {
            Line ray = getRay(angle);
            Color color = new Color(0, 0, 0, 0);
            double shortestDistance = rayLength;
            for (int x = 0; x < map.getWidth(); x++) {
                for (int y = 0; y < map.getHeight(); y++) {
                    if (!map.get(x, y)) continue;
                    Color cellColor = cellStatics[x][y].getColor();
                    Line[] cellLines = cellStatics[x][y].getLines();
                    for (Line line : cellLines) {
                        Vector2D intersection = Line.intersection(ray, line);
                        if (intersection != null) {
                            double distance = player.getPosition().distance(intersection);
                            if (distance > ray.length()) continue;
                            if (distance < shortestDistance) {
                                shortestDistance = distance;
                                color = cellColor;
                            }
                        }
                    }
                }
            }
            newDistances[i] = shortestDistance;
            newRays[i] = new Ray(shortestDistance * Math.cos(player.getAngle() - angle), color);
            angle += angleIncrement;
        }
        distances = newDistances;
        rays = newRays;
    }

    private Line getRay(double angle) {
        return new Line(
                player.getPosition().x, player.getPosition().y,
                player.getPosition().x + rayLength * Math.cos(angle),
                player.getPosition().y + rayLength * Math.sin(angle)
        );
    }

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

        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                if (map.get(i, j)) {
                    Color color = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
                    cellStatics[i][j] = new Cell(i * cellSize, j * cellSize, cellSize, color);
                }
            }
        }

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
            update();
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

    public void update() {

        int direction = 0;
        double angle = player.getAngle();
        if (keys.wPressed) {
            direction += 1;
        }
        if (keys.sPressed) {
            direction -= 1;
        }
        if (keys.aPressed) {
            angle -= PI * deltaTime;
        }
        if (keys.dPressed) {
            angle += PI * deltaTime;
        }
        castRays();
        for (Cell[] cellRow : cellStatics) {
            for (Cell cell : cellRow) {
                if (cell != null) {
                    player.collide(cell);
                }
            }
        }
        player.setAngle(angle);
        player.move(direction, deltaTime);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(new Color(54, 54, 54));
        g2.fillRect(0, 0, SCREEN_SIZE.width, SCREEN_SIZE.height);


        // Right-panel
        double center = DEFAULT_SCREEN_SIZE.height / 2.0;
        double thickness = (double) DEFAULT_SCREEN_SIZE.width / rayCount;
        for (int i = 0; i < rayCount; i++) {
            Ray ray = rays[i];
            if (ray == null) continue;
            double distance = ray.getDistance();
            double height = rayLength * 90 / distance;
            Color color = ray.getColor();
//            double colorConstant = (rayLength - distance) / rayLength;
//            if (colorConstant < 0) colorConstant = 0;
//            if (colorConstant > 1) colorConstant = 1;
//            color = new Color(
//                    (int) (color.getRed() * colorConstant),
//                    (int) (color.getGreen() * colorConstant),
//                    (int) (color.getBlue() * colorConstant)
//            );
            g2.setColor(color);
            g2.fillRect((int) ((i * thickness) * SCREEN_SCALE),
                    (int) ((center - height / 2) * SCREEN_SCALE),
                    (int) Math.ceil(thickness * SCREEN_SCALE), (int) (height * SCREEN_SCALE)
            );
        }

        // Map
        map.draw(g2, 0, 0, 1000,1000, SCREEN_SCALE / (10 + 10/10));
        double angle = player.getAngle() - fov / 2.0;
        double angleIncrement = fov / (double) rayCount;
        g2.setColor(new Color(255, 255, 0));
        g2.setStroke(new BasicStroke((float) (1 * SCREEN_SCALE)));
        for (int i = 0; i < rayCount; i++) {
            Line ray = new Line(
                    player.getPosition().x / 10, player.getPosition().y / 10,
                    (player.getPosition().x + distances[i] * Math.cos(angle)) / 10,
                    (player.getPosition().y + distances[i] * Math.sin(angle)) / 10
            );
            g2.drawLine((int) (ray.point1.x * SCREEN_SCALE), (int) (ray.point1.y * SCREEN_SCALE),
                    (int) (ray.point2.x * SCREEN_SCALE), (int) (ray.point2.y * SCREEN_SCALE));
            angle += angleIncrement;
        }
        player.draw(g2, SCREEN_SCALE / 10.0);

        g2.dispose();
    }
}