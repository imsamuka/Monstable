package ui;
import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.Vector;
import main.Game;
import main.Utilities;
import main.Windows;
import ui.UIObject.animations;

public class UIList{
private static Vector<UIObject> masterlist;
private static Font             alphbeta18 = UIHandler.loadFont("res/fonts/alphbeta.ttf", 18);

public static LinkedList<UIObject> getList(UIStates id){
	LinkedList<UIObject> list = new LinkedList<UIObject>();
	int size = masterlist.size();
	
	for (int i = 0; i < size; i++){
		
		if (masterlist.get(i).id == id){ list.add(masterlist.get(i)); }
	}
	return list;
}
public UIList(){
	masterlist = new Vector<UIObject>(30);
	UIObject obj;
	// Add Buttons and UI Elements Here
	//
	//
	//
	//
	//
	//
	//
	// MAIN MENU
	obj = new ObjButton(20, 20, 80, 30, UIStates.MainMenu, new OnClick(){
	public void onClick(){ UIHandler.enterGame(); }
	});
	obj.setText("Play", Color.black, Color.red, Color.GRAY, alphbeta18);
	obj.setAnimation(animations.slideUp, 60);
	masterlist.add(obj);
	obj = new ObjButton(20, 60, 80, 30, UIStates.MainMenu, new OnClick(){
	public void onClick(){ UIHandler.uiState = UIStates.Options; }
	});
	obj.setText("Options", Color.black, Color.red, Color.GRAY, alphbeta18);
	obj.setAnimation(animations.slideRight, 60);
	masterlist.add(obj);
	obj = new ObjButton(20, 100, 80, 30, UIStates.MainMenu, null);
	obj.setText("Credits", Color.black, Color.red, Color.GRAY, alphbeta18);
	obj.setAnimation(animations.slideLeft, 60);
	masterlist.add(obj);
	obj = new ObjButton(20, 140, 80, 30, UIStates.MainMenu, new OnClick(){
	public void onClick(){ Game.exitGame(); }
	});
	obj.setText("Exit", Color.black, Color.red, Color.GRAY, alphbeta18);
	obj.setAnimation(animations.slideDown, 60);
	masterlist.add(obj);
	//
	//
	//
	//
	//
	//
	//
	//
	// OPTIONS
	obj = new ObjButton(20, 20, 80, 30, UIStates.Options, new OnClick(){
	public void onClick(){ UIHandler.uiState = UIStates.MainMenu; }
	});
	obj.setText("Return", Color.black, Color.red, Color.GRAY, alphbeta18);
	masterlist.add(obj);
	masterlist.add(new ObjText(78, 65, UIStates.Options, "Game Scale", Color.black, alphbeta18));
	obj = new ObjButton(20, 80, 30, 30, UIStates.Options, new OnClick(){
	public void onClick(){
		int tempScale = Windows.SCALE;
		Windows.SCALE = Utilities.clamp(Windows.SCALE - 1, 1, Windows.getMaxScale());
		
		if (tempScale != Windows.SCALE){
			Windows.setScale(Windows.SCALE);
			Game.getNewWindow();
		}
	}
	});
	obj.setText("-", Color.black, Color.red, Color.GRAY, alphbeta18);
	masterlist.add(obj);
	obj = new ObjButton(60, 80, 30, 30, UIStates.Options, new OnClick(){
	public void onClick(){
		int tempScale = Windows.SCALE;
		Windows.SCALE = Utilities.clamp(Windows.SCALE + 1, 1, Windows.getMaxScale());
		
		if (tempScale != Windows.SCALE){
			Windows.setScale(Windows.SCALE);
			Game.getNewWindow();
		}
	}
	});
	obj.setText("+", Color.black, Color.red, Color.GRAY, alphbeta18);
	masterlist.add(obj);
	obj = new ObjButton(100, 80, 40, 30, UIStates.Options, new OnClick(){
	public void onClick(){
		
		if (Windows.SCALE != Windows.getMaxScale()){
			Windows.setScale(Windows.getMaxScale());
			Game.getNewWindow();
		}
	}
	});
	obj.setText("Max", Color.black, Color.red, Color.GRAY, alphbeta18);
	masterlist.add(obj);
	masterlist.add(new ObjText(78, 125, UIStates.Options, "FullScreen", Color.black, alphbeta18));
	obj = new ObjButton(20, 140, 30, 30, UIStates.Options, new OnClick(){
	public void onClick(){
		Windows.windowed = true;
		Game.getNewWindow();
	}
	});
	obj.setText("Off", Color.black, Color.red, Color.GRAY, alphbeta18);
	masterlist.add(obj);
	obj = new ObjButton(60, 140, 30, 30, UIStates.Options, new OnClick(){
	public void onClick(){
		Windows.windowed = false;
		Game.getNewWindow();
	}
	});
	obj.setText("On", Color.black, Color.red, Color.GRAY, alphbeta18);
	masterlist.add(obj);
	//
	//
	//
	//
	// GAME SCREEN
		obj = new ObjButton(240, 0, 16, 16, UIStates.Game, new OnClick(){
		public void onClick(){ UIHandler.enterPause(); }
		});	
		obj.setHitBox(2, 2, 12, 12);
		masterlist.add(obj);
		
	
	//
	//
	//
	// PAUSE SCREEN
	obj = new ObjImage(0, 0, Windows.WIDTH, Windows.HEIGHT, UIStates.Pause, null, 0, 0, 0);
	obj.setFillBar(1, 0, 1, Color.black, Color.black, Color.black);
	obj.setTransparency(0.2f);
	masterlist.add(obj);
	obj = new ObjButton(20, 20, 80, 30, UIStates.Pause, new OnClick(){
	public void onClick(){ UIHandler.leavePause(); }
	});
	obj.setText("Continue", Color.black, Color.red, Color.GRAY, alphbeta18);
	masterlist.add(obj);
	obj = new ObjButton(20, 60, 80, 30, UIStates.Pause, new OnClick(){
	public void onClick(){ UIHandler.returnToMainMenu(); }
	});
	obj.setText("Main Screen", Color.black, Color.red, Color.GRAY, alphbeta18);
	masterlist.add(obj);
}
}