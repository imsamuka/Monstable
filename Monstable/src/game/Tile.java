package game;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Tile extends GameObject{
public Tile(float x, float y, int wSprite, String spritesheet){
	super(x, y, spriteToID(wSprite), spritesheet, 16, 16, wSprite);
	spriteToBounds(wSprite);
	if (id == ID.Wall) collision = true;
}
private static ID spriteToID(int wSprite){
	
	switch(wSprite){
		case 1:
		case 7:
		case 10:
		case 11:
		case 12:
		case 19:
		case 20:
		case 21:
		case 28:
		case 29:
		case 30:
		case 34:
			return ID.Floor;
		default:
			return ID.Wall;
	}
}
private void spriteToBounds(int wSprite){
	if (id != ID.Wall) return;
	Rectangle[] listBounds = new Rectangle[39];
	listBounds[6]  = new Rectangle(0, 0, 3, 16);
	listBounds[8]  = new Rectangle(13, 0, 3, 16);
	listBounds[22] = new Rectangle(0, 0, 16, 3);
	listBounds[23] = new Rectangle(0, 13, 16, 3);
	listBounds[26] = listBounds[23];
	listBounds[27] = listBounds[22];
	listBounds[33] = listBounds[6];
	listBounds[35] = listBounds[8];
	if (listBounds[wSprite]
	!= null) setHitBox(listBounds[wSprite].x, listBounds[wSprite].y, listBounds[wSprite].width, listBounds[wSprite].height);
}
protected void tick(){ refreshBounds(); }
protected void render(Graphics g){
	g.drawImage(image.getSprite(wSprite, sWidth, sHeight), (int) ( x ), (int) ( y ), null);
	renderBounds(g);
}
}