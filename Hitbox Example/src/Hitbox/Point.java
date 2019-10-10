package Hitbox;

import java.awt.Graphics;

public class Point extends Hitbox {
	// Radius of circle used when drawing only, not for testing intersections.
	//  When testing intersections, the width of the point is always 1, but
	//  sometimes when testing we want it to appear a bit larger on the screen.
	public static final int POINT_WIDTH = 1;
	
	// Technically these data members should all be private, but I don't want
	//  to worry about making getters and setters, so all of mine are public.
	public double x;
	public double y;
	
	public Point(double x_, double y_) {
		// Never use the same variable name twice, that's why I have underscores
		//  after them. You could also name it something like xIn and yIn.
		x = x_;
		y = y_;
	}
	public Point(Point p) {  // Easy way to copy a point to avoid aliasing
		x = p.x;
		y = p.y;
	}
	
	@Override
	public String toString() {
		// returns "Point: (x, y)"
		return "Point: (" + x + ", " + y + ")";
	}
	
	@Override
	public void drawHitbox(Graphics g) {
		// This function draws a circle with radius POINT_WIDTH at x and y. It uses the
		//  @Override tag because it overrides the abstract function in the parent class.
		//  The tag isnt necessary, but I like it. Don't worry about how the drawing code
		//  works, I can explain it at a later date.
		int d = POINT_WIDTH * 2; // diameter
		g.fillOval((int) x - POINT_WIDTH,  (int) y - POINT_WIDTH, d, d);
	}
}
