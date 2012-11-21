package gifty.gui;

import java.awt.Component;

import javax.swing.JOptionPane;

public class DialogUtils {

	public static void showErrorDialog(Component parent, String title, String message) {
		JOptionPane.showMessageDialog(parent, message, title,
				JOptionPane.ERROR_MESSAGE);

	}

	public static boolean showOkCancelWarningDialog(Component parent,
			String title, String message) {

		Object[] options = { "Ok", "Cancel" };

		int retVal = JOptionPane.showOptionDialog(parent, message, title,
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,
				options, options[0]);

		return (retVal == 0);
	}

	public static void showEmptyQuestionBodyWarning(Component parent) {
		showErrorDialog(parent, "Empty field error",
				"Your question needs a body to create the question!");
	}

}
