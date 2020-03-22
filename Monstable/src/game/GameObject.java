package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;
import main.Images;
import main.Windows;

public abstract class GameObject{
protected float     x, y, xvel, yvel, Spd;
protected Rectangle bounds;
public Rectangle getBounds(){ return bounds; }
protected ID      id;
protected Images  image;
protected int     wSprite, sWidth, sHeight, hitboxX = 0, hitboxY = 0, life = 0, damage = 0;
protected boolean death = false, visibleBounds = false, collision = false, invertedSprite = false, entitie = false, knockback = false;

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
protected int directionToInt(String pos){
	
	switch(pos){
		case "down":
			return 0;
		case "up":
			return 1;
		case "left":
			return 2;
		case "right":
			return 3;
		case "down-left":
			return 4;
		case "down-right":
			return 5;
		case "upper-left":
			return 6;
		case "upper-right":
			return 7;
		default:
			return 0;
	}
}
protected Point directionToPoint(String pos){
	
	switch(pos){
		case "down":
			return new Point(0,1);
		case "up":
			return new Point(0,-1);
		case "left":
			return new Point(-1,0);
		case "right":
			return new Point(1,0);
		case "down-left":
			return new Point(-1,1);
		case "down-right":
			return new Point(1,1);
		case "upper-left":
			return new Point(-1,-1);
		case "upper-right":
			return new Point(1,-1);
		default:
			return new Point(0,0);
	}
}
protected String checkForDirection(float posx, float posy, int area, boolean withDiagonals){
	if (new Rectangle(0, bounds.y, bounds.x, bounds.height).contains(posx, posy)) return "left";
	else if (new Rectangle(bounds.x + bounds.width, bounds.y, Windows.WIDTH - bounds.x - bounds.width, bounds.height).contains(posx, posy)) return "right";
	else if (new Rectangle(bounds.x, 0, bounds.width, bounds.y).contains(posx, posy)) return "up";
	else if (new Rectangle(bounds.x, bounds.y + bounds.height, bounds.width, Windows.HEIGHT - bounds.y - bounds.height).contains(posx, posy)) return "down";
	else if (new Rectangle(0, 0, bounds.x, bounds.y).contains(posx, posy)){
		if (withDiagonals) return "upper-left";
		double limit = Math.min(Math.ceil(( bounds.y + area * 2 ) / area), Math.ceil(( bounds.x + area * 2 ) / area));
		
		for (int value = 0; value < limit; value++){
			if (new Rectangle(0, bounds.y - ( ( value + 1 ) * area ), bounds.x - ( value * area ), area).contains(posx, posy)) return "left"; // left - up
			else if (new Rectangle(bounds.x - ( ( value + 1 ) * area ), 0, area, bounds.y - ( value * area )).contains(posx, posy)) return "up"; //up - left
		}
	}else if (new Rectangle(bounds.x + bounds.width, 0, Windows.WIDTH - bounds.x - bounds.width, bounds.y).contains(posx, posy)){
		if (withDiagonals) return "upper-right";
		double limit = Math.min(Math.ceil(( bounds.y + area * 2 ) / area), Math.ceil(( Windows.WIDTH - bounds.x - bounds.width + area * 2 ) / area));
		
		for (int value = 0; value < limit; value++){
			if (new Rectangle(bounds.x + bounds.width + ( value * area ), bounds.y - ( ( value + 1 ) * area ), Windows.WIDTH - bounds.x - bounds.width - ( value * area ), area).contains(posx, posy)) return "right"; // right - up
			else if (new Rectangle(bounds.x + bounds.width + ( value * area ), 0, area, bounds.y - ( value * area )).contains(posx, posy)) return "up"; // up - right
		}
	}else if (new Rectangle(0, bounds.y + bounds.height, bounds.x, Windows.HEIGHT - bounds.y - bounds.height).contains(posx, posy)){
		if (withDiagonals) return "down-left";
		double limit = Math.min(Math.ceil(( bounds.x + area * 2 ) / area), Math.ceil(( Windows.HEIGHT - bounds.y - bounds.height + area * 2 ) / area));
		
		for (int value = 0; value < limit; value++){
			if (new Rectangle(0, bounds.y + bounds.height + ( value * area ), bounds.x - ( value * area ), area).contains(posx, posy)) return "left"; // left - down
			else if (new Rectangle(bounds.x - ( ( value + 1 ) * area ), bounds.y + bounds.height + ( value * area ), area, Windows.HEIGHT - bounds.y - bounds.height - ( value * area )).contains(posx, posy)) return "down"; // down - left
		}
	}else if (new Rectangle(bounds.x + bounds.width, bounds.y + bounds.height, Windows.WIDTH - bounds.x - bounds.width, Windows.HEIGHT - bounds.y - bounds.height).contains(posx, posy)){
		if (withDiagonals) return "down-right";
		double limit = Math.min(Math.ceil(( Windows.WIDTH - bounds.x - bounds.width + area * 2 ) / area), Math.ceil(( Windows.HEIGHT - bounds.y - bounds.height + area * 2 ) / area));
		
		for (int value = 0; value < limit; value++){
			if (new Rectangle(bounds.x + bounds.width + ( value * area ), bounds.y + bounds.height + ( value * area ), Windows.WIDTH - bounds.x - bounds.width - ( value * area ), area).contains(posx, posy)) return "right"; // right - down
			else if (new Rectangle(bounds.x + bounds.width + ( value * area ), bounds.y + bounds.height + ( value * area ), area, Windows.HEIGHT - bounds.y - bounds.height - ( value * area )).contains(posx, posy)) return "down"; // down - right
		}
	}
	return null;
}
protected void checkForDeath(){ if (life == 0) death = true; }
protected void autoDestroy(){ GameHandler.objList.remove(this); }
protected void renderBounds(Graphics g){
	
	if (visibleBounds){
		g.setColor(new Color(0, 0, 255, 100));
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		/*
		g.setColor(new Color(255, 255, 255, 130));
		g.fillRect(0,bounds.y,bounds.x,bounds.height); // left
		g.fillRect(bounds.x + bounds.width,bounds.y,Windows.WIDTH - bounds.x - bounds.width,bounds.height); // right
		g.fillRect(bounds.x,0,bounds.width,bounds.y); //up
		g.fillRect(bounds.x,bounds.y + bounds.height,bounds.width,Windows.HEIGHT - bounds.y - bounds.height); // down
		*/
		/*
		g.setColor(new Color(255, 255, 255, 130));
		g.fillRect(0,0,bounds.x,bounds.y); // upper-left
		g.fillRect(bounds.x + bounds.width,0,Windows.WIDTH - bounds.x - bounds.width,bounds.y); // upper-right
		g.fillRect(0,bounds.y + bounds.height, bounds.x,Windows.HEIGHT - bounds.y - bounds.height); // down-left
		g.fillRect(bounds.x + bounds.width,bounds.y + bounds.height,Windows.WIDTH - bounds.x - bounds.width,Windows.HEIGHT - bounds.y - bounds.height); // down-right
		*/
		/*
		g.setColor(new Color(255, 255, 255, 130));
		int area = 1;
		for (int value = 0; value < 200; value++){
			g.fillRect(0, bounds.y - ( ( value + 1 ) * area ), bounds.x - ( value * area ), area); // left - up
			g.fillRect(bounds.x - ( ( value + 1 ) * area ), 0, area, bounds.y - ( value * area )); //up - left
			g.fillRect(bounds.x + bounds.width + ( value * area ), bounds.y - ( ( value + 1 ) * area ), Windows.WIDTH - bounds.x - bounds.width - ( value * area ), area); // right - up
			g.fillRect(bounds.x + bounds.width + ( value * area ), 0, area, bounds.y - ( value * area )); //up - right
			g.fillRect(0, bounds.y + bounds.height + ( value * area ), bounds.x - ( value * area ), area); // left - down
			g.fillRect(bounds.x - ( ( value + 1 ) * area ), bounds.y + bounds.height + ( value * area ), area, Windows.HEIGHT - bounds.y - bounds.height - ( value * area )); // down - left
			g.fillRect(bounds.x + bounds.width + ( value * area ), bounds.y + bounds.height + ( value * area ), Windows.WIDTH - bounds.x - bounds.width - ( value * area ), area); // right - down
			g.fillRect(bounds.x + bounds.width + ( value * area ), bounds.y + bounds.height + ( value * area ), area, Windows.HEIGHT - bounds.y - bounds.height - ( value * area )); // down - right
		}
		*/
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
protected boolean[] getCollisionWithWall(GameObject tO){
	boolean[] collide = new boolean[ ] {false, false};
	if (tO.id != ID.Wall) return collide;
	setBounds(xvel, yvel);
	if (!( bounds.intersects(tO.bounds) )) return collide;
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
		collide[0] = true;
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
		collide[1] = true;
	}
	
	if (tempXvel == xvel && tempYvel == yvel){
		if (new Random().nextInt(2) == 1) xvel = 0;
		else yvel = 0;
		collide[0] = true;
		collide[1] = true;
	}
	return collide;
}
protected abstract void tick();
protected abstract void render(Graphics g);
}
