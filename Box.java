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

import java.awt.Color;
import java.awt.Graphics;

public class Box extends Figure {
	
	boolean isEraser = false;
	
	public Box(boolean isEraser) {
		
//		初期値を白。10にする。
		this.color = Color.BLACK;
		this.isFill = false;
		this.isEraser = isEraser;
	}

	public Box() {

//		初期値を白。10にする。
		this.color = Color.BLACK;
		this.isFill = false;
	}

	@Override
	public void paint(Graphics g) {
		
		if( isEraser ) {
			g.setColor(Color.WHITE);
		}
		else {
			g.setColor(this.color);
		}
		
		if (this.isFill) {

			if (w >= 0 && h >= 0) {
				g.fillRect(x, y, w, h);
			}
			else if( w < 0 && h >= 0 ) {
				g.fillRect(x + w, y, -w, h);
			}
			else if( w >= 0 && h < 0 ) {
				g.fillRect(x, y + h, w, -h);
			}
			else  {
				g.fillRect(x + w, y + h, -w, -h);
			}
		} else {

			if (w >= 0 && h >= 0) {
				g.drawRect(x, y, w, h);
			}
			else if( w < 0 && h >= 0 ) {
				g.drawRect(x + w, y, -w, h);
			}
			else if( w >= 0 && h < 0 ) {
				g.drawRect(x, y + h, w, -h);
			}
			else  {
				g.drawRect(x + w, y + h, -w, -h);
			}
		}
		
//		
//
//		if (this.isFill) {
//			g.fillRect(x - w / 2, y - h / 2, w, h);
//		} else {
//			g.fillRect(x - w / 2, y - h / 2, w, h);
//
//		}
	}
	
	public static double minami(int x, int y) {
		
		return x / y;
	}

	@Override
	public void paintLine(Graphics g, int x, int y) {

		if( isEraser ) {
			g.setColor(Color.WHITE);
		}
		else {
			g.setColor(this.color);
		}
		
//		g.fillRect(x - size / 2, y - size / 2, size, size);
//		g.fillRect(x, y, w, h);
		g.fillRect(x - w / 2, y - h / 2, w, h);
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
		box.isEraser = this.isEraser;
		box.isFill = this.isFill;
		box.x = this.x;
		box.y = this.y;
		box.color = this.color;
		box.w = this.w;
		box.h = this.h;
		return box;
	}
}
