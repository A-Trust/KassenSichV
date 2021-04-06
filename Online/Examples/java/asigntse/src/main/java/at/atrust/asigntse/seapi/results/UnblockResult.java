package at.atrust.asigntse.seapi.results;

public class UnblockResult extends StatusResult {
	
    public enum UnblockStatus {
    	ok, failed, unknownUserId, error
    };

	private UnblockStatus unblockStatus;
	
	public UnblockResult(int status) {
		super(status);
	}
	
	public UnblockResult(int status, UnblockStatus unblockStatus) {
		super(status);
		this.unblockStatus = unblockStatus;
	}
	
	public UnblockStatus getUnblockStatus() {
		return unblockStatus;
	}

}
