package gifty.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;

public class StatusBar extends JPanel {
	
	private JLabel statusLbl;
	
	public StatusBar(){
		
		statusLbl = new JLabel("status");
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		//JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		
		setLayout(new MigLayout("", "grow", ""));
		
		add(statusLbl, "growx,");
		//add(separator, "grow");
	}
	
	public void setStatus(String status) {
		statusLbl.setText(status);
	}	
}

