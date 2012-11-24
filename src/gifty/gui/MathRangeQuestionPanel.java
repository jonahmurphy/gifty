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
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

public class MathRangeQuestionPanel extends JPanel implements IQuestion{

	private static final long serialVersionUID = 1L;

	private GIFTQuestionFormatter formatter;
	
	private JTextField questionTitleTextfield;
	private ScrollableTextArea questionTextarea;
	private JFormattedTextField answerTextField;
	private JSpinner answerRangeSpinner;
	private JFormattedTextField endPointTextField;
	private JFormattedTextField startPointTextField;
	private JCheckBox rangeTypeCb;

	public MathRangeQuestionPanel() {
		formatter = new GIFTQuestionFormatter();
		initLayout();
	}
	
	@Override
	public String getFormattedQuestion() {
		String questionTitle = questionTitleTextfield.getText();
		String question = questionTextarea.getText();
		
		if(!validates()){
			return "";
		}
		
		String formattedQuestion;
		NumericAnswer answer;
		
		if (isIntervalEndPointsType()) {
			answer = new NumericAnswer(Double.parseDouble(startPointTextField.getText()),
										Double.parseDouble(endPointTextField.getText()));
			formattedQuestion = formatter.formatMathRangeQuestion(
					questionTitle, question, answer, isIntervalEndPointsType());
		} else {

			answer = new NumericAnswer(answerTextField.getText(),
									(Integer)answerRangeSpinner.getValue());
			
			formattedQuestion = formatter.formatMathRangeQuestion(
					questionTitle, question, answer, isIntervalEndPointsType());
		}
	
		return formattedQuestion;
	}

	@Override
	public void clearQuestion() {
		questionTitleTextfield.setText("");
		questionTextarea.setText("");
		startPointTextField.setText("");
		endPointTextField.setText("");
		answerRangeSpinner.setValue(0);
		answerTextField.setText("");		
		rangeTypeCb.setSelected(false);
	}
	
	private boolean isIntervalEndPointsType() {
		return rangeTypeCb.isSelected();
	}
	
	private boolean validates() {
		
		if (questionTextarea.getText().isEmpty()) {
			Dialog.showEmptyQuestionBodyWarning(this);
			return false;
		}
			
		if(isIntervalEndPointsType()) {
			if( startPointTextField.getText().isEmpty() ||
					endPointTextField.getText().isEmpty()) {
				Dialog.showErrorDialog(this, "Empty field!", "Missing interval points!");
				return false;
			}
			
			if( Double.parseDouble(startPointTextField.getText()) >=
				Double.parseDouble(endPointTextField.getText()) ) {
				
				Dialog.showErrorDialog(this, "Value error", "Start point should be less then the end point!");
				return false;
			}
			
		} else {
			if(answerTextField.getText().isEmpty()) {
				Dialog.showErrorDialog(this, "Empty field!", "A question needs an answer!");
				return false;
			}
		}

		return true;
	}
	

	private void initLayout() {
		//build
		JLabel questionTitleLbl = new JLabel("Question Title (optional)");
		questionTitleTextfield = new JTextField(20);

		JLabel questionLbl = new JLabel("Question");
		questionTextarea = new ScrollableTextArea();
		
		JLabel answerLbl = new JLabel("Answer");
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumFractionDigits(1);
		nf.setGroupingUsed(false);
		answerTextField =  new JFormattedTextField(nf);
		
		
		JLabel rangeLbl = new JLabel("Range +/-");	
		SpinnerModel answerRangeSpinnerModel = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 10);
	    answerRangeSpinner = new JSpinner(answerRangeSpinnerModel);
        JSpinner.NumberEditor editor = (JSpinner.NumberEditor)answerRangeSpinner.getEditor();  
        DecimalFormat format = editor.getFormat();  
        format.setMinimumFractionDigits(1);  
	    
	    JLabel startPointLbl = new JLabel("Start point");
	    startPointTextField =  new JFormattedTextField(nf);
	    
	    JLabel endPointLbl = new JLabel("End point");
	    endPointTextField =  new JFormattedTextField(nf);
	    
	    rangeTypeCb = new JCheckBox("Specifed interval end points ?");
	    rangeTypeCb.setHorizontalTextPosition(SwingConstants.LEFT);

	    //layout
		setLayout(new MigLayout("", "[]" +
				"[grow]40" + //checkbox
				"[][grow]40" +   //label + textfield
				"[][grow]40" +   //label + textfield
				"[][grow]40" +   //label + textfield
				"[][grow]40"     //label + textfield
				, "[][grow][]"));

		add(questionTitleLbl, "align right");
		add(questionTitleTextfield, "growx, span 9, wrap");

		add(questionLbl, "align right top");
		add(questionTextarea, "grow, span 9, wrap");
		
		add(rangeTypeCb, "cell 1 2");
		
		add(answerLbl, "align right, width 38::38");
		add(answerTextField, "align right, growx 500, width 60::");
		add(rangeLbl, "align right, width 50::50");
		add(answerRangeSpinner, "align right, growx 500, width 60::");
		
		add(startPointLbl, "align right, width 55::55");
		add(startPointTextField, "align right, growx 500, width 60::");
		add(endPointLbl, "align right, width 50::50");
		add(endPointTextField, "align right, growx 500, width 60::");
		
		updateUIRangeType();
		
		//bind
		rangeTypeCb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateUIRangeType();
				
			}
		});
	}
	
	private void updateUIRangeType() {
		startPointTextField.setEnabled(rangeTypeCb.isSelected());
		endPointTextField.setEnabled(rangeTypeCb.isSelected());
		
		answerRangeSpinner.setEnabled(!rangeTypeCb.isSelected());
		answerTextField.setEnabled(!rangeTypeCb.isSelected());
	}
}