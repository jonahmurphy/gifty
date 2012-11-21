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

import gifty.core.FileManager;
import gifty.core.IQuestion;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;

public class MainWindow extends JFrame {

	private final static Logger logger = Logger.getLogger(MainWindow.class
			.getName());

	private static final int LOG_SIZE_IN_BYTES = 20000;
	private static final int LOG_ROTATION_COUNT = 100;

	private static final String APP_NAME = "Gifty";
	private static final String APP_VERSION = "0.1.00001";

	private JPanel mainPanel;

	private Action openFileAction;
	private Action saveFileAction;
	private Action clearQuestionAction;
	private Action newQuestionAction;

	private JTabbedPane tabbedPane;
	private StatusBar statusBar;
	private JToolBar toolbar;

	private FileManager fileManager;
	private ArrayList<String> questions;

	private NewAction newAction;

	/**
	 * Start the app with no file open
	 */
	public MainWindow() {
		fileManager = new FileManager();
		questions = new ArrayList<String>();
		initLogging();
		initUI();
	}

	/**
	 * Start the app with a file that will be appended to..
	 * @param filepath
	 */
	public MainWindow(String filepath) {
		this();

		//open the file to append to ...
		boolean ok = fileManager.addFileAndOpen(new File(filepath), true);

		if (!ok) {
			DialogUtil.showErrorDialog(MainWindow.this, "File error",
					"Error creating file");

			return;
		}

		statusBar.setStatus("File " + fileManager.getOpenFilepath()
				+ " open for writing...");
	}

	public static void main(String[] args) {

		if (args.length == 1) {
			final String filepath = args[0];

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					MainWindow mainWindow = new MainWindow(filepath);
					mainWindow.setVisible(true);

				}
			});
		} else {

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					MainWindow mainWindow = new MainWindow();
					mainWindow.setVisible(true);
				}
			});
		}
	}

	public void initUI() {
		setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

		statusBar = new StatusBar();
		statusBar.setStatus("No File open");

		mainPanel = new JPanel(new MigLayout("", "grow,fill",
				"[][grow,fill, push][]"));

		newAction = new NewAction("New", createIcon("New_24x24"),
				"Start a new instance of Gifty", KeyStroke.getKeyStroke("ctrl N"));

		openFileAction = new OpenFileAction("Open...",
				createIcon("Open_24x24"), "Open an existing GIFT file",
				KeyStroke.getKeyStroke("ctrl O"));

		saveFileAction = new SaveFileAction("Save...",
				createIcon("Save_24x24"),
				"Save questions to GIFT file",
				KeyStroke.getKeyStroke("ctrl S"));

		clearQuestionAction = new ClearQuestionAction("Clear Question",
				createIcon("Delete_24x24"), "Clear Question",
				KeyStroke.getKeyStroke('K', KeyEvent.CTRL_MASK));

		newQuestionAction = new NewQuestionAction("New Question",
				createIcon("Check_24x24"), "Add Question",
				KeyStroke.getKeyStroke('F', KeyEvent.CTRL_MASK));

		saveFileAction.setEnabled(false);

		createMenubar();
		toolbar = createToolbar();
		JTabbedPane tabbedPane = createTabbedpane();
		tabbedPane.setBorder(new BevelBorder(BevelBorder.LOWERED));

		mainPanel.add(toolbar, "growx,  wrap");
		mainPanel.add(tabbedPane, "grow, wrap");
		mainPanel.add(statusBar, "growx");
		setContentPane(mainPanel);

		// Maximise the frame
		setState(Frame.NORMAL);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();
		setSize(dimension);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				fileManager.closeFile();
				System.exit(0);
			}
		});

	}

	public void createMenubar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveFileMenuItem = new JMenuItem(saveFileAction);
		JMenuItem openFileMenuItem = new JMenuItem(openFileAction);
		JMenuItem newMenuItem = new JMenuItem(newAction);
		//saveFileMenuItem.setIcon(null);
		//openFileMenuItem.setIcon(null);

		fileMenu.add(saveFileMenuItem);
		fileMenu.add(openFileMenuItem);
		fileMenu.add(newMenuItem);
		menuBar.add(fileMenu);

		setJMenuBar(menuBar);

	}

	public JToolBar createToolbar() {
		JToolBar toolBar = new JToolBar("");
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		JButton newFileButton = new JButton(newAction);
		JButton openFileButton = new JButton(openFileAction);
		JButton saveFileButton = new JButton(saveFileAction);
		JButton clearQuestionButton = new JButton(clearQuestionAction);
		JButton saveQuestionButton = new JButton(newQuestionAction);

		newFileButton.setText("");
		openFileButton.setText("");
		saveFileButton.setText("");
		clearQuestionButton.setText("");
		saveQuestionButton.setText("");

		toolBar.add(newFileButton);
		toolBar.add(openFileButton);
		toolBar.add(saveFileButton);
		toolBar.add(saveQuestionButton);
		toolBar.add(clearQuestionButton);

		return toolBar;
	}

	public JTabbedPane createTabbedpane() {

		tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.addTab("True/False", new TrueFalseQuestionPanel());
		tabbedPane.addTab("Multiple Choice", new JPanel());
		tabbedPane.addTab("Essay", new JPanel());
		tabbedPane.addTab("Fill in the Blank", new JPanel());
		tabbedPane.addTab("Matching", new JPanel());
		tabbedPane.addTab("Math range", new JPanel());
		tabbedPane.addTab("Math range with interval end points", new JPanel());
		tabbedPane.addTab("Multiple numeric answers with partial credit",
				new JPanel());

		return tabbedPane;
	}

	private Icon createIcon(String imageName) {
		String imageLocation = "/resources/must-have-icon-set-png/png/"
				+ imageName + ".png";
		java.net.URL imageURL = getClass().getResource(imageLocation);

		if (imageURL == null) {
			logger.log(Level.SEVERE, "Image not found: " + imageLocation);
			return new ImageIcon();
		} else {
			return new ImageIcon(imageURL);
		}

	}

	public void initLogging() {
		logger.setLevel(Level.FINEST);
		Handler handler;
		try {
			handler = new FileHandler(APP_NAME + "_V" + APP_VERSION
					+ "_%u-%g.log", LOG_SIZE_IN_BYTES, LOG_ROTATION_COUNT);
			Logger.getLogger("").addHandler(handler);
		} catch (SecurityException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
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
	
	
	private void startNewAppInstance() {
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", "*.jar");
		startNewAppInstance(pb);

	}
	
	private void startNewAppInstance(String filepath) {
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", "*.jar",
				filepath);
		
		startNewAppInstance(pb);
		
	}
	
	private void startNewAppInstance(ProcessBuilder pb) {
		// if running from eclipse...
		File eclipse = new File((new File(".")).getAbsoluteFile(), "build");
		pb.directory(eclipse);
		if (tryStartProcess(pb)) {
			return;
		}

		// if running from jar...
		File jar = new File(".");
		pb.directory(jar);
		if (tryStartProcess(pb)) {
			// return;
		} else {
			DialogUtil.showErrorDialog(MainWindow.this,
					"Error opening instance!", "Could not find jar file!\n");

		}	
	}



	/**
	 * Try to start the process belonging to the processBuilder
	 * 
	 * @param pb
	 * @return
	 */
	public boolean tryStartProcess(ProcessBuilder pb) {
		Process process;
		try {
			process = pb.start();
		} catch (IOException e) {
			logger.log(Level.INFO, e.getMessage(), e);
			return false;
		}
		logger.info("estaseaset");
		return isProcessRunning(process);
	}

	/**
	 * dirty and not so roboust hack to see if a Process is still running N.B
	 * cannot be relied on!!
	 * 
	 * @param process
	 * @return
	 */
	public boolean isProcessRunning(Process process) {

		try {
			// will cause an exception if the process is still running!
			process.exitValue();
		} catch (IllegalThreadStateException e) {
			if (e.getMessage().compareTo("process has not exited") == 0) {

				return true;
			}
			logger.log(Level.INFO, e.getMessage(), e);
		}

		return false;
	}
	

	// ///////////////////////////////////////////////////
	// actions

	class NewAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public NewAction(String text, Icon icon, String desc,
				KeyStroke accelerator) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(ACCELERATOR_KEY, accelerator);

		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			startNewAppInstance();
		}
	}

	class SaveFileAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public SaveFileAction(String text, Icon icon, String desc,
				KeyStroke accelerator) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(ACCELERATOR_KEY, accelerator);

		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if (questions.isEmpty()) {
				return;
			}

			if (!fileManager.hasOpenFile()) {

				JFileChooser fileChooser = new JFileChooserApproveSave();
				int retVal = fileChooser.showSaveDialog(MainWindow.this);

				// get the filepath and try and open the file
				if (retVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();

					//overwrite the file if it exists already
					boolean ok = fileManager.addFileAndOpen(file, false);

					if (!ok) {
						DialogUtil.showErrorDialog(MainWindow.this,
								"File error", "Error creating file");

						return;
					}
					statusBar.setStatus("File " + fileManager.getOpenFilepath()
							+ " open for writing...");
				}else {
					return;
				}
			}
			
			ArrayList<String> questionsCopy = questions;
			questions = new ArrayList<String>();
			for (String question : questionsCopy) {
				fileManager.appendStringToFile(question + "\n\n");
			}
			fileManager.saveFile();
			saveFileAction.setEnabled(false);
		}
	}
	
	class OpenFileAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public OpenFileAction(String text, Icon icon, String desc,
				KeyStroke accelerator) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(ACCELERATOR_KEY, accelerator);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {

			JFileChooser fileChooser = new JFileChooser();
			int retVal = fileChooser.showOpenDialog(MainWindow.this);

			// get the filepath and try and open the file
			if (retVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();

				if (!file.exists()) {
					logger.log(Level.SEVERE, "File doesn't exist!!");
					DialogUtil.showErrorDialog(MainWindow.this, "File Error ",
							"Error finding file!");
					
					return;

				}
				startNewAppInstance(file.getAbsolutePath());
			}
		}
	}

	class NewQuestionAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public NewQuestionAction(String text, Icon icon, String desc,
				KeyStroke accelerator) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(ACCELERATOR_KEY, accelerator);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			IQuestion questionPanel = (IQuestion) tabbedPane.getComponent(0);

			String formattedQuestion = questionPanel.getFormattedQuestion();

			if (formattedQuestion.compareTo("") == 0) {
				return;
			}
			questions.add(formattedQuestion);
			saveFileAction.setEnabled(true);

		}
	}

	class ClearQuestionAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public ClearQuestionAction(String text, Icon icon, String desc,
				KeyStroke accelerator) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(ACCELERATOR_KEY, accelerator);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			IQuestion questionPanel = (IQuestion) tabbedPane.getComponent(0);

			questionPanel.clearQuestion();
		}

	}

	public class ExitAction extends AbstractAction {

		public ExitAction() {
			super("Exit", null);
			putValue(SHORT_DESCRIPTION, "Exit the application");

		}

		public void actionPerformed(ActionEvent aActionEvent) {
			fileManager.closeFile();
			System.exit(0);
		}
	}
}
