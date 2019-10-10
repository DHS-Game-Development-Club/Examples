package Sprite;

import java.awt.Color;
import java.awt.Graphics;

public class Exit extends Sprite {
	public int r;
	public Hitbox.Circle hitbox;
	
	public Exit(int x_, int y_, int r_) {
		super(x_, y_);
		r = r_;
		hitbox = new Hitbox.Circle((int) x, (int) y, r);
		color = new Color(255, 0, 0, 150);
	}
	
	public void draw(Graphics g) {
		if (!alive) { return; }
		hitbox.drawHitbox(g);
		super.draw(g);
		g.fillOval((int) Math.round(x - r) + Main.Visual.screenOffsetX, (int) Math.round(y - r) + Main.Visual.screenOffsetY, r * 2, r * 2);
	}
}
