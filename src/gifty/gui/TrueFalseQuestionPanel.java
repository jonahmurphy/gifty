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

import gifty.core.GIFTQuestionFormatter;
import gifty.core.IQuestion;

import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class TrueFalseQuestionPanel extends JPanel implements IQuestion {
	
	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger
			.getLogger(TrueFalseQuestionPanel.class.getName());

	final private static String TRUE_BUTTON_NAME = "True";
	final private static String FALSE_BUTTON_NAME = "False";
	
	private GIFTQuestionFormatter formatter;
	
	private JTextField questionTitleTextfield;
	private ScrollableTextArea questionTextarea;
	private RadioButtonGroupPanel trueFalseRbgPnl;

	public TrueFalseQuestionPanel() {
		formatter = new GIFTQuestionFormatter();
		initLayout();
	}

	public void initLayout() {
		JLabel questionTitleLbl = new JLabel("Question Title (optional)");
		questionTitleTextfield = new JTextField(20);

		JLabel questionLbl = new JLabel("Question");
		questionTextarea = new ScrollableTextArea();

		trueFalseRbgPnl = new RadioButtonGroupPanel(new String[] {
				TRUE_BUTTON_NAME, FALSE_BUTTON_NAME }, "True");
		setLayout(new MigLayout("", "[][grow]", "[][grow][]"));

		add(questionTitleLbl, "align right");
		add(questionTitleTextfield, "growx, wrap");

		add(questionLbl, "align right top");
		add(questionTextarea, "grow, wrap");

		add(new JLabel("True / False"), "align right");
		add(trueFalseRbgPnl, "align left");
	}

	@Override
	public String getFormattedQuestion() {
		String questionTitle = questionTitleTextfield.getText();
		String question = questionTextarea.getText();
		boolean isTrue = trueFalseRbgPnl.isButtonSelected(TRUE_BUTTON_NAME);

		if (question.compareTo("") == 0) {
			Dialog.showEmptyQuestionBodyWarning(this);
			return "";
		}

		String formattedQuestion = formatter.formatTrueFalseQuestion(
				questionTitle, question, isTrue);
		return formattedQuestion;
	}

	@Override
	public void clearQuestion() {
		questionTitleTextfield.setText("");
		questionTextarea.setText("");
		trueFalseRbgPnl.setSelectedButtonByName(TRUE_BUTTON_NAME);

	}

}
