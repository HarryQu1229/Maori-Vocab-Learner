package application;

/**
 * This class stores the words and results needed to show in the table of Reward Scene
 * @author harry
 */
public class TableValues {
	private String word;
	private String userAnswer;
	private String Result;
	
	public TableValues(String word, String userAnswer, String Result) {
		this.word = word;
		this.userAnswer = userAnswer;
		this.Result = Result;
	}
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public String getUserAnswer() {
		return userAnswer;
	}
	
	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}
	
	public String getResult() {
		return Result;
	}
	
	public void setResult(String result) {
		Result = result;
	}
	
}
