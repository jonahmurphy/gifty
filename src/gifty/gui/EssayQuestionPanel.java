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

public class EssayQuestionPanel extends JPanel implements IQuestion {

	private final static Logger logger = Logger
			.getLogger(TrueFalseQuestionPanel.class.getName());

	private GIFTQuestionFormatter formatter;
	private JTextField questionTitleTextfield;
	private ScrollableTextArea questionTextarea;


	public EssayQuestionPanel() {
		formatter = new GIFTQuestionFormatter();
		initLayout();
	}

	public void initLayout() {
		JLabel questionTitleLbl = new JLabel("Question Title (optional)");
		questionTitleTextfield = new JTextField(20);

		JLabel questionLbl = new JLabel("Question");
		questionTextarea = new ScrollableTextArea();

		setLayout(new MigLayout("", "[][grow]", "[][grow][]"));

		add(questionTitleLbl, "align right");
		add(questionTitleTextfield, "growx, wrap");

		add(questionLbl, "align right top");
		add(questionTextarea, "grow, wrap");
	}

	@Override
	public String getFormattedQuestion() {
		String questionTitle = questionTitleTextfield.getText();
		String question = questionTextarea.getText();

		if (question.compareTo("") == 0) {
			DialogUtils.showEmptyQuestionBodyWarning(this);
			return "";
		}

		String formattedQuestion = formatter.formatEssayQuestion(
				questionTitle, question);
		return formattedQuestion;
	}

	@Override
	public void clearQuestion() {
		questionTitleTextfield.setText("");
		questionTextarea.setText("");
	}

}
