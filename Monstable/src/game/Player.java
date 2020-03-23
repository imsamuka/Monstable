package game;
import java.awt.Color;
import java.awt.Graphics;
import inputs.KeyInput;
import inputs.KeyObj;
import inputs.MouseInput;
import main.Game;
import main.Windows;

public class Player extends GameObject{
protected boolean moving = false, roll = false, rollAnimation = false, jumpAnimation = false , idleAnimation = false;

public boolean isRoll(){ return roll; }
public void setRoll(boolean roll){ this.roll = roll; }

public float       mouseX    = 0, mouseY = 0;
public int         rollCount = 0, knockbackCount = 0, frame = 0, interval = 0;
private GameObject attack;
private double     timer1    = System.nanoTime(), timer2 = System.nanoTime(), timer3 = System.nanoTime() , timer4 = System.nanoTime();

private String     direction = "";

public Player(float x, float y){
	super(x, y, ID.Player, "/Slimesheet.png", 16, 16, 1);
	collision     = true;
	entitie       = true;
	Spd           = 1.3f;
	visibleBounds = false;
	setHitBox(2, 7, 12, 9);
	life = 100;
}
protected void tick(){
	
	
	// Movement check
	if (!roll && !knockback){
		KeyInput.correctListOrder();
		moving = KeyInput.isAnyKeyPressed(KeyObj.types.movement);
		if (moving) timer3 = System.nanoTime();
		if (moving && !jumpAnimation){
			frame = 1;
			timer1 = System.nanoTime();
			timer2 = System.nanoTime();
		}
		if (moving && !rollAnimation) jumpAnimation = true;
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
	
	
	int t = 3;
	
	if (knockback){
		roll = false;
		GameHandler.objList.remove(attack);
		if (rollCount != 0 && (8/2 * t) >= rollCount) knockbackCount -= ( (8/2 * t) - rollCount);
		rollCount       = 0;
		rollAnimation = false;
		jumpAnimation = true;
		waitKnockback = false;
		knockbackCount++;
		
		if (knockbackCount >= 8){
			knockbackCount = 0;
			knockback      = false;
			jumpAnimation = false;
			frame = 1;
			timer1 = System.nanoTime();
			timer2 = System.nanoTime();
			timer3 = System.nanoTime();
		}
	}
	
	if (roll){
		rollCount++;
		
		jumpAnimation = false;
		
		
		frame = (int) Math.ceil(rollCount / t) + 1;
		if (rollCount == 8 * t){
			rollCount = 0;
			roll      = false;
			rollAnimation = false;
			frame = 1;
			timer1 = System.nanoTime();
			timer2 = System.nanoTime();
			timer3 = System.nanoTime();
			GameHandler.objList.remove(attack);
		}else if (rollCount == 1){
			rollAnimation = true;
			frame = 1;
			timer1 = System.nanoTime();
			timer2 = System.nanoTime();
			timer3 = System.nanoTime();
			
			double diffX = bounds.getCenterX() - mouseX;
			double diffY = bounds.getCenterY() - mouseY;
			double distance = Math.sqrt(( bounds.getCenterX() - mouseX ) * ( bounds.getCenterX() - mouseX ) + ( bounds.getCenterY() - mouseY ) * ( bounds.getCenterY() - mouseY ));
			xvel      = (float) ( -1 / ( distance ) * diffX * ( Spd * 1.5 ) );
			yvel      = (float) ( -1 / ( distance ) * diffY * ( Spd * 1.5 ) );
			direction = checkForDirection(mouseX, mouseY, 10, false);
			KeyInput.setFirst(direction);
			attack = new Melee(bounds.x + bounds.width, bounds.y, 5, bounds.height, 10, this, checkForDirection(mouseX, mouseY, 10, true));
			GameHandler.objList.add(attack);
		}
	}
	
	
	collideX = false;
	collideY = false;
	int size = GameHandler.objList.size();
	for (int m = 0; m < size; m++){
		GameObject tO = GameHandler.objList.get(m);
		if (tO == this) continue;
		if (!tO.collision) continue;
		if (filterInTiles(tO)) continue;
		getCollisionWithWall(tO);
	}
	
	if (collideX && waitKnockback){
		knockback = true;
		xvel = xvel > 0 ? -Spd : Spd; }
	else if (collideY && waitKnockback){ 
		knockback = true;
		yvel = yvel > 0 ? -Spd : Spd; }
	
	// Movement Apply
	x = Game.clamp(x + xvel, -hitboxX, Windows.WIDTH - bounds.width - hitboxX);
	y = Game.clamp(y + yvel, -hitboxY, Windows.HEIGHT - bounds.height - hitboxY);
	refreshBounds();
}
protected void render(Graphics g){
	
	
	
	if (System.nanoTime() - timer3 > ( 14 ) * ( 1000000000 )){
		idleAnimation = true;
		if (System.nanoTime() - timer4 > ( 0.2 ) * ( 1000000000 )) {
			frame = Game.clampSwitch(frame + 1, 1, 8);
			timer4 = System.nanoTime();
		}
	}else idleAnimation = false;
	if (!idleAnimation) {
		if (jumpAnimation) if (System.nanoTime() - timer2 > ( 0.065 ) * ( 1000000000 )){
			int min = 1;
			int max = 6;
			
			if (knockback) {
				
				
				if (frame + 1 > max){
					frame += 1 - ( max - min + 1 );
					
					if (!moving){
						jumpAnimation = false;
						frame        = 0;
					}
				}else if (frame <= 3 || knockbackCount >= 4) frame = frame + 1;
				
				
				
				
			}else {
				if (frame + 1 > max){
					frame += 1 - ( max - min + 1 );
					
					if (!moving){
						jumpAnimation = false;
						frame        = 0;
					}
				}else frame = frame + 1;
			}
			
			
			
			
			
			timer2 = System.nanoTime();
		}
	
	if (!roll && !jumpAnimation) if (System.nanoTime() - timer1 > ( 0.1 ) * ( 1000000000 )){
		frame = Game.clampSwitch(frame + 1, 1, 4);
		timer1 = System.nanoTime();
	}
	}
	
	
	
	if (idleAnimation) wSprite = frame + 8 * directionToInt(direction);
	else if (rollAnimation) wSprite = 16 + frame + 8 * directionToInt(direction);
	else if (jumpAnimation && !knockback) wSprite = 48 + frame + 8 * directionToInt(KeyInput.getFirst(KeyObj.types.movement).getName());
	else if (jumpAnimation && knockback) wSprite = 48 + frame + 8 * directionToInt(KeyInput.getFirst(KeyObj.types.movement).getName());
	else wSprite = frame + 4 * directionToInt(KeyInput.getFirst(KeyObj.types.movement).getName());
	g.setColor(new Color(0, 0, 0, 255));
	g.drawString("f:"+frame, 230, 20);
	//g.drawString("2:"+frame, 230, 40);
	g.drawString(getTileUL(16).x+","+getTileUL(16).y, 160, 20);
	g.drawString(bounds.x+","+bounds.y, 185, 20);
	g.drawString("Objects:"+GameHandler.objList.size(), 160, 40);
	g.drawString("HP:"+life, 160, 60);
	g.drawString("interval:"+interval, 160, 80);
	
	
	
	if (jumpAnimation) g.drawString("jumpAnimation", 160, 100);
	if (rollAnimation) g.drawString("rollAnimation", 160, 120);
	if (knockback) g.drawString("knockback", 160, 140);
	if (waitKnockback) g.drawString("waitKnockback", 160, 160);
	
	g.setColor(new Color(255, 255, 255, 130));
	if (MouseInput.isOnScreen()) g.drawLine((int) bounds.getCenterX(), (int) bounds.getCenterY(), (int) MouseInput.getMouseX(), (int) MouseInput.getMouseY());
	renderSprite(g);
	renderBounds(g);
}
}