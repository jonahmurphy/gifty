package gifty.gui;

import gifty.core.GIFTQuestionFormatter;
import gifty.core.IQuestion;

import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class ShortAnswerQuestionPanel extends JPanel implements IQuestion {

	private final static Logger logger = Logger
			.getLogger(TrueFalseQuestionPanel.class.getName());

	private GIFTQuestionFormatter formatter;
	private JTextField questionTitleTextfield;
	private ScrollableTextArea questionTextarea;
	private JTextField answerTextfield;


	public ShortAnswerQuestionPanel() {
		formatter = new GIFTQuestionFormatter();
		initLayout();
	}

	public void initLayout() {
		
		//build
		JLabel questionTitleLbl = new JLabel("Question Title (optional)");
		questionTitleTextfield = new JTextField(20);

		JLabel questionLbl = new JLabel("Question");
		questionTextarea = new ScrollableTextArea();
		
		JLabel answerLbl = new JLabel("Answer");
		answerTextfield = new JTextField(20);
		

		//layout
		setLayout(new MigLayout("", "[][grow]", "[][grow][]"));

		add(questionTitleLbl, "align right");
		add(questionTitleTextfield, "growx, wrap");
		
		add(questionLbl, "align right top");
		add(questionTextarea, "grow, wrap");

		add(answerLbl, "align right top");
		add(answerTextfield, "growx, wrap");
	}

	@Override
	public String getFormattedQuestion() {
		String questionTitle = questionTitleTextfield.getText();
		String question = questionTextarea.getText();

		if (question.isEmpty()) {
			DialogUtils.showEmptyQuestionBodyWarning(this);
			return "";
		}
		
		String answer = answerTextfield.getText();
		
		if (answer.isEmpty()) {
			DialogUtils.showErrorDialog(this, "Empty Field", "You need to add an answer!");
			return "";
		}

		String formattedQuestion = formatter.shortAnswerQuestion(
				questionTitle, question, answer);
		return formattedQuestion;
	}

	@Override
	public void clearQuestion() {
		questionTitleTextfield.setText("");
		questionTextarea.setText("");
	}

}
