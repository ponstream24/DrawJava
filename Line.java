/**
 * Yuki Tetsuka
 *
 * Project: DrawJava
 * Description: A simple drawing application in Java.
 *
 * Copyright (c) 2023 Yuki Tetsuka. All rights reserved.
 * See the project repository at: https://github.com/ponstream24/DrawJava
 */

package enshuReport2_2023;

import java.awt.*;

public class Line extends Figure {

	public Line(int width) {
		this.width = width;
		this.color = Color.BLACK;
		this.isFill = false;
	}

	public Line() {

//		初期値を白。10にする。
		this.color = Color.BLACK;
		this.isFill = false;
	}

	@Override
	public void paint(Graphics g) {

		g.setColor(this.color);

//		g.drawLine(x, y,x+w, y+h);

		int[] xp = new int[2];
		int[] yp = new int[2];

		xp[0] = x;
		yp[0] = y;

		xp[1] = x+w;
		yp[1] = y+h;

		Graphics2D g2 = (Graphics2D) g;
		BasicStroke bs = new BasicStroke(this.width);
		g2.setStroke(bs);
		g2.drawPolyline(xp, yp, 2);
	}

	@Override
	public void paintLine(Graphics g, int x, int y) {
		
		g.setColor(this.color);

		g.drawLine(x, y,x+w, y+h);
	}

	@Override
	public void move(int dx, int dy) {
		x += dx;
		y += dy;
	}

	@Override
	public Line clone() {

		Line line = new Line();

		line.width = this.width;
		line.color = this.color;
		line.isFill = this.isFill;
		line.x = this.x;
		line.y = this.y;
		line.w = this.w;
		line.h = this.h;
		return line;
	}
}
