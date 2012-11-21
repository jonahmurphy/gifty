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

	private GIFTQuestionFormatter formatter;

	// widgets
	private JTextField questionTitleTextfield;
	private JScrollPane questionsScrollpane;
	private JButton addChoiceButton;
	private JButton deleteCheckedButton;
	private ScrollableTextArea questionTextarea;

	private ArrayList<MatchingQuestionRow> questionRows;

	private static final String[] ROWLABELS = { "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };

	private static final int MAX_ROWS = ROWLABELS.length;
	public static final int DEFAULT_NROWS = 3;
	private int nRows;

	public MatchingQuestionPanel() {
		formatter = new GIFTQuestionFormatter();
		initLayout();
	}

	@Override
	public String getFormattedQuestion() {
		String questionTitle = questionTitleTextfield.getText();
		String question = questionTextarea.getText();

		if (question.compareTo("") == 0) {
			DialogUtils.showEmptyQuestionBodyWarning(this);
			return "";
		}
		
		ArrayList<String[]> matchPairs = new ArrayList<String[]>();
		for(MatchingQuestionRow questionRow : questionRows) {
			 matchPairs.add(questionRow.getMatchPair());
		}

		String formattedQuestion = formatter.formatMatchQuestion(questionTitle,
				question, matchPairs);
		return formattedQuestion;
	}

	@Override
	public void clearQuestion() {
		questionTitleTextfield.setText("");
		resetRows();
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

		resetRows();

		// bind
		addChoiceButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addAnotherMatchingQuestion();
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
				deleteCheckedQuestions();
				revalidate();

				if (nRows == 0) {
					deleteCheckedButton.setEnabled(false);
				} else if (nRows < MAX_ROWS) {
					addChoiceButton.setEnabled(true);
				}
			}
		});
	}

	private void deleteCheckedQuestions() {
		ArrayList<MatchingQuestionRow> rowsCopy 
			= (ArrayList<MatchingQuestionRow>) questionRows.clone();
		
		for (MatchingQuestionRow rowPanel : rowsCopy) {
			if (rowPanel.isMarkedForDeletion()) {
				questionRows.remove(rowPanel);
				nRows--;
			}
		}

		relabelRows();
		buildRows();
	}

	private void addNMatchingQuestions(int n) {
		for (int i = 0; i < n; i++) {
			if (nRows < MAX_ROWS) {
				questionRows.add(new MatchingQuestionRow(ROWLABELS[nRows]));
				nRows++;
			}
		}
		buildRows();
	}

	private void addAnotherMatchingQuestion() {
		if (nRows < MAX_ROWS) {
			questionRows.add(new MatchingQuestionRow(ROWLABELS[nRows]));
			nRows++;
			buildRows();
		}
	}

	private void resetRows() {
		questionRows = new ArrayList<MatchingQuestionRow>();
		nRows = 0;
		addNMatchingQuestions(DEFAULT_NROWS);
	}

	/**
	 * Relabel the rows jlabels so that they go from A,B, C .. Z in sequential
	 * ascending order
	 */
	private void relabelRows() {
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
	private void buildRows() {
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
