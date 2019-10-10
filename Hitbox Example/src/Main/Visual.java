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


public class Visual implements Constants, ActionListener, KeyListener, MouseListener, MouseMotionListener {
	// ---------- REQUIRED VARIABLES ---------- //
    private JFrame frame;       // Outside shell of the window
    public DrawingPanel panel;  // Interior window
    private Timer visualtime;   // Refreshes the screen.
    public Point mousePos;
    
    // ---------- GAME VARIABLES ---------- //
    public Hitbox.Polygon screenInnerHitbox;
    public Hitbox.Polygon screenOuterHitbox;
    public static ArrayList<Hitbox.Hitbox> map;
    public static Hitbox.Circle player;
    
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
    	
    	player = new Hitbox.Circle(100, 100, 20);
    	
    	map = new ArrayList<Hitbox.Hitbox>();
    	map.add(new Hitbox.Polygon(new Hitbox.Point[] {
    			new Hitbox.Point(100, 100),
    			new Hitbox.Point(300, 100),
    			new Hitbox.Point(200, 200),
    	}));
    	map.add(new Hitbox.Line(
    			new Hitbox.Point(100, 400),
    			new Hitbox.Point(400, 400)
    	));
    	map.add(new Hitbox.Circle(400, 300, 50));
    	map.add(new Hitbox.Point(200, 300));

    	Hitbox.Point center1 = new Hitbox.Point(375, 100);
    	Hitbox.Point center2 = new Hitbox.Point(450, 100);
    	map.add(new Hitbox.Hybrid(new Hitbox.Hitbox[] {
    			new Hitbox.Circle(center1, 10),
    			new Hitbox.Circle(center2, 25),
    			new Hitbox.Line(center1, center2)
    	}));
    }
 
    // ---------- KEY COMMANDS ---------- //
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_END) {
        	System.exit(0);
        }
        
        if (e.getKeyCode() == KeyEvent.VK_HOME) {
        	initialize();
        }
    }
    
    public void keyTyped(KeyEvent e) { }
    public void keyReleased(KeyEvent e) {}

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
    	player.c.x = mousePos.getX();
    	player.c.y = mousePos.getY();
    	
        panel.repaint();
    }
    
    private class DrawingPanel extends JPanel implements Constants {
    	public void drawPlay(Graphics g) {
    		g.setColor(Color.WHITE);
    		player.drawHitbox(g);
    		
    		for (Hitbox.Hitbox h : map) {
    			if (player.intersects(h)) {
    				g.setColor(Color.RED);
        			h.drawHitbox(g);
            		player.drawHitbox(g);
    			} else {
    				g.setColor(Color.WHITE);
    				h.drawHitbox(g);
    			}
    		}
    	}
    	
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            panel.setBackground(Color.BLACK);

            drawPlay(g);
        }
    }
}