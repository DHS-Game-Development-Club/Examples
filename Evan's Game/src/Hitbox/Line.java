package Hitbox;

import java.awt.Graphics;

public class Line extends Hitbox {
	public Point p1;
	public Point p2;
	public String type;

	public Line(double x1, double y1, double x2, double y2) {
		super(x1, y1);
		p1 = new Point(x1, y1);
		p2 = new Point(x2, y2);
		type = null;
	}
	public Line(double x1, double y1, double x2, double y2, String type_) {
		super(x1, y1);
		p1 = new Point(x1, y1);
		p2 = new Point(x2, y2);
		type = type_;
	}
	public Line(Point p1_, Point p2_) {
		super(p1_.x, p2_.y);
		p1 = new Point(p1_);
		p2 = new Point(p2_);
		type = null;
	}
	
	@Override
	public String toString() {
		return p1 + " --> " + p2;
	}
	
	public void drawHitbox(Graphics g) {
		if (!drawHitBoxes) { return; }
		super.drawHitbox(g);
		g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
	}
	
	public double getAngleP1toP2() {
		double angle = Math.atan2(p1.x - p2.x, p1.y - p2.y);
		angle = (angle + Math.PI) * 180 / Math.PI;
		// For some reason 270 returns 89.9 normally, so this is my fix
		if ((int) angle == 89 && p1.x > p2.x) { return 270; }
		return angle;
	}
	
	public void extendP2ToY(int newY) {
		// Point-Slope Form: y - y1 = m(x - x1)
		// Derived: y - y1 = mx - mx1
		// Derived: y - y1 + mx1 = mx
		// Final: x = (y - y1 + m*x1) / m
		
		double m = (0.0 + p2.y - p1.y) / (p2.x - p1.x);
		double newX;
		if (Double.isFinite(m)) { newX = (newY - p1.y + m*p1.x) / m; }
		else { newX = p2.x; }
		p2 = new Point((int) newX, newY);
	}
}
