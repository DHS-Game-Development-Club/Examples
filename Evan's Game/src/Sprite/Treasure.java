package Sprite;

import java.awt.Color;
import java.awt.Graphics;

public class Treasure extends Sprite {
	public int r;
	public Hitbox.Circle hitbox;
	
	public Treasure(int x_, int y_, int r_) {
		super(x_, y_);
		r = r_;
		hitbox = new Hitbox.Circle((int) x, (int) y, r);
		color = new Color(255, 215, 0, 100);
	}
	
	public void draw(Graphics g) {
		if (!alive) { return; }
		hitbox.drawHitbox(g);
		super.draw(g);
		g.fillOval((int) Math.round(x - r) + Main.Visual.screenOffsetX, (int) Math.round(y - r) + Main.Visual.screenOffsetY, r * 2, r * 2);
	}
	
	public void update(Character player) {
		if (!alive) { return; }
		if (Hitbox.Hitbox.intersects(player.hitbox, hitbox)) {
			Main.Visual.score++;
			Main.Visual.currentTime += (int) (Math.random() * 500) + 250;
			alive = false;
		}
	}
}
