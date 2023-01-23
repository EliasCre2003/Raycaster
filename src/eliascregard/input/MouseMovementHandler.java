package eliascregard.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MouseMovementHandler extends MouseMotionAdapter {

    public int x = 0, y = 0;
    public double scale;

    public MouseMovementHandler(double scale) {
        this.scale = scale;
    }
    public MouseMovementHandler() {
        this(1);
    }

    public void mouseMoved(MouseEvent e) {
        x = (int) (e.getX() / scale);
        y = (int) (e.getY() / scale);
    }

    public void mouseDragged(MouseEvent e) {
        x = (int) (e.getX() / scale);
        y = (int) (e.getY() / scale);
    }

}
