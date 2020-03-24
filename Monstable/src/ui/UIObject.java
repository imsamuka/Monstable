package ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import inputs.MouseInput;
import main.Game;
import main.Images;
import main.Windows;

public abstract class UIObject{
//Getters:
public boolean isHovering(){ return hovering; }
public void setX(float x){ this.x = x; }
public void setY(float y){ this.y = y; }
public void setActive(boolean active){ this.active = active; }

//////// Main ////////
protected float     x, y, transparency = 1;
protected int       width, height, boundsX = 0, boundsY = 0;
protected boolean   hovering = false, active = true;
protected UIStates  id;
protected OnClick   onClick;
protected Rectangle bounds;
public Rectangle getBounds(){ return bounds; }

protected UIObject(float x, float y, int width, int height, UIStates id, OnClick onClick){
	this.x       = x;
	this.y       = y;
	this.width   = width;
	this.height  = height;
	this.id      = id;
	this.onClick = onClick;
	bounds       = new Rectangle((int) x, (int) y, width + 1, height + 1);
}
protected UIObject(float x, float y, int width, int height, UIStates id){
	this.x      = x;
	this.y      = y;
	this.width  = width;
	this.height = height;
	this.id     = id;
	bounds      = new Rectangle((int) x, (int) y, width + 1, height + 1);
}
// Methods:
public abstract void tick();
public abstract void render(Graphics g);
public void onClick(){ if (onClick != null && active) onClick.onClick(); }
public void hoveringCheck(){
	if (bounds.contains(MouseInput.getMouseX(), MouseInput.getMouseY())) hovering = true;
	else hovering = false;
}
public void refreshBounds(){
	bounds.x = (int) x + boundsX;
	bounds.y = (int) y + boundsY;
}
public void setHitBox(int x, int y, int width, int height){
	boundsX       = x;
	boundsY       = y;
	bounds.width  = width;
	bounds.height = height;
	refreshBounds();
}
public void setTransparency(float transp){ transparency = Game.clamp(transp, 0, 1); }

//
////
//////// Animations ////////
public static enum animations{
nothing,
slideLeft,
slideRight,
slideUp,
slideDown,
}
public static enum ftypes{
linear,
pa,
par
}

protected float            offset, finaly, vel, svel = 1f;
protected boolean          rewind    = false;
protected int              frames, currentFrame = 0;
protected Enum<animations> animation = animations.nothing;
protected Enum<ftypes>     ftype     = ftypes.linear;

public void setAnimation(Enum<animations> animation, int frames){
	this.animation = animation;
	this.frames    = frames;
	
	if (animation == animations.slideLeft){
		offset  = x + width + 1;
		finaly  = x;
		x      -= offset;
	}else if (animation == animations.slideRight){
		offset  = Windows.WIDTH - x;
		finaly  = x;
		x      += offset;
	}else if (animation == animations.slideUp){
		offset  = y + height + 1;
		finaly  = y;
		y      -= offset;
	}else if (animation == animations.slideDown){
		offset  = Windows.HEIGHT - y;
		finaly  = y;
		y      += offset;
	}
	if (animation == animations.slideLeft || animation == animations.slideRight) if (ftype
	== ftypes.linear) vel = ( finaly - x ) / frames;
	if (animation == animations.slideDown || animation == animations.slideUp) if (ftype
	== ftypes.linear) vel = ( finaly - y ) / frames;
}
protected void getAnimation(){
	
	if (ftype == ftypes.pa && currentFrame != frames){
		vel = animation == animations.slideRight
		|| animation
		== animations.slideDown ? ( -1 ) * svel * Math.abs(currentFrame * ( 1 / ( frames / 2 ) ) - 1) : svel * Math.abs(currentFrame * ( 1 / ( frames / 2 ) ) - 1);
		currentFrame++;
	}
	
	if (ftype == ftypes.par && currentFrame != frames){
		vel = svel * ( ( ( currentFrame - ( frames / 2 ) ) ^ 2 ) / ( 4 * ( 0.0625f * ( frames ^ 2 ) ) ) );
		currentFrame++;
	}
	if (animation == animations.slideLeft || animation == animations.slideRight) x = Game.clampAuto(x + vel, x, finaly);
	if (animation == animations.slideDown || animation == animations.slideUp) y = Game.clampAuto(y + vel, y, finaly);
}

//////// Images ////////
protected Images        image;
protected int           wSprite, spriteWidth, spriteHeight;
protected boolean       fullImage = true;

public void setImage(String path, int spriteWidth, int spriteHeight){
	if (path != null) this.image = new Images(path);
	this.spriteWidth  = spriteWidth;
	this.spriteHeight = spriteHeight;
}
public void setSprite(int wSprite){
	
	if (wSprite == 0){
		fullImage = true;
		return;
	}
	fullImage    = false;
	this.wSprite = wSprite;
}
protected BufferedImage getImage(){
	if (image == null) return null;

	try{
		if (fullImage) return image.getThisImage();
		return image.getSprite(wSprite, spriteWidth, spriteHeight);
	}
	catch(Exception e){
		return null;
	}
}

//////// Fill ////////
protected float   fillValue, min, max;
protected boolean fill;
protected Color   ffull, fbackground, fdefault;

public void setFillBar(float Value, float Min, float Max, Color Default, Color Full, Color Background){
	fill = true;
	min  = Min;
	max  = Max;
	setFillValue(Value);
	ffull       = Full;
	fbackground = Background;
	fdefault    = Default;
}
public void setFillValue(float Value){ fillValue = Game.clamp(Value, min, max); }
protected void getFillBar(Graphics g){
	if (!fill) return;
	
	if (fbackground != null){
		g.setColor(fbackground);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
	if (fillValue == max) g.setColor(ffull);
	else g.setColor(fdefault);
	g.fillRect(bounds.x, bounds.y, (int) ( bounds.width * ( fillValue / max ) ), bounds.height);
}

//////// Text ////////
protected String text;
protected Color  defaultColor, selectedColor, inactiveColor;
protected Font   font;

public void setText(String text, Color defaultColor, Color selectedColor, Color inactiveColor, Font font){
	this.text          = text;
	this.defaultColor  = defaultColor;
	this.selectedColor = selectedColor;
	this.inactiveColor = inactiveColor;
	this.font          = font;
}
protected void drawString(Graphics g){
	if (text == null) return;
	if (!active) g.setColor(inactiveColor);
	else if (hovering) g.setColor(selectedColor);
	else g.setColor(defaultColor);
	g.setFont(font);
	FontMetrics fm = g.getFontMetrics(font);
	int xpos = (int) x + ( width / 2 );
	int ypos = (int) y + ( height / 2 );
	xpos = xpos - fm.stringWidth(text) / 2;
	ypos = ( ypos - fm.getHeight() / 2 ) + fm.getAscent();
	g.drawString(text, xpos, ypos);
}
}