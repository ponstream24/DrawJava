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

import java.awt.Graphics;

public class Rect extends Figure {

	public Rect() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	//	
	//	@Override public void paint(Graphics g){
	//		g.drawRect(x, y, w, h);
	//	}
	@Override
	public void paint(Graphics g) {

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
}
