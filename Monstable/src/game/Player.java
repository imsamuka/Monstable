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
public static boolean getOneMorePlayer = false, roll = false;
public float mouseX = 0, mouseY = 0;

public Player(float x, float y){
	super(x, y, ID.Player, "/slime.png", 16, 16, 1);
	collision     = true;
	entitie = true;
	Spd           = 1.5f;
	visibleBounds = true;
	setHitBox(2, 7, 12, 9);
}
protected void tick(){
	// Movement check
	if (!roll) {
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
	}else{
		
		
		double diffX = bounds.getCenterX() - mouseX;
		double diffY = bounds.getCenterY() - mouseY;
		double distance = (double) ( Math.sqrt(( x - mouseX ) * ( x - mouseX ) + ( y - mouseY ) * ( y - mouseY )) );
		xvel = (float) ( -1 / ( distance ) * diffX * Spd );
		yvel = (float) ( -1 / ( distance ) * diffY * Spd );
	}
	

	// 
	int size = GameHandler.objList.size();
	
	for (int m = 0; m < size; m++){
		GameObject tO = GameHandler.objList.get(m);
		if (tO == this) continue;
		if (!tO.collision) continue;
		if (filterInTiles(tO)) continue;
		
		tO.visibleBounds = false;
		getCollisionWithWall(tO);
	}
	// Movement Apply
	x = Game.clamp(x + xvel, -hitboxX, Windows.WIDTH - bounds.width - hitboxX);
	y = Game.clamp(y + yvel, -hitboxY, Windows.HEIGHT - bounds.height - hitboxY);
	refreshBounds();
}
protected void render(Graphics g){
	g.setColor(new Color(0, 0, 255, 130));
	g.drawString(getTileUL(16).x+","+getTileUL(16).y, 160, 20);
	g.drawString(getTileDR(16).x+","+getTileDR(16).y, 160, 40);
	g.drawString(bounds.x+","+bounds.y, 160, 60);
	g.drawString(GameHandler.objList.size()+"", 160, 80);
	
	
	g.fillRect(0,bounds.y,bounds.x,bounds.height); // left
	g.fillRect(bounds.x + bounds.width,bounds.y,Windows.WIDTH - bounds.x - bounds.width,bounds.height); // right
	g.fillRect(bounds.x,0,bounds.width,bounds.y); //up
	g.fillRect(bounds.x,bounds.y + bounds.height,bounds.width,Windows.HEIGHT - bounds.y - bounds.height); // down
	
	g.setColor(new Color(255, 255, 255, 130));
	
	g.fillRect(0,0,bounds.x,bounds.y); // upper-left
	g.fillRect(bounds.x + bounds.width,0,Windows.WIDTH - bounds.x - bounds.width,bounds.y); // upper-right
	g.fillRect(0,bounds.y + bounds.height, bounds.x,Windows.HEIGHT - bounds.y - bounds.height); // down-left
	g.fillRect(bounds.x + bounds.width,bounds.y + bounds.height,Windows.WIDTH - bounds.x - bounds.width,Windows.HEIGHT - bounds.y - bounds.height); // down-right
	
	
	
	
	
	int value = 2;
	int area = 1;
	int posi = value*area;
	/*
	g.fillRect(0,bounds.y - posi,bounds.x,area); // left - up
	g.fillRect(bounds.x - posi,0,area,bounds.y); //up - left
	
	g.fillRect(bounds.x + bounds.width,bounds.y - posi,Windows.WIDTH - bounds.x - bounds.width,area); // right - up
	g.fillRect(bounds.x + bounds.width + ((value-1)*area),0,area,bounds.y); //up - right
	
	g.fillRect(0,bounds.y + bounds.height + ((value-1)*area), bounds.x,area); // left - down
	g.fillRect(bounds.x - posi,bounds.y + bounds.height,area,Windows.HEIGHT - bounds.y - bounds.height); // down - left
	
	g.fillRect(bounds.x + bounds.width,bounds.y + bounds.height + ((value-1)*area),Windows.WIDTH - bounds.x - bounds.width,area); // right - down
	g.fillRect(bounds.x + bounds.width + ((value-1)*area),bounds.y + bounds.height,area,Windows.HEIGHT - bounds.y - bounds.height); // down - left
	*/
	
	
	if (MouseInput.isOnScreen()) g.drawLine((int) bounds.getCenterX(), (int) bounds.getCenterY(), (int) MouseInput.getMouseX(), (int) MouseInput.getMouseY());
	renderSprite(g);
	renderBounds(g);
}
}