package game;
import java.awt.Color;
import java.awt.Graphics;
import inputs.KeyInput;
import inputs.KeyObj;
import inputs.MouseInput;
import main.Game;
import main.Windows;
import ui.ObjImage;
import ui.UIObject;
import ui.UIStates;

public class Player extends GameObject{
protected boolean moving = false, roll = false, jumpAnimation = false, idleAnimation = false;

public boolean isRoll(){ return roll; }
public void setRoll(boolean roll){ this.roll = roll; }

public float       stamina = 0;
public int         rollCount  = 0, knockbackCount = 0, frame = 0, rollQtd = 1;
private GameObject attack;
private UIObject   StaminaBar, LifeBar;
private double     timer1     = System.nanoTime(), timer2 = System.nanoTime(), timer3 = System.nanoTime(),
timer4 = System.nanoTime();
private String     direction  = "";

public Player(float x, float y){
	super(x, y, ID.Player, "/Slimesheet.png", 16, 16, 1);
	collision     = true;
	entitie       = true;
	Spd           = 1.5f;
	visibleBounds = false;
	setHitBox(2, 7, 12, 9);
	life = 100;
	LifeBar = new ObjImage(5, 5, 50, 5, UIStates.Game, null, 0, 0, 0);
	LifeBar.setFillBar(life, 0, 100, new Color(200,0,0,210), new Color(250,30,30,255), new Color(140,140,140,140));
	StaminaBar = new ObjImage(5, 15, 50, 5, UIStates.Game, null, 0, 0, 0);
	StaminaBar.setFillBar(stamina, 0, 30, new Color(0,0,200,210), new Color(30,30,230,255), new Color(140,140,140,140));
}
protected void tick(){
	if (!roll) stamina = Game.clamp( stamina + 1f, 0, 30);
	
	// Movement check
	if (!roll && !knockback){
		KeyInput.correctListOrder();
		moving = KeyInput.isAnyKeyPressed(KeyObj.types.movement);
		if (moving) timer3 = System.nanoTime();
		
		if (moving && !jumpAnimation){
			frame  = 1;
			timer1 = System.nanoTime();
			timer2 = System.nanoTime();
		}
		if (moving && !roll) jumpAnimation = true;
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
	checkKnockback();
	checkRoll();
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
		newKnockback();
		xvel = xvel > 0 ? -Spd : Spd;
	}else if (collideY && waitKnockback){
		newKnockback();
		yvel = yvel > 0 ? -Spd : Spd;
	}
	// Movement Apply
	x = Game.clamp(x + xvel, -hitboxX, Windows.WIDTH - bounds.width - hitboxX);
	y = Game.clamp(y + yvel, -hitboxY, Windows.HEIGHT - bounds.height - hitboxY);
	refreshBounds();
}
protected void render(Graphics g){
	getAnimations();
	// Sprite Choosing
	if (idleAnimation && !roll && !knockback) wSprite = 128 + frame;
	else if (roll) wSprite = 16 + frame + 8 * directionToInt(direction);
	else if (knockback) wSprite = 48 + frame + 8 * directionToInt(KeyInput.getFirst(KeyObj.types.movement).getName());
	else if (jumpAnimation) wSprite = 48 + frame + 8 * directionToInt(KeyInput.getFirst(KeyObj.types.movement).getName());
	else wSprite = frame + 4 * directionToInt(KeyInput.getFirst(KeyObj.types.movement).getName());
	//
	g.setColor(new Color(0, 0, 0, 255));
	g.drawString("f:"+frame, 230, 20);
	g.drawString(getTileUL(16).x+","+getTileUL(16).y, 160, 20);
	g.drawString(bounds.x+","+bounds.y, 185, 20);
	g.drawString("Objects:"+GameHandler.objList.size(), 160, 40);
	g.drawString("HP:"+life, 160, 60);
	//
	g.setColor(new Color(255, 255, 255, 130));
	if (MouseInput.isOnScreen()) g.drawLine((int) bounds.getCenterX(), (int) bounds.getCenterY(), (int) MouseInput.getMouseX(), (int) MouseInput.getMouseY());
	renderSprite(g);
	renderBounds(g);
	StaminaBar.setFillValue(stamina);
	StaminaBar.render(g);
	LifeBar.setFillValue(life);
	LifeBar.render(g);
}
private void getAnimations(){
	
	// IDLE ANIMATION
	if (System.nanoTime() - timer3 > ( 14 ) * ( 1000000000 )){
		if (!idleAnimation) frame = 0;
		idleAnimation = true;
		
		if (System.nanoTime() - timer4 > ( 0.13 ) * ( 1000000000 )){
			timer4 = System.nanoTime();
			frame  = Game.clampSwitch(frame + 1, 1, 8);
		}
		return;
	}else idleAnimation = false;
	
	//OTHER ANIMATIONS
	if (!idleAnimation && !roll){
		if (!jumpAnimation) if (System.nanoTime() - timer1 > ( 0.1 ) * ( 1000000000 )){
			timer1 = System.nanoTime();
			frame  = Game.clampSwitch(frame + 1, 1, 4);
			return;
		}
		if (jumpAnimation) if (System.nanoTime() - timer2 > ( 0.065 ) * ( 1000000000 )){
			timer2 = System.nanoTime();
			int max = 6;
			
			if (frame + 1 > max){
				// Final for jumpAnimation
				if (!moving) jumpAnimation = false;
				frame = 1;
				return;
			}
			
			if (knockback){
				if (frame <= 3 || knockbackCount >= 4) frame = frame + 1;
			}else frame = frame + 1;
		}
	}
}
public void newRoll(float goX, float goY, int rollQtd){
	// Checking Stamina
	if (stamina >= 10) stamina -= 10;
	else return;
	//Setting Defaults
	this.rollQtd = rollQtd;
	roll         = true;
	frame        = 1;
	rollCount    = 1;
	timer3 = System.nanoTime();
	// Search for Direction
	double diffX = bounds.getCenterX() - goX;
	double diffY = bounds.getCenterY() - goY;
	double distance = Math.sqrt(( bounds.getCenterX() - goX ) * ( bounds.getCenterX() - goX ) + ( bounds.getCenterY() - goY ) * ( bounds.getCenterY() - goY ));
	xvel      = (float) ( -1 / ( distance ) * diffX * ( Spd * 1.8 ) );
	yvel      = (float) ( -1 / ( distance ) * diffY * ( Spd * 1.8 ) );
	// Creating Melee attack
	direction = checkForDirection(goX, goY, 10, false);
	KeyInput.setFirst(direction);
	attack = new Melee(bounds.x + bounds.width, bounds.y, 5, bounds.height, 10, this, checkForDirection(goX, goY, 10, true));
	GameHandler.objList.add(attack);
}
private void checkRoll(){
	if (!roll) return;
	rollCount++;
	frame = Game.clampSwitch(rollCount / rollQtd + 1, 1, 8);
	
	
	if (rollCount == 8 * rollQtd){
		// Roll End
		rollCount = 0;
		roll      = false;
		frame     = 1;
		timer1    = System.nanoTime();
		timer2    = System.nanoTime();
		timer3    = System.nanoTime();
		GameHandler.objList.remove(attack);
	}
}
private void newKnockback(){
	knockback     = true;
	waitKnockback = false;
	
	if (roll){
		GameHandler.objList.remove(attack);
		if (rollCount != 0 && ( 8 / 2 * rollQtd ) >= rollCount) knockbackCount -= ( ( 8 / 2 * rollQtd ) - rollCount );
		rollCount     = 0;
		roll          = false;
		jumpAnimation = true;
	}
}
private void checkKnockback(){
	if (!knockback) return;
	knockbackCount++;
	
	if (knockbackCount >= 8){
		knockbackCount = 0;
		knockback      = false;
		if (!moving) jumpAnimation = false;
		frame  = 1;
		timer1 = System.nanoTime();
		timer2 = System.nanoTime();
		timer3 = System.nanoTime();
	}
}
}