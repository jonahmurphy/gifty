package gifty.core;

/**
 * Bean to hold an answer..
 * @author jonah
 *
 */
public class Answer {
	
	private String answerText;
	private String feedback;
	private boolean isCorrect;
	private int mark;
	
	public Answer(String answer, String feedback, boolean isCorrect, int mark) {
		this.answerText = answer;
		this.feedback = feedback;
		this.isCorrect = isCorrect;
		this.mark  = mark;
	}
	public Answer(String answer, String feedback, boolean isCorrect) {
		this(answer, feedback, isCorrect, 100);
	}	
	public Answer(String answer, String feedback, int mark) {
		this(answer, feedback, true, mark);		
	}
	public Answer(String answer, String feedback) {
		this(answer, feedback, true, 100);
	}
	public Answer(String answer) {
		this(answer, "", true, 100);
	}
	
	
	public String getAnswerText() {
		return answerText;
	}
	
	public String getFeedback() {
		return feedback;
	}
	
	public int getMark() {
		return mark;
	}
	
	public boolean isCorrect() {
		return isCorrect;
	}
	
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
	
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	
	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
	
	public void setMark(int mark) {
		this.mark = mark;
	}
}
