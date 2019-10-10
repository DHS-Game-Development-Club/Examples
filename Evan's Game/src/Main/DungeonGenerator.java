package Main;

import java.util.ArrayList;

import Hitbox.Line;

public class DungeonGenerator {
	public static Cell[][] generateCellularAutomata(int x, int y, double birthChance, int deathLimit, int birthLimit, int numSteps) {
		Cell[][] arr = new Cell[y][x];
		generateWhiteNoise(arr, birthChance);
		for (int i=0; i<numSteps; i++) {
			arr = doSimulationStep(arr, deathLimit, birthLimit);
		}
		return arr;
	}
	
	public static Cell[][] doSimulationStep(Cell[][] arr, int deathLimit, int birthLimit) {
		Cell[][] temp = new Cell[arr.length][arr[0].length];
		for (int r=0; r<arr.length; r++) {
			for (int c=0; c<arr[0].length; c++) {
				int numNeighbors = neighborCount(arr, r, c);
				temp[r][c] = new Cell();
				if (arr[r][c].wall) {
					if (numNeighbors < deathLimit) {
						temp[r][c].wall = false;
					} else {
						temp[r][c].wall = true;
					}
				} else {
					if (numNeighbors > birthLimit) {
						temp[r][c].wall = true;
					} else {
						temp[r][c].wall = false;
					}
				}
			}
		}
		return temp;
	}
	
	public static int neighborCount(Cell[][] arr, int r, int c) {
		int count = 0;
		for (int a=-1; a<2; a++) {
			for (int b=-1; b<2; b++) {
				int neighborC = c + a;
				int neighborR = r + b;
				if (a == 0 && b == 0) {}
				else if (neighborC < 0 || neighborR < 0 || neighborC >= arr[0].length || neighborR >= arr.length) {
					count++;
				} else if (arr[neighborR][neighborC].wall) {
					count++;
				}
			}
		}
		return count;
	}
	
	public static void generateWhiteNoise(Cell[][] arr, double birthChance) {
		for (int r=0; r<arr.length; r++) {
			for (int c=0; c<arr[0].length; c++) {
				arr[r][c] = new Cell();
				arr[r][c].wall = Math.random() < birthChance;
			}
		}
	}
	
	public static boolean exitReachable(Cell[][] arr, int r, int c) {
		if (r < 0 || c < 0 || r >= arr.length || c >= arr[0].length) { return false; }
		if (arr[r][c].exit) { return true; }
		if (arr[r][c].wall || arr[r][c].visited) { return false; }
		
		arr[r][c].visited = true;
		if (exitReachable(arr, r + 1, c)) { return true; }
		if (exitReachable(arr, r - 1, c)) { return true; }
		if (exitReachable(arr, r, c + 1)) { return true; }
		if (exitReachable(arr, r, c - 1)) { return true; }
		
		return false;
	}
	
	public static int fillMaze(Cell[][] arr, int r, int c) {
		int size = fill(arr, r, c);

		for (int r1=0; r1<arr.length; r1++) {
			for (int c1=0; c1<arr[0].length; c1++) {
				if (!arr[r1][c1].visited) { arr[r1][c1].wall = true; }
			}
		}
		for (int r1=0; r1<arr.length; r1++) {
			for (int c1=0; c1<arr[0].length; c1++) {
				arr[r1][c1].visited = false;
			}
		}
		
		return size;
	}
	
	public static int fill(Cell[][] arr, int r, int c) {
		if (r < 0 || c < 0 || r >= arr.length || c >= arr[0].length) { return 0; }
		if (arr[r][c].wall || arr[r][c].visited) { return 0; }
		
		int size = 0;
		arr[r][c].visited = true;
		size += fill(arr, r + 1, c);
		size += fill(arr, r - 1, c);
		size += fill(arr, r, c + 1);
		size += fill(arr, r, c - 1);
		
		return size;
	}
	
	public static void setStart(Cell[][] arr) {
		int r = (int) (Math.random() * arr.length);
		int c = (int) (Math.random() * arr[0].length);
		if (arr[r][c].wall || arr[r][c].exit) { setStart(arr); }
		arr[r][c].start = true;
	}
	
	public static void setExit(Cell[][] arr) {
		int r = (int) (Math.random() * arr.length);
		int c = (int) (Math.random() * arr[0].length);
		if (arr[r][c].wall || arr[r][c].start) { setExit(arr); }
		arr[r][c].exit = true;
	}
	
	public static int getStartX(Cell[][] arr) {
		for (int r=0; r<arr.length; r++) {
			for (int c=0; c<arr[0].length; c++) {
				if (arr[r][c].start) { return c; }
			}
		}
		return -1;
	}
	
	public static int getStartY(Cell[][] arr) {
		for (int r=0; r<arr.length; r++) {
			for (int c=0; c<arr[0].length; c++) {
				if (arr[r][c].start) { return r; }
			}
		}
		return -1;
	}
	
	public static int getExitX(Cell[][] arr) {
		for (int r=0; r<arr.length; r++) {
			for (int c=0; c<arr[0].length; c++) {
				if (arr[r][c].exit) { return c; }
			}
		}
		return -1;
	}
	
	public static int getExitY(Cell[][] arr) {
		for (int r=0; r<arr.length; r++) {
			for (int c=0; c<arr[0].length; c++) {
				if (arr[r][c].exit) { return r; }
			}
		}
		return -1;
	}
	
	public static void addTreasure(Cell[][] map) {
		for (int r=0; r<map.length; r++) {
			for (int c=0; c<map[0].length; c++) {
				if (map[r][c].wall) {
					if (Math.random() < 0.5) { map[r][c].treasure = true; }
				}
			}
		}
	}
	
	public static ArrayList<Hitbox.Line> combine(ArrayList<Hitbox.Line> old) {
		ArrayList<Hitbox.Line> sides = new ArrayList<Hitbox.Line>();
		for (Hitbox.Line side : old) {
			boolean valid = true;
			for (int i=0; i<sides.size(); i++) {
				if (side.type.equals(sides.get(i).type) && side.type != null) {
					if (side.p1.equals(sides.get(i).p1)) {
						sides.get(i).p1 = side.p2;
						valid = false;
					}
					else if (side.p2.equals(sides.get(i).p2)) {
						sides.get(i).p2 = side.p1;
						valid = false;
					}
					else if (side.p2.equals(sides.get(i).p1)) {
						sides.get(i).p1 = side.p1;
						valid = false;
					}
					else if (side.p1.equals(sides.get(i).p2)) {
						sides.get(i).p2 = side.p2;
						valid = false;
					}
				}
			}
			if (valid) { sides.add(side); }
		}
		return sides;
	}

	public static ArrayList<Hitbox.Line> lookup(String code, int x, int y, int scale) {
		// Codes are genarated as [top left, top right, bottom right, bottom left]; codes has two symbols: W & E
		ArrayList<Hitbox.Line> result = new ArrayList<Hitbox.Line>();
		int halfScale = scale / 2;
		
		if (code.equals("EEEE") || code.equals("WWWW")) {}
		if (code.equals("WWEE") || code.equals("EEWW")) {
			result.add(new Hitbox.Line(
					x * scale, y * scale + halfScale,
					(x + 1) * scale, y * scale + halfScale,
					"A"
			));
		}
		if (code.equals("WEEW") || code.equals("EWWE")) {
			result.add(new Hitbox.Line(
					x * scale + halfScale, y * scale,
					x * scale + halfScale, (y + 1) * scale,
					"B"
			));
		}
		if (code.equals("WEEE") || code.equals("EWWW")) {
			result.add(new Hitbox.Line(
					x * scale, y * scale + halfScale,
					x * scale + halfScale, y * scale,
					"C"
			));
		}
		if (code.equals("EWEE") || code.equals("WEWW")) {
			result.add(new Hitbox.Line(
					x * scale + halfScale, y * scale,
					(x + 1) * scale, y * scale + halfScale,
					"D"
			));
		}
		if (code.equals("EEWE") || code.equals("WWEW")) {
			result.add(new Hitbox.Line(
					x * scale + halfScale, (y + 1) * scale,
					(x + 1) * scale, y * scale + halfScale,
					"E"
			));
		}
		if (code.equals("EEEW") || code.equals("WWWE")) {
			result.add(new Hitbox.Line(
					x * scale, y * scale + halfScale,
					x * scale + halfScale, (y + 1) * scale,
					"F"
			));
		}
		if (code.equals("WEWE")) {
			result.add(new Hitbox.Line(
					x * scale + halfScale, y * scale,
					(x + 1) * scale, y * scale + halfScale,
					"D"
			));
			result.add(new Hitbox.Line(
					x * scale, y * scale + halfScale,
					x * scale + halfScale, (y + 1) * scale,
					"A"
			));
		}
		if (code.equals("EWEW")) {
			result.add(new Hitbox.Line(
					x * scale, y * scale + halfScale,
					x * scale + halfScale, y * scale,
					"C"
			));
			result.add(new Hitbox.Line(
					x * scale + halfScale, (y + 1) * scale,
					(x + 1) * scale, y * scale + halfScale,
					"E"
			));
		}
		
		return result;
	}

	public static ArrayList<Hitbox.Line> gridToPoly(Cell[][] oldMap, int scale) {
		ArrayList<Hitbox.Line> lines = new ArrayList<Hitbox.Line>();
		
		// Put the map into a larger one with wall borders
		Cell[][] map = new Cell[oldMap.length + 2][oldMap[0].length + 2];
		for (int r=0; r<map.length; r++) {
			for (int c=0; c<map.length; c++) {
				if (r > 0 && r < map.length-1 && c > 0 && c < map[0].length-1) {
					map[r][c] = oldMap[r-1][c-1];
				} else {
					map[r][c] = new Cell();
					map[r][c].wall = true;
				}
			}
		}
		
		// Turn the map into lines
		for (int r=0; r<map.length-1; r++) {
			for (int c=0; c<map.length-1; c++) {
				String key = "";
				if (map[r][c].wall) { key += "W"; }
				else { key += "E"; }
				if (map[r][c+1].wall) { key += "W"; }
				else { key += "E"; }
				if (map[r+1][c+1].wall) { key += "W"; }
				else { key += "E"; }
				if (map[r+1][c].wall) { key += "W"; }
				else { key += "E"; }
				lines.addAll(lookup(key, c, r, scale));
			}
		}
		
		return combine(lines);
	}
}
