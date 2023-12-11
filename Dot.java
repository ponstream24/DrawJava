package enshuReport2_2023;

import java.awt.Graphics;

public class Dot extends Figure{

	public Dot() {
		this.isFill = true;
	}

	public Dot(boolean isFill) {
		this.isFill = isFill;
	}

	public void setFill(boolean isFill) {
		this.isFill = isFill;
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
		box.color = this.color;
		box.w = this.w;
		box.h = this.h;
		return box;
	}
}
