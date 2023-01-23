package eliascregard.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class PixelGrid {

    private final int width, height;
    private final BufferedImage image;

    public PixelGrid(int width, int height, BufferedImage image) {
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public PixelGrid(int width, int height) {
        this.width = width;
        this.height = height;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public void set(int x, int y, Color color) {
        image.setRGB(x, y, color.getRGB());
    }

    public int get(int x, int y) {
        return image.getRGB(x, y);
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public BufferedImage getImage() {
        return image;
    }

    public void setAlpha(int x, int y ,float alpha) {
        Color color = new Color(get(x, y));
        Color newColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        set(x, y, newColor);
    }

    public void setOpacity(float opacity) {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                setAlpha(x, y, opacity);
            }
        }
    }

    public static PixelGrid fromPath(String path) {
        if (path == null) throw new IllegalArgumentException("constructor argument is null");
        if (path.length() == 0) throw new IllegalArgumentException("constructor argument is the empty string");
        try {
            File file = new File(path);
            BufferedImage image;
            if (file.isFile()) {
                image = ImageIO.read(file);
            } else {
                URL url = PixelGrid.class.getResource(path);
                if (url == null) {
                    PixelGrid.class.getClassLoader().getResource(path);
                }
                if (url == null)  {
                    url = new URL(path);
                }
                image = ImageIO.read(url);
            }
            if (image == null) {
                throw new IllegalArgumentException("could not read image: " + path);
            }
            int width = image.getWidth();
            int height = image.getHeight();
            return new PixelGrid(width, height, image);
        } catch (IOException ioe) {
            throw new IllegalArgumentException("could not read image: " + path, ioe);
        }
    }

    public void saveAs(String path, String fileName) {
        try {
            String pathName;
            if (path == null) {
                pathName = fileName;
            } else {
                pathName = path + "/" + fileName;
            }
            String format;
            int lastDotIndex = fileName.lastIndexOf('.');
            if (lastDotIndex != -1 && lastDotIndex != fileName.length() - 1) {
                format = fileName.substring(lastDotIndex + 1);
            }
            else {
                format = "png";
                if (fileName.endsWith(".")) {
                    pathName += format;
                }
                else {
                    pathName += "." + format;
                }
            }
            File outputFile = new File(pathName);
            System.out.println(format);
            ImageIO.write(this.image, format, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveAs(String fileName) {saveAs(null, fileName);}

    public void draw(Graphics2D g2, int x, int y, int width, int height) {
        g2.drawImage(image, x, y, width, height, null);
    }
    public void draw(Graphics2D g2, int x, int y, double scale) {
        draw(g2, x, y, (int) (width * scale), (int) (height * scale));
    }
    public void draw(Graphics2D g2, int x, int y, int width, int height, double scale) {
        draw(g2, x, y, (int) (width * scale), (int) (height * scale));
    }
}
