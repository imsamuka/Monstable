package game;
import java.awt.Graphics;
import java.util.LinkedList;

public class GameHandler{
public static LinkedList<GameObject> objList = new LinkedList<GameObject>();
public static Player                 player;
private int                          size;

public GameHandler(){
	player = new Player(66 - 16 * 1, 64 - 16 * 1);
	for (int x = 0; x < 16; x++) for (int y = 0; y
	< 16; y++) objList.add(new Tile(x * 16, y * 16, ID.Floor, main.Game.isEven(x + y) ? 1 : 2, "/Tileset.png"));
	objList.add(new Tile(1 * 16, 1 * 16, ID.Wall, 3, "/assets.png"));
	objList.add(new Tile(1 * 16, 2 * 16, ID.Wall, 3, "/assets.png"));
	objList.add(new Tile(0 * 16, 2 * 16, ID.Wall, 3, "/assets.png"));
	objList.add(new CommonEnemy(3 * 16, 4 * 16, CommonEnemy.Opt.runner));
	objList.add(new CommonEnemy(4 * 16, 6 * 16, CommonEnemy.Opt.runner));
	
	
	objList.add(player);
}
public static boolean exists(GameObject o){
	int size = objList.size();
	for (int i = 0; i < size; i++) if (objList.get(i) == o) return true;
	return false;
}
public void tick(){
	for (int i = 0; i < objList.size(); i++) objList.get(i).tick();
	size = objList.size();
}
public void render(Graphics g){ for (int i = 0; i < size; i++) objList.get(i).render(g); }
}