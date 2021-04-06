package at.atrust.asigntse.seapi.results;

import java.time.ZonedDateTime;

public class TransactionResult extends StatusResult {
	
	private ZonedDateTime logTime;
	private long signatureCounter;
	private byte[] signatureValue;
	
	public TransactionResult(int status) {
		super(status);
	}
	
	public TransactionResult(int status, ZonedDateTime logTime, byte[] signatureValue, long signatureCounter) {
		super(status);
		this.logTime = logTime;
		this.signatureValue = signatureValue;
		this.signatureCounter = signatureCounter;
	}

	public ZonedDateTime getLogTime() {
		return logTime;
	}

	public long getSignatureCounter() {
		return signatureCounter;
	}

	public byte[] getSignatureValue() {
		return signatureValue;
	}
	
}
