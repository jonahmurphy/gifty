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
	
	private static final String[] ROWLABELS = { "A", "B", "C", "D", "E", "F",
		"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
		"T", "U", "V", "W", "X", "Y", "Z" };
	
	private static final int MIN_CORRECT_CHOICES = 1;
	private static final int MIN_CHOICES = 2;
	private static final int MAX_ROWS = ROWLABELS.length;
	public static final int DEFAULT_NROWS = 3;
	private int nRows;
	
	private GIFTQuestionFormatter formatter;

	// widgets
	private JTextField questionTitleTextfield;
	private JScrollPane questionsScrollpane;
	private JButton addChoiceButton;
	private JButton deleteCheckedButton;
	private ScrollableTextArea questionTextarea;
	private JCheckBox multipleRightAnswersCb;
	
	private ArrayList<ChoiceAnswerRow> choiceRows;

	public MultipleChoiceQuestionPanel() {
		formatter = new GIFTQuestionFormatter();
		initLayout();
	}

	@Override
	public String getFormattedQuestion() {
		String questionTitle = questionTitleTextfield.getText();
		String question = questionTextarea.getText();

		String formattedQuestion = "";

		if (question.isEmpty()) {
			Dialog.showEmptyQuestionBodyWarning(this);
			return "";
		}

		ArrayList<Answer> answers = new ArrayList<Answer>();
		
		if(hasMultipleRightAnswers()) {	
			
			int validCorrectAnswerCount = 0;
			int validIncorrectAnswerCount = 0;

			for (ChoiceAnswerRow answerRow : choiceRows) {
				Answer answer = answerRow.getAnswer();
				answers.add(answer);
				
				if(answerRow.isValidIncorrectMultipleAnswer()) {
					validIncorrectAnswerCount++;
				}
				if(answerRow.isValidCorrectMultipleAnswer()) {
					validCorrectAnswerCount++;
				}
			}
		
			//validate..
			if (validCorrectAnswerCount < MIN_CORRECT_CHOICES) {
				Dialog.showErrorDialog(this, "No correct answer",
						"Your question needs atleast one fully correct answer!");
				return "";
			}		
			System.out.println(validIncorrectAnswerCount + " " + validCorrectAnswerCount);	
			
			if ((validIncorrectAnswerCount + validCorrectAnswerCount) < MIN_CHOICES) {
				Dialog.showErrorDialog(this, "Not enough choices",
						"Your question needs atleast two answer choices!");
				return "";
			}
			
		}else {
			int validAnswerCount = 0;
		
			for (ChoiceAnswerRow answerRow : choiceRows) {
				Answer answer = answerRow.getAnswer();
				answers.add(answer);
				
				System.out.println(answer.getAnswerText());
				if(answerRow.isValidSingleAnswer()) {
					validAnswerCount++;
					System.out.println("what the fuck");
				}
			}
			//validate..
			if (validAnswerCount < MIN_CHOICES) {
				Dialog.showErrorDialog(this, "Not enough choices",
						"Your question needs atleast two answer choices!");
				return "";
			}	
		}
		

		formattedQuestion = formatter.formatMultipleChoiceQuestion(
				questionTitle, question, answers, hasMultipleRightAnswers());

		return formattedQuestion;
	}

	@Override
	public void clearQuestion() {
		questionTitleTextfield.setText("");
		addChoiceButton.setEnabled(true);
		deleteCheckedButton.setEnabled(true);
		resetChoiceRows();
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

		resetChoiceRows();

		// bind
		addChoiceButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addChoiceRow();
				revalidate();

				deleteCheckedButton.setEnabled(true);
			}
		});

		deleteCheckedButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteCheckedChoices();
				revalidate();
				
				addChoiceButton.setEnabled(true);
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
		buildChoiceRows();
	}

	private void deleteCheckedChoices() {
		ArrayList<ChoiceAnswerRow> rowsCopy = new ArrayList<ChoiceAnswerRow>(choiceRows);

		for (ChoiceAnswerRow rowPanel : rowsCopy) {
			if (nRows == 1) {
				deleteCheckedButton.setEnabled(false);			
				break;
			} 
			
			if (rowPanel.isMarkedForDeletion()) {
				choiceRows.remove(rowPanel);
				nRows--;
			}
		}

		relabelChoiceRows();
		buildChoiceRows();
	}

	private void addNChoiceRows(int n, boolean isMultipleRightAnswersQuestion) {
		for (int i = 0; i < n; i++) {
			if (nRows < MAX_ROWS) {
				choiceRows.add(new ChoiceAnswerRow(ROWLABELS[nRows], isMultipleRightAnswersQuestion));
				nRows++;
			}else {
				addChoiceButton.setEnabled(false);
				break;
			}
		}
		buildChoiceRows();
	}

	private void addChoiceRow() {
		if(nRows < MAX_ROWS) {
			choiceRows.add(new ChoiceAnswerRow(ROWLABELS[nRows], hasMultipleRightAnswers()));
			nRows++;
			buildChoiceRows();
		}else {
			addChoiceButton.setEnabled(false);
		}
	}

	private void resetChoiceRows() {
		choiceRows = new ArrayList<ChoiceAnswerRow>();
		nRows = 0;
		addNChoiceRows(DEFAULT_NROWS, hasMultipleRightAnswers());
	}

	/**
	 * Relabel the rows jlabels so that they go from A,B, C .. Z in sequential
	 * ascending order
	 */
	private void relabelChoiceRows() {
		int i = 0;
		for (ChoiceAnswerRow rowPanel : choiceRows) {
			rowPanel.setLabelText(ROWLABELS[i++]);

		}
	}

	/**
	 * N.B We must recreate the viewport view each time so that that each new
	 * panel is drawn properlyS
	 */
	private void buildChoiceRows() {
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

	
	private class ChoiceAnswerRow extends JPanel {
		private static final int MAX_MARK = 100;

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
		
		public boolean isValidCorrectMultipleAnswer() {

			if(!choiceTextField.getText().isEmpty() &&
				(Integer)markValueSpinner.getValue() == MAX_MARK) {
			
				return true;
			}
			
			return false;
		}
		
		public boolean isValidIncorrectMultipleAnswer() {
			if(!choiceTextField.getText().isEmpty() &&
				(Integer)markValueSpinner.getValue() < MAX_MARK){
				
				return true;
			}
			
			return false;		
		}
				
		public boolean isValidSingleAnswer() {
			return !choiceTextField.getText().isEmpty();
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
			
	        SpinnerModel spinnerModel = new SpinnerNumberModel(0, -100, MAX_MARK, 10);
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
