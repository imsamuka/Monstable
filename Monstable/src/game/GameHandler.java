package game;
import java.awt.Graphics;
import java.util.LinkedList;

public class GameHandler{

public static LinkedList<GameObject> objList = new LinkedList<GameObject>();
private int size;

public GameHandler(){
	for (int x = 0; x < 9; x++ ) for (int y = 0; y < 9; y++ ) objList.add(new Tile(x*16, y*16, ID.Floor, main.Game.isEven(x+y) ? 1 : 2));
	
	objList.add(new Tile(1*16, 1*16, ID.Wall, 3));
	objList.add(new Tile(1*16, 2*16, ID.Wall, 3));
	objList.add(new Tile(0*16, 2*16, ID.Wall, 3));
	
	
	objList.add(new Player(66- 16*4, 64 - 16*4));
	
	size = objList.size();
}
public void tick(){
	for (int i = 0; i < size; i++) {
		objList.get(i).tick();
	}
}
public void render(Graphics g){
	
	for (int i = 0; i < size; i++) {
		objList.get(i).render(g);
	}
	
	
}
}