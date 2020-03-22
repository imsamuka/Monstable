package game;
import java.awt.Graphics;
import main.Game;
import main.Windows;

public class Goop extends GameObject{
float tempX, tempY;

public Goop(float mouseX, float mouseY){
	super((float) ( GameHandler.player.bounds.getCenterX() - 2 - 6 ), (float) ( GameHandler.player.bounds.getCenterY() - 2 - 6 ), ID.Goop, "/assets.png", 16, 16, 8);
	collision     = true;
	visibleBounds = true;
	Spd           = 3f;
	setHitBox(2, 2, 12, 12);
	double diffX = bounds.getCenterX() - mouseX;
	double diffY = bounds.getCenterY() - mouseY;
	double distance = (double) ( Math.sqrt(( bounds.getCenterX() - mouseX ) * ( bounds.getCenterX() - mouseX ) + ( bounds.getCenterY() - mouseY ) * ( bounds.getCenterY() - mouseY )) );
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
		if (tO.entitie && tO.bounds.intersects(bounds)) tO.life -= damage;
		getCollisionWithWall(tO);
	}
	tempX = x;
	tempY = y;
	x     = Game.clamp(x + xvel, -hitboxX, Windows.WIDTH - bounds.width - hitboxX);
	y     = Game.clamp(y + yvel, -hitboxY, Windows.HEIGHT - bounds.height - hitboxY);
	refreshBounds();
}
protected void render(Graphics g){
	renderSprite(g);
	renderBounds(g);
}
}
