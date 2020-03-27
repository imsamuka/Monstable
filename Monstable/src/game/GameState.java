package game;
import audio.AudioPlayer;
import main.Images;

public class GameState{
private static int            currentState = 0;
public static final int       MAPBASE      = 16;
private static final String[] mapList      = new String[ ] {"/maps/newMap.png"};
private static final String[] tilesetList  = new String[ ] {"/graphics/Tileset.png"};
private static final String[] songList     = new String[ ] {"/sound/athletictheme.mp3"};
private static Images         map;
public static AudioPlayer     song;
//public static final MySubject subject = new MySubject();////
//if (!Windows.windowed) subject.setBackgroundTile(map.getSprite(1, MAPBASE, MAPBASE));

public GameState(){
	
	if (currentState < mapList.length){
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
	for (int xx = 0; xx < width; xx++) for (int yy = 0; yy < height; yy++) GameHandler.objList.add(pixelSwitch(rgbArray[xx + ( yy * map.getWidth() )], xx * MAPBASE, yy * MAPBASE));
	GameHandler.correctOrder();
	
}
public GameObject pixelSwitch(int currentPixel, float x, float y){
	int wSprite = SpriteAndColor(currentPixel, true);
	// if 1 -> Floor
	if (wSprite != 1) switch(wSprite){
		case 37:
			GameHandler.player.setPosition(x, y);
			GameHandler.objList.add(GameHandler.player);
		break;
		case 38:
			GameHandler.objList.add(new CommonEnemy(x, y, CommonEnemy.Opt.Melee));
		break;
		default:
			return new Tile(x, y, wSprite, tilesetList[currentState]);
	}
	return new Tile(x, y, 1, tilesetList[currentState]);
}
public static int SpriteAndColor(int wSpriteOrHex, boolean wHexToSprite){
	int defaultColor = 0xFF000000;
	int defaultNumber = 1;
	int[] colorsArray = new int[39];
	colorsArray[0]  = defaultColor;
	colorsArray[1]  = defaultColor;
	colorsArray[2]  = 0xFFFFFFFF;
	colorsArray[3]  = 0xFF33C5CF;
	colorsArray[4]  = 0xFFEF29F9;
	colorsArray[5]  = 0xFF309DCC;
	colorsArray[6]  = 0xFFDA4A0D;
	colorsArray[7]  = 0xFF159772;
	colorsArray[8]  = 0xFF6C50DC;
	colorsArray[9]  = 0xFFA80920;
	colorsArray[10] = 0xFF5722AF;
	colorsArray[11] = 0xFF787A86;
	colorsArray[12] = 0xFFEE0D3F;
	colorsArray[13] = 0xFF0A18B9;
	colorsArray[14] = defaultColor;
	colorsArray[15] = 0xFFBC51D2;
	colorsArray[16] = 0xFFB60F5E;
	colorsArray[17] = defaultColor;
	colorsArray[18] = 0xFFACCAD5;
	colorsArray[19] = 0xFF2CE82A;
	colorsArray[20] = 0xFFF8AD69;
	colorsArray[21] = 0xFF50495F;
	colorsArray[22] = 0xFFF55143;
	colorsArray[23] = 0xFF4375EC;
	colorsArray[24] = defaultColor;
	colorsArray[25] = defaultColor;
	colorsArray[26] = 0xFF150F4D;
	colorsArray[27] = 0xFF8BBEEE;
	colorsArray[28] = 0xFF697536;
	colorsArray[29] = 0xFFEA4CC4;
	colorsArray[30] = 0xFF697535;
	colorsArray[31] = 0xFFB97A42;
	colorsArray[32] = 0xFFDE6DEB;
	colorsArray[33] = 0xFF44F6E2;
	colorsArray[34] = 0xFFFC0A59;
	colorsArray[35] = 0xFF44F6E1;
	colorsArray[36] = 0xFFC15938;
	colorsArray[37] = 0xFF2DAAFF;  // Player
	colorsArray[38] = 0xFFFF0000;  // Enemy
	
	if (wHexToSprite){
		if (wSpriteOrHex == defaultColor) return defaultNumber;
		int size = colorsArray.length;
		for (int i = 0; i < size; i++) if (colorsArray[i] != 0) if (colorsArray[i] == wSpriteOrHex) return i;
		return defaultNumber;
	}else return colorsArray[wSpriteOrHex];
}
}