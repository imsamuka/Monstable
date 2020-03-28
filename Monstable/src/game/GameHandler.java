package game;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

public class GameHandler{
public static LinkedList<GameObject> objList = new LinkedList<GameObject>();
public static Player                 player;

public GameHandler(){
	player = new Player();
	objList.clear();
	if (GameState.currentState > 0) GameState.currentState = 0;
	new GameState();
}
public void tick(){ for (int i = 0; objList != null && i < objList.size(); i++) objList.get(i).tick(); }
public void render(Graphics g){
	for (int i = 0; objList != null && i < objList.size(); i++) objList.get(i).render(g);
	for (int i = 0; objList != null && i < objList.size(); i++) Tile.createFade(objList.get(i), g);
	
}
public static boolean exists(GameObject o){
	int size = objList.size();
	for (int i = 0; i < size; i++) if (objList.get(i) == o) return true;
	return false;
}
public static boolean exists(ID id){
	int size = objList.size();
	for (int i = 0; i < size; i++) if (objList.get(i).id.is(id)) return true;
	return false;
}
public static GameObject getOnPoint(Point p){
	int size = objList.size();
	float x = p.x * GameState.MAPBASE;
	float y = p.y * GameState.MAPBASE;
	
	for (int i = 0; i < size; i++){
		GameObject tO = objList.get(i);
		if (tO.x == x && tO.y == y) return tO;
	}
	return null;
}
public static boolean LineIntersects(double x1, double y1, double x2, double y2, ID id){
	int size = objList.size();
	
	for (int m = 0; m < size; m++){
		GameObject tO = objList.get(m);
		if (tO.id.is(id) && tO.bounds.intersectsLine(x1, y1, x2, y2)) return true;
	}
	return false;
}
public static GameObject getOnPoint(Point p, ID id){
	int size = objList.size();
	float x = p.x * GameState.MAPBASE;
	float y = p.y * GameState.MAPBASE;
	
	for (int i = 0; i < size; i++){
		GameObject tO = objList.get(i);
		if (tO.id.is(id) && tO.x == x && tO.y == y) return tO;
	}
	return null;
}
public static void correctOrder(){
	setPriorityWallsOnTail();
	setEntitiesOnTail();
}
private static void setPriorityWallsOnTail(){
	
	for (int i = 0; i < GameHandler.objList.size(); i++){
		GameObject tO = GameHandler.objList.get(i);
		
		if (( tO.id.is(ID.Wall) ) && ( tO.wSprite == 6 || tO.wSprite == 13 || tO.wSprite == 18 || tO.wSprite == 33 )){
			objList.offerLast(tO);
			objList.removeFirstOccurrence(tO);
		}else{
			objList.offerFirst(tO);
			objList.removeLastOccurrence(tO);
		}
	}
}
public static void setEntitiesOnTail(){
	
	for (int i = 0; i < GameHandler.objList.size(); i++){
		GameObject tO = GameHandler.objList.get(i);
		
		if (tO.entitie || tO.id.is(ID.Attack)){
			objList.offerLast(tO);
			objList.removeFirstOccurrence(tO);
		}
	}
	objList.offerLast(player);
	objList.removeFirstOccurrence(player);
}
}