package main;
import game.GameHandler;
import game.GameObject;
import game.ID;
import game.Tile;
import observer.MySubject;

public class GameState{
private static int       currentState = 0;
public static final int MAPBASE      = 16;
private static final String[]   mapList      = new String[ ] {"/mapdebug.png"};
private static Images                   map;
public static final MySubject subject = new MySubject();


public GameState(){
	if (currentState < mapList.length) newMap(mapList[currentState]);
	currentState++;
}
public void newMap(String path){
	map = new Images(path);
	GameHandler.objList.clear();
	int[] rgbArray = map.getPixelRGBArray();
	int width = map.getWidth();
	int height = map.getHeight();
	for (int xx = 0; xx < width; xx++) for (int yy = 0; yy
	< height; yy++) GameHandler.objList.add(pixelSwitch(rgbArray[xx + ( yy * map.getWidth() )], xx * MAPBASE, yy * MAPBASE));
	GameHandler.setEntitiesOnTail();
	//if (!Windows.windowed) 
	subject.setBackgroundTile(map.getSprite(1, MAPBASE, MAPBASE));
}
public GameObject pixelSwitch(int currentPixel, float x, float y){
	
	switch(currentPixel){
		case 0xFF2DAAFF:
			GameHandler.player.setPosition(x, y);
			GameHandler.objList.add(GameHandler.player);
		break;
		case 0xFFFFFFFF:
			return new Tile(x, y, ID.Wall, 2, "/Tileset.png");
	}
	return new Tile(x, y, ID.Floor, 1, "/Tileset.png");
}

}