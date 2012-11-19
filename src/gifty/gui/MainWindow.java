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

import gifty.core.IQuestion;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.miginfocom.swing.MigLayout;


public class MainWindow extends JFrame{
	
	private final static Logger logger = Logger.getLogger(MainWindow.class .getName()); 
	
	private static final int LOG_SIZE_IN_BYTES = 20000;
	private static final int LOG_ROTATION_COUNT = 100;
	
	private static final String APP_NAME = "Gifty";
	private static final String APP_VERSION = "0.1.00001";
	
	private JPanel mainPanel;
	
	private Action openFileAction;
	private Action saveFileAction;
	private Action clearQuestionAction;
	private Action newQuestionAction;
	
	JTabbedPane tabbedPane;

	public MainWindow() {
		initLogging();
		initUI();
	}
	
	
	public void initUI() {
		setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		
		mainPanel = new JPanel(new MigLayout("", "grow,fill",
				"[][grow,fill, push]"));

		openFileAction = new OpenFileAction("Open File", createIcon("Open"),
				"Open an existing GIFT file", new Integer(KeyEvent.VK_O));
		
		saveFileAction = new SaveFileAction("Save File", createIcon("Save"),
				"Save current file GIFT question file", new Integer(
						KeyEvent.VK_S));
		
		clearQuestionAction = new ClearQuestionAction("Clear Question",
				createIcon("Delete"), "Clear Question",
				new Integer(KeyEvent.VK_S));

		newQuestionAction = new NewQuestionAction("New Question",
				createIcon("Check"), "New Question",
				new Integer(KeyEvent.VK_S));

		createMenubar();
		JToolBar toolbar = createToolbar();
		JTabbedPane tabbedPane = createTabbedpane();

		mainPanel.add(toolbar, "grow, wrap");
		mainPanel.add(tabbedPane, "grow, push");
		setContentPane(mainPanel);

		// Maximise the frame
		setState(Frame.NORMAL);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();
		setSize(dimension);

	}
	

	public void createMenubar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveFileMenuItem = new JMenuItem(saveFileAction);
		JMenuItem openFileMenuItem = new JMenuItem(openFileAction);
		saveFileMenuItem.setIcon(null);
		openFileMenuItem.setIcon(null);
		
		fileMenu.add(saveFileMenuItem);
		fileMenu.add(openFileMenuItem);
		menuBar.add(fileMenu);
		
		setJMenuBar(menuBar);
		
	}
	
	public JToolBar createToolbar() {
		JToolBar toolBar = new JToolBar("");
		toolBar.setFloatable(false);
        toolBar.setRollover(true);

        JButton openFileButton = new JButton(openFileAction);
        JButton saveFileButton = new JButton(saveFileAction);
        JButton clearQuestionButton = new JButton(clearQuestionAction);
        JButton saveQuestionButton = new JButton(newQuestionAction);
        
        openFileButton.setText(""); 
        saveFileButton.setText("");  
        clearQuestionButton.setText(""); 
        saveQuestionButton .setText(""); 

        
        toolBar.add(openFileButton);
        toolBar.add(saveFileButton);
        toolBar.add(saveQuestionButton);
        toolBar.add(clearQuestionButton);
              
        return toolBar;
	}
	
	public JTabbedPane createTabbedpane() {

		tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.addTab( "True/False", new TrueFalseQuestionPanel() );
		tabbedPane.addTab( "Multiple Choice", new JPanel() );
		tabbedPane.addTab( "Essay", new JPanel()  );
		tabbedPane.addTab( "Fill in the Blank", new JPanel()  );
		tabbedPane.addTab( "Matching", new JPanel()  );
		tabbedPane.addTab( "Math range", new JPanel()  );
		tabbedPane.addTab( "Math range with interval end points", new JPanel()  );
		tabbedPane.addTab( "Multiple numeric answers with partial credit", new JPanel()  );

		return tabbedPane;	
	}
	
	private Icon createIcon(String imageName) {
		String imageLocation = "/resources/must-have-icon-set-png/png/" + imageName+ ".png";
		java.net.URL imageURL = getClass().getResource(imageLocation);

		if (imageURL == null) {
			logger.log(Level.SEVERE, "Image not found: " + imageLocation);
			return new ImageIcon();
		} else {
			return new ImageIcon(imageURL);
		}

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
	
	
	private void setLookAndFeel(String lookAndFeel) {
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (InstantiationException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (UnsupportedLookAndFeelException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	
	private void newQuestion() {
		IQuestion questionPanel = (IQuestion)tabbedPane.getComponent(0);

		String formattedQuestion = questionPanel.getFormattedQuestion();
		
		if(formattedQuestion.compareTo("") == 0) {
			return;
		}
		System.out.println("Got formatted question " + formattedQuestion);
	}
	

	private void clearQuestion() {
		IQuestion questionPanel = (IQuestion)tabbedPane.getComponent(0);

		questionPanel.clearQuestion();
	}

	
	/////////////////////////////////////////////////////
	//actions
	
	
	class SaveFileAction extends AbstractAction {
		public SaveFileAction(String text, Icon icon, String desc,
				Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}


		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("saveFileaction...");
			
		}

	}
	
	class OpenFileAction extends AbstractAction {
		public OpenFileAction(String text, Icon icon, String desc,
				Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}


		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("openFileaction...");
			
		}

	}
	
	class NewQuestionAction extends AbstractAction {
		public NewQuestionAction(String text, Icon icon, String desc,
				Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}


		@Override
		public void actionPerformed(ActionEvent arg0) {
			newQuestion();		
		}
	}
	
	
	class ClearQuestionAction extends AbstractAction {
		public ClearQuestionAction(String text, Icon icon, String desc,
				Integer mnemonic) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}


		@Override
		public void actionPerformed(ActionEvent arg0) {
			clearQuestion();

		}

	}
}
