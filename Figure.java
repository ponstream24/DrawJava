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

public class Figure extends Coord{

	int w, h, width = 1;
	boolean isFill;
	
	public Figure() {
		// TODO 自動生成されたコンストラクター・スタブ
		color = Color.BLACK;
		w = h = 0;
	}

	public void paint() {
		
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
	
	public Figure clone(){

		Figure figure = (Figure) super.clone();
		figure.w = this.w;
		figure.h = this.h;
		return figure;
	}
}
