package eliascregard.game;

import eliascregard.main.PixelGrid;

import java.awt.*;

public class Map {

    private final Dimension size;
    private final int[][] map;
    private final PixelGrid pixelGrid;

    public Map(int[][] map) {
        this.size = new Dimension(map.length, map[0].length);
        this.map = map;
        this.pixelGrid = new PixelGrid(size.width, size.height);
        correctPixelGrid();
    }

    public Map(int width, int height) {
        this.size = new Dimension(width, height);
        this.map = new int[width][height];
        this.pixelGrid = new PixelGrid(width, height);
    }

    public int[][] getMap() {
        return this.map;
    }

    public int get(int x, int y) {
        return this.map[x][y];
    }

    public int get(Point p) {
        return get(p.x, p.y);
    }

    public boolean isWall(int x, int y) {
        if (x < 0 || x >= size.width || y < 0 || y >= size.height) {
            return false;
        }
        return map[x][y] >= 1;
    }
    public boolean isWall(Point p) {
        return isWall(p.x, p.y);
    }

    public void set(int x, int y, int value) {
        this.map[x][y] = value;
    }

    public int getWidth() {
        return this.size.width;
    }

    public int getHeight() {
        return this.size.height;
    }

    private void correctPixelGrid() {
        for (int x = 0; x < size.width; x++) {
            for (int y = 0; y < size.height; y++) {
                if (isWall(x, y)) {
                    pixelGrid.set(x, y, new Color(255, 255, 255, 255));
                }
            }
        }
    }

    public void draw(Graphics2D g2, int x, int y, int width, int height, double scale) {
        pixelGrid.draw(g2, x, y, width, height, scale);
        for (int i = 1; i < size.width; i++) {
            g2.setColor(new Color(0, 0, 0));
            g2.drawLine((int) ((x + i * width / size.width) * scale), (int) (y * scale),
                    (int) ((x + i * width / size.width) * scale), (int) ((y + height) * scale));
        }
        for (int i = 1; i < size.height; i++) {
            g2.setColor(new Color(0, 0, 0));
            g2.drawLine((int) (x * scale), (int) ((y + i * height / size.height) * scale), (int) ((x + width) * scale),
                    (int) ((y + i * height / size.height) * scale));
        }
    }


}
