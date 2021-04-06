package at.atrust.asigntse.seapi.results;

import java.time.ZonedDateTime;

public class StartTransactionResult extends StatusResult {
	
	private long transactionNumber;
	private ZonedDateTime logTime;
	private byte[] serialNumber;
	private long signatureCounter;
	private byte[] signatureValue;
	
	public StartTransactionResult(int status) {
		super(status);
	}
	
	public StartTransactionResult(int status, long transactionNumber, ZonedDateTime logTime, byte[] serialNumber, long signatureCounter, byte[] signatureValue) {
		super(status);
		this.transactionNumber = transactionNumber;
		this.logTime = logTime;
		this.serialNumber = serialNumber;
		this.signatureCounter = signatureCounter;
		this.signatureValue = signatureValue;
	}

	public long getTransactionNumber() {
		return transactionNumber;
	}

	public ZonedDateTime getLogTime() {
		return logTime;
	}

	public byte[] getSerialNumber() {
		return serialNumber;
	}

	public long getSignatureCounter() {
		return signatureCounter;
	}

	public byte[] getSignatureValue() {
		return signatureValue;
	}
	
}
