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

public class MatchingQuestionPanel extends JPanel implements IQuestion {

	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger
			.getLogger(TrueFalseQuestionPanel.class.getName());
	
	private static final String[] ROWLABELS = { "A", "B", "C", "D", "E", "F",
		"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
		"T", "U", "V", "W", "X", "Y", "Z" };

	private static final int MAX_ROWS = ROWLABELS.length;
	public static final int DEFAULT_NROWS = 3;
	private static final int MIN_ROWS = 2;
	private int nRows;
	
	private GIFTQuestionFormatter formatter;

	// widgets
	private JTextField questionTitleTextfield;
	private JScrollPane questionsScrollpane;
	private JButton addChoiceButton;
	private JButton deleteCheckedButton;
	private ScrollableTextArea questionTextarea;

	private ArrayList<MatchingQuestionRow> questionRows;


	public MatchingQuestionPanel() {
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
		
		int validMatchesCount = 0;
		ArrayList<String[]> matchPairs = new ArrayList<String[]>();
		
		for(MatchingQuestionRow questionRow : questionRows) {
			String[] matchPair = questionRow.getMatchPair();
			
			if(!matchPair[0].isEmpty() && !matchPair[1].isEmpty()) {
				
				matchPairs.add(questionRow.getMatchPair());
				validMatchesCount++;
			}	
		}
		
		if (validMatchesCount < MIN_ROWS) {
			Dialog.showErrorDialog(this, "Not enough mathces..",
					"You need to have atleast 2 Match pairs to create a question");
			return "";
		}

		String formattedQuestion = formatter.formatMatchQuestion(questionTitle,
				question, matchPairs);
		return formattedQuestion;
	}

	@Override
	public void clearQuestion() {
		questionTitleTextfield.setText("");
		addChoiceButton.setEnabled(true);
		deleteCheckedButton.setEnabled(true);
		resetAnswerRows();
	}

	private void initLayout() {
		// build
		JLabel questionTitleLbl = new JLabel("Question Title (optional)");
		questionTitleTextfield = new JTextField(20);

		JLabel questionLbl = new JLabel("Question");
		questionTextarea = new ScrollableTextArea();

		addChoiceButton = new JButton("Add Matching...");
		deleteCheckedButton = new JButton("Deleted checked...");

		questionsScrollpane = new JScrollPane();
		questionsScrollpane
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

		add(questionsScrollpane, "cell 1 2, grow, wrap 30");

		add(addChoiceButton, "cell 1 3, split 3, align right, width 180::180");
		add(deleteCheckedButton, "align right, wrap, width 180::180");

		resetAnswerRows();

		// bind
		addChoiceButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addMatchingQuestion();
				revalidate();

				if(nRows > MIN_ROWS) 
					deleteCheckedButton.setEnabled(true);
			}
		});

		deleteCheckedButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteCheckedQuestions();
				revalidate();

				addChoiceButton.setEnabled(true);
			}
		});
	}

	private void deleteCheckedQuestions() {
		ArrayList<MatchingQuestionRow> rowsCopy = new ArrayList<MatchingQuestionRow>(questionRows); 

		for (MatchingQuestionRow rowPanel : rowsCopy) {
			
			if(nRows == MIN_ROWS) {
				deleteCheckedButton.setEnabled(false);
				break;
			}
			if (rowPanel.isMarkedForDeletion()) {
				questionRows.remove(rowPanel);
				nRows--;
			}
		}
		relabelAnswerRows();
		buildAnswerRows();
	}

	private void addNMatchingQuestions(int n) {
		for (int i = 0; i < n; i++) {
			if (nRows < MAX_ROWS) {
				questionRows.add(new MatchingQuestionRow(ROWLABELS[nRows]));
				nRows++;
			}else {
				addChoiceButton.setEnabled(false);
				break;
			}
		}
		buildAnswerRows();
	}

	private void addMatchingQuestion() {
		if (nRows < MAX_ROWS) {
			questionRows.add(new MatchingQuestionRow(ROWLABELS[nRows]));
			nRows++;
			buildAnswerRows();
		}else {
			addChoiceButton.setEnabled(false);
		}
	}

	private void resetAnswerRows() {
		questionRows = new ArrayList<MatchingQuestionRow>();
		nRows = 0;
		addNMatchingQuestions(DEFAULT_NROWS);
	}

	/**
	 * Relabel the rows jlabels so that they go from A,B, C .. Z in sequential
	 * ascending order
	 */
	private void relabelAnswerRows() {
		int i = 0;
		for (MatchingQuestionRow rowPanel : questionRows) {
			rowPanel.setLabelsText(ROWLABELS[i++]);
			System.out.println(rowPanel.getLabelText());
		}
	}

	/**
	 * N.B We must recreate the viewport view each time so that that each new
	 * panel is drawn properlyS
	 */
	private void buildAnswerRows() {
		JPanel basePanel = new JPanel(new MigLayout("fill", "[]", "[]"));

		for (MatchingQuestionRow rowPanel : questionRows) {
			basePanel.add(rowPanel, "growx,wrap");

		}
		questionsScrollpane.setViewportView(basePanel);
	}

	/*
	 * A panel a number of rows of jtextFields with labels date driven by the
	 * fieldNames array in outer class
	 */
	private class MatchingQuestionRow extends JPanel {
		private static final long serialVersionUID = 1L;

		private String labelText;

		private JLabel label1;
		private JLabel label2;
		private JTextField textField1;
		private JTextField textField2;
		private JCheckBox deleteCb;

		public MatchingQuestionRow(String labelText) {
			this.labelText = labelText;

			initLayout();
		}

		public boolean isMarkedForDeletion() {
			return deleteCb.isSelected();
		}

		public String[] getMatchPair() {
			return new String[] { textField1.getText(), textField2.getText() };
		}

		public void setLabelsText(String name) {
			this.labelText = name;
			label1.setText(name);
			label2.setText(name);
			revalidate();
		}

		public String getLabelText() {
			return labelText;
		}

		private void initLayout() {
			// setBorder(BorderFactory.createLineBorder(Color.black));
			setLayout(new MigLayout("", "[][grow][100::][grow][100::100]",
					"[40::40]"));

			textField1 = new JTextField(10);
			textField2 = new JTextField(10);
			label1 = new JLabel(labelText);
			label2 = new JLabel(labelText);

			deleteCb = new JCheckBox("Delete");

			add(label1, "align right, width 10::10");
			add(textField1, "align right, growx");

			add(label2, "align right, width 10::10");
			add(textField2, "align right, growx");

			add(deleteCb);
		}
	}

}
