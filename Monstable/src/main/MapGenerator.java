package main;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import game.GameState;

public class MapGenerator{
public MapGenerator(String inputPath, String output, String tilesetPath, Point playerPoint){
	Images inputImage = new Images(inputPath);
	int size = GameState.MAPBASE * GameState.MAPBASE;
	if (inputImage.getWidth() != size) throw new IllegalArgumentException("Width size is not Correct.");
	if (inputImage.getHeight() != size) throw new IllegalArgumentException("Height size is not Correct.");
	BufferedImage outputImage = new BufferedImage(GameState.MAPBASE, GameState.MAPBASE, BufferedImage.TYPE_INT_RGB);
	Images tileset = new Images(tilesetPath);
	int tilesetSize = 1 + ( tileset.getWidth() * tileset.getHeight() ) / ( GameState.MAPBASE * GameState.MAPBASE );
	for (int xx = 0; xx < GameState.MAPBASE; xx++) for (int yy = 0; yy < GameState.MAPBASE; yy++){
		int x = xx * GameState.MAPBASE;
		int y = yy * GameState.MAPBASE;
		outputImage.setRGB(xx, yy, GameState.SpriteAndColor(1, false));
		
		if (xx == playerPoint.x && yy == playerPoint.y ) {
			outputImage.setRGB(xx, yy, GameState.SpriteAndColor(37, false));
			continue;
			
		}
		
		for (int wSprite = 0; wSprite < tilesetSize; wSprite++){
			int[] array1 = Images.getPixelRGBArray(inputImage.getSubimage(x, y, GameState.MAPBASE, GameState.MAPBASE));
			int[] array2 = Images.getPixelRGBArray(tileset.getSprite(wSprite, GameState.MAPBASE, GameState.MAPBASE));
			
			if (Game.checkIntArrayEquality(array1, array2)){
				int color = GameState.SpriteAndColor(wSprite, false);
				outputImage.setRGB(xx, yy, color);
				break;
			}
		}
	}
	
	try{
		ImageIO.write(outputImage, "png", new File("res/maps/"+output+".png"));
		System.out.println("Novo mapa "+output+".png baseado em "+ inputPath);
	}
	catch(IOException e){
		e.printStackTrace();
	}
}
}
