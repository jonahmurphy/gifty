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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class ShortAnswerQuestionPanel extends JPanel implements IQuestion {

	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger
			.getLogger(TrueFalseQuestionPanel.class.getName());

	private GIFTQuestionFormatter formatter;
	
	private static final String[] ROWLABELS = { "A", "B", "C", "D", "E", "F",
		"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
		"T", "U", "V", "W", "X", "Y", "Z" };

	private static final int MIN_ANSWERS = 1;
	private static final int MAX_ROWS = ROWLABELS.length;
	public static final int DEFAULT_NROWS = 3;
	private int nRows;

	// widgets
	private JTextField questionTitleTextfield;
	private JScrollPane answersScrollpane;
	private JButton addAnswerButton;
	private JButton deleteCheckedButton;
	private ScrollableTextArea questionTextarea;

	private ArrayList<AnswerRow> answerRows;

	public ShortAnswerQuestionPanel() {
		formatter = new GIFTQuestionFormatter();
		initLayout();
	}

	@Override
	public String getFormattedQuestion() {
		String questionTitle = questionTitleTextfield.getText();
		String question = questionTextarea.getText();

		if (question.isEmpty()) {
			Dialog.showEmptyQuestionBodyWarning(this);
			return "";
		}
		
		int validAnswerCount = 0;
		ArrayList<String> answers = new ArrayList<String>();
		for(AnswerRow questionRow : answerRows) {
			
			String answer = questionRow.getAnswerText();
			answers.add(answer);
			if(!answer.isEmpty()) {
				validAnswerCount++;
			}
		}
			
		if(validAnswerCount < MIN_ANSWERS) {
			Dialog.showErrorDialog(this, "No answers error", "Your question needs atleast one answer!");
			return "";
		}

		String formattedQuestion =
				formatter.formatShortAnswerQuestion(questionTitle, question, answers);
		return formattedQuestion;
	}

	@Override
	public void clearQuestion() {
		questionTitleTextfield.setText("");
		addAnswerButton.setEnabled(true);
		deleteCheckedButton.setEnabled(true);
		resetAnswerRows();
	}
	

	private void initLayout() {
		// build
		JLabel questionTitleLbl = new JLabel("Question Title (optional)");
		questionTitleTextfield = new JTextField(20);

		JLabel questionLbl = new JLabel("Question");
		questionTextarea = new ScrollableTextArea();

		addAnswerButton = new JButton("Add Answer...");
		deleteCheckedButton = new JButton("Deleted checked...");

		JLabel answersLbl = new JLabel("Answers");
		answersScrollpane = new JScrollPane();
		answersScrollpane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// layout
		setLayout(new MigLayout("",
				// cols
				"[][grow]",

				// rows
				"[]" + 
				"10" + 
				"[grow 1, align top]" + 
				"10"+ 
				"[grow 1000, fill, align top]" + 
				"[align top]"));

		add(questionTitleLbl, "align right");
		add(questionTitleTextfield, "growx, wrap");

		add(questionLbl, "align right top");
		add(questionTextarea, "growx, height 200::200, wrap 40");

		add(answersLbl, "align right");
		add(answersScrollpane, "grow, wrap 30");

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
				deleteCheckedQuestions();
				revalidate();

				addAnswerButton.setEnabled(true);
			}
		});
	}

	private void deleteCheckedQuestions() {
		ArrayList<AnswerRow> rowsCopy = new ArrayList<AnswerRow>(answerRows);
	
		for (AnswerRow rowPanel : rowsCopy) {
			
			if(nRows == MIN_ANSWERS) {
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
				answerRows.add(new AnswerRow(ROWLABELS[nRows]));
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
			answerRows.add(new AnswerRow(ROWLABELS[nRows]));
			nRows++;
			buildAnswerRows();
		}else {
			addAnswerButton.setEnabled(false);
		}
	}

	private void resetAnswerRows() {
		answerRows = new ArrayList<AnswerRow>();
		nRows = 0;
		addNAnswerRows(DEFAULT_NROWS);
	}

	/**
	 * Relabel the rows jlabels so that they go from A,B, C .. Z in sequential
	 * ascending order
	 */
	private void relabelAnswerRows() {
		int i = 0;
		for (AnswerRow rowPanel : answerRows) {
			rowPanel.setLabelText(ROWLABELS[i++]);
		}
	}

	/**
	 * N.B We must recreate the viewport view each time so that that each new
	 * panel is drawn properly
	 */
	private void buildAnswerRows() {
		JPanel basePanel = new JPanel(new MigLayout("fill", "[]", "[]"));

		for (AnswerRow rowPanel : answerRows) {
			basePanel.add(rowPanel, "growx,wrap");

		}
		answersScrollpane.setViewportView(basePanel);
	}

	private class AnswerRow extends JPanel {
		private static final long serialVersionUID = 1L;

		private String labelText;

		private JLabel label;
		private JTextField textField;
		private JCheckBox deleteCb;

		public AnswerRow(String labelText) {
			this.labelText = labelText;

			initLayout();
		}

		public boolean isMarkedForDeletion() {
			return deleteCb.isSelected();
		}

		public String getAnswerText() {
			return  textField.getText();
		}

		public void setLabelText(String name) {
			this.labelText = name;
			revalidate();
		}

		public String getLabelText() {
			return labelText;
		}

		private void initLayout() {
			setLayout(new MigLayout("", "[][grow][100::]",
					"[40::40]"));

			textField = new JTextField(10);
			label = new JLabel(labelText);
			deleteCb = new JCheckBox("Delete");

			add(label, "align right, width 10::10");
			add(textField, "align right, growx");
			add(deleteCb);
		}
	}
}
