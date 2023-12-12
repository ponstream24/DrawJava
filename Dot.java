/**
 * Yuki Tetsuka
 * <p>
 * Project: DrawJava
 * Description: A simple drawing application in Java.
 * <p>
 * Copyright (c) 2023 Yuki Tetsuka. All rights reserved.
 * See the project repository at: https://github.com/ponstream24/DrawJava
 */

package enshuReport2_2023;

import java.awt.*;

public class Dot extends Figure {

    public Dot() {
        this.isFill = true;
    }

    @Override
    public void paint(Graphics g) {

        g.setColor(this.color);
        if (this.isFill) {
            g.fillOval(x - w / 2, y - h / 2, w, h);
        } else {
            g.drawOval(x - w / 2, y - h / 2, w, h);
        }
    }

    @Override
    public void paintLine(Graphics g, int x, int y) {

        g.setColor(this.color);
        if (this.isFill) {
            g.fillOval(x - w / 2, y - h / 2, w, h);
        } else {
            g.drawOval(x - w / 2, y - h / 2, w, h);
        }
    }

    @Override
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    @Override
    public Box clone() {

        Box box = new Box();

        box.color = this.color;
        box.isFill = this.isFill;
        box.x = this.x;
        box.y = this.y;
        box.w = this.w;
        box.h = this.h;
        return box;
    }
}
