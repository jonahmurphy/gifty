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
