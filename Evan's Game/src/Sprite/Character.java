package Sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Character extends Sprite {
	public int r;
	public Hitbox.Circle hitbox;
	public double speed;
	
	public Character(int x_, int y_, int r_, Color color_, double speed_) {
		super(x_, y_);
		r = r_;
		color = color_;
		speed = speed_;
		hitbox = new Hitbox.Circle((int) x, (int) y, r);
	}
	
	public void draw(Graphics g) {
		if (!alive) { return; }
		hitbox.drawHitbox(g);
		super.draw(g);
		g.fillOval((int) Math.round(x - r) + Main.Visual.screenOffsetX, (int) Math.round(y - r) + Main.Visual.screenOffsetY, r * 2, r * 2);
	}

	public void update(ArrayList<Hitbox.Line> barriers) {
		hitbox.x = x;
		hitbox.y = y;
		
		boolean valid = true;
		for (Hitbox.Hitbox barrier : barriers) {
			if (Hitbox.Hitbox.outlineIntersects(this.hitbox, barrier)) {
				valid = false;
			}
		}
		
		if (valid) {
			oldX = x;
			oldY = y;
		} else {
			x = oldX;
			y = oldY;
		}
	}
}
