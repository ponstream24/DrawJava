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

import java.awt.Color;
import java.awt.Graphics;

public class Dot extends Figure {

    boolean isEraser = false;

    public Dot(boolean isEraser) {

//		初期値を白。10にする。
        this.color = Color.BLACK;
        this.isFill = false;
        this.isEraser = isEraser;
    }
    
	public Dot() {
		this.isFill = true;
	}

	@Override
	public void paint(Graphics g) {
		
        if (isEraser) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(this.color);
        }

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
	public Dot clone() {

		Dot box = new Dot();

		box.color = this.color;
        box.isEraser = this.isEraser;
		box.isFill = this.isFill;
		box.x = this.x;
		box.y = this.y;
		box.w = this.w;
		box.h = this.h;
		return box;
	}
}
