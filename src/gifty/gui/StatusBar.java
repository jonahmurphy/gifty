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

