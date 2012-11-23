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

package gifty.core;

import java.util.ArrayList;

/**
 * 
 * 
 * @author Jonah
 * 
 */

public class GIFTQuestionFormatter {

	private String[] SPECIAL_CHARS = {"~", "=",  "#", "{",  "}" };
	private final static String NEWLINECHAR = System.getProperty("line.separator");

	public GIFTQuestionFormatter() {
	}

	public String formatTrueFalseQuestion(String questionTitle,
			String question, boolean questionIsTrue) {
		
		return formatQuestion(questionTitle, question, questionIsTrue ? "T": "F");
	}
	
	public String shortAnswerQuestion(String questionTitle, String question,
			String answer) {
		
		return formatQuestion(questionTitle, question, "="+escapeSpecialChars(answer));
		
	}

	public String formatEssayQuestion(String questionTitle, String question) {

		return formatQuestion(questionTitle, question, "");
	}

	public String formatMatchQuestion(String questionTitle, String question,
			ArrayList<String[]> matchPairs) {

		String formattedMatches = formatMatchPairs(matchPairs);

		return formatQuestion(questionTitle, question, formattedMatches);
	}

	
	public String formatMultipleChoiceQuestion(String questionTitle, String question,
			ArrayList<Answer> choices, boolean hasMultipleAnswers) {
		
		String formattedChoices = "";
		if(hasMultipleAnswers) {
			formattedChoices = formatMultipleAnswerChoices(choices); 
		}else {
			formattedChoices = formatSingleAnswerChoices(choices); 
		}

		return formatQuestion(questionTitle, question, formattedChoices);
	}
	

	public String formatNumericalQuestion(String questionTitle,
			String question, ArrayList<NumericAnswer> answers) {
		
		String formattedAnswer = "";

		formattedAnswer = formatNumericalAnswers(answers); 
		
		return formatQuestion(questionTitle, question, formattedAnswer);

	}
	
	
	////////////////////////////////////////////////////////////////////////////////
	///////////////////////Private helper methods///////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	

	private String formatNumericalAnswers(ArrayList<NumericAnswer> answers) {
		StringBuilder formattedAnswers = new StringBuilder();
		formattedAnswers.append("# ").append(NEWLINECHAR);
		
		for (NumericAnswer answer : answers) {

			// ignore mal formed answers
			if (answer.getAnswerText().isEmpty())
				continue;

			formattedAnswers.append(String.format("=%s%s:%d%s %s",
					formatPartialCredit(answer.getMark()), 
					escapeSpecialChars(answer.getAnswerText()),
					answer.getTolerance(), formatFeedback(answer.getFeedback()),
					NEWLINECHAR));
		}

		return formattedAnswers.toString();
	}
	


	private String formatSingleAnswerChoices(ArrayList<Answer> choices) {

        StringBuilder formattedChoices = new StringBuilder();
        formattedChoices.append(NEWLINECHAR);
        
        for(Answer choice : choices) {
        	
        	//ignore mal formed choices..
        	if(choice.getAnswerText().isEmpty()) 
        		continue;
        	
        	formattedChoices.append(
        			String.format("%s%s%s%s",
        			choice.isCorrect() ? "=" : "~", 
        			escapeSpecialChars(choice.getAnswerText()), 
        			formatFeedback(choice.getFeedback()),
        			NEWLINECHAR)
        			);
        }
        
        return formattedChoices.toString();	
	}
	
	
	private String formatMultipleAnswerChoices(ArrayList<Answer> choices) {

        StringBuilder formattedChoices = new StringBuilder();
        formattedChoices.append(NEWLINECHAR);
        
        for(Answer choice : choices) {
        	
        	//ignore mal formed choices..
        	if(choice.getAnswerText().isEmpty()) 
        		continue;
        	
        	formattedChoices.append(
        			String.format("~%s%s %s",
        			formatPartialCredit(choice.getMark()), 
        			escapeSpecialChars(choice.getAnswerText()), 
        			formatFeedback(choice.getFeedback())+NEWLINECHAR)
        			);
        }
        return formattedChoices.toString();	
	}



	private String formatMatchPairs(ArrayList<String[]> matchPairs) {
		StringBuilder sb = new StringBuilder();
		sb.append(NEWLINECHAR);
			
		for(String[] matchPair : matchPairs) {
			//ignore mal formed questions
			if(matchPair[0].isEmpty() || matchPair[1].isEmpty())
				continue;
			
			sb.append(String.format(" =%s -> %s", escapeSpecialChars(matchPair[0]), 
												  escapeSpecialChars(matchPair[1])+NEWLINECHAR));		
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @param title - unformatted title
	 * @param question -unformatted
	 * @param answer -formatted answer/s string with or without feedback, should not include ... {}
	 * @return
	 */
	private String formatQuestion(String title, String question, String answerFormatted) {
		
		String formattedQuestion = String.format("%s %s{%s}%s",
				formatTitle(title), 
				escapeSpecialChars(question)+NEWLINECHAR, 
				answerFormatted,
				NEWLINECHAR+NEWLINECHAR
				);	
		return formattedQuestion;
	}
	

	
	private String formatTitle(String questionTitle) {
		if (!questionTitle.isEmpty()) {
			questionTitle = escapeSpecialChars( String.format("::%s::",  escapeSpecialChars(questionTitle)) );
		}

		return questionTitle.trim();
	}
	
	private String formatFeedback(String feedback) {
		String formattedFeedback = feedback.isEmpty() ? "" : " # "+escapeSpecialChars(feedback);
		
		return formattedFeedback;
	}
	
	private String escapeSpecialChars(String str) {
		
		for(String specialChar : SPECIAL_CHARS ) {
			str = str.replace(specialChar, '\\'+specialChar);
		}
		
		return str;
	}
	
	private String formatPartialCredit(int percentage) {
		if(percentage == 100) {
			return "";
		}else {
			return  String.format("%%%d%%", percentage);
		}
	}
	



}
