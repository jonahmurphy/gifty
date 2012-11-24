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
import java.awt.Font;
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
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;

public class GiftyApp extends JFrame {

	private final static Logger logger = Logger.getLogger(GiftyApp.class
			.getName());

	private final static String ICONS_PATH = "/resources/must-have-icon-set-png/png/";
	private static final int LOG_SIZE_IN_BYTES = 20000;
	private static final int LOG_ROTATION_COUNT = 100;
	private static final String APP_NAME = "Gifty";
	private static final String APP_VERSION = "0.1.00001";

	private Action openFileAction;
	private Action saveFileAction;
	private Action newAction;
	private Action saveFileAsAction;
	private Action clearQuestionAction;
	private Action newQuestionAction;

	private JPanel mainPanel;
	private JTabbedPane tabbedPane;
	private StatusBar statusBar;
	private JToolBar toolbar;

	private FileManager fileManager;
	private ArrayList<String> questions;

	/**
	 * Start the app with no file open
	 */
	public GiftyApp() {
		fileManager = new FileManager();
		questions = new ArrayList<String>();
		initLogging();
		initUI();
	}

	/**
	 * Start the app with a file that will be appended to..
	 * 
	 * @param filepath
	 */
	public GiftyApp(String filepath) {
		this();

		// open the file to append to ...
		boolean ok = fileManager.addFileAndOpen(new File(filepath), true);

		if (!ok) {
			Dialog.showErrorDialog(GiftyApp.this, "File error",
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
					GiftyApp mainWindow = new GiftyApp(filepath);
					mainWindow.setVisible(true);

				}
			});
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					GiftyApp mainWindow = new GiftyApp();
					mainWindow.setVisible(true);
				}
			});
		}
	}

	public void initUI() {
		setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		// setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

		statusBar = new StatusBar();
		statusBar.setStatus("No File open");

		mainPanel = new JPanel(new MigLayout("", "grow,fill",
				"[][grow,fill, push][]"));

		newAction = new NewAction("New", createIcon("New_24x24"),
				"Start a new instance of Gifty",
				KeyStroke.getKeyStroke("ctrl N"));

		openFileAction = new OpenFileAction("Open...",
				createIcon("Open_24x24"), "Open an existing GIFT file",
				KeyStroke.getKeyStroke("ctrl O"));

		saveFileAction = new SaveFileAction("Save...",
				createIcon("Save_24x24"), "Save questions to GIFT file",
				KeyStroke.getKeyStroke("ctrl S"));

		saveFileAsAction = new SaveFileAsAction("Save As...",
				createIcon("SaveAs_26x26"), "Save questions to GIFT file",
				KeyStroke.getKeyStroke("ctrl alt S"));

		clearQuestionAction = new ClearQuestionAction("Clear Question",
				createIcon("Delete_24x24"), "Clear Question",
				KeyStroke.getKeyStroke('K', KeyEvent.CTRL_MASK));

		newQuestionAction = new NewQuestionAction("New Question",
				createIcon("Check_24x24"), "Add Question",
				KeyStroke.getKeyStroke('F', KeyEvent.CTRL_MASK));

		saveFileAction.setEnabled(false);
		saveFileAsAction.setEnabled(false);

		createMenubar();
		toolbar = createToolbar();
		JTabbedPane tabbedPane = createTabbedpane();
		tabbedPane.setBorder(new BevelBorder(BevelBorder.LOWERED));

		mainPanel.add(toolbar, "growx,  wrap");
		mainPanel.add(tabbedPane, "grow, wrap");
		mainPanel.add(statusBar, "growx");
		setContentPane(mainPanel);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				fileManager.closeFile();
				System.exit(0);
			}
		});
		
		setMinimumSize(new Dimension(1150,550));
		setSize(new Dimension(1150,550));
		pack();
	}

	public void createMenubar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");

		Action[] actions = { saveFileAction, saveFileAsAction, openFileAction,
				newAction };

		for (Action action : actions) {
			JMenuItem menuItem = new JMenuItem(action);
			fileMenu.add(menuItem);
		}

		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
	}

	public JToolBar createToolbar() {
		JToolBar toolBar = new JToolBar("");
		toolBar.setFloatable(false);
		toolBar.setRollover(true);

		Action[] actions = { newAction, openFileAction, saveFileAction,
				saveFileAsAction, newQuestionAction, clearQuestionAction };

		for (Action action : actions) {
			JButton button = new JButton(action);
			button.setText("");
			toolBar.add(button);
		}
		return toolBar;
	}

	public JTabbedPane createTabbedpane() {

		tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		
		tabbedPane.addTab("Math Range", new MathRangeQuestionPanel());			
		tabbedPane.addTab("Numerical", new NumericalQuestionPanel());
		tabbedPane.addTab("Multiple Choice", new MultipleChoiceQuestionPanel());
		tabbedPane.addTab("Matching", new MatchingQuestionPanel());
		tabbedPane.addTab("Short Answer", new ShortAnswerQuestionPanel());
		tabbedPane.addTab("True / False", new TrueFalseQuestionPanel());
		tabbedPane.addTab("Essay", new EssayQuestionPanel());

		tabbedPane.setFont( new Font( "Dialog", Font.BOLD, 11) );
		
		return tabbedPane;
	}

	private Icon createIcon(String imageName) {
		String imageLocation = ICONS_PATH + imageName + ".png";
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
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", "gifty.jar",
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
			return;
		} else {
			Dialog.showErrorDialog(GiftyApp.this,
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
		return isProcessRunning(process);
	}

	/**
	 * Dirty and not so roboust hack to see if a Process is still running 
	 * N.B cannot be relied on!!
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
	

	private boolean trySaveAs() {
		JFileChooser fileChooser = new JApproveSaveFileChooser();
		int retVal = fileChooser.showSaveDialog(GiftyApp.this);

		// get the filepath and try and open the file
		if (retVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			// overwrite the file if it exists already
			boolean ok = fileManager.addFileAndOpen(file, false);

			if (!ok) {
				Dialog.showErrorDialog(GiftyApp.this,
						"File error", "Error creating file");
				return false;
			}else {
				statusBar.setStatus("File " + fileManager.getOpenFilepath()
						+ " open for writing...");
				return true;
			}
		} else {
			return false;
		}	

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

	class SaveFileAsAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public SaveFileAsAction(String text, Icon icon, String desc,
				KeyStroke accelerator) {
			super(text, icon);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(ACCELERATOR_KEY, accelerator);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (questions.isEmpty() || !trySaveAs()) {
				return;
			}

			ArrayList<String>questionsCopy = new ArrayList<String>(questions);
			fileManager.appendStringsToFile(questionsCopy);
			fileManager.saveFile();
			saveFileAction.setEnabled(false);
			saveFileAsAction.setEnabled(false);
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

			if (questions.isEmpty()) 
				return;
			if (!fileManager.hasOpenFile()) {
				if( !trySaveAs() ) {
					return;
				}
			}
			ArrayList<String>questionsCopy = new ArrayList<String>(questions);
			fileManager.appendStringsToFile(questionsCopy);
			fileManager.saveFile();
			saveFileAction.setEnabled(false);
			saveFileAsAction.setEnabled(false);
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
			int retVal = fileChooser.showOpenDialog(GiftyApp.this);

			// get the filepath and try and open the file
			if (retVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();

				if (!file.exists()) {
					logger.log(Level.SEVERE, "File doesn't exist!!");
					Dialog.showErrorDialog(GiftyApp.this, "File Error ",
							"Error finding file!");
				}else {
					startNewAppInstance(file.getAbsolutePath());
				}
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
			IQuestion questionPanel = (IQuestion) tabbedPane
					.getComponentAt(tabbedPane.getSelectedIndex());

			String formattedQuestion = questionPanel.getFormattedQuestion();

			if (!formattedQuestion.isEmpty()) {
				questions.add(formattedQuestion);
				saveFileAction.setEnabled(true);
				saveFileAsAction.setEnabled(true);
			}
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
			IQuestion questionPanel = (IQuestion) tabbedPane
					.getComponentAt(tabbedPane.getSelectedIndex());

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
