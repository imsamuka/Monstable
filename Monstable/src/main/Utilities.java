package main;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public abstract class Utilities{

	public static Rectangle extendRectangle(Rectangle bounds, int value){
		return new Rectangle(bounds.x - value, bounds.y - value, bounds.width + value * 2, bounds.height + value * 2);
	}

	public static Rectangle extendRectangle(Rectangle bounds, int x, int y, int width, int height){
		return new Rectangle(bounds.x + x, bounds.y + y, bounds.width + width, bounds.height + height);
	}

	public static BufferedImage blurImage(int radius, BufferedImage image){
		int size = radius * radius;
		float[] matrix = new float[size];
		for (int i = 0; i < size; i++) matrix[i] = 1.0f / size;
		return new ConvolveOp(new Kernel(radius, radius, matrix)).filter(image, new BufferedImage(Windows.WIDTH, Windows.HEIGHT, BufferedImage.TYPE_INT_RGB));
	}
	
	public static boolean checkIntArrayEquality(int[] array1, int[] array2){
		int size = array1.length;
		if (size != array2.length) return false;
		for (int m = 0; m < size; m++) if (array1[m] != array2[m]) return false;
		return true;
	}

	public static float clamp(float var, float min, float max){
		if (var > max) return max;
		else if (var < min) return min;
		else return var;
	}

	public static int clamp(int var, int min, int max){
		if (var > max) return max;
		else if (var < min) return min;
		else return var;
	}

	public static float clampAuto(float var, float limiter1, float limiter2){
		float min = limiter1 > limiter2 ? limiter2 : limiter1;
		float max = min == limiter1 ? limiter2 : limiter1;
		if (var > max) return max;
		else if (var < min) return min;
		else return var;
	}

	public static int clampSwitch(int var, int min, int max){
		if (var > max) return var - ( max - min + 1 );
		else if (var < min) return var + ( max - min + 1 );
		else return var;
	}

	public static boolean isEven(int num){ return( num % 2 == 0 ); }
}
