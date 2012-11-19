package gifty.gui;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class TrueFalseQuestionPanel extends JPanel {

	public TrueFalseQuestionPanel() {
		initLayout();
	}
	
	public void initLayout() {
		JLabel questionTitleLbl = new JLabel("Question Title (optional)");
		JTextField questionTitleTextfield = new JTextField(20);
		
		JLabel questionLbl = new JLabel("Question");
		JTextArea questionTextarea = new JTextArea();
		
		RadioButtonGroupPanel trueFalseRbgPnl = new RadioButtonGroupPanel(new String[]{"True", "False"}, "True");
		setLayout(new MigLayout("", "[][grow]", "[][grow][]"));
		
		add(questionTitleLbl, "align right");
		add(questionTitleTextfield, "growx, wrap");
		
		add(questionLbl, "align right top");
		add(questionTextarea, "grow, wrap");
		
		add(new JLabel("True / False"), "align right top");
		add(trueFalseRbgPnl, "align left");	
	}

}
