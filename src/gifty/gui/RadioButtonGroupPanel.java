package gifty.gui;

import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.miginfocom.swing.MigLayout;

public class RadioButtonGroupPanel extends JPanel {

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
	
	public void buildButtons() {
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
	
	public String getSelectedButtonname() {
		for(String buttonName : buttonNames) {
			if(buttonMap.get(buttonName).isSelected()) {
				return buttonName;
			}
		}
		return "";
	}
	
	public void setSelectedButtonByName(String name) {
		buttonMap.get(name).setSelected(true);
	}

}
