package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import inputs.KeyInput;
import inputs.KeyObj;
import inputs.MouseInput;
import main.Game;
import main.Windows;

public class Player extends GameObject{
protected boolean     moving           = false;
public static boolean getOneMorePlayer = false, roll = false;
public float          mouseX           = 0, mouseY = 0;
public int rollCount = 0, knockbackCount = 0;
private GameObject attack;
public Player(float x, float y){
	super(x, y, ID.Player, "/Slimesheet.png", 16, 16, 1);
	collision     = true;
	entitie       = true;
	Spd           = 1.5f;
	visibleBounds = true;
	setHitBox(2, 7, 12, 9);
	life = 100;
}
protected void tick(){
	collideX = false;
	collideY = false;
	
	// Movement check
	if(knockback){
		roll = false;
		GameHandler.objList.remove(attack);
		knockbackCount -= rollCount;
		rollCount = 0;
		
		knockbackCount++;
		if (knockbackCount == 9) {
			knockbackCount = 0;
			knockback = false;
		}
	}
	
	if (!roll && !knockback){
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
	}
	if (roll){
		rollCount++;
		
		if (rollCount == 25) {
			rollCount = 0;
			roll = false;
			GameHandler.objList.remove(attack);
		}else if (rollCount == 1) {
			
			double diffX = bounds.getCenterX() - mouseX;
			double diffY = bounds.getCenterY() - mouseY;
			double distance =  Math.sqrt(( bounds.getCenterX() - mouseX ) * ( bounds.getCenterX() - mouseX ) + ( bounds.getCenterY() - mouseY ) * ( bounds.getCenterY() - mouseY )) ;
			xvel = (float) ( -1 / ( distance ) * diffX * (Spd*1.8) );
			yvel = (float) ( -1 / ( distance ) * diffY * (Spd*1.8) );
			
			attack = new Melee(bounds.x + bounds.width, bounds.y, 5, bounds.height, 10,this,checkForDirection(mouseX, mouseY, 10, true));
			GameHandler.objList.add(attack);
				
			
			
			
			
		}
	}
	
	int size = GameHandler.objList.size();
	
	
	
	for (int m = 0; m < size; m++){
		GameObject tO = GameHandler.objList.get(m);
		if (tO == this) continue;
		if (!tO.collision) continue;
		if (filterInTiles(tO)) continue;
		boolean[] c = getCollisionWithWall(tO);
		collideX = c[0] ? true : collideX;
		collideY = c[1] ? true : collideY;
	}
	
	if (collideX && knockback) {
		xvel = xvel > 0 ? -Spd : Spd;
	}
	if (collideY && knockback) {
		yvel = yvel > 0 ? -Spd : Spd;
	}
	
	// Movement Apply
	x = Game.clamp(x + xvel, -hitboxX, Windows.WIDTH - bounds.width - hitboxX);
	y = Game.clamp(y + yvel, -hitboxY, Windows.HEIGHT - bounds.height - hitboxY);
	refreshBounds();
}
protected void render(Graphics g){
	g.setColor(new Color(0, 0, 0, 255));
	g.drawString(getTileUL(16).x+","+getTileUL(16).y, 160, 20);
	g.drawString(bounds.x+","+bounds.y, 160, 40);
	g.drawString("Objects:"+GameHandler.objList.size(), 160, 60);
	g.drawString("HP:"+life, 160, 80);
	
	g.setColor(new Color(255, 255, 255, 130));
	if (MouseInput.isOnScreen()) g.drawLine((int) bounds.getCenterX(), (int) bounds.getCenterY(), (int) MouseInput.getMouseX(), (int) MouseInput.getMouseY());
	renderSprite(g);
	renderBounds(g);
}
}