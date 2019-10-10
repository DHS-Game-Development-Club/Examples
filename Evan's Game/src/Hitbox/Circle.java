package Hitbox;

import java.awt.Graphics;

public class Circle extends Hitbox {
	public int r;
	
	public Circle(double x_, double y_, int r_) {
		super(x_, y_);
		r = r_;
	}
	
	public void drawHitbox(Graphics g) {
		if (!drawHitBoxes) { return; }
		super.drawHitbox(g);
		g.drawOval((int) x - r, (int) y - r, r * 2, r * 2);
	}
}
