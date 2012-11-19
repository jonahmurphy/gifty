package gifty.gui;

import java.awt.Component;

import javax.swing.JOptionPane;

public class DialogUtil {
	
	public static void showErrorDialog(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message,  "Error ", JOptionPane.ERROR_MESSAGE);
		
	}

}
