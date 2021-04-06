package at.atrust.asigntse.seapi.results;

public class AuthenticationResult extends StatusResult {
	
	public enum AuthenticationStatus {
        ok, failed, pinIsBlocked, unknownUserId
    };

	private AuthenticationStatus authenticationStatus;
	private short remainingRetries;
	
	public AuthenticationResult(int status) {
		super(status);
	}
	
	public AuthenticationResult(int status, AuthenticationStatus authenticationStatus, short remainingRetries) {
		super(status);
		this.authenticationStatus = authenticationStatus;
		this.remainingRetries = remainingRetries;
	}

	public AuthenticationStatus getAuthenticationStatus() {
		return authenticationStatus;
	}

	public short getRemainingRetries() {
		return remainingRetries;
	}
}
