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

public class GIFTQuestionFormatter {

	private StringBuilder formattedQuestions;

	public GIFTQuestionFormatter() {
		formattedQuestions = new StringBuilder();
	}

	public boolean addTrueFalseQuestion(String questionTitle,
			String question, boolean questionIsTrue) {

		String fomattedQuestion = String.format("::%s:: %s %s\n\n",
				questionTitle, question, questionIsTrue ? "{T}" : "{F}");

		formattedQuestions.append(fomattedQuestion);

		return true;

	}

	public String getFormattedQuestions() {
		return formattedQuestions.toString();
	}

	/**
	 * test..
	 */
	public static void main(String[] args) {
		GIFTQuestionFormatter questionFormatter = new GIFTQuestionFormatter();
		questionFormatter.addTrueFalseQuestion("Q1", "Egypt is in africa",
				true);
		questionFormatter.addTrueFalseQuestion("Q2", "Ireland is in africa",
				false);

		System.out.println(questionFormatter.getFormattedQuestions());

	}

}
