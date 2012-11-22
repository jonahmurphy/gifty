package gifty.gui;

import gifty.core.Answer;
import gifty.core.GIFTQuestionFormatter;
import gifty.core.IQuestion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import net.miginfocom.swing.MigLayout;

public class MultipleChoiceQuestionPanel extends JPanel implements IQuestion {

	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger
			.getLogger(MultipleChoiceQuestionPanel.class.getName());

	private GIFTQuestionFormatter formatter;

	// widgets
	private JTextField questionTitleTextfield;
	private JScrollPane questionsScrollpane;
	private JButton addChoiceButton;
	private JButton deleteCheckedButton;
	private ScrollableTextArea questionTextarea;
	private JCheckBox multipleRightAnswersCb;
	
	private ArrayList<ChoiceAnswerRow> choiceRows;

	private static final String[] ROWLABELS = { "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };

	private static final int MAX_ROWS = ROWLABELS.length;
	public static final int DEFAULT_NROWS = 3;
	private int nRows;

	public MultipleChoiceQuestionPanel() {
		formatter = new GIFTQuestionFormatter();
		initLayout();
	}

	@Override
	public String getFormattedQuestion() {
		String questionTitle = questionTitleTextfield.getText();
		String question = questionTextarea.getText();

		String formattedQuestion = "";

		if (question.compareTo("") == 0) {
			DialogUtils.showEmptyQuestionBodyWarning(this);
			return "";
		}

		ArrayList<Answer> answers = new ArrayList<Answer>();
		for (ChoiceAnswerRow answerRow : choiceRows) {
			answers.add(answerRow.getAnswer());
		}

		formattedQuestion = formatter.formatMultipleChoiceQuestion(
				questionTitle, question, answers, hasMultipleRightAnswers());

		return formattedQuestion;
	}

	@Override
	public void clearQuestion() {
		questionTitleTextfield.setText("");
		resetRows();
	}


	private void initLayout() {
		//build
		JLabel questionTitleLbl = new JLabel("Question Title (optional)");
		questionTitleTextfield = new JTextField(20);

		JLabel questionLbl = new JLabel("Question");
		questionTextarea = new ScrollableTextArea();
		
		JLabel choicesLbl = new JLabel("Choices");
		questionsScrollpane = new JScrollPane();
		questionsScrollpane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		multipleRightAnswersCb = new JCheckBox("Multiple Correct Answers ?");
		addChoiceButton = new JButton("Add Choice...");
		deleteCheckedButton = new JButton("Deleted checked...");

		// layout
		setLayout(new MigLayout("",
				// cols
				"[][grow]",

				// rows
				"[]" + 
				"10" + 
				"[grow 1, align top]" + 
				"10"+ 
				"[grow 1000,align top]" + 
				"[align top]"));

		add(questionTitleLbl, "align right");
		add(questionTitleTextfield, "growx, wrap");

		add(questionLbl, "align right top");
		add(questionTextarea, "growx, height 200::200, wrap 40");

		add(choicesLbl, "align right center");
		add(questionsScrollpane, "grow, wrap 40");

		add(multipleRightAnswersCb, "cell 1 3, split 4, align left, growx");
		add(addChoiceButton, "align right, width 180::180");
		add(deleteCheckedButton, "align right, wrap, width 180::180");

		resetRows();

		// bind
		addChoiceButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addChoiceRow();
				revalidate();

				if (nRows == 1) {
					deleteCheckedButton.setEnabled(true);
				} else if (nRows == MAX_ROWS) {
					addChoiceButton.setEnabled(false);
				}
			}
		});

		deleteCheckedButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteCheckedChoices();
				revalidate();

				if (nRows == 0) {
					deleteCheckedButton.setEnabled(false);
				} else if (nRows < MAX_ROWS) {
					addChoiceButton.setEnabled(true);
				}
			}
		});
		
		
		multipleRightAnswersCb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisibleChoices(multipleRightAnswersCb.isSelected());	
			}
		});
	}
	
	
	private void setVisibleChoices(boolean visible) {
		for (ChoiceAnswerRow rowPanel : choiceRows) {
			rowPanel.setMarkValueVisible(visible);
		}
		buildRows();
	}

	private void deleteCheckedChoices() {
		ArrayList<ChoiceAnswerRow> rowsCopy 
			= (ArrayList<ChoiceAnswerRow>) choiceRows.clone();
		
		for (ChoiceAnswerRow rowPanel : rowsCopy) {
			if (rowPanel.isMarkedForDeletion()) {
				choiceRows.remove(rowPanel);
				nRows--;
			}
		}

		relabelRows();
		buildRows();
	}

	private void addNChoiceRows(int n, boolean isMultipleRightAnswersQuestion) {
		for (int i = 0; i < n; i++) {
			if (nRows < MAX_ROWS) {
				choiceRows.add(new ChoiceAnswerRow(ROWLABELS[nRows], isMultipleRightAnswersQuestion));
				nRows++;
			}
		}
		buildRows();
	}

	private void addChoiceRow() {
		if (nRows < MAX_ROWS) {
			choiceRows.add(new ChoiceAnswerRow(ROWLABELS[nRows], hasMultipleRightAnswers()));
			nRows++;
			buildRows();
		}
	}

	private void resetRows() {
		choiceRows = new ArrayList<ChoiceAnswerRow>();
		nRows = 0;
		addNChoiceRows(DEFAULT_NROWS, hasMultipleRightAnswers());
	}

	/**
	 * Relabel the rows jlabels so that they go from A,B, C .. Z in sequential
	 * ascending order
	 */
	private void relabelRows() {
		int i = 0;
		for (ChoiceAnswerRow rowPanel : choiceRows) {
			rowPanel.setLabelText(ROWLABELS[i++]);

		}
	}

	/**
	 * N.B We must recreate the viewport view each time so that that each new
	 * panel is drawn properlyS
	 */
	private void buildRows() {
		JPanel basePanel = new JPanel(new MigLayout("fill", "[]", "[]"));
		ButtonGroup correctAnswerButtonGroup = new ButtonGroup();

		for (ChoiceAnswerRow rowPanel : choiceRows) {
			basePanel.add(rowPanel, "growx,wrap");
			correctAnswerButtonGroup.add(rowPanel.getCorrectAnswerCb());
		}
		choiceRows.get(0).setIsCorrect(true);//one has to be selected!
		questionsScrollpane.setViewportView(basePanel);
	}
	
	private boolean hasMultipleRightAnswers() {
		return multipleRightAnswersCb.isSelected();
	}

	/*
	 * A panel a number of rows of jtextFields with labels date driven by the
	 * fieldNames array in outer class
	 */
	private class ChoiceAnswerRow extends JPanel {
		private static final long serialVersionUID = 1L;

		private String labelText;

		private JLabel label;
		private JTextField choiceTextField;
		private JCheckBox deleteCb;
		private JSpinner markValueSpinner;
		private JTextField feedBackTextField;
		private JLabel markValueLbl;

		private JRadioButton correctAnswerRb;

		public ChoiceAnswerRow(String labelText) {
			this.labelText = labelText;

			initLayout();
		}

		public ChoiceAnswerRow(String labelText, boolean isFeedbackAmountVisble) {
			this(labelText);
			
			setMarkValueVisible(isFeedbackAmountVisble);
		}

		public boolean isMarkedForDeletion() {
			return deleteCb.isSelected();
		}
		
		
		public JRadioButton getCorrectAnswerCb() {
			return correctAnswerRb;
		}
		
		public void setIsCorrect(boolean isSelected) {
			correctAnswerRb.setSelected(isSelected);
			
		}

		public Answer getAnswer() {
			return new Answer(choiceTextField.getText(),
					feedBackTextField.getText(), correctAnswerRb.isSelected(),
					(Integer) markValueSpinner.getValue());
		}
		
		public void setLabelText(String name) {
			this.labelText = name;
			label.setText(name);
			revalidate();
		}
		
		public void setMarkValueVisible(boolean visible) {
			markValueSpinner.setVisible(visible);
			markValueLbl.setVisible(visible);
			correctAnswerRb.setVisible(!visible);
		}

		private void initLayout() {
			// setBorder(BorderFactory.createLineBorder(Color.black));
			setLayout(new MigLayout("", "[][grow ]20[][grow]20[][][]",
					"[40::40]"));
			
			//build
			label = new JLabel(labelText);
			choiceTextField = new JTextField(10);
			JLabel feedbackCommentLbl = new JLabel("Feedback (Optional)");
			feedBackTextField = new JTextField(10);	
			markValueLbl = new JLabel("%");
			correctAnswerRb = new JRadioButton("Correct ?");
			
	        SpinnerModel spinnerModel = new SpinnerNumberModel(0, -100, 100, 10);
	        markValueSpinner = new JSpinner(spinnerModel);
			deleteCb = new JCheckBox("Delete");
			
			
			//layout
			add(label, "align right, width 10::10");
			add(choiceTextField, "align right, growx");
			add(feedbackCommentLbl);
			add(feedBackTextField, "align right, growx");
			
			add(correctAnswerRb);
			add(markValueLbl);
			add(markValueSpinner);
			
			add(deleteCb);
		}
	}

}
