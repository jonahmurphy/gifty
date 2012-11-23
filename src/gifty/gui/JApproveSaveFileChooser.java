package gifty.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * 
 * @see: http://stackoverflow.com/a/3729157/252701
 */
class JApproveSaveFileChooser extends JFileChooser {

	private static final long serialVersionUID = 1L;

	@Override
	public void approveSelection() {
		File file = getSelectedFile();
		if (file.exists() && getDialogType() == SAVE_DIALOG) {
			
			int result = JOptionPane.showConfirmDialog(this,
					"The file exists, overwrite?", "Existing file",
					JOptionPane.YES_NO_CANCEL_OPTION);
			
			switch (result) {
			case JOptionPane.YES_OPTION:
				super.approveSelection();
				break;
			case JOptionPane.CANCEL_OPTION:
				cancelSelection();
				break;
			default:
				break;
			}
		}else {
			
		}	
		super.approveSelection();	
	}
}
