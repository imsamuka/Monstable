package game;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import main.Game;
import main.GameState;
import main.Images;
import main.Windows;

public class CommonEnemy extends GameObject{
public static enum Opt{
fastMelee,
Melee,
}

private Opt     option;
private boolean openPath = true;

public CommonEnemy(float x, float y, Opt option){
	super(x, y, ID.Enemy, null, 16, 16, 1);
	
	if (option == null){
		autoDestroy();
		return;
	}else if (option == Opt.Melee){
		image   = new Images("/assets.png");
		sWidth  = 16;
		sHeight = 16;
		wSprite = 8;
		setHitBox(2, 2, 12, 12);
		life   = 40;
		Spd    = 1;
		damage = 10;
		GameHandler.objList.add(new Melee((float) bounds.getCenterX(), (float) bounds.getCenterX(), bounds.width, bounds.height, damage, this));
	}else if (option == Opt.fastMelee){
		image   = new Images("/assets.png");
		sWidth  = 16;
		sHeight = 16;
		wSprite = 9;
		setHitBox(2, 2, 12, 12);
		life   = 20;
		Spd    = 1.5f;
		damage = 7;
		GameHandler.objList.add(new Melee((float) bounds.getCenterX(), (float) bounds.getCenterX(), bounds.width, bounds.height, damage, this));
	}
	//visibleBounds = true;
	this.option = option;
	entitie     = true;
	collision   = true;
}
protected void tick(){
	
	if (death){
		autoDestroy();
		return;
	}
	float herex = (float) bounds.getCenterX();
	float herey = (float) bounds.getCenterY();
	float playerx = (float) GameHandler.player.getBounds().getCenterX();
	float playery = (float) GameHandler.player.getBounds().getCenterY();
	openPath = true;
	int size = GameHandler.objList.size();
	
	for (int m = 0; m < size; m++){
		GameObject tO = GameHandler.objList.get(m);
		
		if (tO.id == ID.Wall && tO.bounds.intersectsLine(herex, herey, playerx, playery)){
			openPath = false;
			break;
		}
	}
	if (openPath) if (option == Opt.Melee || option == Opt.fastMelee){
		goFromTo(herex, herey, playerx, playery, Spd);
		subject.setDirection(checkForDirection((float) GameHandler.player.getBounds().getCenterX(), (float) GameHandler.player.getBounds().getCenterY(), 10, true));
	}
	
	if (!openPath){
		int MapBase = GameState.MAPBASE;
		Rectangle PointBounds = PointToRectangle(getTileUL(MapBase), MapBase);
		
		if (getTileDR(MapBase) != getTileUL(MapBase)){
			double distance1 = Math.sqrt(( herex - PointBounds.getCenterX() ) * ( herex - PointBounds.getCenterX() ) + ( herey - PointBounds.getCenterY() ) * ( herey - PointBounds.getCenterY() ));
			PointBounds = PointToRectangle(getTileDR(MapBase), MapBase);
			double distance2 = Math.sqrt(( herex - PointBounds.getCenterX() ) * ( herex - PointBounds.getCenterX() ) + ( herey - PointBounds.getCenterY() ) * ( herey - PointBounds.getCenterY() ));
			PointBounds = distance1 > distance2 ? PointBounds : PointToRectangle(getTileUL(MapBase), MapBase);
		}
		
		if (herex != PointBounds.getCenterX() || herey != PointBounds.getCenterY()){
			goFromTo(herex, herey, (float) PointBounds.getCenterX(), (float) PointBounds.getCenterY(), Spd);
			subject.setDirection(checkForDirection((float) GameHandler.player.getBounds().getCenterX(), (float) GameHandler.player.getBounds().getCenterY(), 10, true));
		}else{
			xvel = 0;
			yvel = 0;
		}
	}
	
	for (int m = 0; m < size; m++){
		GameObject tO = GameHandler.objList.get(m);
		if (tO == this) continue;
		if (!tO.collision) continue;
		if (filterInTiles(tO)) continue;
		getCollisionWithWall(tO);
	}
	x = Game.clamp(x + xvel, -hitboxX, Windows.WIDTH - bounds.width - hitboxX);
	y = Game.clamp(y + yvel, -hitboxY, Windows.HEIGHT - bounds.height - hitboxY);
	refreshBounds();
	checkForDeath();
}
protected void render(Graphics g){
	renderSprite(g);
	renderBounds(g);
}
}
