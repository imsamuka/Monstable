package ui;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ObjButton extends UIObject{
public ObjButton(float x, float y, int width, int height, UIStates id, OnClick onClick){
	super(x, y, width, height, id, onClick);
	if (onClick == null) active = false;
}
public void tick(){
	refreshBounds();
	hoveringCheck();
	onHover();
	if (image != null) wSprite = hovering && onClick != null ? hSprite : dSprite; 
	if (animation != animations.nothing) getAnimation();
}
public void render(Graphics g){
	if (!visible) return;
		
	

	( (Graphics2D) g ).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,transparency));
	
	if (image == null){
		if (!active) g.setColor(Color.gray);
		else if (isHovering()) g.setColor(Color.red);
		else g.setColor(Color.black);
		g.drawRect((int) x, (int) y, width, height);
	}
	getFillBar(g);
	g.drawImage(getImage(), (int) ( x ), (int) ( y ), width == 0 ? getImage().getWidth() : width + 1,height == 0? getImage().getHeight() : height + 1, null);
	drawString(g);

	( (Graphics2D) g ).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));
}
}
