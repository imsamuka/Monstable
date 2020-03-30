package main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import game.GameHandler;
import game.GameState;
import game.GameWaves;
import inputs.MouseInput;
import ui.ObjText;
import ui.UIHandler;
import ui.UIList;
import ui.UIStates;


public class Game implements Runnable{
private static Windows       window;
private static GameHandler   gameHandler;
private static BufferedImage image        = new BufferedImage(Windows.WIDTH, Windows.HEIGHT, BufferedImage.TYPE_INT_RGB);
public static UIHandler      uiHandler;
public static boolean        isRunning    = false, imageCreated = false, planB = true;
private Thread               thread       = new Thread(this);           

public static void main(String[] args){ new Game(); }
public Game(){
	SavingData.getSaveFiles();
	getNewWindow();
	uiHandler = new UIHandler();
	
	//for (int i = 0; i < 15; i++) new MapGenerator("/maps/map"+i+".png", "newMap"+i, "/graphics/tileset_florest.png", new Point(3,5));
	
	start();
}
public synchronized void start(){
	isRunning = true;
	thread.setName("FPS");
	thread.start();
}
public static void exitGame(){
	SavingData.setSaveFiles();
	System.exit(1);
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
	if (GameWaves.record == null) GameWaves.record = new ObjText(Windows.WIDTH / 2, 85, UIStates.Modes, "Record: "+ String.valueOf(GameState.lastWave), Color.black, UIList.alphbeta18);
	
	
	
	uiHandler.tick();
	
	if (UIHandler.uiState == UIStates.Game){
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
	if (gameHandler != null){
		gameHandler.render(g);
		if (UIHandler.uiState == UIStates.Game) GameWaves.render(g);
		//else if (UIHandler.uiState != UIStates.Game && UIHandler.uiState != UIStates.Pause){ createBackground(); }
	}
	// Render da UI	
	uiHandler.render(g);
	if (UIHandler.uiState == UIStates.Modes) {
		GameWaves.record.setText( "Record: "+ SavingData.prop.getProperty("Record"));
		GameWaves.record.render(g);
	}
	else if (UIHandler.uiState == UIStates.Death) {
		GameWaves.lastWave.setVisible(GameState.arcadeMode);
		GameWaves.lastWave.render(g);
	}
	//
	//renderMousePoint(g);
	g.dispose();
	g = bs.getDrawGraphics();
	g.drawImage(image, 0, 0, (int) ( Windows.WIDTH * Windows.SCALE ), (int) ( Windows.HEIGHT * Windows.SCALE ), null);
	bs.show();
}
public static void createBackground() {
	if (new File("C:\\Users\\ziza\\Desktop\\backgrounds").exists()) {
		int value = 0;
		File file = new File("C:\\Users\\ziza\\Desktop\\backgrounds\\background_"+value+".png");
		
		while(file.exists()){
			value++;
			file = new File("C:\\Users\\ziza\\Desktop\\backgrounds\\\\background_"+value+".png");
		}
		if (!imageCreated) {
			try{
				ImageIO.write(image, "png", file);
				System.out.println("New Background");
				
				imageCreated = true;
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}
public static void getNewWindow(){ window = new Windows(); }
public static void getNewGameHandler(){ gameHandler = new GameHandler(); }
protected static void renderMousePoint(Graphics g){
	g.setColor(Color.black);
	if (MouseInput.isOnScreen()) g.fillRect((int) ( MouseInput.getMouseX() ), (int) ( MouseInput.getMouseY() ), 1, 1);
}
}
