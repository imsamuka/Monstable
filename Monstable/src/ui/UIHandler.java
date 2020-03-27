package ui;
import java.awt.Graphics;
import java.util.LinkedList;
import audio.AudioPlayer;

public class UIHandler{
public static UIStates             uiState     = UIStates.MainMenu;
public static LinkedList<UIObject> objList;
private int                        size        = 0;
private UIStates                   tempuiState = null;
public static AudioPlayer                menuSong    = new AudioPlayer("/sound/anending.mp3");

public UIHandler(){}
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
}