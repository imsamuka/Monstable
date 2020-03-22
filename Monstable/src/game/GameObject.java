package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;
import main.Images;

public abstract class GameObject{
protected float     x, y, xvel, yvel, Spd;
protected Rectangle bounds;
protected ID        id;
protected Images    image;
protected int       wSprite, sWidth, sHeight, hitboxX = 0, hitboxY = 0, life = 0, damage = 0;
protected boolean   death = false, visibleBounds = false, collision = false, invertedSprite = false;

protected GameObject(float x, float y, ID id, String spritesheet, int sWidth, int sHeight, int wSprite){
	this.x  = x;
	this.y  = y;
	this.id = id;
	
	if (spritesheet != null){
		image = new Images(spritesheet);
		image.getEverySprite(sWidth, sHeight);
	}
	this.sWidth  = sWidth;
	this.sHeight = sHeight;
	this.wSprite = wSprite;
	bounds       = new Rectangle((int) x, (int) y, sWidth, sHeight);
}
protected void refreshBounds(){
	bounds.x = (int) x + hitboxX;
	bounds.y = (int) y + hitboxY;
}
protected void setBounds(float vx, float vy){
	bounds.x = (int) ( x + hitboxX + vx );
	bounds.y = (int) ( y + hitboxY + vy );
}
protected void setHitBox(int x, int y, int width, int height){
	hitboxX       = x;
	hitboxY       = y;
	bounds.width  = width;
	bounds.height = height;
	refreshBounds();
}
protected void checkForDeath(){ if (life == 0) death = true; }
protected void renderBounds(Graphics g){
	
	if (visibleBounds){
		g.setColor(new Color(0, 0, 255, 100));
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
}
protected void renderSprite(Graphics g){
	if (invertedSprite) g.drawImage(image.getSprite(wSprite, sWidth, sHeight), (int) ( x + sWidth ), (int) ( y ), -sWidth, sHeight, null);
	else g.drawImage(image.getSprite(wSprite, sWidth, sHeight), (int) ( x ), (int) ( y ), null);
}
public Point getTileUL(int grid){
	return new Point((int) Math.floor(bounds.x / grid), (int) Math.floor(bounds.y / grid));
}
public Point getTileDR(int grid){
	return new Point((int) Math.floor(( bounds.getMaxX() - 1 ) / grid), (int) Math.floor(( bounds.getMaxY() - 1 ) / grid));
}
protected boolean filterInTiles(GameObject tO){
	
	if (getTileUL(16).x != getTileDR(16).x){
		if (!( tO.bounds.x >= getTileUL(16).x * 16 )) return true;
		if (!( tO.bounds.x + tO.bounds.width - 1 <= ( getTileDR(16).x + 1 ) * 16 )) return true;
	}else{
		if (!( tO.bounds.x >= ( getTileUL(16).x - 1 ) * 16 )) return true;
		if (!( tO.bounds.x + tO.bounds.width - 1 <= ( getTileDR(16).x + 2 ) * 16 )) return true;
	}
	
	if (getTileUL(16).y != getTileDR(16).y){
		if (!( tO.bounds.y >= getTileUL(16).y * 16 )) return true;
		if (!( tO.bounds.y + tO.bounds.height - 1 <= ( getTileDR(16).y + 1 ) * 16 )) return true;
	}else{
		if (!( tO.bounds.y >= ( getTileUL(16).y - 1 ) * 16 )) return true;
		if (!( tO.bounds.y + tO.bounds.height - 1 <= ( getTileDR(16).y + 2 ) * 16 )) return true;
	}
	return false;
}
protected void getCollisionWithWall(GameObject tO){
	if (tO.id != ID.Wall) return;
	setBounds(xvel, yvel);
	if (!( bounds.intersects(tO.bounds) )) return;
	tO.visibleBounds = true;
	float tempXvel = xvel;
	float tempYvel = yvel;
	setBounds(xvel, 0);
	
	if (tO.bounds.intersects(bounds)){
		refreshBounds();
		
		if (xvel > 0){
			int a = (int) bounds.getMaxX();
			int b = tO.bounds.x;
			xvel = b - a;
		}else if (xvel < 0){
			int a = bounds.x;
			int b = (int) tO.bounds.getMaxX();
			xvel = -( a - b );
		}
	}
	setBounds(0, yvel);
	
	if (tO.bounds.intersects(bounds)){
		refreshBounds();
		
		if (yvel > 0){
			int a = (int) bounds.getMaxY();
			int b = tO.bounds.y;
			yvel = b - a;
		}else if (yvel < 0){
			int a = bounds.y;
			int b = (int) tO.bounds.getMaxY();
			yvel = -( a - b );
		}
	}
	
	if (tempXvel == xvel && tempYvel == yvel){
		if (new Random().nextInt(2) == 1) xvel = 0;
		else yvel = 0;
	}
}
protected abstract void tick();
protected abstract void render(Graphics g);
}
