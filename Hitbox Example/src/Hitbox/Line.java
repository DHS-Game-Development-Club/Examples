package Hitbox;

import java.awt.Graphics;

public class Line extends Hitbox {
	public Point p1;
	public Point p2;
	
	public Line(Point p1_, Point p2_) {
		p1 = new Point(p1_);
		p2 = new Point(p2_);
	}
	public Line(double x1, double y1, double x2, double y2) {
		p1 = new Point(x1, y1);
		p2 = new Point(x2, y2);
	}
	public Line(Line line) {
		p1 = line.p1;
		p2 = line.p2;
	}
	
	@Override
	public String toString() {
		// return "Line: (x1, y1) --> (x2, y2)"
		return "Line: " + p1 + " --> " + p2;
	}
	
	@Override
	public void drawHitbox(Graphics g) {
		g.drawLine((int) p1.x,  (int) p1.y, (int) p2.x, (int) p2.y);
		p1.drawHitbox(g);
		p2.drawHitbox(g);
	}
}
