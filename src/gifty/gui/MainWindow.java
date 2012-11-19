package gifty.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;


public class MainWindow extends JFrame{
	
	private final static Logger logger = Logger.getLogger(MainWindow.class .getName()); 
	private static final int LOG_SIZE_IN_BYTES = 20000;
	private static final int LOG_ROTATION_COUNT = 100;
	
	private static final String APP_NAME = "Gifty";
	private static final String APP_VERSION = "0.1.00001";

	public MainWindow() {
		initUI();
	}

	
	public void initUI() {
		initLogging();
		setLayout(new MigLayout());
		
		//Maximise the frame
		setState(Frame.NORMAL);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();
		setSize(dimension);

	}
	
	public void  initLogging() {
		logger.setLevel(Level.FINEST);
		Handler handler;
		try {
			handler = new FileHandler(APP_NAME+"_V"+APP_VERSION+"_%u-%g.log", LOG_SIZE_IN_BYTES, LOG_ROTATION_COUNT);
			Logger.getLogger("").addHandler(handler);
		} catch (SecurityException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				MainWindow mainWindow = new MainWindow();
				mainWindow.setVisible(true);
			}
		});
	}
	
}
