package main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import game.GameHandler;
import game.GameWaves;
import inputs.MouseInput;
import ui.UIHandler;
import ui.UIStates;

public class Game implements Runnable{
private static Windows     window    = new Windows();
private static GameHandler gameHandler;
private BufferedImage      image     = new BufferedImage(Windows.WIDTH, Windows.HEIGHT, BufferedImage.TYPE_INT_RGB);
private Thread             thread    = new Thread(this);
public final UIHandler     uiHandler = new UIHandler();
public boolean             isRunning = false;

public static void main(String[] args){ new Game(); }
public Game(){
	//new MapGenerator("/maps/mockup 1.png", "newMap" , "/graphics/Tileset.png", new Point(5,5));
	start();
}
public synchronized void start(){
	isRunning = true;
	thread.setName("FPS");
	thread.start();
}
public void run(){
	long lastTime = System.nanoTime();
	double amountOfTicks = 60;
	double ns = 1000000000 / amountOfTicks;
	double delta = 0;
	int FPS = 0;
	double timer = System.currentTimeMillis();
	
	while(isRunning){
		long now = System.nanoTime();
		delta    += ( now - lastTime ) / ns;
		lastTime  = now;
		
		if (delta >= 1){
			tick();
			render();
			FPS++;
			delta--;
		}
		
		if (System.currentTimeMillis() - timer >= 1000){
			System.out.println("FPS: "+FPS);
			FPS    = 0;
			timer += 1000;
		}
	}
}
private void tick(){
	uiHandler.tick();
	if (UIHandler.uiState == UIStates.Game) {
		gameHandler.tick();
		GameWaves.tick();
	}
}
private void render(){
	BufferStrategy bs = window.getBufferStrategy();
	
	if (bs == null){
		window.createBufferStrategy(3);
		return;
	}
	Graphics g = image.getGraphics();
	// Background Color
	g.setColor(Color.white);
	g.fillRect(0, 0, Windows.WIDTH, Windows.HEIGHT);
	// Render the game
	if (gameHandler != null) {
		gameHandler.render(g);
		GameWaves.render(g);
	}
	
	// Blur if the main menu comes from the game
	if (gameHandler != null && UIHandler.uiState != UIStates.Game && UIHandler.uiState != UIStates.Pause){
		image = blurImage(3, image);
		g = image.getGraphics();
	}
	// Render de UI
	
	uiHandler.render(g);
	//
	
	// Setting music main menu music do loop again
	if (UIHandler.uiState != UIStates.Game
	&& UIHandler.uiState != UIStates.Pause && !UIHandler.menuSong.isRunning()) UIHandler.menuSong.loop();
	// Mouse Point
	g.setColor(Color.black);
	if (MouseInput.isOnScreen()) g.fillRect((int) ( MouseInput.getMouseX() ), (int) ( MouseInput.getMouseY() ), 1, 1);
	//
	g.dispose();
	g = bs.getDrawGraphics();
	g.drawImage(image, 0, 0, (int) ( Windows.WIDTH * Windows.SCALE ), (int) ( Windows.HEIGHT * Windows.SCALE ), null);
	bs.show();
}
public static void getNewWindow(){ window = new Windows(); }
public static void getNewGameHandler(){ gameHandler = new GameHandler(); }
public static Rectangle extendRectangle(Rectangle bounds, int x, int y, int width, int height){
	return new Rectangle(bounds.x + x, bounds.y + y, bounds.width + width, bounds.height + height);
}
public static Rectangle extendRectangle(Rectangle bounds, int value){
	return new Rectangle(bounds.x - value, bounds.y - value, bounds.width + value * 2, bounds.height + value * 2);
}
public static boolean isEven(int num){ return( num % 2 == 0 ); }
public static int clamp(int var, int min, int max){
	if (var > max) return max;
	else if (var < min) return min;
	else return var;
}
public static int clampSwitch(int var, int min, int max){
	if (var > max) return var - ( max - min + 1 );
	else if (var < min) return var + ( max - min + 1 );
	else return var;
}
public static float clamp(float var, float min, float max){
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
public static boolean checkIntArrayEquality(int[] array1, int[] array2){
	int size = array1.length;
	if (size != array2.length) return false;
	for (int m = 0; m < size; m++) if (array1[m] != array2[m]) return false;
	return true;
}
private static BufferedImage blurImage(int radius, BufferedImage image){
	int size = radius * radius;
	float[] matrix = new float[size];
	for (int i = 0; i < size; i++) matrix[i] = 1.0f / size;
	return new ConvolveOp(new Kernel(radius, radius, matrix)).filter(image, new BufferedImage(Windows.WIDTH, Windows.HEIGHT, BufferedImage.TYPE_INT_RGB));
}





}
