package game;
import java.awt.Graphics;
import main.Images;
import main.Utilities;
import main.Windows;

public class Projectile extends GameObject{
public static enum Opt{
Goop,
Fire
}

private int Shift = 0, MainSprite;
private float ShiftTime = 1;
private double timer = System.nanoTime();
private Opt option;

public Projectile(float fromX, float fromY, float toX, float toY, Opt option){
	super(fromX, fromY, ID.Projectile, null, 16, 16, 1);
	
	if (option == null){
		autoDestroy();
		return;
	}else if (option == Opt.Goop){
		GameHandler.player.stamina -= GameHandler.player.goopCost;
		image                       = new Images("/graphics/sheet_slime.png");
		wSprite                     = 119;
		Shift                       = 0;
		x                          -= 7 + 2;
		y                          -= 8 + 1.5;
		setHitBox(7, 8, 4, 3);
		Spd = 3;
		damage = 10;
	}
	else if (option == Opt.Fire) {
		image                       = new Images("/graphics/sheet_enemy.png");
		wSprite                     = 65;
		Shift                       = 3;
		ShiftTime = 0.1f;
		x                          -= 1 + 2.5;
		y                          -= 1 + 2.5;
		setHitBox(1, 1, 5, 5);
		Spd = 2.6f;
		damage = 10;
	}
	this.option = option;
	MainSprite  = wSprite;
	collision   = true;
	//visibleBounds = true;
	goFromTo((float) bounds.getCenterX(), (float) bounds.getCenterY(), toX, toY, Spd);
}
protected void tick(){
	
	if (collideX || collideY || death){
		autoDestroy();
		return;
	}
	int size = GameHandler.objList.size();
	float tempXvel = xvel, tempYvel = yvel;
	
	for (int m = 0; m < size; m++){
		GameObject tO = GameHandler.objList.get(m);
		if (tO == this) continue;
		if (!tO.collision) continue;
		if (filterInTiles(tO)) continue;
		if (tO.id.is(ID.Wall) && tO.bounds.intersects(bounds)) death = true;
		
		if (tO.entitie && tO.bounds.intersects(bounds)){
			if (option == Opt.Goop){
				if (tO == GameHandler.player) continue;
			}else if (tO != GameHandler.player) continue;
			tO.takeDamage(damage);
			autoDestroy();
			return;
		}
		getCollisionWith(tO, ID.Wall);
	}
	x += tempXvel;
	y += tempYvel;
	if (x >= Windows.WIDTH - bounds.width - hitboxX) death = true;
	else if (x <= -hitboxX) death = true;
	if (y >= Windows.HEIGHT - bounds.height - hitboxY) death = true;
	else if (y <= -hitboxY) death = true;
	x -= tempXvel;
	y -= tempYvel;
	x += xvel;
	y += yvel;
	refreshBounds();
	
	
	
	if (System.nanoTime() - timer > ( ShiftTime ) * ( 1000000000 )){
		timer = System.nanoTime();
		wSprite = Utilities.clampSwitch(++wSprite, MainSprite, MainSprite + Shift);
	}
	
	
	
	
}
protected void render(Graphics g){
	//private double angle;
	//angle         = Math.atan2(toY - bounds.getCenterY(), toX - bounds.getCenterX());
	/*AffineTransform at = AffineTransform.getTranslateInstance(x, y);
	double value1 = angle/Math.PI;	
	double value2 = -4*value1*value1*value1 + 3*value1;
	if (angle < 0) at.rotate(angle,  -angle * bounds.width / 2,  -angle * bounds.height / 2);
	else if (angle > 0) at.rotate(angle, value2*bounds.width/1.5 + angle * bounds.width / 2, -(1/(angle/Math.PI)) +  angle * bounds.height / 2);
	else at.rotate(angle, bounds.width / 2, bounds.height / 2);
	renderSprite(g, at);*/
	renderSprite(g);
	renderBounds(g);
}
}
