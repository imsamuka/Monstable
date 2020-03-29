package inputs;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import ui.UIHandler;
import ui.UIStates;

public class KeyInput extends KeyAdapter{
// KeyList
public static LinkedList<KeyObj> KeyList = new LinkedList<KeyObj>();

public static KeyObj getFirst(KeyObj.types type){
	for (int i = 0; i < KeyList.size(); i++) if (KeyList.get(i).getType() == type) return KeyList.get(i);
	return null;
}
public static void setFirst(String name){
	for (int i = 0; i < KeyList.size(); i++) if (KeyList.get(i).getName() == name){
		KeyObj oKey = KeyList.get(i);
		KeyList.offerFirst(oKey);
		KeyList.removeLastOccurrence(oKey);
		break;
	}
}
public static KeyObj getFirstOf(KeyObj key1, KeyObj key2){
	return KeyList.indexOf(key1) < KeyList.indexOf(key2) ? key1 : key2;
}
public static boolean keyIsFirst(KeyObj key1, KeyObj key2){
	return KeyList.indexOf(key1) < KeyList.indexOf(key2) ? true : false;
}
public static boolean isAnyKeyPressed(KeyObj.types type){
	
	for (int i = 0; i < KeyList.size(); i++){
		KeyObj key = KeyList.get(i);
		if (key.getType() == type && key.isPressed()) return true;
	}
	return false;
}
public static boolean isOppositeKeyPressed(KeyObj oKey){
	if (oKey.getOppositeKey() != null) for (KeyObj oKey2: KeyList) if (oKey.getOppositeKey() == oKey2.getName()
	&& oKey2.isPressed()) return true;
	return false;
}
public static void correctListOrder(){
	boolean again = true;
	int offset = 0;
	while(again) for (int i = KeyList.size() - 1; i > offset - 1; i--){
		if (i == offset) again = false;
		KeyObj oKey = KeyList.get(i);
		
		if (oKey.isPressed()){
			KeyList.offerFirst(oKey);
			KeyList.removeLastOccurrence(oKey);
			offset++;
			break;
		}
	}
}

// The Keys
public static KeyObj down  = new KeyObj(new int[ ] {KeyEvent.VK_S}, "down", "up");
public static KeyObj up    = new KeyObj(new int[ ] {KeyEvent.VK_W}, "up", "down");
public static KeyObj left  = new KeyObj(new int[ ] {KeyEvent.VK_A}, "left", "right");
public static KeyObj right = new KeyObj(new int[ ] {KeyEvent.VK_D}, "right", "left");
public static KeyObj esc   = new KeyObj(new int[ ] {KeyEvent.VK_ESCAPE}, "esc", new InputInt(){
							public void OnSinglePressed(){
								if (UIHandler.uiState == UIStates.Game) UIHandler.enterPause();
								else if (UIHandler.uiState == UIStates.Pause) UIHandler.leavePause();
							}
							public void OnPressed(){}
							public void OnReleased(){}
							});

public KeyInput(){
	KeyList.clear();
	KeyList.add(down);
	KeyList.add(up);
	KeyList.add(left);
	KeyList.add(right);
	KeyList.add(esc);
}
// Key Pressing
public void keyPressed(KeyEvent e){
	int key = e.getKeyCode();
	
	for (int i = 0; i < KeyList.size(); i++){
		KeyObj oKey = KeyList.get(i);
		for (int j: oKey.getKeyID()) if (key == j){
			// Key Recognized
			
			if (oKey.isPressed() == false){
				oKey.setPressed(true);
				oKey.setSinglePressed(true);
			}else oKey.setSinglePressed(false);
			
			if (oKey.isSinglePressed()){
				// Key Unique OnSinglePressed()
				if (oKey.getInputs() != null) oKey.getInputs().OnSinglePressed();
				// Reorder the SinglePressed Key to the head of the list:
				KeyList.offerFirst(oKey);
				KeyList.removeLastOccurrence(oKey);
				return;
			}
			// Key Unique OnPressed()
			if (oKey.getInputs() != null) oKey.getInputs().OnPressed();
			
			
			
			return;
		}
	}

}
//Key Releasing
public void keyReleased(KeyEvent e){
	int key = e.getKeyCode();
	
	for (int i = 0; i < KeyList.size(); i++){
		KeyObj oKey = KeyList.get(i);
		for (int j: oKey.getKeyID()) if (key == j){
			// Key Recognized
			oKey.setPressed(false);
			oKey.setSinglePressed(false);
			correctListOrder();
			// Key Unique OnReleased()
			if (oKey.getInputs() != null) oKey.getInputs().OnReleased();
		}
	}
	//for (int l = 0; l < KeyList.size(); l++) if (KeyList.get(l).getType() == KeyObj.types.movement) System.out.println(l+":"+(KeyList.get(l).getName()));
	
}
}