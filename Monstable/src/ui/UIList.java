package ui;
import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.Vector;
import main.Game;
import main.Windows;
import ui.UIObject.animations;

public class UIList{
private static Vector<UIObject> masterlist;
private static Font             font1 = new Font("Arial", Font.PLAIN, 18);

public UIList(){
	masterlist = new Vector<UIObject>(30);
	UIObject obj;
	// Add Buttons and UI Elements Here
	//
	// MAIN MENU
	obj = new ObjButton(20, 20, 80, 30, UIStates.MainMenu, new OnClick(){
	public void onClick(){ UIHandler.uiState = UIStates.Game; }
	});
	obj.setText("Play", Color.black, Color.red, Color.GRAY, font1);
	obj.setAnimation(animations.slideUp, 60);
	masterlist.add(obj);
	obj = new ObjButton(20, 60, 80, 30, UIStates.MainMenu, new OnClick(){
	public void onClick(){ UIHandler.uiState = UIStates.Options; }
	});
	obj.setText("Options", Color.black, Color.red, Color.GRAY, font1);
	obj.setAnimation(animations.slideRight, 60);
	masterlist.add(obj);
	obj = new ObjButton(20, 100, 80, 30, UIStates.MainMenu, null);
	obj.setText("Credits", Color.black, Color.red, Color.GRAY, font1);
	obj.setAnimation(animations.slideLeft, 60);
	masterlist.add(obj);
	obj = new ObjButton(20, 140, 80, 30, UIStates.MainMenu, new OnClick(){
	public void onClick(){ System.exit(1); }
	});
	obj.setText("Exit", Color.black, Color.red, Color.GRAY, font1);
	obj.setAnimation(animations.slideDown, 60);
	masterlist.add(obj);
	// OPTIONS
	obj = new ObjButton(20, 20, 80, 30, UIStates.Options, new OnClick(){
	public void onClick(){ UIHandler.uiState = UIStates.MainMenu; }
	});
	obj.setText("Return", Color.black, Color.red, Color.GRAY, font1);
	masterlist.add(obj);
	masterlist.add(new ObjText(78, 65, UIStates.Options, "Game Scale", Color.black, font1));
	obj = new ObjButton(20, 80, 30, 30, UIStates.Options, new OnClick(){
	public void onClick(){
		int tempScale = Windows.SCALE;
		Windows.SCALE = Game.clamp(Windows.SCALE - 1, 1, Windows.getMaxScale());
		if (tempScale != Windows.SCALE) Game.getNewWindow();
	}
	});
	obj.setText("-", Color.black, Color.red, Color.GRAY, font1);
	masterlist.add(obj);
	obj = new ObjButton(60, 80, 30, 30, UIStates.Options, new OnClick(){
	public void onClick(){
		int tempScale = Windows.SCALE;
		Windows.SCALE = Game.clamp(Windows.SCALE + 1, 1, Windows.getMaxScale());
		if (tempScale != Windows.SCALE)  Game.getNewWindow();
	}
	});
	obj.setText("+", Color.black, Color.red, Color.GRAY, font1);
	masterlist.add(obj);
	obj = new ObjButton(100, 80, 40, 30, UIStates.Options, new OnClick(){
	public void onClick(){
		if (Windows.SCALE != Windows.getMaxScale()) {
			Windows.SCALE = Windows.getMaxScale();
			Game.getNewWindow();
		}
	}
	});
	obj.setText("Max", Color.black, Color.red, Color.GRAY, font1);
	masterlist.add(obj);
	masterlist.add(new ObjText(78, 125, UIStates.Options, "FullScreen", Color.black, font1));
	obj = new ObjButton(20, 140, 30, 30, UIStates.Options, new OnClick(){
	public void onClick(){
		Windows.windowed = true;
		Game.getNewWindow();
	}
	});
	obj.setText("Off", Color.black, Color.red, Color.GRAY, font1);
	masterlist.add(obj);
	obj = new ObjButton(60, 140, 30, 30, UIStates.Options, new OnClick(){
	public void onClick(){
		Windows.windowed = false;
		Game.getNewWindow();
	}
	});
	obj.setText("On", Color.black, Color.red, Color.GRAY, font1);
	masterlist.add(obj);
}
public static LinkedList<UIObject> getList(UIStates id){
	LinkedList<UIObject> list = new LinkedList<UIObject>();
	int size = masterlist.size();
	
	for (int i = 0; i < size; i++){
		
		if (masterlist.get(i).id == id){ list.add(masterlist.get(i)); }
	}
	return list;
}
}