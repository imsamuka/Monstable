package game;
import java.awt.Graphics;
import java.awt.Point;

public class Melee extends GameObject{
protected GameObject parent;
protected Point direction;
protected int size;

protected Melee(float x, float y, int sWidth, int sHeight, int damage, GameObject parent, String direction){
	super(x, y, ID.Melee, null, sWidth, sHeight, 0);
	this.damage = damage;
	this.parent = parent;
	//visibleBounds = true;
	size = 4;
	if (direction != null) {
		this.direction = directionToPoint(direction);
		setHitBox(0, 0, parent.bounds.width, parent.bounds.height);
	}
}
protected void tick(){
	if (death) {
		autoDestroy();
		return;
	}
	
	
	if (direction != null) {
		x = parent.bounds.x + size * direction.x;
		y = parent.bounds.y + size * direction.y;
	}
	
	
	refreshBounds();
	
	collideX = false;
	
	int size = GameHandler.objList.size();
	for (int m = 0; m < size; m++){
		GameObject tO = GameHandler.objList.get(m);
		if (tO == this) continue;
		if (tO == parent) continue;
		if (!tO.collision) continue;
		if (filterInTiles(tO)) continue;
		if (tO.entitie && tO.bounds.intersects(bounds)){
			tO.life -= damage;
			death = true;
			return;
		}
		if (tO.id == ID.Wall && tO.bounds.intersects(bounds)) collideX = true;
		
	}
	
	if (collideX) parent.waitKnockback = true;
	else parent.waitKnockback = false;
	
	
}
protected void render(Graphics g){renderBounds(g);}
}