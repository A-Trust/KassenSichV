package at.atrust.asigntse.seapi.results;

public class IntArrayResult extends StatusResult {

	private int[] data;
	
	public IntArrayResult(int status) {
		super(status);
	}
	
	public IntArrayResult(int status, int[] data) {
		super(status);
		this.data = data;
	}

	public int[] getData() {
		return data;
	}

}
