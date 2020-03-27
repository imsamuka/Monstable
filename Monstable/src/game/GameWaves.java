package game;
import java.awt.Color;
import java.awt.Graphics;
import main.Game;
import ui.ObjImage;
import ui.UIObject;
import ui.UIStates;

public class GameWaves{
public static final Waves[] WavesfromState = new Waves[ ] {new Waves(1, 0, 0)};
public final static int            waveSeconds    = 10;
public static UIObject      WaveBar;
public static double        time           = System.nanoTime();
private static int currentState = 0;

public GameWaves(int state){
	currentState = state;
	if (WaveBar == null){
		WaveBar = new ObjImage(5, 40, 50, 5, UIStates.Game, null, 0, 0, 0);
		WaveBar.setFillBar(0, 0, 100, new Color(200, 0, 0, 210), new Color(250, 30, 30, 255), new Color(140, 140, 140, 140));
	}
	WavesfromState[currentState].init();
}
public static void tick(){ WavesfromState[currentState].tick(); }
public static void render(Graphics g){ if(WaveBar != null) WaveBar.render(g); }

public static class Waves{
public int      quantity;
public double[] difficulties;
public boolean[] enemiesG;
public int      currentWave = -1;
public boolean  hasInitied  = false;

public Waves(int Quantity, double initialDificculties, float dificcultyMultiplier){
	quantity             = Quantity;
	dificcultyMultiplier = Game.clamp(dificcultyMultiplier, 0.05f, 0.2f);
	if (dificcultyMultiplier != 0.05f && dificcultyMultiplier != 0.2f) dificcultyMultiplier = 0.1f;
	difficulties = new double[Quantity];
	enemiesG = new boolean[Quantity];
	for (int i = 0; i < Quantity; i++) {
		difficulties[i] = initialDificculties + i * i * dificcultyMultiplier;
		enemiesG[i] = false;
	}
	
}
public void init(){ hasInitied = true;}
public void tick(){ if (!hasInitied) return;
if (currentWave == -1){
	WaveBar.moreFillValue(1);
	if (WaveBar.isFull()) {
		WaveBar.setFillValue(0);
		time           = System.nanoTime();
		currentWave++;
	}else return;
}
if (!enemiesG[currentWave]) {
	
	enemiesG[currentWave] = true;
}
if (   (WaveBar.isFull() || GameHandler.exists(ID.Enemy) ) && currentWave + 1 < quantity) {
	WaveBar.setFillValue(0);
	time           = System.nanoTime();
	currentWave++;
	return;
}else if(   (WaveBar.isFull() || GameHandler.exists(ID.Enemy) ) && currentWave + 1 == quantity){
	WaveBar.setFillValue(100);
	System.out.println("Waves for this state end");
	new GameState();
	hasInitied = false;
}


WaveBar.setFillValue((float) ( ((((System.nanoTime() - time)/1000000000)/waveSeconds)*100) ));







}
}
}
