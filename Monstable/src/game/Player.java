package game;
import java.awt.Color;
import java.awt.Graphics;
import inputs.KeyInput;
import inputs.KeyObj;
import inputs.MouseInput;
import main.Game;
import main.Windows;

public class Player extends GameObject{
protected boolean moving = false;

public Player(float x, float y){
	super(x, y, ID.Player, "/slime.png", 16, 16, 1);
	collision     = true;
	Spd           = 1.5f;
	visibleBounds = true;
	setHitBox(2, 7, 12, 9);
}
protected void tick(){
	// Movement check
	moving = KeyInput.isAnyKeyPressed(KeyObj.types.movement);
	if (KeyInput.right.isPressed()
	&& KeyInput.left.isPressed()) if (KeyInput.keyIsFirst(KeyInput.right, KeyInput.left)) xvel = Spd;
	else xvel = -Spd;
	else if (KeyInput.right.isPressed()) xvel = Spd;
	else if (KeyInput.left.isPressed()) xvel = -Spd;
	else xvel = 0;
	if (KeyInput.down.isPressed()
	&& KeyInput.up.isPressed()) if (KeyInput.keyIsFirst(KeyInput.down, KeyInput.up)) yvel = Spd;
	else yvel = -Spd;
	else if (KeyInput.down.isPressed()) yvel = Spd;
	else if (KeyInput.up.isPressed()) yvel = -Spd;
	else yvel = 0;
	// 
	int size = GameHandler.objList.size();
	
	for (int m = 0; m < size; m++){
		GameObject tO = GameHandler.objList.get(m);
		if (tO == this) continue;
		
		if (tO.collision){
			tO.visibleBounds = false;
			if (filterInTiles(tO)) continue;
			getCollisionWithWall(tO);
		}
	}
	// Movement Apply
	x = Game.clamp(x + xvel, -hitboxX, Windows.WIDTH - bounds.width - hitboxX);
	y = Game.clamp(y + yvel, -hitboxY, Windows.HEIGHT - bounds.height - hitboxY);
	refreshBounds();
}
protected void render(Graphics g){
	g.setColor(new Color(0, 0, 255, 100));
	g.drawString(getTileUL(16).x+","+getTileUL(16).y, 160, 20);
	g.drawString(getTileDR(16).x+","+getTileDR(16).y, 160, 40);
	g.drawString(bounds.x+"", 160, 60);
	g.drawString(bounds.y+"", 160, 80);
	if (MouseInput.isOnScreen()) g.drawLine((int)bounds.getCenterX(), (int) bounds.getCenterY(),(int) MouseInput.getMouseX(),(int) MouseInput.getMouseY());
	g.drawImage(image.getSprite(wSprite, sWidth, sHeight), (int) ( x ), (int) ( y ), null);
	renderBounds(g);
}
}