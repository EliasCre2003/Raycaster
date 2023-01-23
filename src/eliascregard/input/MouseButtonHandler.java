package eliascregard.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseButtonHandler extends MouseAdapter {

    public boolean leftIsPressed = false;
    public boolean rightIsPressed = false;

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftIsPressed = true;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightIsPressed = true;
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftIsPressed = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightIsPressed = false;
        }
    }

}
