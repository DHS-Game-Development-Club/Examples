package Sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Bullet extends Sprite implements Main.Physics {
	public static Color BULLET_COLOR = new Color(255, 215, 0);

	public Hitbox.Circle hitbox;
	public boolean active;

	public double velX;
	public double velY;
	public int r;
	
	public Bullet(double x_, double y_, int r_, double speed, Hitbox.Point target) {
		super(x_, y_);
		color = BULLET_COLOR;
		
		double magnitude = Math.sqrt(Math.pow(x - target.x, 2) + Math.pow(y - target.y, 2));
		double unitX = (target.x - x) / magnitude;
		double unitY = (target.y - y) / magnitude; 
		velX = unitX * speed;
		velY = unitY * speed;
		
		r = r_;
		hitbox = new Hitbox.Circle(x, y, r);
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public void update(ArrayList<Hitbox.Polygon> barriers) {
		x += velX;
		y += velY;
		hitbox.x = x;
		hitbox.y = y;
		
		boolean valid = true;
		for (Hitbox.Hitbox barrier : barriers) {
			if (Hitbox.Hitbox.outlineIntersects(this.hitbox, barrier)) {
				valid = false;
			}
		}
		
		if (!valid) { alive = false; }
	}
	
	public void update(ArrayList<Hitbox.Polygon> barriers, int lagAdjust) {
		if (velX < 0) { x += velX - lagAdjust; }
		if (velX > 0) { x += velX + lagAdjust; }
		if (velY < 0) { y += velY - lagAdjust; }
		if (velY > 0) { y += velY + lagAdjust; }
//		x += velX;
//		y += velY;
		hitbox.x = x;
		hitbox.y = y;
		
		boolean valid = true;
		for (Hitbox.Hitbox barrier : barriers) {
			if (Hitbox.Hitbox.outlineIntersects(this.hitbox, barrier)) {
				valid = false;
			}
		}
		
		if (!valid) { alive = false; }
	}
	
	public void draw(Graphics g) {
		if (!alive) { return; }
		hitbox.drawHitbox(g);
		super.draw(g);
		g.fillOval((int) Math.round(x - r) + Main.Visual.screenOffsetX, (int) Math.round(y - r) + Main.Visual.screenOffsetY, r * 2, r * 2);
	}
}
