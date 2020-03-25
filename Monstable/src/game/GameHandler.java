package game;
import java.awt.Graphics;
import java.util.LinkedList;
import main.GameState;

public class GameHandler{
public static LinkedList<GameObject> objList = new LinkedList<GameObject>();
public static Player                 player;
private int                          size;

public GameHandler(){
	player = new Player(16 * 7, 16 * 7);
	new GameState();
}
public static boolean exists(GameObject o){
	int size = objList.size();
	for (int i = 0; i < size; i++) if (objList.get(i) == o) return true;
	return false;
}
public static void setEntitiesOnTail(){
	
	for (int i = 0; i < GameHandler.objList.size(); i++){
		GameObject tO = GameHandler.objList.get(i);
		
		if (tO.entitie){
			objList.offerLast(tO);
			objList.removeFirstOccurrence(tO);
		}
	}
	objList.offerLast(player);
	objList.removeFirstOccurrence(player);
}
public void tick(){
	for (int i = 0; i < objList.size(); i++) objList.get(i).tick();
	size = objList.size();
}
public void render(Graphics g){ for (int i = 0; i < size; i++) objList.get(i).render(g); }
}