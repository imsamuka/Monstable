package main;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import inputs.KeyInput;
import inputs.MouseInput;
import observer.MyObserver;

public class Windows extends Canvas{
private static final long            serialVersionUID = -4810618286807932601L;
public static final int              WIDTH            = 256, HEIGHT = (int) ( 1 * WIDTH );
public static int                    SCALE            = 2;
public static boolean                windowed         = true;
private static Canvas                background       = new Canvas();
private static JFrame                frame;
private static GraphicsEnvironment   Ge               = GraphicsEnvironment.getLocalGraphicsEnvironment();
private static GraphicsDevice        Gd               = Ge.getDefaultScreenDevice();
private static GraphicsConfiguration Gc               = Gd.getDefaultConfiguration();
private static int                   maxScale         = (int) Math.min(Math.floor(Gc.getBounds().height / HEIGHT), Math.floor(Gc.getBounds().width / WIDTH));
private static MouseInput            mouseInput       = new MouseInput();
private static KeyInput              keyInput         = new KeyInput();
private static BufferedImage         backgroundTile;
@SuppressWarnings("unused")
private static final MyObserver      observer         = new MyObserver(GameState.subject, new observer.ObsInter(){
														public void OnDirectionChange(String direction){}
														public void OnBackgroundChange(BufferedImage background){
															backgroundTile = background;
															setBackgroundExtension();
														}
														});

public static GraphicsConfiguration getGc(){ return Gc; }
public static int getMaxScale(){ return maxScale; }
public Windows(){
	int width = (int) ( WIDTH * SCALE );
	int height = (int) ( HEIGHT * SCALE );
	// Frame
	if (frame != null) frame.dispose();
	frame = new JFrame("Monstable");
	frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setBackground(Color.black);
	// Canvas
	this.setBackground(Color.black);
	frame.add(this);
	
	//DisplayMode Dm = Gd.getDisplayMode();
	if (!windowed){
		
		if (Gd.isFullScreenSupported()){
			Rectangle screen = Gc.getBounds();
			frame.setUndecorated(true);
			setBackgroundExtension();
			Gd.setFullScreenWindow(frame);
			this.setBounds(screen.x + ( screen.width / 2 ) - ( width / 2 ), screen.y + ( screen.height / 2 ) - ( height / 2 ), width, height);
		}else{
			windowed = true;
			throw new UnsupportedOperationException("Fullscreen mode is unsupported.");
		}
	}else{
		frame.setSize(width + 16, height + 38);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	frame.requestFocusInWindow();
	frame.addKeyListener(keyInput);
	frame.addMouseListener(mouseInput);
	frame.addMouseMotionListener(mouseInput);
	this.addKeyListener(keyInput);
	this.addMouseListener(mouseInput);
	this.addMouseMotionListener(mouseInput);
}
public static void setBackgroundExtension(){
	int MAPBASE = GameState.MAPBASE;
	Rectangle screen = Windows.getGc().getBounds();
	Rectangle gameScreen = new Rectangle();
	background = new Canvas();
	background.setBackground(Color.black);
	background.setBounds(new Rectangle(screen.x + 10,screen.y + 10,screen.width - 20,screen.height - 20));
	frame.add(background);
	if (backgroundTile != null && !windowed){
		int width = (int) ( Windows.WIDTH * Windows.SCALE );
		int height = (int) ( Windows.HEIGHT * Windows.SCALE );
		int mapBaseScaled = MAPBASE * Windows.SCALE;
		gameScreen.setBounds(screen.x + ( screen.width / 2 ) - ( width / 2 ), screen.y + ( screen.height / 2 ) - ( height / 2 ), width, height);
		double topDiff = gameScreen.y - screen.y;
		double bottomDiff = screen.getMaxY() - gameScreen.getMaxY();
		double leftDiff = gameScreen.x - screen.x;
		double rightDiff = screen.getMaxX() - gameScreen.getMaxX();
		System.out.println("yAxis: "+( Math.ceil(topDiff / mapBaseScaled) ));
		System.out.println("xAxis: "+( Math.ceil(leftDiff / mapBaseScaled) ));
		Graphics g = background.getGraphics();
		//for (int x = 0; x < screen.width / mapBaseScaled; x++) for (int y = 0; y< screen.height / mapBaseScaled; y++) 
		//background.getGraphics().drawImage(backgroundTile, screen.x, screen.y, mapBaseScaled, mapBaseScaled, null);
		background.getGraphics().setColor(Color.yellow);
		background.getGraphics().fillRect(-screen.width/2, -screen.height/2, screen.width, screen.height);
		
	}
}
}