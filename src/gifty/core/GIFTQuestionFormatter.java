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

	private StringBuilder formattedQuestions;
	private String[] SPECIAL_CHARS = {"~", "=",  "#", "{",  "}" };

	public GIFTQuestionFormatter() {
		formattedQuestions = new StringBuilder();
	}

	public String formatTrueFalseQuestion(String questionTitle,
			String question, boolean questionIsTrue) {
		
		question = escapeSpecialChars(question);

		String formattedQuestion = String.format("%s %s %s\n",
				formatTitle(questionTitle), question, questionIsTrue ? "{T}"
						: "{F}");

		return formattedQuestion;
	}
	
	public String shortAnswerQuestion(String questionTitle, String question,
			String answer) {
		
		question = escapeSpecialChars(question);
		answer = escapeSpecialChars(answer);
		
		String formattedQuestion = String.format("%s %s {=%s}\n",
				formatTitle(questionTitle), question, answer);
		

		return formattedQuestion;
	}

	public String formatEssayQuestion(String questionTitle, String question) {

		question = escapeSpecialChars(question);
		
		String formattedQuestion = String.format("%s %s %s {}\n",
				formatTitle(questionTitle), question);

		return formattedQuestion;
	}

	public String formatMatchQuestion(String questionTitle, String question,
			ArrayList<String[]> matchPairs) {

		String formattedQuestion = String.format("%s %s {\n %s }\n\n",
				formatTitle(questionTitle), 
				question, 
				formatMatchPairs(matchPairs));

		return formattedQuestion;
	}
	
	private String formatTitle(String questionTitle) {
		if (!questionTitle.isEmpty()) {
			questionTitle = String.format("::%s::",  questionTitle);
		}

		return escapeSpecialChars( questionTitle.trim() );
	}
	
	private String formatMatchPairs(ArrayList<String[]> matchPairs) {
		StringBuilder sb = new StringBuilder();
			
		for(String[] matchPair : matchPairs) {
			//ignore mal formed questions
			if(matchPair[0].isEmpty() || matchPair[1].isEmpty())
				continue;
			
			sb.append(String.format(" =%s -> %s \n", matchPair[0], matchPair[1]));		
		}
		return escapeSpecialChars( sb.toString() );
	}
	
	private String escapeSpecialChars(String str) {
		
		for(String specialChar : SPECIAL_CHARS ) {
			str = str.replace(specialChar, '\\'+specialChar);
		}
		
		return str;
	}


}
