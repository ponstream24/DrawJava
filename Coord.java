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
import java.io.Serializable;

public class Coord implements Serializable {

	int x, y;
	Color color = Color.BLACK;

	public Coord() {
		this.x = this.y = 0;
	}

	public void move(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}

	public void moveto(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void paint(Graphics g) {
		//		Override用
	}

	public void paintLine(Graphics g, int x, int y) {
		//		Override用
	}

	@Override
	public Coord clone() {

		Coord coord = new Coord();
		coord.x = this.x;
		coord.y = this.y;
		coord.color = this.color;
		return coord;
	}
}
