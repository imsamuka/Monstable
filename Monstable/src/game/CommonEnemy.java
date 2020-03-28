package game;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import main.Images;
import main.Utilities;
import main.Windows;

public class CommonEnemy extends GameObject{
public static enum Opt{
fastMelee,
Melee,
}

@SuppressWarnings("unused")
private Opt     option;
private boolean openPath = true, entered = false, isMoving = false, attackAnimation = false;
private int     Human, frame = 0;
private double timer1 = System.nanoTime();
private String  direction;

public CommonEnemy(float x, float y, Opt option){
	super(x, y, ID.Enemy, null, 16, 16, 1);
	
	if (option == null){
		autoDestroy();
		return;
	}else if (option == Opt.Melee){
		image   = new Images("/graphics/Enemysheet.png");
		Human   = 0;
		wSprite = 1;
		setHitBox(2, 7, 8, 9);
		life   = 40;
		Spd    = 1;
		damage = 10;
		GameHandler.objList.add(new Melee((float) bounds.getCenterX(), (float) bounds.getCenterX(), bounds.width, bounds.height, damage, this));
	}else if (option == Opt.fastMelee){
		image   = new Images("/graphics/Enemysheet.png");
		Human   = 1;
		wSprite = 17;
		setHitBox(2, 7, 8, 9);
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
	
	if (attacked){
		attacked        = false;
		attackAnimation = true;
	}
	float herex = (float) bounds.getCenterX();
	float herey = (float) bounds.getCenterY();
	float playerx = (float) GameHandler.player.getBounds().getCenterX();
	float playery = (float) GameHandler.player.getBounds().getCenterY();
	if (herex != playerx
	&& herey != playery) openPath = !GameHandler.LineIntersects(herex, herey, playerx, playery, ID.Wall);
	
	if (!entered){
		
		if (x < -hitboxX + 1 * GameState.MAPBASE){
			xvel      = Spd;
			direction = "right";
		}else if (y < -hitboxY + 1 * GameState.MAPBASE){
			yvel      = Spd;
			direction = "down";
		}else if (x > Windows.WIDTH - bounds.width - hitboxX - 1 * GameState.MAPBASE){
			xvel      = -Spd;
			direction = "left";
		}else if (y > Windows.HEIGHT - bounds.height - hitboxY - 1 * GameState.MAPBASE){
			yvel      = -Spd;
			direction = "up";
		}else entered = true;
	}else if (openPath){
		goFromTo(herex, herey, playerx, playery, Spd);
		subject.setDirection(checkForDirection((float) GameHandler.player.getBounds().getCenterX(), (float) GameHandler.player.getBounds().getCenterY(), 10, true));
		direction = checkForDirection((float) GameHandler.player.getBounds().getCenterX(), (float) GameHandler.player.getBounds().getCenterY(), 10, false);
	}else{
		int MapBase = GameState.MAPBASE;
		Point closestPoint = getTileUL(MapBase);
		Rectangle closestBounds = PointToRectangle(closestPoint, MapBase);
		
		if (getTileDR(MapBase) != getTileUL(MapBase)){
			double distance1 = Math.sqrt(( herex - closestBounds.getCenterX() ) * ( herex - closestBounds.getCenterX() ) + ( herey - closestBounds.getCenterY() ) * ( herey - closestBounds.getCenterY() ));
			closestBounds = PointToRectangle(getTileDR(MapBase), MapBase);
			double distance2 = Math.sqrt(( herex - closestBounds.getCenterX() ) * ( herex - closestBounds.getCenterX() ) + ( herey - closestBounds.getCenterY() ) * ( herey - closestBounds.getCenterY() ));
			closestBounds = distance1 > distance2 ? closestBounds : PointToRectangle(getTileUL(MapBase), MapBase);
			closestPoint  = distance1 > distance2 ? getTileDR(MapBase) : closestPoint;
		}
		
		if (herex != (float) closestBounds.getCenterX() && herey != (float) closestBounds.getCenterY()){
			goFromTo(herex, herey, (float) closestBounds.getCenterX(), (float) closestBounds.getCenterY(), Spd);
			subject.setDirection(checkForDirection((float) closestBounds.getCenterX(), (float) closestBounds.getCenterY(), 10, true));
			direction = checkForDirection((float) closestBounds.getCenterX(), (float) closestBounds.getCenterY(), 10, false);
		}else{
			xvel = 0;
			yvel = 0;
			subject.setDirection(null);
			direction = "down";
		}
	}
	int size = GameHandler.objList.size();
	
	for (int m = 0; m < size; m++){
		GameObject tO = GameHandler.objList.get(m);
		if (tO == this) continue;
		if (!tO.collision) continue;
		if (filterInTiles(tO)) continue;
		getCollisionWithWall(tO);
	}
	
	if (herex == playerx && herey == playery){
		xvel      = 0;
		yvel      = 0;
		direction = "down";
	}
	isMoving  = xvel != 0 && yvel != 0 ? true : false;
	x        += xvel;
	y        += yvel;
	refreshBounds();
	checkForDeath();
	
	
	if (isMoving) if (System.nanoTime() - timer1 > ( 0.065 ) * ( 1000000000 )){
			timer1 = System.nanoTime();
			frame = Utilities.clampSwitch(frame + 1, 1, 4);
	}
}
protected void render(Graphics g){
	
	if (isMoving) wSprite = Human*16 + frame + 4 * directionToInt(direction);
	else wSprite = Human*16 + 1;
	

	renderSprite(g);
	renderBounds(g);
}
/*
private static int       currentList = 0;
private ArrayList<Point> list;
list = null;
if (list == null){
	xvel = 0;
	yvel = 0;
	@SuppressWarnings("unchecked")
	ArrayList<Point>[] masterList = (ArrayList<Point>[]) new ArrayList<?>[100];
	checkWallAndAdjacents(closestPoint, playerx, playery, null, masterList, 3);
	subject.setDirection(null);
	currentList = 0;
	
	if (masterList[0] != null){
		int SmallestNumberOfSteps = 255;
		int listID = 0;
		for (int m = 0; m < masterList.length; m++) if (masterList[m] == null) break;
		else if (masterList[m].size() < SmallestNumberOfSteps){
			SmallestNumberOfSteps = masterList[m].size();
			listID                = m;
		}
		list = masterList[listID];
	}
}else{
	
	if (list.size() <= 0){
		subject.setDirection(null);
		if (herex != (float) closestBounds.getCenterX() && herey != (float) closestBounds.getCenterY()) goFromTo(herex, herey, (float) closestBounds.getCenterX(), (float) closestBounds.getCenterY(), Spd);
		else list = null;
	}else{
		Rectangle nextBounds = PointToRectangle(list.get(0), MapBase);
		
		if (herex == (float) nextBounds.getCenterX()
		&& herey == (float) nextBounds.getCenterY()) list.remove(0);
		else{
			goFromTo(herex, herey, (float) nextBounds.getCenterX(), (float) nextBounds.getCenterY(), Spd);
			subject.setDirection(null);
		}
	}
}
public void checkWallAndAdjacents(Point p, float toX, float toY, ArrayList<Point> list, ArrayList<Point>[] masterList, int limit){
	Point[] adjacents = new Point[ ] {new Point(p.x, p.y - 1), // up
	new Point(p.x, p.y + 1), // down
	new Point(p.x - 1, p.y), // left
	new Point(p.x + 1, p.y) // right
	};
	
	for (int i = 0; i < 4; i++){
		if (GameHandler.getOnPoint(adjacents[i], ID.Tile).id.is(ID.Wall)) continue;
		Rectangle tileRect = PointToRectangle(adjacents[i], GameState.MAPBASE);
		boolean openPath = !GameHandler.LineIntersects(tileRect.getCenterX(), tileRect.getCenterY(), toX, toY, ID.Wall);
		
		if (openPath){
			
			if (list == null){
				ArrayList<Point> alist = new ArrayList<Point>(1);
				alist.add(adjacents[i]);
				masterList[currentList] = alist;
			}else masterList[currentList] = list;
			currentList++;
		}else{
			boolean alreadyGo = false;
			
			if (list == null){
				ArrayList<Point> alist = new ArrayList<Point>(256);
				alist.add(adjacents[i]);
				checkWallAndAdjacents(adjacents[i], toX, toY, alist, masterList, limit);
			}else{
				for (int m = 0; m < list.size(); m++) if (adjacents[i].x == list.get(m).x
				&& adjacents[i].y == list.get(m).y){
					alreadyGo = true;
					break;
				}
				if (alreadyGo) continue;
				if (list.size() >= limit) continue;
				ArrayList<Point> alist = new ArrayList<Point>(256);
				alist.addAll(list);
				alist.add(adjacents[i]);
				checkWallAndAdjacents(adjacents[i], toX, toY, alist, masterList, limit);
			}
		}
	}
}*/
}
