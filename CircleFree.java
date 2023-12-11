package enshuReport2_2023;

//public class Circle extends Figure {
//	Circle(){}
//
//	@Override public void paint(Graphics g){
//		int r = (int)Math.sqrt((double)(w * w + h * h));
//		g.drawOval(x - r, y - r, r * 2 , r * 2);
//	}
//}

public class CircleFree extends Coord {
//
//	boolean isFill;
//
//	public CircleFree() {
//		this.size = 1;
//		this.isFill = false;
//	}
//
//	public CircleFree(boolean isFill, int size) {
//		this.size = size;
//		this.isFill = isFill;
//	}
//
//	public CircleFree(boolean isFill) {
//		this.size = 1;
//		this.isFill = isFill;
//	}
//
//	public void setFill(boolean isFill) {
//		this.isFill = isFill;
//	}
//
//	@Override
//	public void paint(Graphics g) {
//
//		g.setColor(this.color);
//		if (this.isFill) {
//			g.fillOval(x - size / 2, y - size / 2, size, size);
//		} else {
//			g.drawOval(x - size / 2, y - size / 2, size, size);
//		}
//	}
//
//	@Override
//	public void paintLine(Graphics g, int x, int y) {
//
//		g.setColor(this.color);
//
//		if (this.isFill) {
//			g.fillOval(x - size / 2, y - size / 2, size, size);
//		} else {
//			g.drawOval(x - size / 2, y - size / 2, size, size);
//		}
//	}
//
//	@Override
//	public void move(int dx, int dy) {
//		x += dx;
//		y += dy;
//	}
//
//	@Override
//	public Coord clone() {
//
//		Circle circle = new Circle();
//
//		circle.color = this.color;
//		circle.size = this.size;
//		circle.isFill = this.isFill;
//		circle.x = this.x;
//		circle.y = this.y;
//
//		return circle;
//	}
}
