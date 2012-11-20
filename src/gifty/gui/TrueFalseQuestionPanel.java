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

	private final static Logger logger = Logger
			.getLogger(TrueFalseQuestionPanel.class.getName());

	private GIFTQuestionFormatter formatter;
	private JTextField questionTitleTextfield;
	private JTextArea questionTextarea;
	private RadioButtonGroupPanel trueFalseRbgPnl;

	final private static String TRUE_BUTTON_NAME = "True";
	final private static String FALSE_BUTTON_NAME = "False";

	public TrueFalseQuestionPanel() {
		formatter = new GIFTQuestionFormatter();
		initLayout();
	}

	public void initLayout() {
		JLabel questionTitleLbl = new JLabel("Question Title (optional)");
		questionTitleTextfield = new JTextField(20);

		JLabel questionLbl = new JLabel("Question");
		questionTextarea = new JTextArea();

		trueFalseRbgPnl = new RadioButtonGroupPanel(new String[] {
				TRUE_BUTTON_NAME, FALSE_BUTTON_NAME }, "True");
		setLayout(new MigLayout("", "[][grow]", "[][grow][]"));

		add(questionTitleLbl, "align right");
		add(questionTitleTextfield, "growx, wrap");

		add(questionLbl, "align right top");
		add(questionTextarea, "grow, wrap");

		add(new JLabel("True / False"), "align right top");
		add(trueFalseRbgPnl, "align left");
	}

	@Override
	public String getFormattedQuestion() {
		String questionTitle = questionTitleTextfield.getText();
		String question = questionTextarea.getText();
		boolean isTrue = trueFalseRbgPnl.isButtonSelected(TRUE_BUTTON_NAME);

		if (question.compareTo("") == 0) {
			DialogUtil.showErrorDialog(this, "Empty field error",
					"Your question needs body to create the question!");
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
