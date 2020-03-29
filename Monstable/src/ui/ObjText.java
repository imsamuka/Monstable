package ui;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ObjText extends UIObject{
public ObjText(float x, float y, UIStates id, String text, Color defaultColor, Font font){ 
	super(x, y, 0, 0, id);
	setText(text, defaultColor, null, null, font);
}
public void tick(){ if (animation != animations.nothing) getAnimation(); }
public void render(Graphics g){ 
	
	( (Graphics2D) g ).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,transparency));
	drawString(g); 
	( (Graphics2D) g ).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));
}
}