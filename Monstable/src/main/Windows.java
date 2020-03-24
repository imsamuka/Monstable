package main;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import javax.swing.JFrame;
import inputs.KeyInput;
import inputs.MouseInput;

public class Windows extends Canvas{
private static final long            serialVersionUID = -4810618286807932601L;
public static final int              WIDTH            = 256, HEIGHT = (int) ( 1 * WIDTH );
//0.5625
public static int                    SCALE            = 2;
public static boolean                windowed         = true;
private static JFrame                frame;
private static GraphicsEnvironment   Ge               = GraphicsEnvironment.getLocalGraphicsEnvironment();
private static GraphicsDevice        Gd               = Ge.getDefaultScreenDevice();
private static GraphicsConfiguration Gc               = Gd.getDefaultConfiguration();
private static int                   maxScale         = (int) Math.min(Math.floor(Gc.getBounds().height / HEIGHT), Math.floor(Gc.getBounds().width / WIDTH));
private static MouseInput                   mouseInput       = new MouseInput();
private static KeyInput                     keyInput         = new KeyInput();

public static int getMaxScale(){ return maxScale; }
public Windows(){
	int width = (int) ( WIDTH * SCALE );
	int height = (int) ( HEIGHT * SCALE );
	if (frame != null) frame.dispose();
	frame = new JFrame("Monstable");
	frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setBackground(Color.black);
	Canvas background = new Canvas();
	this.setBackground(Color.black);
	background.setBackground(Color.black);
	frame.add(this);
	//DisplayMode Dm = Gd.getDisplayMode();
	
	if (Gd.isFullScreenSupported() && !windowed){
		frame.add(background);
		Rectangle screen = Gc.getBounds();
		frame.setUndecorated(true);
		Gd.setFullScreenWindow(frame);
		background.setBounds(screen);
		this.setBounds(screen.x + ( screen.width / 2 ) - ( width / 2 ), screen.y + ( screen.height / 2 ) - ( height / 2 ), width, height);
	}else if (!windowed){
		windowed = true;
		throw new UnsupportedOperationException("Fullscreen mode is unsupported.");
	}
	
	if (windowed){
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
}