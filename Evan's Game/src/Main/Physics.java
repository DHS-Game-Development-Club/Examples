package Main;

import java.awt.Color;
import java.awt.Font;

public interface Physics {
	// ---------- Setup Constants ---------- //
	public static final int WIDTH = 500;
	public static final int HEIGHT = 600;
	public static final int WIDTH_ = WIDTH;  // To avoid naming issues in DrawingPanel
	public static final int HEIGHT_ = HEIGHT;
	public static final int BOTTOM_MARGIN = 100;
	public static final int SCALE = 100;
	public static final int DIAGONAL_ADJUST = SCALE * 3;
	public static final double DIAGONAL = Math.sqrt(Math.pow(WIDTH / 2 + DIAGONAL_ADJUST, 2) + Math.pow(HEIGHT / 2 + DIAGONAL_ADJUST, 2));
	public static final int TARGET_PLAYER_X = WIDTH / 2;
	public static final int TARGET_PLAYER_Y = (HEIGHT - BOTTOM_MARGIN) / 2;
	
	public static final int OUTER_SCREEN_MARGIN = 10000;

	public static final Font LARGE_FONT = new Font("Times New Roman", Font.BOLD, 80);
	public static final Font MEDIUM_FONT = new Font("Times New Roman", Font.BOLD, 40);
	public static final Font SMALL_FONT = new Font("Times New Roman", Font.PLAIN, 20);
	
	// ---------- Game Constants ---------- //
	public static final Color SHADOW_COLOR = new Color(27, 16, 5);
	public static final Color BACKGROUND_COLOR = new Color(112, 43, 20);
	public static enum STATE {INTRO, PAUSE, PLAY, SAVE_LOAD, GAME_OVER, };
    public static enum SAVE_STATE {SAVE, LOAD, NONE, };
	
	// ---------- Game Variables ---------- //
	
	// ---------- Play Screen Constants ---------- //
	public static final double TOP_MARGIN = .20;
	public static final int PAD_WIDTH = 30;
	public static final int PAD_HEIGHT = 100;
	public static final int PAD_MARGIN = PAD_WIDTH / 2;
	public static final int BALL_RADIUS = PAD_WIDTH;
}
