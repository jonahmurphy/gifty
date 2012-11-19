/*Copyright (C) 2012  Jonah Murphy

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

package gifty.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;


public class MainWindow extends JFrame{
	
	private final static Logger logger = Logger.getLogger(MainWindow.class .getName()); 
	private static final int LOG_SIZE_IN_BYTES = 20000;
	private static final int LOG_ROTATION_COUNT = 100;
	
	private static final String APP_NAME = "Gifty";
	private static final String APP_VERSION = "0.1.00001";
	
	private JPanel mainPanel;

	public MainWindow() {
		initLogging();
		initUI();
	}

	
	public void initUI() {

		mainPanel = new JPanel(new MigLayout("", "grow,fill", "[][grow,fill, push]"));
		
		createMenubar();	
		JToolBar toolbar = createToolbar();
		JTabbedPane tabbedPane = createTabbedpane();
		
		mainPanel.add(toolbar, "grow, wrap");
		mainPanel.add(tabbedPane, "grow, push");
		setContentPane(mainPanel);
		
		//Maximise the frame
		setState(Frame.NORMAL);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();
		setSize(dimension);

	}
	
	public void createMenubar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveFileMenuItem = new JMenuItem("Save file");
		JMenuItem openFileMenuItem = new JMenuItem("Open file");
		
		fileMenu.add(saveFileMenuItem);
		fileMenu.add(openFileMenuItem);
		menuBar.add(fileMenu);
		
		setJMenuBar(menuBar);
		
	}
	
	public JToolBar createToolbar() {
		JToolBar toolBar = new JToolBar("");
		toolBar.setFloatable(false);
        toolBar.setRollover(true);
      
        ImageIcon openIcon = new ImageIcon(getClass().getResource("/resources/must-have-icon-set-png/png/Open.png"));
        ImageIcon saveIcon = new ImageIcon(getClass().getResource("/resources/must-have-icon-set-png/png/Save.png"));
        
        JButton openFileButton = new JButton(openIcon);
        JButton saveFileButton = new JButton(saveIcon);
        
        toolBar.add(openFileButton);
        toolBar.add(saveFileButton);
        
        return toolBar;
	}
	
	public JTabbedPane createTabbedpane() {

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.addTab( "True/False", new JPanel() );
		tabbedPane.addTab( "Multiple Choice", new JPanel() );
		tabbedPane.addTab( "Essay", new JPanel()  );
		tabbedPane.addTab( "Fill in the Blank", new JPanel()  );
		tabbedPane.addTab( "Matching", new JPanel()  );
		tabbedPane.addTab( "Math range", new JPanel()  );
		tabbedPane.addTab( "Math range with interval end points", new JPanel()  );
		tabbedPane.addTab( "Multiple numeric answers with partial credit", new JPanel()  );
		
		return tabbedPane;	
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
