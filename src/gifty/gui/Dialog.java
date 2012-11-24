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

import java.awt.Component;

import javax.swing.JOptionPane;

public class Dialog {

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
