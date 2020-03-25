package main;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import game.GameHandler;
import game.GameObject;
import game.ID;
import game.Tile;

public class GameState{
private static int       currentState = 0;
private static final int MAPBASE      = 16;
private final String[]   mapList      = new String[ ] {"/mapdebug.png"};
private static Images                   map;
private static BufferedImage            backgroundTile = null;

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
	backgroundTile = map.getSprite(1, MAPBASE, MAPBASE);
	//if (!Windows.windowed) 
	setBackgroundExtension();
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
public static void setBackgroundExtension(){
	Canvas background = new Canvas();
	background.setBackground(Color.black);
	if (backgroundTile != null && false) {
		int width = (int) ( Windows.WIDTH * Windows.SCALE );
		int height = (int) ( Windows.HEIGHT * Windows.SCALE );
		int mapBaseScaled = MAPBASE * Windows.SCALE;
		Rectangle screen = Windows.getGc().getBounds();
		Rectangle gameScreen = new Rectangle();
		gameScreen.setBounds(screen.x + ( screen.width / 2 ) - ( width / 2 ), screen.y + ( screen.height / 2 ) - ( height / 2 ), width, height);
		
		double topDiff = gameScreen.y - screen.y;
		double bottomDiff = screen.getMaxY() - gameScreen.getMaxY();
		double leftDiff = gameScreen.x - screen.x;
		double rightDiff = screen.getMaxX() - gameScreen.getMaxX();
		
		
		System.out.println("yAxis: "+( Math.ceil(topDiff / mapBaseScaled )));
		System.out.println("xAxis: "+( Math.ceil(leftDiff / mapBaseScaled )));
		
		Graphics g = background.getGraphics();
		
		for (int x = 0; x < screen.width/mapBaseScaled;x++) for (int y = 0; y < screen.height/mapBaseScaled;y++)
			g.drawImage( new Images("/Tileset.png").getSprite(1, MAPBASE, MAPBASE),x,y,null);
	}
	
	Windows.background = background;
}
}