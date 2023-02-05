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