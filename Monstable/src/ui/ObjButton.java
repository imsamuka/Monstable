package ui;
import java.awt.Color;
import java.awt.Graphics;

public class ObjButton extends UIObject{
public ObjButton(float x, float y, int width, int height, UIStates id, OnClick onClick){
	super(x, y, width, height, id, onClick);
	if (onClick == null) active = false;
}
protected void tick(){
	refreshBounds();
	hoveringCheck();
	if (animation != animations.nothing) getAnimation();
}
protected void render(Graphics g){
	
	if (image != null) g.drawImage(getImage(), (int) ( x ), (int) ( y ), width + 1, height + 1, null);
	else{
		if (!active) g.setColor(Color.gray);
		else if (isHovering()) g.setColor(Color.red);
		else g.setColor(Color.black);
		g.drawRect((int) x, (int) y, width, height);
	}
	if (text != null) drawString(g);
}
}
