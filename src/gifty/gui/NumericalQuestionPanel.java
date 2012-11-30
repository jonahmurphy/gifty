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
import gifty.core.NumericAnswer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import net.miginfocom.swing.MigLayout;

public class NumericalQuestionPanel extends JPanel implements IQuestion {

	private static final long serialVersionUID = 1L;
	
	private static final String[] ROWLABELS = { "A", "B", "C", "D", "E", "F",
		"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
		"T", "U", "V", "W", "X", "Y", "Z" };

	private static final int MAX_ROWS = ROWLABELS.length;
	public static final int DEFAULT_NROWS = 3;
	private static final int MAX_MARK = 100;
	private int nRows;

	private GIFTQuestionFormatter formatter;

	// widgets
	private JTextField questionTitleTextfield;
	private JScrollPane questionsScrollpane;
	private JButton addAnswerButton;
	private JButton deleteCheckedButton;
	private ScrollableTextArea questionTextarea;

	private ArrayList<NumericAnswerRow> answerRows;

	public NumericalQuestionPanel() {
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

		int validCorrectAnswerCount = 0;
		ArrayList<NumericAnswer> answers = new ArrayList<NumericAnswer>();
		for (NumericAnswerRow answerRow : answerRows) {
			NumericAnswer answer = answerRow.getAnswer();
			answers.add(answer);
			
			if(!answer.getAnswerText().isEmpty() && 
				answer.getMark() == MAX_MARK) {
				validCorrectAnswerCount++;
			}
		}
			
		if (validCorrectAnswerCount < 1) {
			Dialog.showErrorDialog(this, "No Correct answers error",
					"Your question needs atleast one fully correct answer!");
			return "";
		}

		formattedQuestion = formatter.formatNumericalQuestion(questionTitle, question, answers);

		return formattedQuestion;
	}

	@Override
	public void clearQuestion() {
		questionTitleTextfield.setText("");
		questionTextarea.setText("");
		addAnswerButton.setEnabled(true);
		deleteCheckedButton.setEnabled(true);
		resetAnswerRows();
	}
	
	private void initLayout() {
		//build
		JLabel questionTitleLbl = new JLabel("Question Title (optional)");
		questionTitleTextfield = new JTextField(20);

		JLabel questionLbl = new JLabel("Question");
		questionTextarea = new ScrollableTextArea();
		
		JLabel answersLbl = new JLabel("Answers");
		questionsScrollpane = new JScrollPane();
		questionsScrollpane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		addAnswerButton = new JButton("Add Answer...");
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

		add(answersLbl, "align right center");
		add(questionsScrollpane, "grow, wrap 40");

		add(addAnswerButton, "cell 1 3, split 3, align right, width 180::180");
		add(deleteCheckedButton, "align right, wrap, width 180::180");

		resetAnswerRows();

		// bind
		addAnswerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addAnswerRow();
				revalidate();

				deleteCheckedButton.setEnabled(true);
			}
		});

		deleteCheckedButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteCheckedAnswers();
				revalidate();

				addAnswerButton.setEnabled(true);
			}
		});

	}
	

	private void deleteCheckedAnswers() {
		ArrayList<NumericAnswerRow> rowsCopy = new ArrayList<NumericAnswerRow>(answerRows);

		for (NumericAnswerRow rowPanel : rowsCopy) {		
			if(nRows == 1) {
				deleteCheckedButton.setEnabled(false);
				break;
			}
			if (rowPanel.isMarkedForDeletion()) {
				answerRows.remove(rowPanel);
				nRows--;
			}
		}

		relabelAnswerRows();
		buildAnswerRows();
	}

	private void addNAnswerRows(int n) {
		for (int i = 0; i < n; i++) {
			if (nRows < MAX_ROWS) {
				answerRows.add(new NumericAnswerRow(ROWLABELS[nRows]));
				nRows++;
			}else {
				addAnswerButton.setEnabled(false);
				break;
			}
		}
		buildAnswerRows();
	}

	private void addAnswerRow() {
		if (nRows < MAX_ROWS) {
			answerRows.add(new NumericAnswerRow(ROWLABELS[nRows]));
			nRows++;
			buildAnswerRows();
		}else {
			addAnswerButton.setEnabled(false);
		}
	}

	private void resetAnswerRows() {
		answerRows = new ArrayList<NumericAnswerRow>();
		nRows = 0;
		addNAnswerRows(DEFAULT_NROWS);
	}

	/**
	 * Relabel the rows jlabels so that they go from A,B, C .. Z in sequential
	 * ascending order
	 */
	private void relabelAnswerRows() {
		int i = 0;
		for (NumericAnswerRow rowPanel : answerRows) {
			rowPanel.setLabelText(ROWLABELS[i++]);
		}
	}

	/**
	 * N.B We must recreate the viewport view each time so that that each new
	 * panel is drawn properly
	 */
	private void buildAnswerRows() {
		JPanel basePanel = new JPanel(new MigLayout("fill", "[]", "[]"));

		for (NumericAnswerRow rowPanel : answerRows) {
			basePanel.add(rowPanel, "growx,wrap");
		}
		questionsScrollpane.setViewportView(basePanel);
	}
	


	private class NumericAnswerRow extends JPanel {
		private static final long serialVersionUID = 1L;

		private String labelText;

		private JLabel label;
		private JTextField answerTextField;
		private JCheckBox deleteCb;
		private JSpinner markValueSpinner;
		private JTextField feedBackTextField;
		private JLabel markValueLbl;
		private JSpinner answerToleranceSpinner;


		public NumericAnswerRow(String labelText) {
			this.labelText = labelText;

			initLayout();
		}

		public boolean isMarkedForDeletion() {
			return deleteCb.isSelected();
		}
		
		public NumericAnswer getAnswer() {
			return new NumericAnswer(answerTextField.getText(),
					feedBackTextField.getText(), (Integer)answerToleranceSpinner.getValue(),
					(Integer) markValueSpinner.getValue());
		}
		
		public void setLabelText(String name) {
			this.labelText = name;
			label.setText(name);
			revalidate();
		}
		
		private void initLayout() {
			// setBorder(BorderFactory.createLineBorder(Color.black));
			setLayout(new MigLayout("", "[][grow 500]20[]0[]20[][grow 1000]20[][][]",
					"[40::40]"));
			
			//build
			label = new JLabel(labelText);
			
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setGroupingUsed(false);
			nf.setMinimumFractionDigits(1);
			answerTextField =  new JFormattedTextField(nf);
			
			JLabel toleranceLbl = new JLabel("Tolerance +/-");

			JLabel feedbackCommentLbl = new JLabel("Feedback (Optional)");
			feedBackTextField = new JTextField(10);	
			markValueLbl = new JLabel("%");
			
			SpinnerModel answerRangeSpinnerModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 10);
		    answerToleranceSpinner = new JSpinner(answerRangeSpinnerModel);
			
	        SpinnerModel markSpinnerModel = new SpinnerNumberModel(0, -100, 100, 10);
	        markValueSpinner = new JSpinner(markSpinnerModel);
			deleteCb = new JCheckBox("Delete");
			
			
			//layout
			add(label, "align right, width 10::10");
			add(answerTextField, "align right, growx, width 30::");
			add(toleranceLbl, "align right, width 70::70");
			add(answerToleranceSpinner, "width 60::60");
			add(feedbackCommentLbl);
			add(feedBackTextField, "align right, growx");
			
			add(markValueLbl);
			add(markValueSpinner);
			
			add(deleteCb);
		}
	}

}
