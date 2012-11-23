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

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.miginfocom.swing.MigLayout;

public class RadioButtonGroupPanel extends JPanel {

	private final static Logger logger = Logger.getLogger(RadioButtonGroupPanel.class.getName()); 
	
	private String[] buttonNames;
	private HashMap<String,JRadioButton> buttonMap;
	private String defaultCheckedButton;

	
	public RadioButtonGroupPanel(String[] buttonNames) {
		this.buttonNames = buttonNames;
		buttonMap = new HashMap<String, JRadioButton>();
		
		defaultCheckedButton = buttonNames[0];
		
		buildButtons();
	}
	
	public RadioButtonGroupPanel(String[] buttonNames, String defaultCheckedButton) {
		this.buttonNames = buttonNames;
		buttonMap = new HashMap<String, JRadioButton>();
		this.defaultCheckedButton = defaultCheckedButton;
		
		buildButtons();
		
	}
	
	public String getSelectedButtonname() {
		for(String buttonName : buttonNames) {
			if(buttonMap.get(buttonName).isSelected()) {
				return buttonName;
			}
		}
		return "";
	}
	
	public boolean setSelectedButtonByName(String name) {

		if(buttonMap.containsKey(name)) {
			buttonMap.get(name).setSelected(true);
			return true;
		}else {
			logger.log(Level.SEVERE, "No button with the name" + name + " exits");
			return false;
		}
	}
	
	
	public boolean isButtonSelected(String name) {
		if(buttonMap.containsKey(name)) {
			return buttonMap.get(name).isSelected();
		}else {
			logger.log(Level.SEVERE, "No button with the name" + name + " exits");
			return false;
		}
	}
	
	private void buildButtons() {
		ButtonGroup buttonGroup = new ButtonGroup();
		setLayout(new MigLayout(""));
		
		for(String buttonName : buttonNames) {
			JRadioButton radioButton = new JRadioButton(buttonName);
			buttonGroup.add(radioButton);
			add(radioButton);
			
			buttonMap.put(buttonName, radioButton);
		}
		
		buttonMap.get(defaultCheckedButton).setSelected(true);

	}

}
