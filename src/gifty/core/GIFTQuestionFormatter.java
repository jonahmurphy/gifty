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
