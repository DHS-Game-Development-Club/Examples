package Hitbox;

import java.awt.Graphics;

public class Point extends Hitbox {
	public static final int POINT_WIDTH = 5;

	public Point(double x_, double y_) {
		super(x_, y_);
	}
	public Point(Point p) {
		super(p.x, p.y);
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public void drawHitbox(Graphics g) {
		if (!drawHitBoxes) { return; }
		super.drawHitbox(g);
		g.fillOval((int) x - POINT_WIDTH, (int) y - POINT_WIDTH, POINT_WIDTH * 2, POINT_WIDTH * 2);
	}
	
	public boolean equals(Point other) {
		return (x == other.x && y == other.y);
	}
}
