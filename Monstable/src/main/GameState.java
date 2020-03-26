package main;
import audio.AudioPlayer;
import game.CommonEnemy;
import game.GameHandler;
import game.GameObject;
import game.ID;
import game.Tile;

public class GameState{
private static int       currentState = 0;
public static final int MAPBASE      = 16;
private static final String[]   mapList      = new String[ ] {"/maps/mapdebug.png"};
private static final String[]   songList      = new String[ ] {"/sound/theworld.mp3"};
private static Images                   map;
public static AudioPlayer              song;
//public static final MySubject subject = new MySubject();//


public GameState(){
	if (currentState < mapList.length) {
		newMap(mapList[currentState]);
		song = new AudioPlayer(songList[currentState]);
	}
	
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
	//if (!Windows.windowed) subject.setBackgroundTile(map.getSprite(1, MAPBASE, MAPBASE));
}
public GameObject pixelSwitch(int currentPixel, float x, float y){
	
	switch(currentPixel){
		case 0xFF2DAAFF:
			GameHandler.player.setPosition(x, y);
			GameHandler.objList.add(GameHandler.player);
		break;
		case 0xFFFF0000:
			GameHandler.objList.add(new CommonEnemy(x, y, CommonEnemy.Opt.Melee));
		break;
		case 0xFFFFFFFF:
			return new Tile(x, y, ID.Wall, 2, "/graphics/Tileset.png");
	}
	return new Tile(x, y, ID.Floor, 1, "/graphics/Tileset.png");
}

}