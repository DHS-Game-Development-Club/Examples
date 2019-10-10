package Hitbox;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polygon extends Hitbox {
	public ArrayList<Line> sides;
	public ArrayList<Point> points;
	
	public Polygon(List<Point> points_) {
		points = new ArrayList<Point>(points_);
		initSides();
	}
	public Polygon(Point[] points_) {
		points = new ArrayList<Point>(Arrays.asList(points_));
		initSides();
	}
	public Polygon(Polygon p) {
		// create a copy of the arraylists to avoid aliasing issues
		points = new ArrayList<Point>(p.points);
		sides = new ArrayList<Line>(p.sides);
	}
	
	private void initSides() {
		// I moved this method outside of the constructor because it's
		//  used in two different constructors, and it's bad practice
		//  to repeat yourself.
		sides = new ArrayList<Line>();
		for (int i=0; i<points.size()-1; i++) {
			sides.add(new Line(points.get(i), points.get(i+1)));
		}
		// connect the last and first point
		sides.add(new Line(points.get(points.size()-1), points.get(0)));
	}
	
	@Override
	public String toString() {
		// returns "Polygon: [(x1, y1), (x2, y2), (x3, y3), ...]"
		return "Polygon: " + points;
	}
	
	@Override
	public void drawHitbox(Graphics g) {
		// draw every side of the polygon
		for (Line side : sides) {
			side.drawHitbox(g);
		}
	}
}
