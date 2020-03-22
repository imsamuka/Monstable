package ui;
import java.awt.Graphics;
import java.util.LinkedList;

public class UIHandler{
public static UIStates       uiState     = UIStates.MainMenu;
public static LinkedList<UIObject> objList;
private int                  size        = 0;
private UIStates             tempuiState = null;

public UIHandler(){ new UIList(); }
public void tick(){
	
	if (tempuiState != uiState){
		objList = UIList.getList(uiState);
		size    = objList.size();
	}
	for (int i = 0; i < size; i++) objList.get(i).tick();
	tempuiState = uiState;
}
public void render(Graphics g){ for (int i = 0; i < size; i++) objList.get(i).render(g); }

}