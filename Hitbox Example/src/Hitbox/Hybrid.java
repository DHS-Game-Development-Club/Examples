package Hitbox;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

public class Hybrid extends Hitbox {
	public ArrayList<Hitbox> parts;
	
	public Hybrid(ArrayList<Hitbox> parts_) {
		parts = new ArrayList<Hitbox>(parts_);
	}
	public Hybrid(Hitbox[] parts_) {
		parts = new ArrayList<Hitbox>(Arrays.asList(parts_));
	}
	public Hybrid(Hybrid h) {
		parts = new ArrayList<Hitbox>(h.parts);
	}
	
	@Override
	public String toString() {
		return "Hybrid: " + parts;
	}
	
	@Override
	public void drawHitbox(Graphics g) {
		for (Hitbox part : parts) {
			part.drawHitbox(g);
		}
	}
}
