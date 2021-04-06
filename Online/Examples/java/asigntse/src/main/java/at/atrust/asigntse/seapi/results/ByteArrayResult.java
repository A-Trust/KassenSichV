package at.atrust.asigntse.seapi.results;

public class ByteArrayResult extends StatusResult {

	private byte[] data;
	
	public ByteArrayResult(int status) {
		super(status);
	}
	
	public ByteArrayResult(int status, byte[] data) {
		super(status);
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

}
