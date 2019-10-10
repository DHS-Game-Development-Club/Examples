package Hitbox;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polygon extends Hitbox {
	public ArrayList<Line> sides;
	public ArrayList<Point> points;
	
	public Polygon(List<Point> points_) {
		super(points_.get(0));
		points = new ArrayList<Point>(points_);
		initSides();
	}
	public Polygon(Point[] points_) {
		super(points_[0]);
		points = new ArrayList<Point>(Arrays.asList(points_));
		initSides();
	}
	public Polygon(List<Line> sides_, boolean DONT_KNOW_WHY_I_NEED_THIS) {
		super(null);
		points = new ArrayList<Point>();
		sides = new ArrayList<Line>(sides_);
	}
	
	private void initSides() {
		sides = new ArrayList<Line>();
		for (int i=0; i<points.size()-1; i++) {
			sides.add(new Line(points.get(i), points.get(i+1)));
		}
		sides.add(new Line(points.get(points.size()-1), points.get(0)));
	}
	
	private void updateSideCollisions() {
		for (int i=0; i<sides.size(); i++) {
			sides.get(i).collision = collision;
		}
	}
	
	public void drawHitbox(Graphics g) {
		if (!drawHitBoxes) { return; }
		super.drawHitbox(g);
		updateSideCollisions();
		for (int i=0; i<sides.size(); i++) {
			sides.get(i).drawHitbox(g);
		}
	}
}
