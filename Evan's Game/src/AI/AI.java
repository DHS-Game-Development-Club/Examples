package AI;

import java.util.ArrayList;

public abstract class AI {
	public Sprite.Character character;
	
	public AI(Sprite.Character character_) {
		character = character_;
	}
	
	public abstract void update(Sprite.Character player, ArrayList<Hitbox.Hitbox> barriers, ArrayList<Sprite.Bullet> bullets);
}
