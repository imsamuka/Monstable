package game;

import java.awt.Graphics;
import main.Game;
import main.Images;
import main.MySubject;
import main.Windows;

public class CommonEnemy extends GameObject{

public static enum Opt{
runner,
}

private Opt option;
public CommonEnemy(float x, float y, Opt option) {
		super(x, y, ID.Enemy, null, 16, 16, 1);
		if (option == null) {
			autoDestroy();
			return;
		}
		this.option = option;
	
		
		if(option == Opt.runner) {
			
			image = new Images("/assets.png");
			sWidth = 16;
			sHeight = 16;
			wSprite = 8;
			setHitBox(2, 2, 12, 12);
			
			
			GameHandler.objList.add(new Melee((float)bounds.getCenterX(),(float)bounds.getCenterX(), bounds.width, bounds.height, damage, this, null));
			
			life = 50;
			Spd = 1;
			damage     = 10;
		}
		
		
		//visibleBounds = true;
		entitie = true;
		collision = true;
		
	}


	protected void tick(){
		if (death) {
		autoDestroy();
		return;
		}
		if(option == Opt.runner) {
			goFromTo((float) bounds.getCenterX(), (float) bounds.getCenterY(),(float) GameHandler.player.getBounds().getCenterX(),(float)  GameHandler.player.getBounds().getCenterY(), Spd);
			
			subject.setDirection(checkForDirection((float) GameHandler.player.getBounds().getCenterX(), (float) GameHandler.player.getBounds().getCenterY(), 10, true));
			
		}
		x = Game.clamp(x + xvel, -hitboxX, Windows.WIDTH - bounds.width - hitboxX);
		y = Game.clamp(y + yvel, -hitboxY, Windows.HEIGHT - bounds.height - hitboxY);
		refreshBounds();
		checkForDeath();
	 }

	
	protected void render(Graphics g){ 
		renderSprite(g);
		renderBounds(g);
	 }
}
