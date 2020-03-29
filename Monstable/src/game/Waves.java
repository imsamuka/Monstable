package game;
import java.awt.Point;
import java.util.Random;
import main.Game;
import main.Utilities;

public class Waves{
public static Point     up          = new Point(7 * GameState.MAPBASE, -1 * GameState.MAPBASE),
down = new Point(7 * GameState.MAPBASE, 16 * GameState.MAPBASE),
left = new Point(-1 * GameState.MAPBASE, 7 * GameState.MAPBASE),
right = new Point(16 * GameState.MAPBASE, 7 * GameState.MAPBASE);
public final static int waveSeconds = 10;
public int              quantity;
public double[]         difficulties;
public boolean[]        enemiesG;
public int              currentWave = -1;
public boolean          hasInitied  = false;
public Random           r           = new Random();

public Waves(int Quantity, double initialDificculties, float dificcultyMultiplier){
	quantity             = Quantity;
	dificcultyMultiplier = Utilities.clamp(dificcultyMultiplier, 0.05f, 0.2f);
	if (dificcultyMultiplier != 0.05f && dificcultyMultiplier != 0.2f) dificcultyMultiplier = 0.1f;
	difficulties = new double[Quantity];
	enemiesG     = new boolean[Quantity];
	
	for (int i = 0; i < Quantity; i++){
		difficulties[i] = initialDificculties + i * i * dificcultyMultiplier;
		enemiesG[i]     = false;
	}
}
public void init(){ hasInitied = true; }
public Point intToPoint(int w) {
w = Utilities.clampSwitch(w, 0, 4);
if (w == 0) return down;
if (w == 1) return up;
if (w == 2) return left;
if (w == 3) return right;
return null;
}
public void tick(){
	if (!hasInitied) return;
	
	if (currentWave == -1){
		GameWaves.WaveBar.moreFillValue(1);
		
		if (GameWaves.WaveBar.isFull()){
			GameWaves.WaveBar.setFillValue(0);
			GameWaves.time = System.nanoTime();
			currentWave++;
		}else return;
	}
	
	if (!enemiesG[currentWave]){
		enemiesG[currentWave] = true;
		int value1 = (int) ( ( r.nextInt((int) ( 49 )) + 51 ) * difficulties[currentWave] );
		int spawnQtd = 1;
		if (value1 > 10) spawnQtd++;
		if (value1 > 40) spawnQtd++;
		if (value1 > 70) spawnQtd++;
		if (value1 > 100) spawnQtd++;
		if (value1 > 150) spawnQtd++;
		if (value1 > 300) spawnQtd++;
		
		for (int i = spawnQtd; i > 0; i--){
			int w = r.nextInt(4);
			Point p = intToPoint(w);
				
			if (p == null) for (int j = 0; j < 4;j++) {
				p = intToPoint(j);
				if (p != null) break;
			}
			
			if (p == null) {
				GameWaves.WaveBar.setFillValue(100);
				System.out.println("No Entrances");
				new GameState();
				hasInitied = false;
			}
			
			int value2 = (int) ( ( r.nextInt((int) ( 30 )) + 1 ) * difficulties[currentWave] );
			Enemy.Opt option = Enemy.Opt.melee;
			if (value2 > 20) option = Enemy.Opt.fastMelee;
			if (value2 > 40) option = Enemy.Opt.zombie;
			if (value2 > 60) option = Enemy.Opt.mage;
			GameHandler.objList.add(new Enemy(p.x, p.y, option));
		}
	}
	
	if (GameWaves.WaveBar.isFull() || !GameHandler.exists(ID.Enemy)){
		
		if (currentWave + 1 < quantity){
			GameWaves.WaveBar.setFillValue(0);
			GameWaves.time = System.nanoTime();
			currentWave++;
		}else if (currentWave + 1 == quantity){
			GameWaves.WaveBar.setFillValue(100);
			System.out.println("Waves for this state end");
			new GameState();
			hasInitied = false;
		}
		return;
	}
	GameWaves.WaveBar.setFillValue((float) ( ( ( ( ( System.nanoTime() - GameWaves.time ) / 1000000000 ) / waveSeconds ) * 100 ) ));
}
}
