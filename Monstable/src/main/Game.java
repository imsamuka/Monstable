package main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
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
	//new MapGenerator("/maps/mockup1.png", "newMap1" , "/graphics/Tileset.png", new Point(3,5));
	//new MapGenerator("/maps/mockup2.png", "newMap2" , "/graphics/Tileset.png", new Point(7,7));
	
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
		if (UIHandler.uiState == UIStates.Game) GameWaves.render(g);
		else if (UIHandler.uiState != UIStates.Game && UIHandler.uiState != UIStates.Pause){
			image = Utilities.blurImage(3, image);
			g = image.getGraphics();
		}
	}
	// Render da UI	
	uiHandler.render(g);
	//
	renderMousePoint(g);
	g.dispose();
	g = bs.getDrawGraphics();
	g.drawImage(image, 0, 0, (int) ( Windows.WIDTH * Windows.SCALE ), (int) ( Windows.HEIGHT * Windows.SCALE ), null);
	bs.show();
}
public static void getNewWindow(){ window = new Windows(); }
public static void getNewGameHandler(){ gameHandler = new GameHandler(); }

private static void renderMousePoint(Graphics g) {
	g.setColor(Color.black);
	if (MouseInput.isOnScreen()) g.fillRect((int) ( MouseInput.getMouseX() ), (int) ( MouseInput.getMouseY() ), 1, 1);
	
}




}
