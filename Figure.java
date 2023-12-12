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

public class Figure extends Coord {

    int w, h, width = 1;
    boolean isFill;

    public Figure() {
        color = Color.BLACK;
        w = h = 0;
    }

    public void setWH(int w, int h) {
        this.w = w;
        this.h = h;
    }

    public void paintCursor(Graphics g) {

        g.setColor(this.color);

        if (this.isFill) {
            g.fillOval(x - w / 2, y - h / 2, w, h);
        } else {
            g.drawOval(x - w / 2, y - h / 2, w, h);
        }
    }

	@Override
    public Figure clone() {

		Figure figure = new Figure();
        figure.w = this.w;
        figure.h = this.h;
        return figure;
    }
}
