package Main;

public class Cell {
	public boolean wall;
	public boolean visited;
	public boolean exit;
	public boolean start;
	public boolean treasure;
	
	public Cell() {
		wall = false;
		visited = false;
		exit = false;
		treasure = false;
		start = false;
	}
}
