package inputs;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import game.GameHandler;
import game.Goop;
import game.Player;
import main.Windows;
import ui.UIHandler;
import ui.UIObject;
import ui.UIStates;

public class MouseInput implements MouseListener, MouseMotionListener{
// Fields
private static boolean[] pressed  = new boolean[3];
private static boolean   onScreen = true;
private static float     mouseX   = -1000, mouseY = -1000;

//Getters
public static boolean[] getPressed(){ return pressed; }
public static boolean getPressed(int btn){ return pressed[btn]; }
public static float getMouseX(){ return mouseX; }
public static float getMouseY(){ return mouseY; }
public static boolean isOnScreen(){ return onScreen; }

// What happens to witch key
private InputInt left  = new InputInt(){
						public void OnSinglePressed(){}                                        // Mouse n tem Singlepressed
						public void OnPressed(){
							int size = UIHandler.objList.size();
							
							for (int i = 0; i < size; i++){
								UIObject tO = UIHandler.objList.get(i);
								tO.hoveringCheck();
								
								if (tO.isHovering()){
									tO.onClick();
									break;
								}
							}
							
							if (UIHandler.uiState == UIStates.Game){
								if (!GameHandler.player.getBounds().contains(mouseX, mouseY))
									GameHandler.objList.add(new Goop(mouseX, mouseY));
							}
						}
						public void OnReleased(){}
						};
private InputInt right = new InputInt(){
						public void OnSinglePressed(){}
						public void OnPressed(){
							if (UIHandler.uiState == UIStates.Game){
								if (!GameHandler.player.isRoll() && !GameHandler.player.getBounds().contains(mouseX, mouseY)) {
									GameHandler.player.mouseX = mouseX;
									GameHandler.player.mouseY = mouseY;
									GameHandler.player.setRoll(true);
								}
								
							}
							
							
						}
						public void OnReleased(){}
						};
private InputInt middle;

private InputInt getInputInt(int btn){
	if (btn == 0) return left;
	else if (btn == 1) return middle;
	else if (btn == 2) return right;
	else return null;
}
// INFO
/* btn = 0 -> Mouse Left Button 
 * btn = 1 -> Mouse Middle Button 
 * btn = 2 -> Mouse Right Button
 */
public MouseInput(){}
public void mousePressed(MouseEvent e){
	int btn = e.getButton() - 1;
	if (btn < 0 || btn > 2) return;
	pressed[btn] = true;
	if (getInputInt(btn) != null) getInputInt(btn).OnPressed();
}
public void mouseReleased(MouseEvent e){
	int btn = e.getButton() - 1;
	if (btn < 0 || btn > 2) return;
	pressed[btn] = false;
	if (getInputInt(btn) != null) getInputInt(btn).OnReleased();
}
public void mouseMoved(MouseEvent e){
	mouseX = e.getX() / Windows.SCALE;
	mouseY = e.getY() / Windows.SCALE;
}
public void mouseDragged(MouseEvent e){
	mouseX = e.getX() / Windows.SCALE;
	mouseY = e.getY() / Windows.SCALE;
}
public void mouseEntered(MouseEvent e){ onScreen = true; }
public void mouseExited(MouseEvent e){ onScreen = false; }
public void mouseClicked(MouseEvent e){}
}
