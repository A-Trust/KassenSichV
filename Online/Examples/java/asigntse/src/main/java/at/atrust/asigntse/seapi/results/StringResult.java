package at.atrust.asigntse.seapi.results;

public class StringResult extends StatusResult {

	private String data;
	
	public StringResult(int status) {
		super(status);
	}
	
	public StringResult(int status, String data) {
		super(status);
		this.data = data;
	}

	public String getData() {
		return data;
	}

}
