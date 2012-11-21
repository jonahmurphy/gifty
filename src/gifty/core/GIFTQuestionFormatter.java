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

	public String formatTrueFalseQuestion(String questionTitle,
			String question, boolean questionIsTrue) {

		String formattedQuestion = String.format("::%s:: %s %s\n\n",
				questionTitle, question, questionIsTrue ? "{T}" : "{F}");


		return formattedQuestion;
	}

	public String formatEssayQuestion(String questionTitle, String question) {
		String formattedQuestion = String.format("::%s:: %s %s\n\n",
				questionTitle, question, "{}");

		return formattedQuestion;
	}
}
