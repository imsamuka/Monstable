package ui;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ObjImage extends UIObject{
private boolean fullWidth = false, fullHeight = false;

public ObjImage(float x, float y, int width, int height, UIStates id, String path, int spriteWidth, int spriteHeight, int dSprite, int hSprite){
	super(x, y, width, height, id);
	setImage(path, spriteWidth, spriteHeight);
	setSprite(dSprite, hSprite);
	if (width == 0) fullWidth = true;
	if (height == 0) fullHeight = true;
}
public void tick(){ 
	hoveringCheck();
	onHover();
	if (animation != animations.nothing) getAnimation(); }
public void render(Graphics g){
	if (!visible) return;
	
	( (Graphics2D) g ).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,transparency));
	
	
	
	getFillBar(g);
	g.drawImage(getImage(), (int) ( x ), (int) ( y ), fullWidth ? getImage().getWidth() : width + 1, fullHeight ? getImage().getHeight() : height + 1, null);
	drawString(g);
	( (Graphics2D) g ).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));
}
}
