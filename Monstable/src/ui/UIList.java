package ui;
import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.Random;
import game.GameState;
import main.Game;
import main.Utilities;
import main.Windows;

public class UIList{
private static LinkedList<UIObject> masterlist;
//public static Font                  alphbeta18 = Game.planB ? new Font("Arial", Font.PLAIN, 18) : UIHandler.loadFont("C:\\Users\\ziza\\Desktop\\Monstable\\archives\\alphbeta.ttf", 18);
public static Font                  alphbeta18 = Game.planB ? new Font("Arial", Font.PLAIN, 18) : UIHandler.loadFont("C:\\Users\\ziza\\Desktop\\Monstable\\archives\\a_LCDMini.ttf", 16);
public static Font                  font2      = Game.planB ? new Font("Arial", Font.PLAIN, 10) : UIHandler.loadFont("C:\\Users\\ziza\\Desktop\\Monstable\\archives\\a_LCDMini.ttf", 10);
public static UIObject              controls, rimuru;

public static LinkedList<UIObject> getList(UIStates id){
	LinkedList<UIObject> list = new LinkedList<UIObject>();
	int size = masterlist.size();
	for (int i = 0; i < size; i++) if (masterlist.get(i) != null
	&& masterlist.get(i).id == id) list.add(masterlist.get(i));
	return list;
}
public static UIObject defaultButton1(float x, float y, String text, UIStates id, OnClick onClick){
	UIObject obj = new ObjButton(x, y, 0, 0, id, onClick);
	obj.setHitBox(0, 0, 70, 25);
	obj.setImage("/graphics/button_main2.png", 70, 25);
	obj.setSprite(1, 2);
	obj.setText(text, Color.black, Color.white, Color.GRAY, alphbeta18);
	return obj;
}
public static UIObject defaultButton1(float x, float y, int w, int h, String text, UIStates id, OnClick onClick){
	UIObject obj = new ObjButton(x, y, w, h, id, onClick);
	obj.setImage("/graphics/button_main2.png", 70, 25);
	obj.setSprite(1, 2);
	obj.setText(text, Color.black, Color.white, Color.GRAY, alphbeta18);
	return obj;
}
public UIList(){
	masterlist = new LinkedList<UIObject>();
	UIObject obj;
	// Add Buttons and UI Elements Here
	//
	//
	//
	//Backgrounds:
	int bg = new Random().nextInt(4);
	masterlist.add(new ObjImage(0, 0, 0, 0, UIStates.MainMenu, "/backgrounds/background_"+bg+".png", 0, 0, 1, 1));
	masterlist.add(new ObjImage(0, 0, 0, 0, UIStates.Options, "/backgrounds/background_"+bg+".png", 0, 0, 1, 1));
	masterlist.add(new ObjImage(0, 0, 0, 0, UIStates.Modes, "/backgrounds/background_"+bg+".png", 0, 0, 1, 1));
	masterlist.add(new ObjImage(0, 0, 0, 0, UIStates.CharSelection, "/backgrounds/background_"+bg+".png", 0, 0, 1, 1));
	//
	//
	//
	//
	// MAIN MENU
	
	masterlist.add(new ObjImage(0, 0, 0, 0, UIStates.MainMenu, "/graphics/title_monstable.png", 0, 0, 1, 1));
	
	int h = 45;
	
	masterlist.add(defaultButton1(( Windows.WIDTH / 2 ) - ( 70 / 2 ),h+ 30, "Play", UIStates.MainMenu, new OnClick(){
	public void onClick(){ UIHandler.uiState = UIStates.Modes; }
	}));
	masterlist.add(defaultButton1(( Windows.WIDTH / 2 ) - ( 70 / 2 ),h+ 60, "Options", UIStates.MainMenu, new OnClick(){
	public void onClick(){ UIHandler.uiState = UIStates.Options; }
	}));
	masterlist.add(defaultButton1(( Windows.WIDTH / 2 ) - ( 70 / 2 ),h+ 90, "Credits", UIStates.MainMenu, null));
	masterlist.add(defaultButton1(( Windows.WIDTH / 2 ) - ( 70 / 2 ),h+ 120, "Exit", UIStates.MainMenu, new OnClick(){
	public void onClick(){ Game.exitGame(); }
	}));
	//
	//
	//
	//
	// OPTIONS
	masterlist.add(defaultButton1(20, 20, "Return", UIStates.Options, new OnClick(){
	public void onClick(){ UIHandler.uiState = UIStates.MainMenu; }
	}));
	/*				Game Scale				 */
	masterlist.add(new ObjText(78, 65, UIStates.Options, "Game Scale", Color.black, alphbeta18));
	masterlist.add(defaultButton1(20, 80, 25, 25, "-", UIStates.Options, new OnClick(){
	public void onClick(){
		int tempScale = Windows.SCALE;
		Windows.SCALE = Utilities.clamp(Windows.SCALE - 1, 1, Windows.getMaxScale());
		
		if (tempScale != Windows.SCALE){
			Windows.setScale(Windows.SCALE);
			Game.getNewWindow();
		}
	}
	}));
	masterlist.add(defaultButton1(60, 80, 25, 25, "+", UIStates.Options, new OnClick(){
	public void onClick(){
		int tempScale = Windows.SCALE;
		Windows.SCALE = Utilities.clamp(Windows.SCALE + 1, 1, Windows.getMaxScale());
		
		if (tempScale != Windows.SCALE){
			Windows.setScale(Windows.SCALE);
			Game.getNewWindow();
		}
	}
	}));
	masterlist.add(defaultButton1(100, 80, 35, 25, "Max", UIStates.Options, new OnClick(){
	public void onClick(){
		
		if (Windows.SCALE != Windows.getMaxScale()){
			Windows.setScale(Windows.getMaxScale());
			Game.getNewWindow();
		}
	}
	}));
	/*				FullScreen				 */
	masterlist.add(new ObjText(78, 125, UIStates.Options, "FullScreen", Color.black, alphbeta18));
	masterlist.add(defaultButton1(20, 140, 35, 25, "Off", UIStates.Options, new OnClick(){
	public void onClick(){
		Windows.windowed = true;
		Game.getNewWindow();
	}
	}));
	masterlist.add(defaultButton1(60, 140, 35, 25, "On", UIStates.Options, new OnClick(){
	public void onClick(){
		Windows.windowed = false;
		Game.getNewWindow();
	}
	}));
	//
	//
	//
	//
	//new OnClick(){ public void onClick(){ GameState.arcadeMode = false; UIHandler.uiState    = UIStates.CharSelection; } }
	//
	// MODE SELECTION
	masterlist.add(new ObjText(Windows.WIDTH / 2, 20, UIStates.Modes, "Select Your Mode", Color.black, alphbeta18));
	masterlist.add(defaultButton1(( Windows.WIDTH / 2 ) - ( 70 / 2 ), 100, "History", UIStates.Modes, null));
	masterlist.add(defaultButton1(( Windows.WIDTH / 2 ) - ( 70 / 2 ), 130, "Arcade", UIStates.Modes, new OnClick(){
	public void onClick(){
		GameState.arcadeMode = true;
		UIHandler.uiState    = UIStates.CharSelection;
	}
	}));
	masterlist.add(defaultButton1(( Windows.WIDTH / 2 ) - ( 70 / 2 ), 160, "Return", UIStates.Modes, new OnClick(){
	public void onClick(){ UIHandler.uiState = UIStates.MainMenu; }
	}));
	//
	//
	//
	// CHARACTER SELECTION
	
	h = 64;
	
	masterlist.add(new ObjText(Windows.WIDTH / 2, 20, UIStates.CharSelection, "Select Your Character", Color.black, alphbeta18));
	masterlist.add(defaultButton1(96 + 5, h + 4, "Return", UIStates.CharSelection, new OnClick(){
	public void onClick(){ UIHandler.uiState = UIStates.Modes; }
	}));
	controls = new ObjImage(32, h, 0, 0, UIStates.CharSelection, "/graphics/ui_controls.png", 64, 64, 1, 1);
	masterlist.add(controls);
	rimuru = new ObjButton(64, h + 64, 0, 0, UIStates.CharSelection, new OnClick(){
	public void onClick(){ UIHandler.enterGame(); }
	});
	rimuru.setHitBox(0, 0, 32, 32);
	rimuru.setImage("/graphics/characters.png", 32, 32);
	rimuru.setSprite(1, 2);
	masterlist.add(rimuru);
	obj = new ObjButton(96 + 5, h + 64, 0, 0, UIStates.CharSelection, null);
	obj.setHitBox(0, 0, 32, 32);
	obj.setImage("/graphics/characters.png", 32, 32);
	obj.setSprite(3, 4);
	masterlist.add(obj);
	//
	//
	//
	//
	//
	// GAME SCREEN
	obj = new ObjButton(240, 0, 16, 16, UIStates.Game, new OnClick(){
	public void onClick(){ UIHandler.enterPause(); }
	});
	obj.setImage("/graphics/button_pause.png", 16, 16);
	obj.setSprite(1, 2);
	obj.setHitBox(2, 2, 12, 12);
	masterlist.add(obj);
	//
	//
	//
	//
	// Death SCREEN
	obj = new ObjImage(0, 0, Windows.WIDTH, Windows.HEIGHT, UIStates.Death, null, 0, 0, 0, 0);
	obj.setFillBar(1, 0, 1, Color.black, Color.black, Color.black);
	obj.setTransparency(0.2f);
	masterlist.add(obj);
	masterlist.add(new ObjText(Windows.WIDTH / 2, 70, UIStates.Death, "You Died", Color.black, alphbeta18));
	masterlist.add(defaultButton1(( Windows.WIDTH / 2 ) - ( 70 / 2 ), 160, "Restart", UIStates.Death, new OnClick(){
	public void onClick(){ UIHandler.resetGame(); }
	}));
	masterlist.add(defaultButton1(( Windows.WIDTH / 2 ) - ( 70 / 2 ), 200, "Return", UIStates.Death, new OnClick(){
	public void onClick(){ UIHandler.returnToMainMenu(); }
	}));
	//
	//
	//
	//
	//
	//
	// PAUSE SCREEN
	obj = new ObjImage(0, 0, Windows.WIDTH, Windows.HEIGHT, UIStates.Pause, null, 0, 0, 0, 0);
	obj.setFillBar(1, 0, 1, Color.black, Color.black, Color.black);
	obj.setTransparency(0.2f);
	masterlist.add(obj);
	obj = new ObjButton(20, 20, 80, 30, UIStates.Pause, new OnClick(){
	public void onClick(){ UIHandler.leavePause(); }
	});
	obj.setText("Continue", Color.black, Color.white, Color.GRAY, alphbeta18);
	masterlist.add(obj);
	obj = new ObjButton(20, 60, 80, 30, UIStates.Pause, new OnClick(){
	public void onClick(){ UIHandler.returnToMainMenu(); }
	});
	obj.setText("Return", Color.black, Color.white, Color.GRAY, alphbeta18);
	masterlist.add(obj);
}
}