package eliascregard.main;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Main {
    public static GamePanel gamePanel;
    public static JFrame window;
    public static Dimension SCREEN_SIZE;

    public static void main(String[] args) {
        boolean maximized = false;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        try {
            if (args.length > 2) {
                maximized = Boolean.parseBoolean(args[2]);
                if (maximized) {
                    SCREEN_SIZE = toolkit.getScreenSize();
                } else {
                    SCREEN_SIZE = new Dimension(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
                }
            } else {
                SCREEN_SIZE = new Dimension(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            }
        } catch (Exception e) {
            SCREEN_SIZE = new Dimension(1600, 900);
        }
        System.out.println("Window size: " + SCREEN_SIZE.width + " x " + SCREEN_SIZE.height);

        window = new JFrame("Raycaster");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setUndecorated(maximized);

        gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();
        window.setLocation(0, 0);
        window.setVisible(true);
        gamePanel.startGameThread();
        System.out.println("Game started");
    }
}