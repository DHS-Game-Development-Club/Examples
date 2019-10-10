package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Lighting implements Physics {
	public static void sortByAngle(ArrayList<Hitbox.Line> rays) {
		boolean sorted = false;
		while (!sorted) {
			sorted = true;
			for (int i=0; i<rays.size()-1; i++) {
				Hitbox.Line a = rays.get(i);
				Hitbox.Line b = rays.get(i + 1);
				if (a.getAngleP1toP2() > b.getAngleP1toP2()) {
					rays.set(i,  b);
					rays.set(i + 1, a);
					sorted = false;
				}
			}
		}
	}
	
	public static double distanceFrom(Hitbox.Point p1, Hitbox.Point p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}
	
	public static void drawVignette(Graphics g, Hitbox.Point origin) {
		int r = 100;
		g.setColor(new Color(0, 0, 0, 200));
		g.fillOval((int) origin.x - r, (int) origin.y - r, r * 2, r * 2);
	}
	
	public static void drawLightingPolygons(ArrayList<Hitbox.Line> lightRays, Hitbox.Point origin, ArrayList<Hitbox.Polygon> barriers, Hitbox.Polygon outerBarrier, Graphics g) {
		sortByAngle(lightRays);
        Hitbox.Line ray1, ray2;
        Hitbox.Point temp, inner1, outer1, inner2, outer2;
        
        for (int i=0; i<lightRays.size(); i++) {
        	inner1 = null;
        	inner2 = null;
        	outer1 = null;
        	outer2 = null;
        	
        	// Define rays 1 & 2, to include combination 0 & -1
        	ray1 = lightRays.get(i);
        	if (i + 1 < lightRays.size()) {
        		ray2 = lightRays.get(i + 1);
        	} else {
        		ray2 = lightRays.get(0);
        	}
        	
        	// Set outer intersections
        	for (Hitbox.Line side : outerBarrier.sides) {
        		temp = Hitbox.Hitbox.intersectionPoint(ray1, side);
        		if (temp != null) { outer1 = temp; }
        		temp = Hitbox.Hitbox.intersectionPoint(ray2, side);
        		if (temp != null) { outer2 = temp; }
        	}
        	
        	for (Hitbox.Polygon barrier : barriers) {
        		for (Hitbox.Line side : barrier.sides) {
        			temp = Hitbox.Hitbox.intersectionPoint(ray1, side);
        			if (inner1 == null) { inner1 = temp; }
        			else if (temp != null) {
        				if (distanceFrom(origin, temp) < distanceFrom(origin, inner1)) {
        					inner1 = temp;
        				}
        			}
        			
        			temp = Hitbox.Hitbox.intersectionPoint(ray2, side);
        			if (inner2 == null) { inner2 = temp; }
        			else if (temp != null) {
        				if (distanceFrom(origin, temp) < distanceFrom(origin, inner2)) {
        					inner2 = temp;
        				}
        			}
        		}
        	}
        	
        	int nPoints = 4;
        	int[] xPoints = {(int) Math.round(inner1.x) + Visual.screenOffsetX, (int) Math.round(outer1.x) + Visual.screenOffsetX, (int) Math.round(outer2.x) + Visual.screenOffsetX, (int) Math.round(inner2.x) + Visual.screenOffsetX};
        	int[] yPoints = {(int) Math.round(inner1.y) + Visual.screenOffsetY, (int) Math.round(outer1.y) + Visual.screenOffsetY, (int) Math.round(outer2.y) + Visual.screenOffsetY, (int) Math.round(inner2.y) + Visual.screenOffsetY};
        	
        	g.setColor(SHADOW_COLOR);
        	g.fillPolygon(xPoints, yPoints, nPoints);
        }
	}
	
	public static void drawLightingLines(ArrayList<Hitbox.Line> lightRays, Hitbox.Point origin, ArrayList<Hitbox.Line> barriers, Hitbox.Polygon outerBarrier, Graphics g) {
		sortByAngle(lightRays);
        Hitbox.Line ray1, ray2;
        Hitbox.Point temp, inner1, outer1, inner2, outer2;
        
        for (int i=0; i<lightRays.size(); i++) {
        	inner1 = null;
        	inner2 = null;
        	outer1 = null;
        	outer2 = null;
        	
        	// Define rays 1 & 2, to include combination 0 & -1
        	ray1 = lightRays.get(i);
        	if (i + 1 < lightRays.size()) {
        		ray2 = lightRays.get(i + 1);
        	} else {
        		ray2 = lightRays.get(0);
        	}
        	
        	// Set outer intersections
        	for (Hitbox.Line side : outerBarrier.sides) {
        		temp = Hitbox.Hitbox.intersectionPoint(ray1, side);
        		if (temp != null) { outer1 = temp; }
        		temp = Hitbox.Hitbox.intersectionPoint(ray2, side);
        		if (temp != null) { outer2 = temp; }
        	}
        	
        	for (Hitbox.Line side : barriers) {
        		temp = Hitbox.Hitbox.intersectionPoint(ray1, side);
        		if (inner1 == null) { inner1 = temp; }
        		else if (temp != null) {
        			if (distanceFrom(origin, temp) < distanceFrom(origin, inner1)) {
        				inner1 = temp;
        			}
        		}
        		
        		temp = Hitbox.Hitbox.intersectionPoint(ray2, side);
        		if (inner2 == null) { inner2 = temp; }
        		else if (temp != null) {
        			if (distanceFrom(origin, temp) < distanceFrom(origin, inner2)) {
        				inner2 = temp;
        			}
        		}
        	}
        	
        	// I don't know why inner2 is sometimes null
        	if (inner2 != null && inner1 != null) {
        		int nPoints = 4;
            	int[] xPoints = {(int) Math.round(inner1.x) + Visual.screenOffsetX, (int) Math.round(outer1.x) + Visual.screenOffsetX, (int) Math.round(outer2.x) + Visual.screenOffsetX, (int) Math.round(inner2.x) + Visual.screenOffsetX};
            	int[] yPoints = {(int) Math.round(inner1.y) + Visual.screenOffsetY, (int) Math.round(outer1.y) + Visual.screenOffsetY, (int) Math.round(outer2.y) + Visual.screenOffsetY, (int) Math.round(inner2.y) + Visual.screenOffsetY};
            	
            	g.setColor(SHADOW_COLOR);
            	g.fillPolygon(xPoints, yPoints, nPoints);
        	}
        	
        }
	}

	public static void addRayOld(ArrayList<Hitbox.Line> rays, Hitbox.Point origin, Hitbox.Point target) {
		Hitbox.Line temp;
		temp = new Hitbox.Line(
				origin.x, origin.y,
        		target.x, target.y
        );
		if (origin.y > target.y) { temp.extendP2ToY(-OUTER_SCREEN_MARGIN); }
		else { temp.extendP2ToY(HEIGHT_ + OUTER_SCREEN_MARGIN); }
		rays.add(temp);
		temp = new Hitbox.Line(
				origin.x, origin.y,
           		target.x + 1, target.y + 1
        );
		if (origin.y > target.y + 1) { temp.extendP2ToY(-OUTER_SCREEN_MARGIN); }
		else { temp.extendP2ToY(HEIGHT_ + OUTER_SCREEN_MARGIN); }
		rays.add(temp);
			
		temp = new Hitbox.Line(
				origin.x, origin.y,
        		target.x - 1, target.y - 1
        );
		if (origin.y > target.y - 1) { temp.extendP2ToY(-OUTER_SCREEN_MARGIN); }
		else { temp.extendP2ToY(HEIGHT_ + OUTER_SCREEN_MARGIN); }
		rays.add(temp);
			
		temp = new Hitbox.Line(
				origin.x, origin.y,
           		target.x + 1, target.y - 1
        );
		if (origin.y > target.y - 1) { temp.extendP2ToY(-OUTER_SCREEN_MARGIN); }
		else { temp.extendP2ToY(HEIGHT_ + OUTER_SCREEN_MARGIN); }
		rays.add(temp);
			
		temp = new Hitbox.Line(
				origin.x, origin.y,
            	target.x - 1, target.y + 1
        );
		if (origin.y > target.y + 1) { temp.extendP2ToY(-OUTER_SCREEN_MARGIN); }
		else { temp.extendP2ToY(HEIGHT_ + OUTER_SCREEN_MARGIN); }
		rays.add(temp);
	}
	
	public static void addRay(ArrayList<Hitbox.Line> rays, Hitbox.Point origin, Hitbox.Point target) {
		Hitbox.Line temp;
		temp = new Hitbox.Line(
				origin.x, origin.y,
        		target.x, target.y
        );
		if (origin.y > target.y) { temp.extendP2ToY(-OUTER_SCREEN_MARGIN); }
		else { temp.extendP2ToY(HEIGHT_ + OUTER_SCREEN_MARGIN); }
		rays.add(temp);
		
		temp = new Hitbox.Line(
				origin.x, origin.y,
           		target.x + 1, target.y + 1
        );
		if (origin.y > target.y + 1) { temp.extendP2ToY(-OUTER_SCREEN_MARGIN); }
		else { temp.extendP2ToY(HEIGHT_ + OUTER_SCREEN_MARGIN); }
		rays.add(temp);
			
		temp = new Hitbox.Line(
				origin.x, origin.y,
        		target.x - 1, target.y - 1
        );
		if (origin.y > target.y - 1) { temp.extendP2ToY(-OUTER_SCREEN_MARGIN); }
		else { temp.extendP2ToY(HEIGHT_ + OUTER_SCREEN_MARGIN); }
		rays.add(temp);
			
		temp = new Hitbox.Line(
				origin.x, origin.y,
           		target.x + 1, target.y - 1
        );
		if (origin.y > target.y - 1) { temp.extendP2ToY(-OUTER_SCREEN_MARGIN); }
		else { temp.extendP2ToY(HEIGHT_ + OUTER_SCREEN_MARGIN); }
		rays.add(temp);
			
		temp = new Hitbox.Line(
				origin.x, origin.y,
            	target.x - 1, target.y + 1
        );
		if (origin.y > target.y + 1) { temp.extendP2ToY(-OUTER_SCREEN_MARGIN); }
		else { temp.extendP2ToY(HEIGHT_ + OUTER_SCREEN_MARGIN); }
		rays.add(temp);
	}
}
