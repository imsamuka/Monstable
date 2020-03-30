package game;
import java.awt.Color;
import java.awt.Graphics;
import main.Windows;
import ui.ObjImage;
import ui.ObjText;
import ui.UIList;
import ui.UIObject;
import ui.UIStates;

public class GameWaves{
public static UIObject WaveBar, WaveNumber,lastWave, record;
public static double   time           = System.nanoTime(), timeBackup = System.nanoTime();
public static int      thisState      = 0, currentWaveMaster = 0;
public static Waves[]  WavesfromState = newWaves();

private static Waves[] newWaves(){ return new Waves[ ] {new Waves(9, 0, 0), new Waves(5, 0, 0)}; }
public static void resetGameWaves(){
	WavesfromState = newWaves();
	WaveBar        = null;
	initWaveBar();
	thisState  = 0;
	time       = System.nanoTime();
	timeBackup = System.nanoTime();
}

public static void initWaveBar(){
	if (WaveBar != null) return;
	int x = 16*11;
	int y = 0;
	WaveBar = new ObjImage(x, y, 0, 0, UIStates.Game, "/graphics/ui_bars.png", 64, 16, 3, 3);
	WaveBar.setHitBox(1, 6, 47, 4);
	WaveBar.setFillBar(0, 0, 100, new Color(87, 19, 161, 255), new Color(87, 19, 161, 255), null);
	WaveNumber = new ObjText(x + 55, y + 7, UIStates.Game, "0", Color.white, UIList.font2);
	lastWave = new ObjText(Windows.WIDTH / 2, 85, UIStates.Death, "Last Wave - ", Color.black, UIList.alphbeta18);
}
public GameWaves(int state){
	initWaveBar();
	thisState = state;
	getCurrentWaves().init();
}
public static Waves getCurrentWaves(){ return WavesfromState[thisState]; }
public static void tick(){ getCurrentWaves().tick(); }
public static void render(Graphics g){
	if (WaveBar != null){
		WaveBar.render(g);
		WaveNumber.render(g);
	}
}
}
