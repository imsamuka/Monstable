package game;
import java.awt.Graphics;
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
	if (option == null) {
		autoDestroy();
		return;
	}else if (option == Opt.Goop) {
		image = new Images("/assets.png");
		sWidth = 16;
		sHeight = 16;
		wSprite = 8;
		Shift = 0;
		setHitBox(2, 2, 12, 12);
		x -= 2 + 6;
		y -= 2 + 6;
	}
	
	MainSprite = wSprite;
	Spd        = Speed;
	damage     = Damage;
	
	collision     = true;
	//visibleBounds = true;
	double diffX = bounds.getCenterX() - toX;
	double diffY = bounds.getCenterY() - toY;
	double distance = (double) ( Math.sqrt(( bounds.getCenterX() - toX ) * ( bounds.getCenterX() - toX ) + ( bounds.getCenterY() - toY ) * ( bounds.getCenterY() - toY )) );
	xvel = (float) ( -1 / ( distance ) * diffX * Spd );
	yvel = (float) ( -1 / ( distance ) * diffY * Spd );
}
protected void tick(){
	if (tempX == x || tempY == y) autoDestroy();
	int size = GameHandler.objList.size();
	
	for (int m = 0; m < size; m++){
		GameObject tO = GameHandler.objList.get(m);
		if (tO == this || tO == GameHandler.player) continue;
		if (!tO.collision) continue;
		if (filterInTiles(tO)) continue;
		
		if (tO.entitie && tO.bounds.intersects(bounds)){
			tO.life -= damage;
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
	renderSprite(g);
	renderBounds(g);
}
}