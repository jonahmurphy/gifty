package gifty.core;

import gifty.gui.GiftyApp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple utility class to manage a single file methods to open,create,save and
 * append a string to a file
 * 
 */
public class FileManager {

	private final static Logger logger = Logger.getLogger(FileManager.class
			.getName());

	private File file;
	private PrintWriter writer;

	public FileManager() {
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * If the FileManager is currently managing a file it closes it before
	 * creating the new file
	 * 
	 * if the file doesnt exist it trys to create it
	 * 
	 * @param filepath
	 * @return
	 */
	public boolean addFileAndOpen(File aFile, boolean append) {
		closeFile();

		file = aFile;

		// create the file if it doesn't exist
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				return false;
			}
		} 

		// setup the printwriter
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(file, append)), append);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return false;
		}

		return true;
	}

	public boolean saveFile() {
		writer.flush();
		writer.close();
		
		return addFileAndOpen(file, true);

	}

	public void closeFile() {
		if(hasOpenFile() ) {
			writer.flush();
			writer.close();
		}
	}

	public boolean appendStringToFile(String string) {
		if (file == null || !file.canWrite()) {
			logger.info("Could not write to file...");
			return false;
		}
		writer.println(string);
		return true;
	}

	public boolean hasOpenFile() {

		if (file == null || writer == null || !file.exists()
				|| !file.canWrite()) {

			return false;
		}

		return true;

	}
	
	public String getOpenFilepath() {
		if(hasOpenFile()) {
			return file.getPath();
		} else {
			return "";
		}
	}

}
