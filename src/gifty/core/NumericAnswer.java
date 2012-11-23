package gifty.core;

public class NumericAnswer extends Answer{

	private int tolerance;

	public NumericAnswer(String answer, String feedback, int tolerance, int mark) {
		super(answer, feedback, mark);
		this.tolerance = tolerance;
	}
	
	public NumericAnswer(String answer, int tolerance, int mark) {
		//no feedback..
		super(answer, "", mark);
		this.tolerance = tolerance;
		
	}
	
	public int getTolerance() {
		return tolerance;
	}
}
