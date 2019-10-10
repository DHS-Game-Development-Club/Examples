package Hitbox;

import java.awt.Graphics;

public class Circle extends Hitbox {
	public double r;  // radius
	public Point c;  // center
	
	public Circle(double x_, double y_, double r_) {
		c = new Point(x_, y_);
		r = r_;
	}
	public Circle(Point p, double r_) {
		c = new Point(p);  // avoid aliasing issues by making a copy
		r = r_;
	}
	public Circle(Circle circle) {
		c = circle.c;
		r = circle.r;
	}
	
	@Override
	public String toString() {
		// returns "Circle: (x, y), r=r"
		return "Circle: " + c + ", r=" + r;
	}
	
	@Override
	public void drawHitbox(Graphics g) {
		int d = (int) r * 2; // diameter
		g.drawOval((int) (c.x - r), (int) (c.y - r), d, d);
	}
}
