package enshuReport2_2023;

import java.awt.Graphics;

//public class Circle extends Figure {
//	Circle(){}
//
//	@Override public void paint(Graphics g){
//		int r = (int)Math.sqrt((double)(w * w + h * h));
//		g.drawOval(x - r, y - r, r * 2 , r * 2);
//	}
//}

public class Circle extends Figure {

	public Circle() {
		this.isFill = true;
	}

	public Circle(boolean isFill) {
		this.isFill = isFill;
	}

	public void setFill(boolean isFill) {
		this.isFill = isFill;
	}

	@Override
	public void paint(Graphics g) {

		g.setColor(this.color);
		if (this.isFill) {

			if (w >= 0 && h >= 0) {
				g.fillOval(x, y, w, h);
			}
			else if( w < 0 && h >= 0 ) {
				g.fillOval(x + w, y, -w, h);
			}
			else if( w >= 0 && h < 0 ) {
				g.fillOval(x, y + h, w, -h);
			}
			else  {
				g.fillOval(x + w, y + h, -w, -h);
			}
		} else {

			if (w >= 0 && h >= 0) {
				g.drawOval(x, y, w, h);
			}
			else if( w < 0 && h >= 0 ) {
				g.drawOval(x + w, y, -w, h);
			}
			else if( w >= 0 && h < 0 ) {
				g.drawOval(x, y + h, w, -h);
			}
			else  {
				g.drawOval(x + w, y + h, -w, -h);
			}
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
	public Circle clone() {

		Circle circle = new Circle();

		circle.color = this.color;
		circle.isFill = this.isFill;
		circle.x = this.x;
		circle.y = this.y;
		circle.color = this.color;
		circle.w = this.w;
		circle.h = this.h;
		return circle;
	}
}
