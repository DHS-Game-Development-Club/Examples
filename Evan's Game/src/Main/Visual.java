package Main;

import java.awt.Color;
import java.awt.Dimension; 
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.awt.event.KeyEvent; 
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame; 
import javax.swing.JPanel; 
import javax.swing.Timer;


public class Visual implements Physics, ActionListener, KeyListener, MouseListener, MouseMotionListener {
	// ---------- REQUIRED VARIABLES ---------- //
    private JFrame frame;       // Outside shell of the window
    public DrawingPanel panel;  // Interior window
    private Timer visualtime;   // Refreshes the screen.
    public Point mousePos;
    
    // ---------- GAME SETUP VARIABLES ---------- //
    public static boolean SUPPRESS_WARNING = true;
    public static boolean LAGOMETER = false;
    public static boolean DEBUG_OPTIONS = true;

    public Hitbox.Polygon screenInnerHitbox;
    public Hitbox.Polygon screenOuterHitbox;
    public static ArrayList<Hitbox.Line> map;
    public static Cell[][] maze;
    public static int size;
    
    public static STATE gameState;
    public static SAVE_STATE saveState;
    
    public static boolean f1, f2, f3, f4, f5, f6, f7, f8, f9;
    public static String[] fileNames = {"f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8", "f9", };
    
    // ---------- GAMEPLAY VARIABLES ---------- //
    public static boolean drawShadows = true;
    public static boolean drawRays = false;
    
    public static Sprite.Character player;
    public static int screenOffsetX;
    public static int screenOffsetY;
    
    public static ArrayList<Sprite.Treasure> treasures;
    public static Sprite.Exit exit;
    
    public static int lagAdjust;
    public static int score;
    
    public static long lastTime;
    public static long currentTime;
    public static int startTime = 15000;
    
    // ---------- CONTROLS ---------- //
    public static boolean pressingW;
    public static boolean pressingA;
    public static boolean pressingS;
    public static boolean pressingD;
    
    // ---------- CONSTRUCTORS ---------- //
    public Visual() {
        frame = new JFrame("Hitbox Testing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new DrawingPanel();
        panel.setPreferredSize(new Dimension(WIDTH + 1, HEIGHT + 1)); //width, height in #pixels.
        frame.getContentPane().add(panel);
        panel.setFocusable(true);
        panel.requestFocus();
        panel.addKeyListener(this);
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); 
 
        initialize();

        visualtime = new Timer(20, this);
        visualtime.start();  // Begin process
    } 
 
    public void initialize() {
    	mousePos = new Point(WIDTH / 2, HEIGHT / 2);
    	gameState = STATE.INTRO;

    	int innerMargin = OUTER_SCREEN_MARGIN - 10;
    	screenInnerHitbox = new Hitbox.Polygon(new Hitbox.Point[] {
    			new Hitbox.Point(-innerMargin, -innerMargin),
    			new Hitbox.Point(-innerMargin, WIDTH + innerMargin),
    			new Hitbox.Point(HEIGHT + innerMargin, WIDTH + innerMargin),
    			new Hitbox.Point(HEIGHT + innerMargin, -innerMargin),
    	});
    	int outerMargin = OUTER_SCREEN_MARGIN - 5;
    	screenOuterHitbox = new Hitbox.Polygon(new Hitbox.Point[] {
    			new Hitbox.Point(-outerMargin, -outerMargin),
    			new Hitbox.Point(-outerMargin, WIDTH + outerMargin),
    			new Hitbox.Point(HEIGHT + outerMargin, WIDTH + outerMargin),
    			new Hitbox.Point(HEIGHT + outerMargin, -outerMargin),
    	});
    	
    	player = new Sprite.Character(WIDTH / 2, HEIGHT / 2, 20, Color.BLUE, 5);

    	maze = loadMap("RANDOM");
    	createMap(maze);
    	
    	screenOffsetX = 0;
    	screenOffsetY = 0;
    	lagAdjust = 0;
    	score = 0;
    	
    	lastTime = System.currentTimeMillis();
    	currentTime = startTime;
    }
    
    public static void createMap(Cell[][] maze) {
    	pressingW = false;
    	pressingA = false;
    	pressingS = false;
    	pressingD = false;
    	maze[DungeonGenerator.getExitY(maze)][DungeonGenerator.getExitX(maze)].wall = false;
    	map = DungeonGenerator.gridToPoly(maze, SCALE);
    	treasures = new ArrayList<Sprite.Treasure>();
    	for (int r=0; r<maze.length; r++) {
    		for (int c=0; c<maze[0].length; c++) {
    			if (maze[r][c].treasure) {
    				treasures.add(new Sprite.Treasure(c * SCALE + (SCALE / 2), r * SCALE + (SCALE / 2), SCALE / 4));
    			}
    		}
    	}
    	System.out.println(treasures);
    	exit = new Sprite.Exit(DungeonGenerator.getExitX(maze) * SCALE + (SCALE / 2), DungeonGenerator.getExitY(maze) * SCALE + (SCALE / 2), SCALE / 2);
//    	System.out.println(maze[DungeonGenerator.getExitY(maze)][DungeonGenerator.getExitX(maze)].wall);
//    	exit = new Sprite.Exit(DungeonGenerator.getExitX(maze) * SCALE, DungeonGenerator.getExitY(maze) * SCALE, SCALE / 2);

    	player.x = DungeonGenerator.getStartX(maze) * SCALE + SCALE;
    	player.y = DungeonGenerator.getStartY(maze) * SCALE + SCALE;
    }
    
    public static Cell[][] loadMap(String file) {
    	maze = null;
    	if (file.equals("RANDOM")) {
        	size = 10;
        	boolean valid = false;
        	while (!valid) {
        		maze = DungeonGenerator.generateCellularAutomata(size, size, .2, 5, 2, 1);
            	DungeonGenerator.setStart(maze);
            	DungeonGenerator.setExit(maze);
            	DungeonGenerator.fillMaze(maze,
            			DungeonGenerator.getStartY(maze),
            			DungeonGenerator.getStartX(maze));
            	valid = DungeonGenerator.exitReachable(maze,
            			DungeonGenerator.getStartY(maze),
            			DungeonGenerator.getStartX(maze));
        	}
        	DungeonGenerator.addTreasure(maze);
    	}
    	return maze;
    }
    
    public static void loadFiles() {
    	try {
	    	for (String fileName : fileNames) {
	    		FileReader input;
				input = new FileReader(fileName + ".txt");
	        	Scanner fin = new Scanner(input);
	        	
	        	boolean filled = !fin.next().equals("empty");
	        	if (fileName.equals("f1")) { f1 = filled; }
	        	if (fileName.equals("f2")) { f2 = filled; }
	        	if (fileName.equals("f3")) { f3 = filled; }
	        	if (fileName.equals("f4")) { f4 = filled; }
	        	if (fileName.equals("f5")) { f5 = filled; }
	        	if (fileName.equals("f6")) { f6 = filled; }
	        	if (fileName.equals("f7")) { f7 = filled; }
	        	if (fileName.equals("f8")) { f8 = filled; }
	        	if (fileName.equals("f9")) { f9 = filled; }
	        	
	        	fin.close();
	    	}
    	} catch (FileNotFoundException e) {
			if (!SUPPRESS_WARNING) { e.printStackTrace(); }
    	}
    }
    
    public static void saveToFile(String name) {
    	FileWriter output;
		try {
			output = new FileWriter(name + ".txt");
	    	BufferedWriter fout = new BufferedWriter(output);
	    	
	    	fout.write("full");
	    	fout.newLine();
	    	fout.write(maze.length + " " + maze[0].length);
	    	fout.newLine();
	    	
	    	// ORDER: cell,r,c,wall,start,exit,treasure (T/F)
	    	for (int r=0; r<maze.length; r++) {
	    		for (int c=0; c<maze[0].length; c++) {
	    			fout.write("cell " + r + " " + c + " " + maze[r][c].wall + " " + maze[r][c].start + " " + maze[r][c].exit + " " + maze[r][c].treasure);
	    			fout.newLine();
	    		}
	    	}
	    	fout.write("end");
	    	
	    	fout.close();
		} catch (IOException e) {
			if (!SUPPRESS_WARNING) { e.printStackTrace(); }
		}
    }
    
    public static void loadFromFile(String name) {
    	FileReader input;
		try {
			input = new FileReader(name + ".txt");
	    	Scanner fin = new Scanner(input);
	    	
	    	if (fin.next().equals("empty")) { return; }
	    	
	    	int row = fin.nextInt();
	    	int col = fin.nextInt();
	    	
	    	maze = new Cell[row][col];
	    	boolean reading = true;
	    	while (reading) {
	    		String tag = fin.next();
	    		if (tag.equals("end")) {
	    			reading = false;
	    		} else {
	    			int r = fin.nextInt();
	    			int c = fin.nextInt();
	    			Cell temp = new Cell();
	    			temp.wall = fin.nextBoolean();
	    			temp.start = fin.nextBoolean();
	    			temp.exit = fin.nextBoolean();
	    			temp.treasure = fin.nextBoolean();
	    			maze[r][c] = temp;
	    		}
	    	}
	    	createMap(maze);
	    	score = 0;
	    	currentTime = startTime;
		} catch (FileNotFoundException e) {
			if (!SUPPRESS_WARNING) { e.printStackTrace(); }
		}
    }
 
    // ---------- KEY COMMANDS ---------- //
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_END) {
        	System.exit(0);
        }
        
        if (DEBUG_OPTIONS) {
            if (e.getKeyCode() == KeyEvent.VK_H) {
            	Hitbox.Hitbox.toggleDrawHitboxes();
            }
            if (e.getKeyCode() == KeyEvent.VK_J) {
            	drawShadows = !drawShadows;
            }
            if (e.getKeyCode() == KeyEvent.VK_K) {
            	drawRays = !drawRays;
            }
            if (e.getKeyCode() == KeyEvent.VK_HOME) {
            	initialize();
            }
        }
        
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
        	if (gameState == STATE.PLAY) { gameState = STATE.PAUSE; }
        	else if (gameState == STATE.PAUSE) {
        		gameState = STATE.PLAY;
            	lastTime = System.currentTimeMillis();
        	}
        	else if (gameState == STATE.SAVE_LOAD) {
        		gameState = STATE.PAUSE;
        		saveState = SAVE_STATE.NONE;
        	}
        }
        
        if (gameState == STATE.SAVE_LOAD && saveState == SAVE_STATE.SAVE) {
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD1) { saveToFile("f1"); gameState = STATE.PAUSE; }
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD2) { saveToFile("f2"); gameState = STATE.PAUSE; }
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD3) { saveToFile("f3"); gameState = STATE.PAUSE; }
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD4) { saveToFile("f4"); gameState = STATE.PAUSE; }
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD5) { saveToFile("f5"); gameState = STATE.PAUSE; }
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD6) { saveToFile("f6"); gameState = STATE.PAUSE; }
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD7) { saveToFile("f7"); gameState = STATE.PAUSE; }
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) { saveToFile("f8"); gameState = STATE.PAUSE; }
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD9) { saveToFile("f9"); gameState = STATE.PAUSE; }
        }
        
        if (gameState == STATE.SAVE_LOAD && saveState == SAVE_STATE.LOAD) {
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD1) { loadFromFile("f1"); gameState = STATE.PAUSE; }
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD2) { loadFromFile("f2"); gameState = STATE.PAUSE; }
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD3) { loadFromFile("f3"); gameState = STATE.PAUSE; }
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD4) { loadFromFile("f4"); gameState = STATE.PAUSE; }
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD5) { loadFromFile("f5"); gameState = STATE.PAUSE; }
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD6) { loadFromFile("f6"); gameState = STATE.PAUSE; }
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD7) { loadFromFile("f7"); gameState = STATE.PAUSE; }
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) { loadFromFile("f8"); gameState = STATE.PAUSE; }
        	if (e.getKeyCode() == KeyEvent.VK_NUMPAD9) { loadFromFile("f9"); gameState = STATE.PAUSE; }
        }
        
        if (gameState == STATE.PAUSE) {
        	if (e.getKeyCode() == KeyEvent.VK_S) {
        		gameState = STATE.SAVE_LOAD;
        		saveState = SAVE_STATE.SAVE;
        	}
        	if (e.getKeyCode() == KeyEvent.VK_L) {
        		gameState = STATE.SAVE_LOAD;
        		saveState = SAVE_STATE.LOAD;
        	}
        }
        
        if (gameState == STATE.INTRO) {
        	if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        		gameState = STATE.PLAY;
            	lastTime = System.currentTimeMillis();
            	currentTime = 15000;
        	}
        }

        if (gameState == STATE.PLAY) {
	        if (e.getKeyCode() == KeyEvent.VK_W) { pressingW = true; }
	        if (e.getKeyCode() == KeyEvent.VK_A) { pressingA = true; }
	        if (e.getKeyCode() == KeyEvent.VK_S) { pressingS = true; }
	        if (e.getKeyCode() == KeyEvent.VK_D) { pressingD = true; }
        }

        if (gameState == STATE.GAME_OVER) {
	        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { initialize(); }
        }
    }
    
    public void keyTyped(KeyEvent e) { }
    public void keyReleased(KeyEvent e) {
        if (gameState == STATE.PLAY) {
	    	if (e.getKeyCode() == KeyEvent.VK_W) { pressingW = false; }
	        if (e.getKeyCode() == KeyEvent.VK_A) { pressingA = false; }
	        if (e.getKeyCode() == KeyEvent.VK_S) { pressingS = false; }
	        if (e.getKeyCode() == KeyEvent.VK_D) { pressingD = false; }
        }
    }

    // ---------- MOUSE COMMANDS ---------- //
    public void mouseMoved(MouseEvent e) {
    	mousePos = new Point(e.getX(), e.getY());
    }
    public void mouseDragged(MouseEvent e) {
    	mousePos = new Point(e.getX(), e.getY());
    }

    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    // ---------- DRAWING/UPDATE CODE ---------- //
    public void actionPerformed(ActionEvent e) {  // Loops every clock cycle
        if (gameState == STATE.PLAY) {
        	if (pressingW) { player.y -= player.speed + lagAdjust; }
            if (pressingA) { player.x -= player.speed + lagAdjust; }
            if (pressingS) { player.y += player.speed + lagAdjust; }
            if (pressingD) { player.x += player.speed + lagAdjust; }
            
            player.update(map);
            screenOffsetX = TARGET_PLAYER_X - (int) player.x;
            screenOffsetY = TARGET_PLAYER_Y - (int) player.y;
            
            for (Sprite.Treasure treasure : treasures) {
            	treasure.update(player);
            }
            
            if (Hitbox.Hitbox.intersects(exit.hitbox, player.hitbox) && exit.alive) {
            	score += 1;
            	currentTime += (int) (Math.random() * 6000);
            	maze = loadMap("RANDOM");
            	createMap(maze);
            }
            
            long time = System.currentTimeMillis();
            currentTime -= time - lastTime;
            lastTime = time;
            
            if (currentTime <= 0) {
            	gameState = STATE.GAME_OVER;
            }
        }
        
        panel.repaint();
    }
    
    private class DrawingPanel extends JPanel implements Physics {
    	public void putText(Graphics g, String text, int x, int y, String center) {
    		if (center.equals("CENTER")) {
        		int width = g.getFontMetrics().stringWidth(text) / 2;
        		g.drawString(text, x - width, y);
    		} else if (center.equals("LEFT")) {
    			g.drawString(text, x, y);
    		}
    	}
    	
    	public void drawIntro(Graphics g) {
    		g.setColor(SHADOW_COLOR);
    		
    		g.setFont(LARGE_FONT);
    		putText(g, "GAME", WIDTH_ / 2, 100, "CENTER");
    		
    		g.drawLine(50, 140, WIDTH_ - 50, 140);
    		
    		g.setFont(MEDIUM_FONT);
    		putText(g, "Controls:", WIDTH_ / 2, 200, "CENTER");
    		g.setFont(SMALL_FONT);
    		putText(g, "WASD - Move", WIDTH_ / 2, 230, "CENTER");
    		putText(g, "Esc - Pause", WIDTH_ / 2, 250, "CENTER");
    		
    		g.setFont(MEDIUM_FONT);
    		putText(g, "Instructions:", WIDTH_ / 2, 300, "CENTER");
    		g.setFont(SMALL_FONT);
    		putText(g, "Explore the map to find treasures (yellow)", WIDTH_ / 2, 330, "CENTER");
    		putText(g, "Reach the next level by finding the exit (red)", WIDTH_ / 2, 350, "CENTER");
    		putText(g, "When time runs out, it's game over", WIDTH_ / 2, 370, "CENTER");
    		
    		g.drawLine(50, 400, WIDTH_ - 50, 400);

    		g.setFont(MEDIUM_FONT);
    		putText(g, "Enter to Play", WIDTH_ / 2, 450, "CENTER");
    	}
    	public void drawPause(Graphics g) {
    		g.setColor(SHADOW_COLOR);
    		
    		g.setFont(LARGE_FONT);
    		putText(g, "PAUSE", WIDTH_ / 2, 100, "CENTER");
    		
    		g.drawLine(50, 140, WIDTH_ - 50, 140);
    		
    		g.setFont(MEDIUM_FONT);
    		putText(g, "Hit Escape to Continue", WIDTH_ / 2, 200, "CENTER");
    		
    		g.drawLine(50, 240, WIDTH_ - 50, 240);
    		
    		g.setFont(MEDIUM_FONT);
    		putText(g, "Save/Load Map", WIDTH_ / 2, 300, "CENTER");
    		g.setFont(SMALL_FONT);
    		putText(g, "S - Save Map", WIDTH_ / 2, 330, "CENTER");
    		putText(g, "L - Loap Map", WIDTH_ / 2, 350, "CENTER");
    		
    		g.drawLine(50, 380, WIDTH_ - 50, 380);
    	}
    	public void drawPlay(Graphics g) {
            player.draw(g);
            exit.draw(g);
            for (Sprite.Treasure treasure : treasures) {
            	treasure.draw(g);
            	System.out.println("TRIGGER");
            }
            
            // Draw Lighting
            Hitbox.Point origin = new Hitbox.Point(player.x, player.y);
            ArrayList<Hitbox.Line> lightRays = new ArrayList<Hitbox.Line>();
            for (Hitbox.Point point : screenInnerHitbox.points) {
            	Lighting.addRay(lightRays,  origin,  point);
            }
            ArrayList<Hitbox.Point> points = new ArrayList<Hitbox.Point>();
            boolean valid;
            for (Hitbox.Line side : map) {
            	valid = true;
            	for (Hitbox.Point point : points) {
            		if (side.p1.equals(point)) { valid = false; System.out.println("TRIGGER"); }
            	}
            	if (valid && Lighting.distanceFrom(origin, side.p1) < DIAGONAL) { Lighting.addRay(lightRays, origin, side.p1); }
            	
            	valid = true;
            	for (Hitbox.Point point : points) {
            		if (side.p2.equals(point)) { valid = false; }
            	}
            	if (valid && Lighting.distanceFrom(origin, side.p2) < DIAGONAL) { Lighting.addRay(lightRays, origin, side.p2); }
            }
            if (drawShadows) { Lighting.drawLightingLines(lightRays, origin, map, screenOuterHitbox, g); }
            
            // Draw Hitboxes
            for (Hitbox.Line side : map) { side.drawHitbox(g); }
            if (drawRays) { for (Hitbox.Line ray : lightRays) { ray.drawHitbox(g); } }
            
            g.setColor(Color.BLACK);
            g.fillRect(0, HEIGHT_ - BOTTOM_MARGIN, WIDTH_, BOTTOM_MARGIN + 1);
            
            g.setFont(SMALL_FONT);
            g.setColor(Color.WHITE);
            putText(g, "Score: " + score, WIDTH_ / 2, HEIGHT_ - BOTTOM_MARGIN + 30, "CENTER");
            putText(g, "Time: " + (currentTime / 1000) + ":" + (currentTime % 1000 / 10), WIDTH_ / 2, HEIGHT_ - BOTTOM_MARGIN + 60, "CENTER");
    	}
    	public void drawSaveLoad(Graphics g) {
    		loadFiles();
    		
    		g.setColor(SHADOW_COLOR);
    		
    		g.setFont(LARGE_FONT);
    		if (saveState == SAVE_STATE.SAVE) {
    			putText(g, "SAVE MAP", WIDTH_ / 2, 100, "CENTER");
    		} else if (saveState == SAVE_STATE.LOAD) {
    			putText(g, "LOAD MAP", WIDTH_ / 2, 100, "CENTER");
    		}
    		
    		g.drawLine(50, 140, WIDTH_ - 50, 140);
    		
    		g.setFont(MEDIUM_FONT);
    		putText(g, "Hit Escape to Return", WIDTH_ / 2, 200, "CENTER");
    		
    		g.drawLine(50, 240, WIDTH_ - 50, 240);
    		
    		g.setFont(MEDIUM_FONT);
    		Color full = Color.WHITE;
    		Color empty = SHADOW_COLOR;
    		if (f7) { g.setColor(full); } else { g.setColor(empty); }
    		putText(g, "7", WIDTH_ / 2 - 100, 300, "CENTER");
    		if (f8) { g.setColor(full); } else { g.setColor(empty); }
    		putText(g, "8", WIDTH_ / 2, 300, "CENTER");
    		if (f9) { g.setColor(full); } else { g.setColor(empty); }
    		putText(g, "9", WIDTH_ / 2 + 100, 300, "CENTER");
    		if (f4) { g.setColor(full); } else { g.setColor(empty); }
    		putText(g, "4", WIDTH_ / 2 - 100, 360, "CENTER");
    		if (f5) { g.setColor(full); } else { g.setColor(empty); }
    		putText(g, "5", WIDTH_ / 2, 360, "CENTER");
    		if (f6) { g.setColor(full); } else { g.setColor(empty); }
    		putText(g, "6", WIDTH_ / 2 + 100, 360, "CENTER");
    		if (f1) { g.setColor(full); } else { g.setColor(empty); }
    		putText(g, "1", WIDTH_ / 2 - 100, 420, "CENTER");
    		if (f2) { g.setColor(full); } else { g.setColor(empty); }
    		putText(g, "2", WIDTH_ / 2, 420, "CENTER");
    		if (f3) { g.setColor(full); } else { g.setColor(empty); }
    		putText(g, "3", WIDTH_ / 2 + 100, 420, "CENTER");
    		
    		g.setFont(SMALL_FONT);
            g.setColor(SHADOW_COLOR);
    		putText(g, "White is full, black is empty.", WIDTH_ / 2, 460, "CENTER");
    		putText(g, "Use numpad to select.", WIDTH_ / 2, 480, "CENTER");
            g.setColor(Color.WHITE);
    		putText(g, "WARNING: your game will be restarted!", WIDTH_ / 2, 500, "CENTER");
    	}
    	public void drawGameOver(Graphics g) {
    		g.setColor(SHADOW_COLOR);
    		
    		g.setFont(LARGE_FONT);
    		putText(g, "GAME", WIDTH_ / 2, 100, "CENTER");
    		putText(g, "OVER", WIDTH_ / 2, 170, "CENTER");
    		
    		g.drawLine(50, 210, WIDTH_ - 50, 210);
    		
    		g.setFont(MEDIUM_FONT);
    		putText(g, "Your Score: " + score, WIDTH_ / 2, 270, "CENTER");
    		
    		g.drawLine(50, 300, WIDTH_ - 50, 300);
    		
    		g.setFont(SMALL_FONT);
    		putText(g, "Hit Escape to Continue", WIDTH_ / 2, 330, "CENTER");
    	}
    	
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            panel.setBackground(BACKGROUND_COLOR);

            long time = System.currentTimeMillis();
            if (gameState == STATE.INTRO) { drawIntro(g); }
            if (gameState == STATE.PLAY) { drawPlay(g); }
            if (gameState == STATE.PAUSE) { drawPause(g); }
            if (gameState == STATE.SAVE_LOAD) { drawSaveLoad(g); }
            if (gameState == STATE.GAME_OVER) { drawGameOver(g); }
            time = System.currentTimeMillis() - time;
            lagAdjust = (int) time / 20;
            if (time < 30) { lagAdjust--; }
            if (time > 50) { lagAdjust++; }
            if (time > 60) { lagAdjust++; }
            if (time > 70) { lagAdjust++; }
            if (LAGOMETER) { System.out.println("TIME: " + time + ", LA: " + lagAdjust); }
        }
    }
}