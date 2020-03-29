package main;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import inputs.KeyInput;
import inputs.MouseInput;

public class Windows extends Canvas{
private static final long            serialVersionUID = -4810618286807932601L;
public static final int              WIDTH            = 256, HEIGHT = (int) ( 1 * WIDTH );
public static int                    SCALE            = 2;
public static boolean                windowed         = true;
private static Canvas                background       = new Canvas();
protected static JFrame              frame;
private static GraphicsEnvironment   Ge               = GraphicsEnvironment.getLocalGraphicsEnvironment();
private static GraphicsDevice        Gd               = Ge.getDefaultScreenDevice();
private static GraphicsConfiguration Gc               = Gd.getDefaultConfiguration();
private static int                   maxScale         = (int) Math.min(Math.floor(Gc.getBounds().height / HEIGHT), Math.floor(Gc.getBounds().width / WIDTH));
private static MouseInput            mouseInput       = new MouseInput();
private static KeyInput              keyInput         = new KeyInput();
@SuppressWarnings("unused")
private static BufferedImage         backgroundTile;
/*private static final MyObserver      observer         = new MyObserver(GameState.subject, new observer.ObsInter(){
														public void OnDirectionChange(String direction){}
														public void OnBackgroundChange(BufferedImage background){
															backgroundTile = background
															!= null ? background : backgroundTile;
															setBackgroundExtension();
														}
														});*/

public static GraphicsConfiguration getGc(){ return Gc; }
public static int getMaxScale(){ return maxScale; }
public static void setScale(int value){ SavingData.prop.setProperty("Scale", String.valueOf(value)); }
public Windows(){
	String scale = SavingData.prop.getProperty("Scale");
	if (scale != null) SCALE = Integer.parseInt(scale);
	else SavingData.prop.setProperty("Scale", String.valueOf(SCALE));
	int width = (int) ( WIDTH * SCALE );
	int height = (int) ( HEIGHT * SCALE );
	// Frame
	if (frame != null) frame.dispose();
	frame = new JFrame("Monstable");
	frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	frame.addWindowListener(new WindowAdapter(){
	public void windowClosing(WindowEvent e){ Game.exitGame(); }
	});
	frame.setBackground(Color.black);
	// Canvas
	this.setBackground(Color.black);
	frame.add(this);
	Image i = Toolkit.getDefaultToolkit().getImage("res/graphics/cursor.png");
	Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(i, new Point(0, 0), "NewCursor");
	frame.setCursor(cursor);
	frame.requestFocusInWindow();
	frame.addKeyListener(keyInput);
	frame.addMouseListener(mouseInput);
	frame.addMouseMotionListener(mouseInput);
	this.addKeyListener(keyInput);
	this.addMouseListener(mouseInput);
	this.addMouseMotionListener(mouseInput);
	
	//DisplayMode Dm = Gd.getDisplayMode();
	if (!windowed){
		
		if (Gd.isFullScreenSupported()){
			Rectangle screen = Gc.getBounds();
			frame.setUndecorated(true);
			background = new Canvas();
			background.setBackground(Color.black);
			background.setBounds(new Rectangle(screen.x, screen.y, screen.width, screen.height));
			frame.add(background);
			//setBackgroundExtension();
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
}
public void setBackgroundExtension(){
	/*
	//background.getGraphics().drawImage(new Images("/Tileset.png").getSprite(1, 16, 16),0,0,null);
	if (backgroundTile != null && !windowed){
		int MAPBASE = GameState.MAPBASE;
		Rectangle gameScreen = new Rectangle();
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
		g.setColor(Color.yellow);
		g.fillRect(0,0, 60, 60);
		
	}
	*/
}
}