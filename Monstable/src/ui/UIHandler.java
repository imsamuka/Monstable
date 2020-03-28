package ui;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import audio.AudioPlayer;
import game.GameState;
import game.GameWaves;
import main.Game;

public class UIHandler{
public static UIStates             uiState     = UIStates.MainMenu;
public static LinkedList<UIObject> objList;
private int                        size        = 0;
private UIStates                   tempuiState = null;
public static AudioPlayer          menuSong    = new AudioPlayer("/sound/anending.mp3");

public UIHandler(){ UIHandler.menuSong.loop(); }
public void tick(){
	
	if (tempuiState != uiState){
		new UIList();
		objList = UIList.getList(uiState);
		size    = objList.size();
	}
	for (int i = 0; i < size; i++) objList.get(i).tick();
	tempuiState = uiState;
}
public void render(Graphics g){ for (int i = 0; i < size; i++) objList.get(i).render(g); }
public static void enterPause(){
	UIHandler.uiState    = UIStates.Pause;
	GameWaves.timeBackup = System.nanoTime();
	GameState.song.stop();
}
public static void leavePause(){
	UIHandler.uiState  = UIStates.Game;
	GameWaves.time    += System.nanoTime() - GameWaves.timeBackup;
	GameState.song.continueSong();
}
public static void enterGame(){
	Game.getNewGameHandler();
	UIHandler.uiState = UIStates.Game;
	UIHandler.menuSong.stop();
	GameState.song.loop();
}
public static void returnToMainMenu(){
	UIHandler.menuSong.setBeginning();
	UIHandler.menuSong.loop();
	GameWaves.resetGameWaves();
	UIHandler.uiState = UIStates.MainMenu;
}
public static Font loadFont(String path, float size){
	
	try{
		return Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(Font.PLAIN, size);
	}
	catch(FontFormatException | IOException e){
		e.printStackTrace();
		System.exit(1);
	}
	return null;
}
}