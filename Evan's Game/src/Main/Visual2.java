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
import java.util.ArrayList;

import javax.swing.JFrame; 
import javax.swing.JPanel; 
import javax.swing.Timer;


public class Visual2 implements Physics, ActionListener, KeyListener, MouseListener, MouseMotionListener {
	// ---------- REQUIRED VARIABLES ---------- //
    private JFrame frame;       // Outside shell of the window
    public DrawingPanel panel;  // Interior window
    private Timer visualtime;   // Refreshes the screen.
    
    // ---------- OTHER VARIABLES ---------- //
    Cell[][] maze;
    int size;
    int scale;
    
    // ---------- CONTROLS ---------- //
    public boolean pressingW;
    public boolean pressingA;
    public boolean pressingS;
    public boolean pressingD;
    
    // ---------- CONSTRUCTORS ---------- //
    public Visual2() {
        frame = new JFrame("Hitbox Testing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new DrawingPanel();
        panel.setPreferredSize(new Dimension(WIDTH + 1, HEIGHT + 1));  //width, height in #pixels.
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
//    	size = 50;
    	size = 100;
    	scale = WIDTH / size;
    	boolean valid = false;
    	while (!valid) {
    		maze = DungeonGenerator.generateCellularAutomata(size, size, .4, 3, 4, 2);
//    		maze = DungeonGenerator.generateCellularAutomata(size, size, .2, 2, 3, 1);
        	DungeonGenerator.setStart(maze);
        	DungeonGenerator.setExit(maze);
        	DungeonGenerator.fillMaze(maze,
        			DungeonGenerator.getStartY(maze),
        			DungeonGenerator.getStartX(maze));
        	valid = DungeonGenerator.exitReachable(maze,
        			DungeonGenerator.getStartY(maze),
        			DungeonGenerator.getStartX(maze));
    	}
    	ArrayList<Hitbox.Line> map = DungeonGenerator.gridToPoly(maze, scale);
    	
    }
 
    // ---------- KEY COMMANDS ---------- //
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_HOME) {
        	initialize();
        }
        if (e.getKeyCode() == KeyEvent.VK_END) {
        	System.exit(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_1) {
        	maze = DungeonGenerator.doSimulationStep(maze, 3, 4);
        }
    }
    
    public void keyTyped(KeyEvent e) { }
    public void keyReleased(KeyEvent e) {}

    // ---------- MOUSE COMMANDS ---------- //
    public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    // ---------- DRAWING/UPDATE CODE ---------- //
    public void actionPerformed(ActionEvent e) {  // Loops every clock cycle
        panel.repaint();
    }
    
    private class DrawingPanel extends JPanel implements Physics {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            panel.setBackground(BACKGROUND_COLOR);

            // Timer
            for (int r=0; r<maze.length; r++) {
            	for (int c=0; c<maze[0].length; c++) {
            		if (maze[r][c].wall) { g.setColor(Color.BLACK); }
            		else if (maze[r][c].exit) { g.setColor(Color.RED); }
            		else if (maze[r][c].start) { g.setColor(Color.GREEN); }
            		else { g.setColor(Color.WHITE); }
            		g.fillRect(c * scale, r * scale, scale, scale);
            	}
            }
        }
    }
}