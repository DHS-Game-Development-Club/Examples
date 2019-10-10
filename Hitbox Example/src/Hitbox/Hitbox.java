package Hitbox;

import java.awt.Graphics;
import java.awt.geom.Line2D;

public abstract class Hitbox {
	public static boolean intersects(Hitbox h1, Hitbox h2) {
		// The method receives 2 Hitboxes, but we don't know what type they are. This
		//  is an issue because testing intersections changes between types, e.g. a 
		//  point and a circle have different tests than a polygon and a line. To fix
		//  this the below code returns the name of the class of the hitbox, so a point
		//  would return the string "Point". We can then use that in a series of if
		//  statements to choose the right method to test intersection.
		String h1Name = h1.getClass().getSimpleName();
		String h2Name = h2.getClass().getSimpleName();
		
		// Hybrid
		if (h1Name.equals("Hybrid") || h2Name.equals("Hybrid")) {
			// The first thing we want to check is if one of the hitboxes is a hybrid
			//  hitbox. A hybrid hitbox is just a combination of other hitboxes, it's
			//  nothing special itself. Because of this, we can just loop through
			//  every part of the hybrid hitbox and check if any of them intersect
			//  the other hitbox. This is a recursive function call, but if you don't 
			//  get that it's okay, Mr. Murray will cover it later.
			Hybrid hybrid;
			Hitbox other;
			if (h1Name.equals("Hybrid")) {
				hybrid = (Hybrid) h1;
				other = h2;
			} else {
				hybrid = (Hybrid) h2;
				other = h1;
			}
			
			boolean intersection = false;
			for (Hitbox part : hybrid.parts) {
				intersection = part.intersects(other);
				if (intersection) { return true; }
			}
			return false;
		}
		
		// Point * Point
		if (h1Name.equals("Point") && h2Name.equals("Point")) {
			// Just as a Hitbox, you can't access Point-specific variables like x and y.
			//  To fix this we cast the hitboxes to points, and we can then use them
			//  like normal.
			Point p1 = (Point) h1;
			Point p2 = (Point) h2;
			return (p1.x == p2.x && p1.y == p2.y);
		}
		
		// Circle * Circle
		if (h1Name.equals("Circle") && h2Name.equals("Circle")) {
			Circle c1 = (Circle) h1;
			Circle c2 = (Circle) h2;
			// More info on circle intersections:
			//  https://www.geeksforgeeks.org/check-two-given-circles-touch-intersect/
			return Math.pow(c1.r + c2.r, 2) >= Math.pow(c1.c.x - c2.c.x, 2) + Math.pow(c1.c.y - c2.c.y, 2);
		}
		
		// Circle * Point
		if ((h1Name.equals("Circle") && h2Name.equals("Point")) ||
				(h1Name.equals("Point") && h2Name.equals("Circle"))) {
			// The if statement is called twice here to check for the following case:
			//  intersects(myCircle, myPoint) and intersects(myPoint, myCircle). They
			//  would each have different calues of h1 and h2, so they both have to
			//  be checked. The following code seperates them out and casts them
			//  correctly.
			Circle c1;
			Point p1;
			if (h1Name.equals("Circle")) {
				c1 = (Circle) h1;
				p1 = (Point) h2;
			} else {
				c1 = (Circle) h2;
				p1 = (Point) h1;
			}
			// A point is in a circle if the distance between their centers <= the radius
			return Math.pow(c1.r, 2) >= Math.pow(c1.c.x - p1.x, 2) + Math.pow(c1.c.y - p1.y, 2);
		}
		
		// Circle * Line
		if ((h1Name.equals("Circle") && h2Name.equals("Line")) ||
				(h1Name.equals("Line") && h2Name.equals("Circle"))) {
			Circle c1;
			Line l1;
			if (h1Name.equals("Circle")) {
				c1 = (Circle) h1;
				l1 = (Line) h2;
			} else {
				c1 = (Circle) h2;
				l1 = (Line) h1;
			}
			
			// Use a built in Java method to get the length from the center of the circle
			//  to the line segment. If the distance is <= the radius, they intersect.
			double distance = Line2D.ptSegDist(
					l1.p1.x,  l1.p1.y,
					l1.p2.x, l1.p2.y,
					c1.c.x, c1.c.y
			);
			return distance <= c1.r;
		}
		
		// Circle * Polygon
		if ((h1Name.equals("Circle") && h2Name.equals("Polygon")) ||
				(h1Name.equals("Polygon") && h2Name.equals("Circle"))) {
			Circle c1;
			Polygon p1;
			if (h1Name.equals("Circle")) {
				c1 = (Circle) h1;
				p1 = (Polygon) h2;
			} else {
				c1 = (Circle) h2;
				p1 = (Polygon) h1;
			}
			
			// for side in polygon, return true if circle intersects side
			for (Line side : p1.sides) {
				if (c1.intersects(side)) {
					return true;
				}
			}
			
			return false;
		}
		
		// Point * Line
		if ((h1Name.equals("Point") && h2Name.equals("Line")) ||
				(h1Name.equals("Line") && h2Name.equals("Point"))) {
			Point p1;
			Line l1;
			if (h1Name.equals("Circle")) {
				p1 = (Point) h1;
				l1 = (Line) h2;
			} else {
				p1 = (Point) h2;
				l1 = (Line) h1;
			}
			
			return Line2D.ptSegDist(
					l1.p1.x,  l1.p1.y,
					l1.p2.x, l1.p2.y,
					p1.x, p1.y
			) == 0;  // if distance is zero, the point is on the line
		}

		// Line * Line
		if (h1Name.equals("Line") && h2Name.equals("Line")) {
			Line l1 = (Line) h1;
			Line l2 = (Line) h2;
			// Use a built in Java function to check if lines intersect
			return Line2D.linesIntersect(
					l1.p1.x, l1.p1.y,
					l1.p2.x, l1.p2.y,
					l2.p1.x, l2.p1.y,
					l2.p2.x, l2.p2.y
			);
		}
		
		// Unknown
		System.out.println("Error on intersection, unknown types of '" + h1Name + "'[h1] and '" + h2Name + "'[h2]");
		return false;
	}
	
	// This is just another wat to call the intersection method. Above is called by
	//  the following because it's a static method belonging to the parent Hitbox class.
	//
	//      Hitbox.intersects(hitbox1, hitbox2);
	//
	// The below method is not static, so it can be called on any instance of the class.
	//  It's called by the following code, and is really only an ease-of-use thing.
	//
	//      hitbox1.intersects(hitbox2);
	//
	public boolean intersects(Hitbox h2) {
		return intersects(this, h2);
	}
	
	// If you havn't learned about abstract functions, this code basically requires that
	//  any child class of type Hitbox MUST have a drawHitbox(Graphics h) function.
	public abstract void drawHitbox(Graphics h);
}
