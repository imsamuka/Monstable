package game;
import java.awt.Graphics;
import java.awt.Point;
import main.MyObserver;
import main.ObserverInterface;

public class Melee extends GameObject{
protected GameObject parent;
protected Point      direction = directionToPoint(null);
protected int        size;
protected MyObserver observer;

protected Melee(float x, float y, int sWidth, int sHeight, int damage, GameObject parent){
	super(x, y, ID.Melee, null, sWidth, sHeight, 0);
	this.damage   = damage;
	this.parent   = parent;
	visibleBounds = true;
	size          = 4;
	
		observer = new MyObserver(parent.subject, (ObserverInterface) new ObserverInterface(){
			public void OnDirectionChange(String dir){
				direction = directionToPoint(dir); }
	});
		
		
	setHitBox(0, 0, parent.bounds.width, parent.bounds.height);
}
protected void tick(){
	
	if (death || (!GameHandler.exists(parent) && parent != null)){
		autoDestroy();
		return;
	}
	
	if (parent != null) {
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
			if (parent != GameHandler.player && tO == GameHandler.player) tO.takeDamage(damage);
			else if (parent == GameHandler.player) GameHandler.player.newKnockback();
			return;
		}
		if (tO.id == ID.Wall && tO.bounds.intersects(bounds)) collideX = true;
	}
	if (collideX) parent.waitKnockback = true;
	else parent.waitKnockback = false;
}
protected void render(Graphics g){ renderBounds(g); }
}