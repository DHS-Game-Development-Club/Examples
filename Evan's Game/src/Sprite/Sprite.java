package Sprite;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Sprite {
	public static boolean drawSprites = true;
	
	public double x;
	public double y;
	public double oldX;
	public double oldY;
	
	public boolean alive;
	public Color color;
	
	public Sprite(double x_, double y_) {
		x = x_;
		y = y_;
		oldX = x;
		oldY = y;
		
		alive = true;
	}
	
	public void draw(Graphics g) {
		if (!drawSprites) { return; }
		g.setColor(color);
	}
	
	public static void toggleDrawSprites() {
		drawSprites = !drawSprites;
	}
}
