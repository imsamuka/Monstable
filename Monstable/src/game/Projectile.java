package game;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import main.Game;
import main.Images;
import main.Windows;

public class Projectile extends GameObject{
public static enum Opt{
Goop,
}

private float tempX, tempY;
private int   Shift = 0, MainSprite;

public Projectile(float fromX, float fromY, float toX, float toY, Opt option, float Speed, int Damage){
	super(fromX, fromY, ID.Projectile, null, 0, 0, 1);
	
	if (option == null){
		autoDestroy();
		return;
	}else if (option == Opt.Goop){
		GameHandler.player.stamina -= GameHandler.player.goopCost;
		image                       = new Images("/Slimesheet.png");
		sWidth                      = 16;
		sHeight                     = 16;
		wSprite                     = 119;
		Shift                       = 0;
		x                          -= 7 + 2;
		y                          -= 8 + 1.5;
		setHitBox(7, 8, 4, 3);
	}
	MainSprite = wSprite;
	Spd        = Speed;
	damage     = Damage;
	collision  = true;
	//visibleBounds = true;
	goFromTo((float) bounds.getCenterX(), (float) bounds.getCenterY(), toX, toY, Spd);
}
protected void tick(){
	if (( tempX == x && tempY == y ) || collideX || collideY) autoDestroy();
	int size = GameHandler.objList.size();
	
	for (int m = 0; m < size; m++){
		GameObject tO = GameHandler.objList.get(m);
		if (tO == this || tO == GameHandler.player) continue;
		if (!tO.collision) continue;
		if (filterInTiles(tO)) continue;
		
		if (tO.entitie && tO.bounds.intersects(bounds)){
			tO.takeDamage(damage);
			autoDestroy();
			return;
		}
		getCollisionWithWall(tO);
	}
	tempX = x;
	tempY = y;
	x     = Game.clamp(x + xvel, -hitboxX, Windows.WIDTH - bounds.width - hitboxX);
	y     = Game.clamp(y + yvel, -hitboxY, Windows.HEIGHT - bounds.height - hitboxY);
	refreshBounds();
	wSprite = Game.clampSwitch(++wSprite, MainSprite, MainSprite + Shift);
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
