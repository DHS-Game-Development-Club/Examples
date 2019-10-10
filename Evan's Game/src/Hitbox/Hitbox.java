package Hitbox;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;

public abstract class Hitbox {
	public static final Color HITBOX_DEFAULT_COLOR = Color.WHITE;
	public static final Color HITBOX_COLLISION_COLOR = Color.RED;
	public static boolean drawHitBoxes = false;
	public static final boolean SUPPRESS_WARNINGS = true;
	
	public double x;
	public double y;
	public boolean collision;

	public Hitbox(double x_, double y_) {
		x = x_;
		y = y_;
		collision = false;
	}
	public Hitbox(Point p) {
		x = p.x;
		y = p.y;
		collision = false;
	}
	
	public void drawHitbox(Graphics g) {
		g.setColor(HITBOX_DEFAULT_COLOR);
		if (collision) { g.setColor(HITBOX_COLLISION_COLOR); }
	}
	
	public static void toggleDrawHitboxes() {
		drawHitBoxes = !drawHitBoxes;
	}
	
	public static Point intersectionPoint(Hitbox h1, Hitbox h2) {
		if (!intersects(h1, h2)) { return null; }
		String h1Name = h1.getClass().getSimpleName();
		String h2Name = h2.getClass().getSimpleName();

		// Line * Line V2
		if (h1Name.equals("Line") && h2Name.equals("Line")) {
			Line l1 = (Line) h1;
			Line l2 = (Line) h2;
			
			double x, y;
			// If vertical line...
			if (l1.p1.x == l1.p2.x || l2.p1.x == l2.p2.x) {
				if (l1.p1.x == l1.p2.x && l2.p1.x == l2.p2.x) {
					if (l1.p1.x != l2.p1.x) { return null; }
					else {
						if (!SUPPRESS_WARNINGS) {
							System.out.println("ERROR: infinite solutions not supported for Line * Line intersectionPoint, returning null");
						}
						return null;
					}
				}
				
				// y = mx - mx1 + y1
				double m, x1, y1;
				if (l1.p1.x == l1.p2.x) {
					m = (0.0 + l2.p1.y - l2.p2.y) / (l2.p1.x - l2.p2.x);
					x1 = l2.p1.x;
					y1 = l2.p1.y;
					x = l1.p1.x;
				} else {
					m = (0.0 + l1.p1.y - l1.p2.y) / (l1.p1.x - l1.p2.x);
					x1 = l1.p1.x;
					y1 = l1.p1.y;
					x = l2.p1.x;
				}
				y = m * x - m * x1 + y1;
			}
			else {
				// Point Slope Form: y - y1 = m(x - x1)
				// m = a(line 1) or b(line 2)
				// x = (bx3 + y1 - ax1 - y3) / (b - a)
				// y = ax - ax1 + y1
				double a = (0.0 + l1.p1.y - l1.p2.y) / (l1.p1.x - l1.p2.x);
				double b = (0.0 + l2.p1.y - l2.p2.y) / (l2.p1.x - l2.p2.x);
				
				x = (b * l2.p1.x + l1.p1.y - a * l1.p1.x - l2.p1.y) / (b - a);
				y = a * x - a * l1.p1.x + l1.p1.y;
			}
			
			return new Point((int) x, (int) y);
		}
		
		System.out.println("ERROR: point at intersection is not supported for " + h1Name + "[h1] and " + h2Name + "[h2]");
		return null;
	}
	
	public static boolean outlineIntersects(Hitbox h1, Hitbox h2) {
		String h1Name = h1.getClass().getSimpleName();
		String h2Name = h2.getClass().getSimpleName();
		
		// Circle * Polygon
		if ((h1Name.equals("Circle") && h2Name.equals("Polygon")) || (h1Name.equals("Polygon") && h2Name.equals("Circle"))) {
			Circle c1;
			Polygon p1;
			if (h1Name.equals("Circle")) {
				c1 = (Circle) h1;
				p1 = (Polygon) h2;
			}
			else {
				c1 = (Circle) h2;
				p1 = (Polygon) h1;
			}

			for (int i=0; i<p1.sides.size(); i++) {
				if (intersects(c1, p1.sides.get(i))) {
					return true;
				}
			}
					
			return false;
		}
		
		return intersects(h1, h2);
	}
	
	public static boolean intersects(Hitbox h1, Hitbox h2) {
		String h1Name = h1.getClass().getSimpleName();
		String h2Name = h2.getClass().getSimpleName();
		
		// Circle * Circle
		if (h1Name.equals("Circle") && h2Name.equals("Circle")) {
			Circle c1 = (Circle) h1;
			Circle c2 = (Circle) h2;
			return Math.pow(c1.r + c2.r, 2) >= Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2);
		}
		
		// Circle * Point
		if ((h1Name.equals("Circle") && h2Name.equals("Point")) || (h1Name.equals("Point") && h2Name.equals("Circle"))) {
			Circle c1;
			Point p1;
			if (h1Name.equals("Circle")) {
				c1 = (Circle) h1;
				p1 = (Point) h2;
			}
			else {
				c1 = (Circle) h2;
				p1 = (Point) h1;
			}
			return Math.pow(c1.r, 2) >= Math.pow(c1.x - p1.x, 2) + Math.pow(c1.y - p1.y, 2);
		}
		
		// Circle * Line
		if ((h1Name.equals("Circle") && h2Name.equals("Line")) || (h1Name.equals("Line") && h2Name.equals("Circle"))) {
			Circle c1;
			Line l1;
			if (h1Name.equals("Circle")) {
				c1 = (Circle) h1;
				l1 = (Line) h2;
			}
			else {
				c1 = (Circle) h2;
				l1 = (Line) h1;
			}
			
			double distance = Line2D.ptSegDist(
					l1.p1.x, l1.p1.y,
					l1.p2.x, l1.p2.y,
					c1.x, c1.y
			);
			return distance <= c1.r;
		}
		
		// Circle * Polygon
		if ((h1Name.equals("Circle") && h2Name.equals("Polygon")) || (h1Name.equals("Polygon") && h2Name.equals("Circle"))) {
			Circle c1;
			Polygon p1;
			if (h1Name.equals("Circle")) {
				c1 = (Circle) h1;
				p1 = (Polygon) h2;
			}
			else {
				c1 = (Circle) h2;
				p1 = (Polygon) h1;
			}

			for (int i=0; i<p1.sides.size(); i++) {
				if (intersects(c1, p1.sides.get(i))) {
					return true;
				}
			}
			
			// Check for Point * Poly collision
			return intersects(p1, new Point(c1.x, c1.y));
		}
		
		// Line * Line
		if (h1Name.equals("Line") && h2Name.equals("Line")) {
			Line l1 = (Line) h1;
			Line l2 = (Line) h2;
			return Line2D.linesIntersect(
					l1.p1.x, l1.p1.y,
					l1.p2.x, l1.p2.y,
					l2.p1.x, l2.p1.y,
					l2.p2.x, l2.p2.y
			);
		}
		
		// Line * Polygon
		if ((h1Name.equals("Line") && h2Name.equals("Polygon")) || (h1Name.equals("Polygon") && h2Name.equals("Line"))) {
			Line l1;
			Polygon p1;
			if (h1Name.equals("Line")) {
				l1 = (Line) h1;
				p1 = (Polygon) h2;
			}
			else {
				l1 = (Line) h2;
				p1 = (Polygon) h1;
			}

			for (int i=0; i<p1.sides.size(); i++) {
				if (intersects(l1, p1.sides.get(i))) {
					return true;
				}
			}
			return false;
		}
		
		// Polygon * Polygon
		if (h1Name.equals("Polygon") && h2Name.equals("Polygon")) {
			Polygon p1 = (Polygon) h1;
			Polygon p2 = (Polygon) h2;
			for (int i=0; i<p1.sides.size(); i++) {
				if (intersects(p2, p1.sides.get(i))) {
					return true;
				}
			}
			return false;
		}
		
		// Point * Polygon
		if ((h1Name.equals("Point") && h2Name.equals("Polygon")) || (h1Name.equals("Polygon") && h2Name.equals("Point"))) {
			Point point;
			Polygon poly;
			if (h1Name.equals("Point")) {
				point = (Point) h1;
				poly = (Polygon) h2;
			}
			else {
				point = (Point) h2;
				poly = (Polygon) h1;
			}

			Line line = new Line(point, new Point(point.x * 100, point.y));
			int numIntersections = 0;
			for (int i=0; i<poly.sides.size(); i++) {
				if (intersects(poly.sides.get(i), line)) {
					numIntersections++;
				}
			}
			return numIntersections % 2 == 1;
		}
		
		// Point * Line
		if ((h1Name.equals("Line") && h2Name.equals("Point")) || (h1Name.equals("Point") && h2Name.equals("Line"))) {
			Point p1;
			Line l1;
			if (h1Name.equals("Line")) {
				l1 = (Line) h1;		
				p1 = (Point) h2;
				}
			else {
				l1 = (Line) h2;
				p1 = (Point) h1;
			}
			return Line2D.ptSegDist(
					l1.p1.x, l1.p1.y,
					l1.p2.x, l1.p2.y,
					p1.x, p1.y
			) == 0;
		}
		
		System.out.println("ERROR: intersections is not supported for " + h1Name + "[h1] and " + h2Name + "[h2]");
		return false;
	}
}
