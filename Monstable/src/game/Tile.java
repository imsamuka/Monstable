package game;
import java.awt.Graphics;
import java.awt.Point;
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
		case 10:
		case 11:
		case 12:
		case 19:
		case 20:
		case 21:
		case 28:
		case 29:
		case 30:
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

	listBounds[22] = new Rectangle(0, 13, 16, 3);
	listBounds[27] = listBounds[22];

	listBounds[33] = listBounds[6];
	listBounds[35] = listBounds[8];
	if (listBounds[wSprite]
	!= null) setHitBox(listBounds[wSprite].x, listBounds[wSprite].y, listBounds[wSprite].width, listBounds[wSprite].height);
}

public static void createFade(GameObject tO, Graphics g) {
	if (!tO.id.is(ID.Wall)) return;	
	if (!(tO.wSprite == 13 || tO.wSprite == 18 || tO.wSprite == 6 || tO.wSprite == 33)) return;
	
	
	Point p = tO.getTileDR(GameState.MAPBASE);
	
	if (tO.wSprite == 13 || tO.wSprite == 18){
		int FadeSprite = 16;
		int off = 2;

		
		if (tO.wSprite == 13) g.drawImage(tO.image.getSprite(FadeSprite, tO.sWidth, tO.sHeight, new Rectangle(0, off, 0, 0)), (int) tO.x, (int) tO.y + off, null);
		else tO.renderInverted(tO.image.getSprite(FadeSprite, tO.sWidth, tO.sHeight, new Rectangle(0, off, 0, 0)), (int) tO.x, (int) tO.y + off, true, false, g);
		p = new Point(p.x, p.y + 1); 
		
		
		while(GameHandler.getOnPoint(p, ID.Tile) != null && GameHandler.getOnPoint(p, ID.Tile).id.is(ID.Floor)){
			GameObject tempO = GameHandler.getOnPoint(p, ID.Tile);
			//tO.visibleBounds = true;
		if (tO.wSprite == 13) g.drawImage(tO.image.getSprite(FadeSprite, tO.sWidth, tO.sHeight), (int) tempO.x, (int) tempO.y, null);
			else tO.renderInverted(tO.image.getSprite(FadeSprite, tO.sWidth, tO.sHeight), (int) tempO.x, (int) tempO.y, true, false, g);
			p = new Point(p.x, p.y + 1);
		}
		GameObject tempO = GameHandler.getOnPoint(p, ID.Tile);
		
		if (tempO.id.is(ID.Wall)){
			if (tO.wSprite
			== 13) g.drawImage(tO.image.getSprite(FadeSprite, tO.sWidth, tO.sHeight, new Rectangle(0, 0, 0, tO.sHeight - tempO.bounds.height)), (int) tempO.x, (int) tempO.y, null);
			else tO.renderInverted(tO.image.getSprite(FadeSprite, tO.sWidth, tO.sHeight, new Rectangle(0, 0, 0, tO.sHeight - tempO.bounds.height)), (int) tempO.x, (int) tempO.y, true, false, g);
		}
	}else if (tO.wSprite == 6 || tO.wSprite == 33){
		int FadeSprite = 7;
		
		
		if (tO.wSprite == 6) g.drawImage(tO.image.getSprite(FadeSprite, tO.sWidth, tO.sHeight, new Rectangle(tO.bounds.width, 0, 0, 0)), (int) tO.x + tO.bounds.width, (int) tO.y, null);
		else tO.renderInverted(tO.image.getSprite(FadeSprite, tO.sWidth, tO.sHeight, new Rectangle(tO.bounds.width, 0, 0, 0)), (int) tO.x + tO.bounds.width, (int) tO.y, false, true, g);
		p = new Point(p.x + 1, p.y);
		
		while(GameHandler.getOnPoint(p, ID.Tile) != null && GameHandler.getOnPoint(p, ID.Tile).id.is(ID.Floor)){
			GameObject tempO = GameHandler.getOnPoint(p, ID.Tile);
			
			if (tO.wSprite == 6) g.drawImage(tO.image.getSprite(FadeSprite, tO.sWidth, tO.sHeight), (int) tempO.x, (int) tempO.y, null);
			else tO.renderInverted(tO.image.getSprite(FadeSprite, tO.sWidth, tO.sHeight), (int) tempO.x, (int) tempO.y, false, true, g);
			p = new Point(p.x + 1, p.y);
		}
		GameObject tempO = GameHandler.getOnPoint(p, ID.Tile);
		if (tempO == null) return;
		
		if (tempO.id.is(ID.Wall)){
			if (tO.wSprite == 6) g.drawImage(tO.image.getSprite(FadeSprite, tO.sWidth, tO.sHeight, new Rectangle(0, 0, tO.sWidth - tempO.bounds.width, 0)), (int) tempO.x, (int) tempO.y, null);
			else tO.renderInverted(tO.image.getSprite(FadeSprite, tO.sWidth, tO.sHeight, new Rectangle(0, 0, tO.sWidth - tempO.bounds.width, 0)), (int) tempO.x, (int) tempO.y, false, true, g);
		}
	}
}



protected void tick(){ refreshBounds(); }
protected void render(Graphics g){
	g.drawImage(image.getSprite(wSprite, sWidth, sHeight), (int) x, (int) y, null);
	renderBounds(g);
}
}