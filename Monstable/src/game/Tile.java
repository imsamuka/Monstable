package game;


import java.awt.Graphics;

public class Tile extends GameObject{

	protected Tile(float x, float y, ID id, int wSprite, String spritesheet) {
		super(x, y, id, spritesheet, 16, 16, wSprite);
		if (id == ID.Wall) collision = true;
		
	}

	protected void tick(){
		refreshBounds();
	}


	protected void render(Graphics g){
		g.drawImage(image.getSprite(wSprite, sWidth, sHeight), (int) ( x ), (int) ( y ), null);
		renderBounds(g);
	}


}