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
import java.util.Scanner;

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

	/**
	 * 
	 * @param questionTitle
	 * @param question
	 * @param questionIsTrue
	 * @return
	 */
	public String formatTrueFalseQuestion(String questionTitle,
			String question, boolean questionIsTrue) {
		
		return formatQuestion(questionTitle, question, questionIsTrue ? "T": "F");
	}
	
	/**
	 * 
	 * @param questionTitle
	 * @param question
	 * @param answers
	 * @return
	 */
	public String formatShortAnswerQuestion(String questionTitle,
			String question, ArrayList<String> answers) {

		return formatQuestion(questionTitle, question, formatShortAnswers(answers));
	}

	/**
	 * 
	 * @param questionTitle
	 * @param question
	 * @return
	 */
	public String formatEssayQuestion(String questionTitle, String question) {

		return formatQuestion(questionTitle, question, "");
	}

	/**
	 * 
	 * @param questionTitle
	 * @param question
	 * @param matchPairs
	 * @return
	 */
	public String formatMatchQuestion(String questionTitle, String question,
			ArrayList<String[]> matchPairs) {

		String formattedMatches = formatMatchPairs(matchPairs);

		return formatQuestion(questionTitle, question, formattedMatches);
	}

	/**
	 * 
	 * @param questionTitle
	 * @param question
	 * @param choices
	 * @param hasMultipleAnswers
	 * @return
	 */
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
	
	/**
	 * 
	 * @param questionTitle
	 * @param question
	 * @param answers
	 * @return
	 */
	public String formatNumericalQuestion(String questionTitle,
			String question, ArrayList<NumericAnswer> answers) {
		
		String formattedAnswer = "";

		formattedAnswer = formatNumericalAnswers(answers); 
		
		return formatQuestion(questionTitle, question, formattedAnswer);

	}
	
	/**
	 * 
	 * @param questionTitle
	 * @param question
	 * @param answer
	 * @param isIntervalEndPointsType
	 * @return
	 */
	public String formatMathRangeQuestion(String questionTitle,
			String question, NumericAnswer answer,
			boolean isIntervalEndPointsType) {

		String formattedAnswer = null;
		if (isIntervalEndPointsType) {
			formattedAnswer = String.format("#%f..%f", answer.getRangeMin(),
					answer.getRangeMax());
		} else {
			formattedAnswer = String.format("#%s:%f", answer.getAnswerText(),
					answer.getTolerance());
		}

		return formatQuestion(questionTitle, question, formattedAnswer);
	}

	
	
	////////////////////////////////////////////////////////////////////////////////
	///////////////////////Private Methods///////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Format answers to a short answer style question
	 * @param answers
	 * @return
	 */
	public String formatShortAnswers(ArrayList<String> answers) {
		
		StringBuilder formattedAnswers = new StringBuilder();
		formattedAnswers.append(NEWLINECHAR);
		
		for (String answer : answers) {
			// ignore mal formed answers
			if (answer.isEmpty())
				continue;

			formattedAnswers.append(String.format("=%s%s", answer, NEWLINECHAR));
		}

		return formattedAnswers.toString();
		
	}
	
	/**
	 * format answers to numerical type questions
	 * @param answers
	 * @return
	 */
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
					(int)answer.getTolerance(), formatFeedback(answer.getFeedback()),
					NEWLINECHAR));
		}

		return formattedAnswers.toString();
	}
	

    /**
     * format multiple choice answers with a single correct answer
     * @param choices
     * @return
     */
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
	
	/**
	 * format multiple choice answers with multiple correct answers
	 * @param choices
	 * @return
	 */
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


	/**
	 * format answers for "Matching" type question.
	 * @param matchPairs
	 * @return
	 */
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
	

	/**
	 * 
	 * @param questionTitle
	 * @return
	 */
	private String formatTitle(String questionTitle) {
		if (!questionTitle.isEmpty()) {
			questionTitle = escapeSpecialChars( String.format("::%s::",  escapeSpecialChars(questionTitle)) );
		}

		return questionTitle.trim();
	}
	
	/**
	 * 
	 * @param feedback
	 * @return
	 */
	private String formatFeedback(String feedback) {
		String formattedFeedback = feedback.isEmpty() ? "" : " # "+escapeSpecialChars(feedback);
		
		return formattedFeedback;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	private String escapeSpecialChars(String str) {
		
		for(String specialChar : SPECIAL_CHARS ) {
			str = str.replace(specialChar, '\\'+specialChar);
		}
		
		return str;
	}
	
	/**
	 * format partial feedback 
	 * e.g output for input of 90 would be %90%
	 * @param percentage
	 * @return
	 */
	private String formatPartialCredit(int percentage) {
		if(percentage == 100) {
			return "";
		}else {
			return  String.format("%%%d%%", percentage);
		}
	}



}
