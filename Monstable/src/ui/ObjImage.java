package ui;
import java.awt.Graphics;

public class ObjImage extends UIObject{
private boolean fullWidth = false, fullHeight = false;

public ObjImage(float x, float y, int width, int height, UIStates id, String path, int spriteWidth, int spriteHeight, int wSprite){
	super(x, y, width, height, id);
	setImage(path, spriteWidth, spriteHeight);
	setSprite(wSprite);
	if (width == 0) fullWidth = true;
	if (height == 0) fullHeight = true;
}
public void tick(){ if (animation != animations.nothing) getAnimation(); }
public void render(Graphics g){
	getFillBar(g);
	g.drawImage(getImage(), (int) ( x ), (int) ( y ), fullWidth ? getImage().getWidth() : width + 1, fullHeight ? getImage().getHeight() : height + 1, null);
	drawString(g);
}
}
