package ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class ObjText extends UIObject{
protected ObjText(float x, float y, UIStates id, String text, Color defaultColor, Font font){ 
	super(x, y, 0, 0, id);
	setText(text, defaultColor, null, null, font);
}
public void tick(){ if (animation != animations.nothing) getAnimation(); }
public void render(Graphics g){ drawString(g); }
}