package game;
import java.awt.Point;
import java.util.Random;
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
	quantity             = Quantity + 1;
	dificcultyMultiplier = Utilities.clamp(dificcultyMultiplier, 0.05f, 0.2f);
	if (dificcultyMultiplier != 0.05f && dificcultyMultiplier != 0.2f) dificcultyMultiplier = 0.1f;
	difficulties = new double[quantity];
	enemiesG     = new boolean[quantity];
	
	for (int i = 0; i < quantity; i++){
		difficulties[i] = initialDificculties + i * i * dificcultyMultiplier;
		enemiesG[i]     = false;
	}
}
public void init(){ hasInitied = true; }
public Point intToPoint(int w, int[] quantities){
	int off = 8;
	w = Utilities.clampSwitch(w, 0, 4);
	if (down != null && w == 0) { 
		return new Point(down.x,down.y + off*quantities[w]);
	}
	if (up != null && w == 1) {
		return new Point(up.x,up.y - off*quantities[w]);
	}
	if (left != null && w == 2) {
		return new Point(left.x - off*quantities[w],left.y);
	}
	if (right != null && w == 3) {
		return new Point(right.x + off*quantities[w],right.y);
	}
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
			GameWaves.currentWaveMaster++;
			GameWaves.WaveNumber.setText(String.valueOf(GameWaves.currentWaveMaster));
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
		
		int[] quantities = new int[] {0,0,0,0};
		
		for (int i = spawnQtd; i > 0; i--){
			int w = r.nextInt(4);
			Point p = intToPoint(w, quantities);
			
			if (p == null) for (int j = 0; j < 4; j++){
				p = intToPoint(j, quantities);
				if (p != null) {
					quantities[j]++;
					break;
				}
			}else quantities[w]++;
			
			if (p == null){
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
	
	if (currentWave + 1 < quantity){
		
		if (GameWaves.WaveBar.isFull() || !GameHandler.exists(ID.Enemy)){
			GameWaves.WaveBar.setFillValue(0);
			GameWaves.time = System.nanoTime();
			currentWave++;
			GameWaves.currentWaveMaster++;
			GameWaves.WaveNumber.setText(String.valueOf(GameWaves.currentWaveMaster));
			return;
		}
	}else if (currentWave + 1 == quantity){
		
		if (!GameHandler.exists(ID.Enemy)){
			GameWaves.WaveBar.setFillValue(100);
			System.out.println("Waves for this state end");
			new GameState();
			hasInitied = false;
			return;
		}
	}

	GameWaves.WaveBar.setFillValue((float) ( ( ( ( ( System.nanoTime() - GameWaves.time ) / 1000000000 ) / waveSeconds ) * 100 ) ));
}
}
