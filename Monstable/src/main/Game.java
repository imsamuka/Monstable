package main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import game.GameHandler;
import inputs.MouseInput;
import ui.UIHandler;
import ui.UIStates;

public class Game implements Runnable{
private static Windows      window      = new Windows();
private final BufferedImage image       = new BufferedImage(Windows.WIDTH, Windows.HEIGHT, BufferedImage.TYPE_INT_RGB);
private final UIHandler     uiHandler   = new UIHandler();
private final GameHandler   gameHandler = new GameHandler();
private boolean             isRunning   = false;
private Thread              thread      = new Thread(this);

public static void main(String[] args){ new Game(); }
public Game(){ start(); }
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
			//System.out.println("FPS: "+FPS);
			FPS    = 0;
			timer += 1000;
		}
	}
}
private void tick(){
	uiHandler.tick();
	if (UIHandler.uiState == UIStates.Game) gameHandler.tick();
}
private void render(){
	BufferStrategy bs = window.getBufferStrategy();
	
	if (bs == null){
		window.createBufferStrategy(3);
		return;
	}
	Graphics g = image.getGraphics();
	//
	g.setColor(Color.white);
	g.fillRect(0, 0, Windows.WIDTH, Windows.HEIGHT);
	//
	uiHandler.render(g);
	if (UIHandler.uiState == UIStates.Game) gameHandler.render(g);
	//
	g.setColor(Color.black);
	if (MouseInput.isOnScreen()) g.fillRect((int) ( MouseInput.getMouseX() ), (int) ( MouseInput.getMouseY() ), 1, 1);
	//
	g.dispose();
	g = bs.getDrawGraphics();
	g.drawImage(image, 0, 0, (int) ( Windows.WIDTH * Windows.SCALE ), (int) ( Windows.HEIGHT * Windows.SCALE ), null);
	bs.show();
}
public static void getNewWindow(){ window = new Windows(); }
public static boolean isEven(int num){ return( num % 2 == 0 ); }
public static int clamp(int var, int min, int max){
	if (var > max) return max;
	else if (var < min) return min;
	else return var;
}
public static int clampSwitch(int var, int min, int max){
	if (var > max) return var - (max - min + 1);
	else if (var < min) return var + (max - min + 1);
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
}
