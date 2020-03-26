package main;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import game.GameState;

public class MapGenerator{
public MapGenerator(String inputPath, String outputName, String tilesetPath) {
	
		Images inputImage = new Images(inputPath);
		int size = GameState.MAPBASE*GameState.MAPBASE;
		
		
		
		
		if (inputImage.getWidth() != size) throw new IllegalArgumentException("Width size is not Correct.");
		if (inputImage.getHeight() != size) throw new IllegalArgumentException("Height size is not Correct.");
		
		System.out.println("Imagem válida");
		
		BufferedImage outputImage = new BufferedImage(GameState.MAPBASE, GameState.MAPBASE, BufferedImage.TYPE_INT_RGB);
		Images tileset = new Images(tilesetPath);
		int tilesetSize = tileset.getWidth()*tileset.getHeight();
		
		
		System.out.println("Começando loop");
		
		for (int xx = 0; xx < GameState.MAPBASE;xx++) for (int yy = 0; yy < GameState.MAPBASE;yy++) {
			int x = xx*GameState.MAPBASE;
			int y = yy*GameState.MAPBASE;
			
			for (int wSprite = 0; wSprite < tilesetSize; wSprite++)
				if ( Images.getPixelRGBArray(inputImage.getSubimage(x, y, GameState.MAPBASE, GameState.MAPBASE)) == Images.getPixelRGBArray(tileset.getSprite(wSprite, GameState.MAPBASE, GameState.MAPBASE))     ) {
					//.equals(tileset.getSprite(wSprite, GameState.MAPBASE, GameState.MAPBASE))
					int color = wSpriteToPixelColor(wSprite, tilesetSize);
					
					outputImage.setRGB(xx, yy, color);
					
					break;
				}

		}
		
		System.out.println("Terminando de ler o mapa");
		
		
		
		
		
		try{
			ImageIO.write(outputImage, "png", new File("C:\\Users\\ziza\\Desktop\\"+outputName+".png"));
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		
		
	}
private int wSpriteToPixelColor(int wSprite, int tilesetSize){
	wSprite = Game.clamp(wSprite, 1, tilesetSize);
	
	switch(wSprite){
		case 2:
			return 0xFF0000FF;
		default:
			return 0xFF000000;
	}
}
}
