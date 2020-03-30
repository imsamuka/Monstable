package game;
import java.util.LinkedList;
import java.util.Random;
import audio.AudioPlayer;
import main.Images;
import main.SavingData;

public class GameState{
protected static int          currentState = 0;
public static final int       MAPBASE      = 16;
private static final String[] mapList      = new String[ ] {"/maps/newMap2.png", "/maps/newMap1.png"};
private static final String[] tilesetList  = new String[ ] {"/graphics/tileset_florest.png", "/graphics/tileset_florest.png"};
private static final String[] songList     = new String[ ] {"/sound/thoseofuswhofight.mp3"};
public static int[] alreadyGoMaps = new int[14];
private static Images         map;
public static AudioPlayer     song;
public static int             lastWave     = 0, record = 0, alreadyGoMapsQtd = 0;
public static boolean         arcadeMode   = false;

public static void setLastWave(){
	if (arcadeMode){
		lastWave = GameWaves.currentWaveMaster;
		String lw = String.valueOf(lastWave);
		System.out.println("lastWave: "+lw);
		
		if (lastWave > record){
			record = lastWave;
			SavingData.prop.setProperty("Record", lw);
		}
		GameWaves.lastWave.setText("Last Wave - "+lw);
	}
}
public GameState(){
	String rec = SavingData.prop.getProperty("Record");
	if (rec != null) lastWave = Integer.parseInt(rec);
	else SavingData.prop.setProperty("Record", String.valueOf(lastWave));
	
	if (arcadeMode) {
			boolean getMap = false;
			int r = new Random().nextInt(14);
			
			while(getMap) {
				boolean brake = false;
				
				if (alreadyGoMapsQtd == 15) {
					alreadyGoMapsQtd = 0;
					alreadyGoMaps = new int[14];
				}
				
				r = new Random().nextInt(14);
				
				for (int i = 0; i < alreadyGoMapsQtd; i++)
					if (r == alreadyGoMaps[i]) brake = true;
				if (brake) continue;
				getMap = true;
				alreadyGoMaps[alreadyGoMapsQtd] = r;
				alreadyGoMapsQtd++;
			}
			
			newMap("/maps/newMap"+r+".png",0);
			
			if (!GameHandler.getPointsOfMap()){
				new GameState();
			}
			GameWaves.resetGameWaves();
			new GameWaves(0);
			song = new AudioPlayer(songList[0]);
			song.setBeginning();
		
			
	}else {
		if (currentState < mapList.length){
			newMap(mapList[currentState],currentState);
			
			if (!GameHandler.getPointsOfMap()){
				currentState++;
				new GameState();
			}
			new GameWaves(currentState);
			song = new AudioPlayer(songList[0]);
			song.setBeginning();
		}
	}
	currentState++;
}
public void newMap(String path, int state){
	map                 = new Images(path);
	GameHandler.objList = new LinkedList<GameObject>();
	int[] rgbArray = map.getPixelRGBArray();
	int width = map.getWidth();
	int height = map.getHeight();
	for (int xx = 0; xx < width; xx++) for (int yy = 0; yy
	< height; yy++) GameHandler.objList.add(pixelSwitch(rgbArray[xx + ( yy * map.getWidth() )], xx * MAPBASE, yy * MAPBASE,state));
	GameHandler.correctOrder();
}
public GameObject pixelSwitch(int currentPixel, float x, float y, int state){
	int wSprite = SpriteAndColor(currentPixel, true);
	// if 1 -> Floor
	if (wSprite != 1) switch(wSprite){
		case PLAYERSPRITE:
			GameHandler.player.setPosition(x, y);
			GameHandler.objList.add(GameHandler.player);
		break;
		default:
			return new Tile(x, y, wSprite, tilesetList[state]);
	}
	return new Tile(x, y, 1, tilesetList[state]);
}
public static final int PLAYERSPRITE = 39;
public static int SpriteAndColor(int wSpriteOrHex, boolean wHexToSprite){
	int defaultColor = 0xFF000000;
	int defaultNumber = 1;
	int[] colorsArray = new int[40];
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
	colorsArray[14] = 0xFFDF0AB9;
	colorsArray[15] = 0xFFBC51D2;
	colorsArray[16] = 0xFFB60F5E;
	colorsArray[17] = 0xFF134233;
	colorsArray[18] = 0xFFACCAD5;
	colorsArray[19] = 0xFF2CE82A;
	colorsArray[20] = 0xFFF8AD69;
	colorsArray[21] = 0xFF50495F;
	colorsArray[22] = 0xFF4375EC;
	colorsArray[23] = defaultColor;
	colorsArray[24] = defaultColor;
	colorsArray[25] = defaultColor;
	colorsArray[26] = defaultColor;
	colorsArray[27] = 0xFF150F4D;
	colorsArray[28] = 0xFF697536;
	colorsArray[29] = 0xFFEA4CC4;
	colorsArray[30] = 0xFF697535;
	colorsArray[31] = 0xFFB97A42;
	colorsArray[32] = 0xFFDE6DEB;
	colorsArray[33] = 0xFF44F6E2;
	colorsArray[34] = defaultColor;
	colorsArray[35] = 0xFF44F600;
	colorsArray[36] = 0xFFC15938;
	colorsArray[37] = 0xFF2DAAFF;  
	colorsArray[38] = 0xFFF80641;
	colorsArray[39] = 0xFFFF0000; // PLAYERSPRITE
	
	if (wHexToSprite){
		if (wSpriteOrHex == defaultColor) return defaultNumber;
		int size = colorsArray.length;
		for (int i = 0; i < size; i++) if (colorsArray[i] != 0) if (colorsArray[i] == wSpriteOrHex) return i;
		return defaultNumber;
	}else return colorsArray[wSpriteOrHex];
}
}