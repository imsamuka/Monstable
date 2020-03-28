package game;

import java.awt.Point;
import main.Utilities;

public class Waves{
public static final Point up             = new Point(7 * GameState.MAPBASE, -1 * GameState.MAPBASE),
down = new Point(7 * GameState.MAPBASE, 16 * GameState.MAPBASE),
left = new Point(-1 * GameState.MAPBASE, 7 * GameState.MAPBASE),
right = new Point(16 * GameState.MAPBASE, 7 * GameState.MAPBASE);
public final static int   waveSeconds    = 10;
public int       quantity;
public double[]  difficulties;
public boolean[] enemiesG;
public int       currentWave = -1;
public boolean   hasInitied  = false;

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
		GameHandler.objList.add(new CommonEnemy(up.x, up.y, CommonEnemy.Opt.fastMelee));
		GameHandler.objList.add(new CommonEnemy(down.x, down.y, CommonEnemy.Opt.Melee));
		GameHandler.objList.add(new CommonEnemy(left.x, left.y, CommonEnemy.Opt.fastMelee));
		GameHandler.objList.add(new CommonEnemy(right.x, right.y, CommonEnemy.Opt.Melee));
		enemiesG[currentWave] = true;
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
