package ui;
import java.awt.Graphics;

public class ObjImage extends UIObject{
private boolean fullWidth = false, fullHeight = false;

protected ObjImage(float x, float y, int width, int height, UIStates id, String path, int spriteWidth, int spriteHeight, int wSprite){
	super(x, y, width, height, id);
	setImage(path, spriteWidth, spriteHeight);
	setSprite(wSprite);
	if (width == 0) fullWidth = true;
	if (height == 0) fullHeight = true;
}
protected void tick(){ if (animation != animations.nothing) getAnimation(); }
protected void render(Graphics g){
	g.drawImage(getImage(), (int) ( x ), (int) ( y ), fullWidth ? getImage().getWidth() : width + 1, fullHeight ? getImage().getHeight() : height + 1, null);
	if (text != null) drawString(g);
}
}
