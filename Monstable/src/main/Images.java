package main;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Images{
private static BufferedImage[] images   = new BufferedImage[32];
private static String[]        pathList = new String[32];
private static int             current  = 0;
private BufferedImage          thisImage = null;
private int                    thisImageIndex = 0;

public Images(String path){
	if (path == null) return;
	
	for (int i = 0; i < current; i++) if (path.equals(pathList[i])){
		thisImage      = images[i];
		thisImageIndex = i;
		return;
	}
	
	try{
		thisImage = ImageIO.read(getClass().getResource(path));
	}
	catch(IOException e){
		e.printStackTrace();
	}
	thisImageIndex    = current;
	images[current]   = thisImage;
	pathList[current] = path;
	current++;
}
public BufferedImage getThisImage(){ return thisImage; }
public BufferedImage getSubimage(int x, int y, int width, int height){
	return thisImage == null ? null : thisImage.getSubimage(x, y, width, height);
}
public int getPixelRGB(int x, int y){ return thisImage.getRGB(x, y); }
public int[] getPixelRGBArray(int[] array){
	return thisImage.getRGB(0, 0, thisImage.getWidth(), thisImage.getHeight(), array, 0, thisImage.getWidth());
}
public BufferedImage getSprite(int wSprite, int width, int height){
	if (thisImage == null) return null;
	int spriteRows = thisImage.getHeight() / (height == 0 ? 1 : height);
	int spriteCols = thisImage.getWidth() / (width == 0 ? 1 : width);
	
	height = height == 0 ? thisImage.getHeight() : height ;
	width = width == 0 ? thisImage.getWidth() : width ;
	
	wSprite = Game.clamp(( wSprite - 1 ), 0, ( ( spriteRows * spriteCols ) - 1 ));
	// Save Processing pt1
	String path = pathList[thisImageIndex]+"."+( wSprite + 1 );
	for (int i = thisImageIndex; i < current; i++) if (path.equals(pathList[i])) return images[i];
	//
	int y = (int) Math.floor(wSprite / spriteCols);
	int x = y > 0 ? wSprite - ( spriteCols * y ) : wSprite;
	// Save Processing pt2
	images[current]   = getSubimage(x * width, y * height, width, height);
	pathList[current] = path;
	current++;
	return images[current - 1];
	//
}
public void getEverySprite(int width, int height){
	if (thisImage == null) return;
	int spriteRows = thisImage.getHeight() / height;
	int spriteCols = thisImage.getWidth() / width;
	int limit = spriteRows * spriteCols;
	boolean skip = false;
	
	for (int wSprite = 0; wSprite < limit; wSprite++){
		String path = pathList[thisImageIndex]+"."+( wSprite + 1 );
		skip = false;
		for (int i = thisImageIndex; i < current; i++) if (path.equals(pathList[i])){
			skip = true;
			break;
		}
		if (skip) continue;
		int y = (int) Math.floor(wSprite / spriteCols);
		int x = y > 0 ? wSprite - ( spriteCols * y ) : wSprite;
		images[current]   = getSubimage(x * width, y * height, width, height);
		pathList[current] = path;
		current++;
	}
}

public static void getEverySprite(String path,int width, int height){
	if (path == null) return;
	Images img = new Images(path);
	img.getEverySprite(width, height);
}




}
