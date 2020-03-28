package game;
import java.awt.Color;
import java.awt.Graphics;
import ui.ObjImage;
import ui.UIObject;
import ui.UIStates;

public class GameWaves{
public static UIObject WaveBar;
public static double   time           = System.nanoTime(), timeBackup = System.nanoTime();
public static int      thisState      = 0;
public static Waves[]  WavesfromState = newWaves();
private static Waves[] newWaves(){ return new Waves[ ] {new Waves(10, 0, 0), new Waves(5, 0, 0)}; }

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
	WaveBar = new ObjImage(5, 40, 50, 5, UIStates.Game, null, 0, 0, 0);
	WaveBar.setFillBar(0, 0, 100, new Color(200, 0, 0, 210), new Color(250, 30, 30, 255), new Color(140, 140, 140, 140));
}
public GameWaves(int state){
	initWaveBar();
	thisState = state;
	WavesfromState[thisState].init();
}
public static void tick(){ WavesfromState[thisState].tick(); }
public static void render(Graphics g){ if (WaveBar != null) WaveBar.render(g); }
}
