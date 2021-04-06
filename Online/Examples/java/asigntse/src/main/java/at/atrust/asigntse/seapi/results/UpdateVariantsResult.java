package at.atrust.asigntse.seapi.results;

public class UpdateVariantsResult extends StatusResult {
	
	public enum Type {
        signedUpdate, unsignedUpdate, signedAndUnsignedUpdate
    };
	
	private Type type;
	
	public UpdateVariantsResult(int status) {
		super(status);
	}
	
	public UpdateVariantsResult(int status, Type type) {
		super(status);
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}

}
