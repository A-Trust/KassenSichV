package at.atrust.asigntse.seapi;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.time.ZonedDateTime;

import com.sun.jna.ptr.PointerByReference;

import at.atrust.asigntse.seapi.results.AuthenticationResult;
import at.atrust.asigntse.seapi.results.ByteArrayResult;
import at.atrust.asigntse.seapi.results.IntArrayResult;
import at.atrust.asigntse.seapi.results.StartTransactionResult;
import at.atrust.asigntse.seapi.results.StringResult;
import at.atrust.asigntse.seapi.results.TransactionResult;
import at.atrust.asigntse.seapi.results.UnblockResult;
import at.atrust.asigntse.seapi.results.UpdateVariantsResult;

public class SEAPIImpl implements SEAPI  {

	private NativeLibraryWrapper wrappedLibrary = NativeLibraryWrapper.INSTANCE;
	
	@Override
	public ByteArrayResult exportData(int maximumNumberRecords) {
		NativeInt32 nl = new NativeInt32(maximumNumberRecords);
		PointerByReference ed = new PointerByReference();
		NativeInt32ByReference edLength = new NativeInt32ByReference();
		int status = wrappedLibrary.exportData(nl, ed, edLength);
		return getByteArrayResult(ed, edLength, status);
	}
	
	@Override
	public ByteArrayResult exportDataWithTse(int maximumNumberRecords, String tseId) {
		NativeInt32 nl = new NativeInt32(maximumNumberRecords);
		PointerByReference ed = new PointerByReference();
		NativeInt32ByReference edLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int status = wrappedLibrary.exportDataWithTse(nl, ed, edLength, tse, tseLength);
		return getByteArrayResult(ed, edLength, status);
	}

	@Override
	public ByteArrayResult exportDataFilteredByTransactionNumber(long transactionNumber) {
		NativeInt32 nl = new NativeInt32((int) transactionNumber);
		PointerByReference ed = new PointerByReference();
		NativeInt32ByReference edLength = new NativeInt32ByReference();
		int status = wrappedLibrary.exportDataFilteredByTransactionNumber(nl, ed, edLength);
		return getByteArrayResult(ed, edLength, status);
	}
	
	@Override
	public ByteArrayResult exportDataFilteredByTransactionNumberWithTse(long transactionNumber, String tseId) {
		NativeInt32 nl = new NativeInt32((int) transactionNumber);
		PointerByReference ed = new PointerByReference();
		NativeInt32ByReference edLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int status = wrappedLibrary.exportDataFilteredByTransactionNumberWithTse(nl, ed, edLength, tse, tseLength);
		return getByteArrayResult(ed, edLength, status);
	}
	
	@Override
	public ByteArrayResult exportDataFilteredByTransactionNumberAndClientId(int transactionNumber, String clientId) {
		// in
		NativeInt32 tn = new NativeInt32(transactionNumber);
		ByteBuffer cid = NativeUtil.getAsciiByteBuffer(clientId);
		NativeInt32 cidLength = NativeUtil.getBufferLen(cid);
		// out
		PointerByReference ed = new PointerByReference();
		NativeInt32ByReference edLength = new NativeInt32ByReference();
		
		int status = wrappedLibrary.exportDataFilteredByTransactionNumberAndClientId(tn, cid, cidLength, ed, edLength);
		return getByteArrayResult(ed, edLength, status);
	}
	
	@Override
	public ByteArrayResult exportDataFilteredByTransactionNumberAndClientIdWithTse(int transactionNumber, String clientId, String tseId) {
		NativeInt32 tn = new NativeInt32(transactionNumber);
		ByteBuffer cid = NativeUtil.getAsciiByteBuffer(clientId);
		NativeInt32 cidLength = NativeUtil.getBufferLen(cid);
		PointerByReference ed = new PointerByReference();
		NativeInt32ByReference edLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);		
		int status = wrappedLibrary.exportDataFilteredByTransactionNumberAndClientIdWithTse(tn, cid, cidLength, ed, edLength, tse, tseLength);
		return getByteArrayResult(ed, edLength, status);
	}

	@Override
	public ByteArrayResult exportDataFilteredByTransactionNumberInterval(int startTransactionNumber, int endTransactionNumber, int maximumNumberRecords) {
		NativeInt32 stn = new NativeInt32(startTransactionNumber);
		NativeInt32 etn = new NativeInt32(endTransactionNumber);
		NativeInt32 mnr = new NativeInt32(maximumNumberRecords);
		PointerByReference ed = new PointerByReference();
		NativeInt32ByReference edLength = new NativeInt32ByReference();
		
		int status = wrappedLibrary.exportDataFilteredByTransactionNumberInterval(stn, etn, mnr, ed, edLength);
		return getByteArrayResult(ed, edLength, status);
	}
	
	@Override
	public ByteArrayResult exportDataFilteredByTransactionNumberIntervalWithTse(int startTransactionNumber, int endTransactionNumber, int maximumNumberRecords, String tseId) {
		NativeInt32 stn = new NativeInt32(startTransactionNumber);
		NativeInt32 etn = new NativeInt32(endTransactionNumber);
		NativeInt32 mnr = new NativeInt32(maximumNumberRecords);
		PointerByReference ed = new PointerByReference();
		NativeInt32ByReference edLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);		
		int status = wrappedLibrary.exportDataFilteredByTransactionNumberIntervalWithTse(stn, etn, mnr, ed, edLength, tse, tseLength);
		return getByteArrayResult(ed, edLength, status);
	}

	@Override
	public ByteArrayResult exportDataFilteredByTransactionNumberIntervalAndClientId(int startTransactionNumber, int endTransactionNumber, String clientId, int maximumNumberRecords)  {
		NativeInt32 stn = new NativeInt32(startTransactionNumber);
		NativeInt32 etn = new NativeInt32(endTransactionNumber);
		ByteBuffer cid = NativeUtil.getAsciiByteBuffer(clientId);
		NativeInt32 cidLength = NativeUtil.getBufferLen(cid);
		NativeInt32 mnr = new NativeInt32(maximumNumberRecords);
		PointerByReference ed = new PointerByReference();
		NativeInt32ByReference edLength = new NativeInt32ByReference();
		int status = wrappedLibrary.exportDataFilteredByTransactionNumberIntervalAndClientId(stn, etn, cid, cidLength, mnr, ed, edLength);
		return getByteArrayResult(ed, edLength, status);
	}
	
	@Override
	public ByteArrayResult exportDataFilteredByTransactionNumberIntervalAndClientIdWithTse(int startTransactionNumber, int endTransactionNumber, String clientId, int maximumNumberRecords, String tseId)  {
		NativeInt32 stn = new NativeInt32(startTransactionNumber);
		NativeInt32 etn = new NativeInt32(endTransactionNumber);
		ByteBuffer cid = NativeUtil.getAsciiByteBuffer(clientId);
		NativeInt32 cidLength = NativeUtil.getBufferLen(cid);
		NativeInt32 mnr = new NativeInt32(maximumNumberRecords);
		PointerByReference ed = new PointerByReference();
		NativeInt32ByReference edLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);	
		int status = wrappedLibrary.exportDataFilteredByTransactionNumberIntervalAndClientIdWithTse(stn, etn, cid, cidLength, mnr, ed, edLength, tse, tseLength);
		return getByteArrayResult(ed, edLength, status);
	}

	@Override
	public ByteArrayResult exportDataFilteredByPeriodOfTime(ZonedDateTime startDate, ZonedDateTime endDate, int maximumNumberRecords) {
		// in
		long startD =  NativeUtil.getTimeStruct(startDate);
		long endD =  NativeUtil.getTimeStruct(endDate);
		NativeInt32 mnr = new NativeInt32(maximumNumberRecords);
		PointerByReference ed = new PointerByReference();
		NativeInt32ByReference edLength = new NativeInt32ByReference();
		int status = wrappedLibrary.exportDataFilteredByPeriodOfTime(startD, endD, mnr, ed, edLength);
		return getByteArrayResult(ed, edLength, status);
	}
	
	@Override
	public ByteArrayResult exportDataFilteredByPeriodOfTimeWithTse(ZonedDateTime startDate, ZonedDateTime endDate, int maximumNumberRecords, String tseId) {
		long startD =  NativeUtil.getTimeStruct(startDate);
		long endD =  NativeUtil.getTimeStruct(endDate);
		NativeInt32 mnr = new NativeInt32(maximumNumberRecords);
		PointerByReference ed = new PointerByReference();
		NativeInt32ByReference edLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int status = wrappedLibrary.exportDataFilteredByPeriodOfTimeWithTse(startD, endD, mnr, ed, edLength, tse, tseLength);
		return getByteArrayResult(ed, edLength, status);
	}

	@Override
	public ByteArrayResult exportDataFilteredByPeriodOfTimeAndClientId(ZonedDateTime startDate, ZonedDateTime endDate, String clientId, int maximumNumberRecords) {
		// in
		long startD =  NativeUtil.getTimeStruct(startDate);
		long endD =  NativeUtil.getTimeStruct(endDate);
		ByteBuffer cid = NativeUtil.getAsciiByteBuffer(clientId);
		NativeInt32 cidLength = NativeUtil.getBufferLen(cid);
		NativeInt32 mnr = new NativeInt32(maximumNumberRecords);
		// out
		PointerByReference ed = new PointerByReference();
		NativeInt32ByReference edLength = new NativeInt32ByReference();

		int status = wrappedLibrary.exportDataFilteredByPeriodOfTimeAndClientId(startD, endD, cid, cidLength, mnr, ed, edLength);
		return getByteArrayResult(ed, edLength, status);
	}
	
	@Override
	public ByteArrayResult exportDataFilteredByPeriodOfTimeAndClientIdWithTse(ZonedDateTime startDate, ZonedDateTime endDate, String clientId, int maximumNumberRecords, String tseId) {
		long startD =  NativeUtil.getTimeStruct(startDate);
		long endD =  NativeUtil.getTimeStruct(endDate);
		ByteBuffer cid = NativeUtil.getAsciiByteBuffer(clientId);
		NativeInt32 cidLength = NativeUtil.getBufferLen(cid);
		NativeInt32 mnr = new NativeInt32(maximumNumberRecords);
		PointerByReference ed = new PointerByReference();
		NativeInt32ByReference edLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int status = wrappedLibrary.exportDataFilteredByPeriodOfTimeAndClientIdWithTse(startD, endD, cid, cidLength, mnr, ed, edLength, tse, tseLength);
		return getByteArrayResult(ed, edLength, status);
	}
	
	private ByteArrayResult getByteArrayResult(PointerByReference ed, NativeInt32ByReference edLength, int status) {
		ByteArrayResult toReturn;
		if(statusRequiresMemoryCleanup(status)) {
			byte[] ba = ed.getValue().getByteArray(0, edLength.getValue());
			toReturn = new ByteArrayResult(status, ba);
			wrappedLibrary.at_free(ed);
		} else {
			toReturn = new ByteArrayResult(status);
		}
		return toReturn;
	}
	
	private StringResult getByteArrayAsHexStringResult(PointerByReference ed, NativeInt32ByReference edLength, int status) {
		StringResult toReturn;
		if(statusRequiresMemoryCleanup(status)) {
			byte[] ba = ed.getValue().getByteArray(0, edLength.getValue());
			String hex = String.format("%064x", new BigInteger(1, ba));
			toReturn = new StringResult(status, hex);
			wrappedLibrary.at_free(ed);
		} else {
			toReturn = new StringResult(status);
		}
		return toReturn;
	}
	
	@Override
	public StartTransactionResult startTransaction(String clientId) {
		return startTransaction(clientId, null, null, null);
	}
	
	@Override
	public StartTransactionResult startTransactionWithTse(String clientId, String tse) {
		return startTransactionWithTse(clientId, null, null, null, tse);
	}

	@Override
	public StartTransactionResult startTransaction(String clientId, byte[] processData, String processType, byte[] additionalData) {
		ByteBuffer cid = NativeUtil.getAsciiByteBuffer(clientId);
		NativeInt32 cidLength = NativeUtil.getBufferLen(cid);
		ByteBuffer pd = NativeUtil.getByteArrayByteBuffer(processData);
		NativeInt32 pdLength = NativeUtil.getBufferLen(pd);
		ByteBuffer pt = NativeUtil.getAsciiByteBuffer(processType);
		NativeInt32 ptLength = NativeUtil.getBufferLen(pt);
		ByteBuffer ad = NativeUtil.getByteArrayByteBuffer(additionalData);
		NativeInt32 adLength =  NativeUtil.getBufferLen(ad);
		NativeInt32ByReference tn = new NativeInt32ByReference();
		NativeInt64ByReference t = new NativeInt64ByReference();
		PointerByReference ser = new PointerByReference();
		NativeInt32ByReference serLength = new NativeInt32ByReference();
		NativeInt32ByReference sc = new NativeInt32ByReference();
		PointerByReference sv = new PointerByReference();
		NativeInt32ByReference svLength = new NativeInt32ByReference();
		int status = wrappedLibrary.startTransaction(cid, cidLength, pd, pdLength, pt, ptLength, ad, adLength, tn, t, ser, serLength, sc, sv, svLength);
		StartTransactionResult toReturn;
		if(statusRequiresMemoryCleanup(status) || status == TseStatusCode.ERROR_CERTIFICATE_EXPIRED) {
			toReturn = new  StartTransactionResult(status, tn.getValue(), NativeUtil.getTimeFromStruct(t), ser.getValue().getByteArray(0, serLength.getValue()), sc.getValue(), sv.getValue().getByteArray(0, svLength.getValue()));
			wrappedLibrary.at_free(ser);
			wrappedLibrary.at_free(sv);
		} else {
			toReturn = new StartTransactionResult(status);
		}
		return toReturn;
	}
	
	@Override
	public StartTransactionResult startTransactionWithTse(String clientId, byte[] processData, String processType, byte[] additionalData, String tseId) {
		ByteBuffer cid = NativeUtil.getAsciiByteBuffer(clientId);
		NativeInt32 cidLength = NativeUtil.getBufferLen(cid);
		ByteBuffer pd = NativeUtil.getByteArrayByteBuffer(processData);
		NativeInt32 pdLength =  NativeUtil.getBufferLen(pd);
		ByteBuffer pt = NativeUtil.getAsciiByteBuffer(processType);
		NativeInt32 ptLength = NativeUtil.getBufferLen(pt);
		ByteBuffer ad = NativeUtil.getByteArrayByteBuffer(additionalData);
		NativeInt32 adLength =  NativeUtil.getBufferLen(ad);
		NativeInt32ByReference tn = new NativeInt32ByReference();
		NativeInt64ByReference t = new NativeInt64ByReference();
		PointerByReference ser = new PointerByReference();
		NativeInt32ByReference serLength = new NativeInt32ByReference();
		NativeInt32ByReference sc = new NativeInt32ByReference();
		PointerByReference sv = new PointerByReference();
		NativeInt32ByReference svLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);		
		int status = wrappedLibrary.startTransactionWithTse(cid, cidLength, pd, pdLength, pt, ptLength, ad, adLength, tn, t, ser, serLength, sc, sv, svLength, tse, tseLength);
		StartTransactionResult toReturn;
		if(statusRequiresMemoryCleanup(status) || status == TseStatusCode.ERROR_CERTIFICATE_EXPIRED) {
			toReturn = new  StartTransactionResult(status, tn.getValue(), NativeUtil.getTimeFromStruct(t), ser.getValue().getByteArray(0, serLength.getValue()), sc.getValue(), sv.getValue().getByteArray(0, svLength.getValue()));
			wrappedLibrary.at_free(ser);
			wrappedLibrary.at_free(sv);
		} else {
			toReturn = new StartTransactionResult(status);
		}
		return toReturn;
	}

	@Override
	public TransactionResult updateTransaction(String clientId, long transactionNumber, byte[] processData, String processType) {
		ByteBuffer cid = NativeUtil.getAsciiByteBuffer(clientId);
		NativeInt32 cidLength = NativeUtil.getBufferLen(cid);
		NativeInt32 tn = new NativeInt32((int)transactionNumber);
		ByteBuffer pd = NativeUtil.getByteArrayByteBuffer(processData);
		NativeInt32 pdLength =  NativeUtil.getBufferLen(pd);
		ByteBuffer pt = NativeUtil.getAsciiByteBuffer(processType);
		NativeInt32 ptLength = NativeUtil.getBufferLen(pt);
		NativeInt64ByReference t = new NativeInt64ByReference();
		NativeInt32ByReference sc = new NativeInt32ByReference();
		PointerByReference sv = new PointerByReference();
		NativeInt32ByReference svLength = new NativeInt32ByReference();
		int status = wrappedLibrary.updateTransaction(cid, cidLength, tn, pd, pdLength, pt, ptLength, t, sv, svLength, sc);
		TransactionResult toReturn;
		if(statusRequiresMemoryCleanup(status) || status == TseStatusCode.ERROR_CERTIFICATE_EXPIRED) {
			toReturn = new  TransactionResult(status,  NativeUtil.getTimeFromStruct(t), sv.getValue().getByteArray(0, svLength.getValue()), sc.getValue());
			wrappedLibrary.at_free(sv);
		} else {
			toReturn = new TransactionResult(status);
		}
		return toReturn;
	}
	
	@Override
	public TransactionResult updateTransactionWithTse(String clientId, long transactionNumber, byte[] processData, String processType, String tseId) {
		ByteBuffer cid = NativeUtil.getAsciiByteBuffer(clientId);
		NativeInt32 cidLength = NativeUtil.getBufferLen(cid);
		NativeInt32 tn = new NativeInt32((int)transactionNumber);
		ByteBuffer pd = NativeUtil.getByteArrayByteBuffer(processData);
		NativeInt32 pdLength =  NativeUtil.getBufferLen(pd);
		ByteBuffer pt = NativeUtil.getAsciiByteBuffer(processType);
		NativeInt32 ptLength = NativeUtil.getBufferLen(pt);
		NativeInt64ByReference t = new NativeInt64ByReference();
		NativeInt32ByReference sc = new NativeInt32ByReference();
		PointerByReference sv = new PointerByReference();
		NativeInt32ByReference svLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int status = wrappedLibrary.updateTransactionWithTse(cid, cidLength, tn, pd, pdLength, pt, ptLength, t, sv, svLength, sc, tse, tseLength);
		TransactionResult toReturn;
		if(statusRequiresMemoryCleanup(status) || status == TseStatusCode.ERROR_CERTIFICATE_EXPIRED) {
			toReturn = new  TransactionResult(status,  NativeUtil.getTimeFromStruct(t), sv.getValue().getByteArray(0, svLength.getValue()), sc.getValue());
			wrappedLibrary.at_free(sv);
		} else {
			toReturn = new TransactionResult(status);
		}
		return toReturn;
	}
	
	@Override
	public TransactionResult finishTransaction(String clientId, long transactionNumber, byte[] processData, String processType) {
		return finishTransaction(clientId, transactionNumber, processData, processType, null);
	}
	
	@Override
	public TransactionResult finishTransactionWithTse(String clientId, long transactionNumber, byte[] processData, String processType, String tse) {
		return finishTransactionWithTse(clientId, transactionNumber, processData, processType, null, tse);
	}

	@Override
	public TransactionResult finishTransaction(String clientId, long transactionNumber, byte[] processData, String processType, byte[] additionalData) {
		ByteBuffer cid = NativeUtil.getAsciiByteBuffer(clientId);
		NativeInt32 cidLength = NativeUtil.getBufferLen(cid);
		NativeInt32 tn = new NativeInt32((int)transactionNumber);
		ByteBuffer pd = NativeUtil.getByteArrayByteBuffer(processData);
		NativeInt32 pdLength = NativeUtil.getBufferLen(pd);
		ByteBuffer pt = NativeUtil.getAsciiByteBuffer(processType);
		NativeInt32 ptLength = NativeUtil.getBufferLen(pt);
		ByteBuffer ad = NativeUtil.getByteArrayByteBuffer(additionalData);
		NativeInt32 adLength = NativeUtil.getBufferLen(ad);
		NativeInt64ByReference t = new NativeInt64ByReference();
		NativeInt32ByReference sc = new NativeInt32ByReference();
		PointerByReference sv = new PointerByReference();
		NativeInt32ByReference svLength = new NativeInt32ByReference();
		int status = wrappedLibrary.finishTransaction(cid, cidLength, tn, pd, pdLength, pt, ptLength, ad, adLength, t, sv, svLength, sc);
		TransactionResult toReturn;
		if(statusRequiresMemoryCleanup(status) || status == TseStatusCode.ERROR_CERTIFICATE_EXPIRED) {
			toReturn = new  TransactionResult(status,  NativeUtil.getTimeFromStruct(t), sv.getValue().getByteArray(0, svLength.getValue()), sc.getValue());
			wrappedLibrary.at_free(sv);
		} else {
			toReturn = new TransactionResult(status);
		}
		return toReturn;
	}
	
	@Override
	public TransactionResult finishTransactionWithTse(String clientId, long transactionNumber, byte[] processData, String processType, byte[] additionalData, String tseId) {
		ByteBuffer cid = NativeUtil.getAsciiByteBuffer(clientId);
		NativeInt32 cidLength = NativeUtil.getBufferLen(cid);
		NativeInt32 tn = new NativeInt32((int)transactionNumber);
		ByteBuffer pd = NativeUtil.getByteArrayByteBuffer(processData);
		NativeInt32 pdLength = NativeUtil.getBufferLen(pd);
		ByteBuffer pt = NativeUtil.getAsciiByteBuffer(processType);
		NativeInt32 ptLength = NativeUtil.getBufferLen(pt);
		ByteBuffer ad = NativeUtil.getByteArrayByteBuffer(additionalData);
		NativeInt32 adLength = NativeUtil.getBufferLen(ad);
		NativeInt64ByReference t = new NativeInt64ByReference();
		NativeInt32ByReference sc = new NativeInt32ByReference();
		PointerByReference sv = new PointerByReference();
		NativeInt32ByReference svLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int status = wrappedLibrary.finishTransactionWithTse(cid, cidLength, tn, pd, pdLength, pt, ptLength, ad, adLength, t, sv, svLength, sc, tse, tseLength);
		TransactionResult toReturn;
		if(statusRequiresMemoryCleanup(status) || status == TseStatusCode.ERROR_CERTIFICATE_EXPIRED) {
			toReturn = new  TransactionResult(status,  NativeUtil.getTimeFromStruct(t), sv.getValue().getByteArray(0, svLength.getValue()), sc.getValue());
			wrappedLibrary.at_free(sv);
		} else {
			toReturn = new TransactionResult(status);
		}
		return toReturn;
	}
	
	@Override
	public ByteArrayResult exportCertificates() {
		PointerByReference c = new PointerByReference();
		NativeInt32ByReference cLength = new NativeInt32ByReference();
		int status = wrappedLibrary.exportCertificates(c, cLength);
		ByteArrayResult bar;
		if(statusRequiresMemoryCleanup(status)) {
			byte[] ba = c.getValue().getByteArray(0, cLength.getValue());
			bar = new ByteArrayResult(status, ba);
			wrappedLibrary.at_free(c);
		} else {
			bar = new ByteArrayResult(status);
		}
		return bar;
	}
	
	@Override
	public ByteArrayResult exportCertificatesWithTse(String tseId) {
		PointerByReference c = new PointerByReference();
		NativeInt32ByReference cLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int status = wrappedLibrary.exportCertificatesWithTse(c, cLength, tse, tseLength);
		ByteArrayResult bar;
		if(statusRequiresMemoryCleanup(status)) {
			byte[] ba = c.getValue().getByteArray(0, cLength.getValue());
			bar = new ByteArrayResult(status, ba);
			wrappedLibrary.at_free(c);
		} else {
			bar = new ByteArrayResult(status);
		}
		return bar;
	}

	@Override
	public ByteArrayResult readLogMessage() {
		PointerByReference c = new PointerByReference();
		NativeInt32ByReference cLength = new NativeInt32ByReference();
		int status = wrappedLibrary.readLogMessage(c, cLength);
		ByteArrayResult bar;
		if(statusRequiresMemoryCleanup(status)) {
			byte[] ba = c.getValue().getByteArray(0, cLength.getValue());
			bar = new ByteArrayResult(status, ba);
			wrappedLibrary.at_free(c);
		} else {
			bar = new ByteArrayResult(status);
		}
		return bar;
	}
	
	@Override
	public ByteArrayResult readLogMessageWithTse(String tseId) {
		PointerByReference c = new PointerByReference();
		NativeInt32ByReference cLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int status = wrappedLibrary.readLogMessageWithTse(c, cLength, tse, tseLength);
		ByteArrayResult bar;
		if(statusRequiresMemoryCleanup(status)) {
			byte[] ba = c.getValue().getByteArray(0, cLength.getValue());
			bar = new ByteArrayResult(status, ba);
			wrappedLibrary.at_free(c);
		} else {
			bar = new ByteArrayResult(status);
		}
		return bar;
	}

	@Override
	public ByteArrayResult exportSerialNumbers() {
		PointerByReference c = new PointerByReference();
		NativeInt32ByReference cLength = new NativeInt32ByReference();
		int status = wrappedLibrary.exportSerialNumbers(c, cLength);
		ByteArrayResult bar;
		if(statusRequiresMemoryCleanup(status)) {
			byte[] ba = c.getValue().getByteArray(0, cLength.getValue());
			bar = new ByteArrayResult(status, ba);
			wrappedLibrary.at_free(c);
		} else {
			bar = new ByteArrayResult(status);
		}
		return bar;
	}
	
	@Override
	public ByteArrayResult exportSerialNumbersWithTse(String tseId) {
		PointerByReference c = new PointerByReference();
		NativeInt32ByReference cLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int status = wrappedLibrary.exportSerialNumbersWithTse(c, cLength, tse, tseLength);
		ByteArrayResult bar;
		if(statusRequiresMemoryCleanup(status)) {
			byte[] ba = c.getValue().getByteArray(0, cLength.getValue());
			bar = new ByteArrayResult(status, ba);
			wrappedLibrary.at_free(c);
		} else {
			bar = new ByteArrayResult(status);
		}
		return bar;
	}
	
	@Override
	public ByteArrayResult exportPublicKey(String key) {
		ByteBuffer keyId = NativeUtil.getAsciiByteBuffer(key);
		NativeInt32 keyIdLength = NativeUtil.getBufferLen(keyId);
		PointerByReference exportedPubKey = new PointerByReference();
		NativeInt32ByReference exportedPubKeyLength = new NativeInt32ByReference();
		int status = wrappedLibrary.exportPublicKey(keyId, keyIdLength, exportedPubKey, exportedPubKeyLength);
		ByteArrayResult bar;
		if(statusRequiresMemoryCleanup(status)) {
			byte[] ba = exportedPubKey.getValue().getByteArray(0, exportedPubKeyLength.getValue());
			bar = new ByteArrayResult(status, ba);
			wrappedLibrary.at_free(exportedPubKey);
		} else {
			bar = new ByteArrayResult(status);
		}
		return bar;
	}
	
	@Override
	public ByteArrayResult exportPublicKey(String key, String tseId) {
		ByteBuffer keyId = NativeUtil.getAsciiByteBuffer(key);
		NativeInt32 keyIdLength = NativeUtil.getBufferLen(keyId);
		PointerByReference exportedPubKey = new PointerByReference();
		NativeInt32ByReference exportedPubKeyLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int status = wrappedLibrary.exportPublicKeyWithTse(keyId, keyIdLength, exportedPubKey, exportedPubKeyLength, tse, tseLength);
		ByteArrayResult bar;
		if(statusRequiresMemoryCleanup(status)) {
			byte[] ba = exportedPubKey.getValue().getByteArray(0, exportedPubKeyLength.getValue());
			bar = new ByteArrayResult(status, ba);
			wrappedLibrary.at_free(exportedPubKey);
		} else {
			bar = new ByteArrayResult(status);
		}
		return bar;
	}

	@Override
	public long getMaxNumberOfClients() {
		NativeInt32ByReference m = new NativeInt32ByReference();
		long toReturn = wrappedLibrary.getMaxNumberOfClients(m);
		if(hasResultData(toReturn)) {
			toReturn = m.getValue();
		} 
		return toReturn;
	}
	
	@Override
	public long getMaxNumberOfClientsWithTse(String tseId) {
		NativeInt32ByReference m = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		long toReturn = wrappedLibrary.getMaxNumberOfClientsWithTse(m, tse, tseLength);
		if(hasResultData(toReturn)) {
			toReturn = m.getValue();
		} 
		return toReturn;
	}

	@Override
	public long getCurrentNumberOfClients() {
		NativeInt32ByReference m = new NativeInt32ByReference();
		long toReturn  = wrappedLibrary.getCurrentNumberOfClients(m);
		if(hasResultData(toReturn)) {
			toReturn = m.getValue();
		}
		return toReturn;
	}
	
	@Override
	public long getCurrentNumberOfClientsWithTse(String tseId) {
		NativeInt32ByReference m = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		long toReturn  = wrappedLibrary.getCurrentNumberOfClientsWithTse(m, tse, tseLength);
		if(hasResultData(toReturn)) {
			toReturn = m.getValue();
		}
		return toReturn;
	}

	@Override
	public long getMaxNumberOfTransactions() {
		NativeInt32ByReference m = new NativeInt32ByReference();
		long toReturn  = wrappedLibrary.getMaxNumberOfTransactions(m);
		if(hasResultData(toReturn)) {
			toReturn = m.getValue();
		}
		return toReturn;
	}

	@Override
	public long getMaxNumberOfTransactionsWithTse(String tseId) {
		NativeInt32ByReference m = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		long toReturn  = wrappedLibrary.getMaxNumberOfTransactionsWithTse(m, tse, tseLength);
		if(hasResultData(toReturn)) {
			toReturn = m.getValue();
		}
		return toReturn;
	}
	
	@Override
	public long getCurrentNumberOfTransactions() {
		NativeInt32ByReference m = new NativeInt32ByReference();
		long toReturn  = wrappedLibrary.getCurrentNumberOfTransactions(m);
		if(hasResultData(toReturn)) {
			toReturn = m.getValue();
		}
		return toReturn;
	}
	
	@Override
	public long getCurrentNumberOfTransactionsWithTse(String tseId) {
		NativeInt32ByReference m = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		long toReturn  = wrappedLibrary.getCurrentNumberOfTransactionsWithTse(m, tse, tseLength);
		if(hasResultData(toReturn)) {
			toReturn = m.getValue();
		}
		return toReturn;
	}
	
	@Override
	public UpdateVariantsResult getSupportedTransactionUpdateVariants() {
		IntBuffer suv = IntBuffer.allocate(1);
		int status = wrappedLibrary.getSupportedTransactionUpdateVariants(suv);
		UpdateVariantsResult uvr;
		if(status == 0) {
			int variant = suv.get();
			if(variant == 0) {
				uvr = new UpdateVariantsResult(status, UpdateVariantsResult.Type.signedUpdate);
			} else if(variant == 1) {
				uvr = new UpdateVariantsResult(status, UpdateVariantsResult.Type.unsignedUpdate);
			} else if (variant == 2) {
				uvr = new UpdateVariantsResult(status, UpdateVariantsResult.Type.signedAndUnsignedUpdate);
			} else {
				uvr = new UpdateVariantsResult(TseStatusCode.ERROR_GET_SUPPORTED_UPDATE_VARIANTS_FAILED);
			}
		} else {
			uvr = new UpdateVariantsResult(status);
		}
		return uvr;
	}
	
	@Override
	public UpdateVariantsResult getSupportedTransactionUpdateVariantsWithTse(String tseId) {
		IntBuffer suv = IntBuffer.allocate(1);
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int status = wrappedLibrary.getSupportedTransactionUpdateVariantsWithTse(suv, tse, tseLength);
		UpdateVariantsResult uvr;
		if(status == 0) {
			int variant = suv.get();
			if(variant == 0) {
				uvr = new UpdateVariantsResult(status, UpdateVariantsResult.Type.signedUpdate);
			} else if(variant == 1) {
				uvr = new UpdateVariantsResult(status, UpdateVariantsResult.Type.unsignedUpdate);
			} else if (variant == 2) {
				uvr = new UpdateVariantsResult(status, UpdateVariantsResult.Type.signedAndUnsignedUpdate);
			} else {
				uvr = new UpdateVariantsResult(TseStatusCode.ERROR_GET_SUPPORTED_UPDATE_VARIANTS_FAILED);
			}
		} else {
			uvr = new UpdateVariantsResult(status);
		}
		return uvr;
	}

	@Override
	public AuthenticationResult authenticateUser(String userId, byte[] pin)  {
		// In
		ByteBuffer uid =  NativeUtil.getAsciiByteBuffer(userId);
		NativeInt32 uidLength = NativeUtil.getBufferLen(uid);
		ByteBuffer p = NativeUtil.getByteArrayByteBuffer(pin);
		NativeInt32 pLength =  new NativeInt32(p.capacity());
		// Out
		IntBuffer ar = IntBuffer.allocate(1);
		ShortBuffer rt = ShortBuffer.allocate(1);
		int status = wrappedLibrary.authenticateUser(uid, uidLength, p, pLength, ar, rt);
		AuthenticationResult toReturn;
		if(status == 0  || status == -4000) {
			int a = ar.get();
			short rtr = rt.get();
			if(a == 0) {
				toReturn = new AuthenticationResult(status, AuthenticationResult.AuthenticationStatus.ok, rtr);
			} else if(a == 1) {
				toReturn = new AuthenticationResult(status, AuthenticationResult.AuthenticationStatus.failed, rtr);
			} else if(a == 2) {
				toReturn = new AuthenticationResult(status, AuthenticationResult.AuthenticationStatus.pinIsBlocked, rtr);
			} else if(a == 3) {
				toReturn = new AuthenticationResult(status, AuthenticationResult.AuthenticationStatus.unknownUserId, rtr);
			} else {
				toReturn = new AuthenticationResult(status, AuthenticationResult.AuthenticationStatus.failed, rtr);
			}
		} else {
			toReturn = new AuthenticationResult(status);
		}
		return toReturn;
	}
	
	@Override
	public AuthenticationResult authenticateUserWithTse(String userId, byte[] pin, String tseId)  {
		ByteBuffer uid =  NativeUtil.getAsciiByteBuffer(userId);
		NativeInt32 uidLength = NativeUtil.getBufferLen(uid);
		ByteBuffer p = NativeUtil.getByteArrayByteBuffer(pin);
		NativeInt32 pLength =  new NativeInt32(p.capacity());
		IntBuffer ar = IntBuffer.allocate(1);
		ShortBuffer rt = ShortBuffer.allocate(1);
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int status = wrappedLibrary.authenticateUserWithTse(uid, uidLength, p, pLength, ar, rt, tse, tseLength);
		AuthenticationResult toReturn;
		if(status == 0  || status == -4000) {
			int a = ar.get();
			short rtr = rt.get();
			if(a == 0) {
				toReturn = new AuthenticationResult(status, AuthenticationResult.AuthenticationStatus.ok, rtr);
			} else if(a == 1) {
				toReturn = new AuthenticationResult(status, AuthenticationResult.AuthenticationStatus.failed, rtr);
			} else if(a == 2) {
				toReturn = new AuthenticationResult(status, AuthenticationResult.AuthenticationStatus.pinIsBlocked, rtr);
			} else if(a == 3) {
				toReturn = new AuthenticationResult(status, AuthenticationResult.AuthenticationStatus.unknownUserId, rtr);
			} else {
				toReturn = new AuthenticationResult(status, AuthenticationResult.AuthenticationStatus.failed, rtr);
			}
		} else {
			toReturn = new AuthenticationResult(status);
		}
		return toReturn;
	}

	@Override
	public UnblockResult unblockUser(String userId, byte[] puk, byte[] newPin) {
		
		// Input 
		ByteBuffer uid =  NativeUtil.getAsciiByteBuffer(userId); 
		NativeInt32 uidLength = NativeUtil.getBufferLen(uid);
		ByteBuffer pu = NativeUtil.getByteArrayByteBuffer(puk);
		NativeInt32 puLength =  new NativeInt32(pu.capacity());
		ByteBuffer np = NativeUtil.getByteArrayByteBuffer(newPin);
		NativeInt32 npLength =  new NativeInt32(np.capacity());
		IntBuffer ur = IntBuffer.allocate(1);
		
		int status = wrappedLibrary.unblockUser(uid, uidLength, pu, puLength, np, npLength, ur);
		
		UnblockResult ubr;
		if(statusRequiresMemoryCleanup(status)) {
			int urInt = ur.get();
			if(urInt == 0) {
				ubr = new UnblockResult(status, UnblockResult.UnblockStatus.ok);  
			} else if(urInt == 1) {
				ubr = new UnblockResult(status, UnblockResult.UnblockStatus.failed);
			} else if(urInt == 2) {
				ubr = new UnblockResult(status, UnblockResult.UnblockStatus.unknownUserId);
			} else if(urInt == 3) {
				ubr = new UnblockResult(status, UnblockResult.UnblockStatus.error);
			} else  {
				ubr = new UnblockResult(status, UnblockResult.UnblockStatus.error);
			}
		} else {
			ubr = new UnblockResult(status);
		} 
		return ubr;
	}
	
	@Override
	public UnblockResult unblockUserWithTse(String userId, byte[] puk, byte[] newPin, String tseId) {
		ByteBuffer uid =  NativeUtil.getAsciiByteBuffer(userId); 
		NativeInt32 uidLength = NativeUtil.getBufferLen(uid);
		ByteBuffer pu = NativeUtil.getByteArrayByteBuffer(puk);
		NativeInt32 puLength =  new NativeInt32(pu.capacity());
		ByteBuffer np = NativeUtil.getByteArrayByteBuffer(newPin);
		NativeInt32 npLength =  new NativeInt32(np.capacity());
		IntBuffer ur = IntBuffer.allocate(1);
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);		
		int status = wrappedLibrary.unblockUserWithTse(uid, uidLength, pu, puLength, np, npLength, ur, tse, tseLength);
		UnblockResult ubr;
		if(statusRequiresMemoryCleanup(status)) {
			int urInt = ur.get();
			if(urInt == 0) {
				ubr = new UnblockResult(status, UnblockResult.UnblockStatus.ok);  
			} else if(urInt == 1) {
				ubr = new UnblockResult(status, UnblockResult.UnblockStatus.failed);
			} else if(urInt == 2) {
				ubr = new UnblockResult(status, UnblockResult.UnblockStatus.unknownUserId);
			} else if(urInt == 3) {
				ubr = new UnblockResult(status, UnblockResult.UnblockStatus.error);
			} else  {
				ubr = new UnblockResult(status, UnblockResult.UnblockStatus.error);
			}
		} else {
			ubr = new UnblockResult(status);
		} 
		return ubr;
	}

	@Override
	public int deleteStoredData() {
		return wrappedLibrary.deleteStoredData();
	}
	
	@Override
	public int deleteStoredDataWithTse(String tseId) {
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		return wrappedLibrary.deleteStoredDataWithTse(tse, tseLength);
	}
	
	@Override
	public int logOut(String userId) {
		ByteBuffer uid =  NativeUtil.getAsciiByteBuffer(userId); 
		NativeInt32 uidLength = NativeUtil.getBufferLen(uid);
		return wrappedLibrary.logOut(uid, uidLength);
	}
	
	@Override
	public int logOutWithTse(String userId, String tseId) {
		ByteBuffer uid =  NativeUtil.getAsciiByteBuffer(userId); 
		NativeInt32 uidLength = NativeUtil.getBufferLen(uid);
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		return wrappedLibrary.logOutWithTse(uid, uidLength, tse, tseLength);
	}
	
	@Override
	public int initialize(String description) {
		ByteBuffer d = NativeUtil.getAsciiByteBuffer(description);
		NativeInt32 dLength = NativeUtil.getBufferLen(d);
		return wrappedLibrary.initializeDescriptionNotSet(d, dLength);
	}
	
	@Override
	public int initializeWithTse(String description, String tseId) {
		ByteBuffer d = NativeUtil.getAsciiByteBuffer(description);
		NativeInt32 dLength = NativeUtil.getBufferLen(d);
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		return wrappedLibrary.initializeDescriptionNotSetWithTse(d, dLength, tse, tseLength);
	}

	@Override
	public int initialize() {
		return wrappedLibrary.initializeDescriptionSet();
	}
	
	@Override
	public int initializeWithTse(String tseId) {
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		return wrappedLibrary.initializeDescriptionSetWithTse(tse, tseLength);
	}

	@Override
	public int updateTime(ZonedDateTime newDateTime) {
		long time = NativeUtil.getTimeStruct(newDateTime);
		return wrappedLibrary.updateTime(time);
	}
	
	@Override
	public int updateTimeWithTse(ZonedDateTime newDateTime, String tseId) {
		long time = NativeUtil.getTimeStruct(newDateTime);
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		return wrappedLibrary.updateTimeWithTse(time, tse, tseLength);
	}

	@Override
	public int updateTime() {
		return wrappedLibrary.updateTimeWithTimeSync();
	}
	
	@Override
	public int updateTimeWithTse(String tseId) {
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		return wrappedLibrary.updateTimeWithTimeSynWithTse(tse, tseLength);
	}

	@Override
	public int disableSecureElement() {
		return wrappedLibrary.disableSecureElement();
	}
	
	@Override
	public int disableSecureElementWithTse(String tseId) {
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		return wrappedLibrary.disableSecureElementWithTse(tse, tseLength);
	}
	
	@Override
	public int restoreFromBackup(byte[] restoreData) {
		ByteBuffer rd = NativeUtil.getByteArrayByteBuffer(restoreData);
		NativeInt32 rdLength = new NativeInt32(rd.capacity());
		return wrappedLibrary.restoreFromBackup(rd, rdLength);
	}
	
	@Override
	public int restoreFromBackupWithTse(byte[] restoreData, String tseId) {
		ByteBuffer rd = NativeUtil.getByteArrayByteBuffer(restoreData);
		NativeInt32 rdLength = new NativeInt32(rd.capacity());
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		return wrappedLibrary.restoreFromBackupWithTse(rd, rdLength, tse, tseLength);
	}
    
    @Override
    public int at_load() {
    	return wrappedLibrary.at_load();
    }
    
    @Override
    public int at_unload() {
    	return wrappedLibrary.at_unload();
    }
    
   
    //// //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //// NEEDED
    //// //////////////////////////////////////////////////////////////////////////////////////////////////////////////    

    private long int32ValueOrStatus(int res, NativeInt32ByReference int32) {
    	long toReturn;
    	if(res == 0) {
    		toReturn = int32.getValue();
    	} else {
    		toReturn = res;
    	}
		return toReturn;
	}
	
    private long int64ValueOrStatus(int res, NativeInt64ByReference int64) {
    	long toReturn;
    	if(res == 0) {
    		toReturn = int64.getValue();
    	} else {
    		toReturn = res;
    	}
		return toReturn;
	}
	
	private StringResult stringValueOrStatus(int res, PointerByReference pointerByReference, NativeInt32ByReference pointerByReferenceLength) {
		StringResult toReturn;
    	if(res == 0) {
    		byte[] ba = pointerByReference.getValue().getByteArray(0, pointerByReferenceLength.getValue());
    		String s = new String(ba);
    		toReturn = new StringResult(res, s.replace("\0", ""));
    		wrappedLibrary.at_free(pointerByReference);
    	} else {
    		toReturn = new StringResult(res);
    	}
		return toReturn;
	}
   
	private static boolean hasResultData(long status) {
		return status == 0 || status == TseStatusCode.ERROR_CERTIFICATE_EXPIRED;
	}
	
	private static boolean statusRequiresMemoryCleanup(int status) {
		return status == 0 || status == TseStatusCode.ERROR_CERTIFICATE_EXPIRED;
	}
	
	private IntArrayResult intArrayOrStatus(int status, PointerByReference ia, NativeInt32ByReference iaLength) {
		IntArrayResult toReturn;
		if(statusRequiresMemoryCleanup(status)) {
			int[] ba = ia.getValue().getIntArray(0, iaLength.getValue());
			toReturn = new IntArrayResult(status, ba);
			wrappedLibrary.at_free(ia);
		} else {
			toReturn = new IntArrayResult(status);
		}
		return toReturn;
	}

	// ///////////////////////////////////
	//      A-Trust functions
    // ///////////////////////////////////
	
	@Override
	public StringResult at_getVersion() {
		PointerByReference version = new PointerByReference();
		NativeInt32ByReference versionLength = new NativeInt32ByReference();
    	int res = wrappedLibrary.at_getVersion(version, versionLength);
    	return stringValueOrStatus(res, version, versionLength);
	}

	@Override
	public ByteArrayResult at_getCertificate() {
		PointerByReference cert = new PointerByReference();
		NativeInt32ByReference certLength = new NativeInt32ByReference();
		int status = wrappedLibrary.at_getCertificate(cert, certLength);
		return getByteArrayResult(cert, certLength, status);
	}

	@Override
	public ByteArrayResult at_getCertificateWithTse(String tseId) {
		PointerByReference cert = new PointerByReference();
		NativeInt32ByReference certLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int status = wrappedLibrary.at_getCertificateWithTse(cert, certLength, tse, tseLength);
		return getByteArrayResult(cert, certLength, status);
	}

	@Override
	public long at_getLifecycleState() {
		NativeInt32ByReference state = new NativeInt32ByReference();
		int res = wrappedLibrary.at_getLifecycleState(state);
		return int32ValueOrStatus(res, state);
	}

	@Override
	public long at_getLifecycleStateWithTse(String tseId) {
		NativeInt32ByReference state = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int res = wrappedLibrary.at_getLifecycleStateWithTse(state, tse, tseLength);
		return int32ValueOrStatus(res, state);
	}

	@Override
	public StringResult at_getLogTimeFormat() {
		PointerByReference format = new PointerByReference();
		NativeInt32ByReference formatLength = new NativeInt32ByReference();
    	int res = wrappedLibrary.at_getLogTimeFormat(format, formatLength);
    	return stringValueOrStatus(res, format, formatLength);
	}

	@Override
	public StringResult at_getLogTimeFormatWithTse(String tseId) {
		PointerByReference format = new PointerByReference();
		NativeInt32ByReference formatLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
    	int res = wrappedLibrary.at_getLogTimeFormatWithTse(format, formatLength, tse, tseLength);
    	return stringValueOrStatus(res, format, formatLength);
	}

	@Override
	public IntArrayResult at_getOpenTransactions() {
    	PointerByReference transactionNumbers = new PointerByReference();
		NativeInt32ByReference transactionNumbersLength = new NativeInt32ByReference();
		int res = wrappedLibrary.at_getOpenTransactions(transactionNumbers, transactionNumbersLength);
    	return intArrayOrStatus(res, transactionNumbers, transactionNumbersLength);
	}

	@Override
	public IntArrayResult at_getOpenTransactionsWithTse(String tseId) {
		PointerByReference transactionNumbers = new PointerByReference();
		NativeInt32ByReference transactionNumbersLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int res = wrappedLibrary.at_getOpenTransactionsWithTse(transactionNumbers, transactionNumbersLength, tse, tseLength);
    	return intArrayOrStatus(res, transactionNumbers, transactionNumbersLength);
	}

	@Override
	public ByteArrayResult at_getPublicKey() {
		PointerByReference pubKey = new PointerByReference();
		NativeInt32ByReference pubKeyLength = new NativeInt32ByReference();
		int status = wrappedLibrary.at_getPublicKey(pubKey, pubKeyLength);
		return getByteArrayResult(pubKey, pubKeyLength, status);
	}

	@Override
	public ByteArrayResult at_getPublicKeyWithTse(String tseId) {
		PointerByReference pubKey = new PointerByReference();
		NativeInt32ByReference pubKeyLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int status = wrappedLibrary.at_getPublicKeyWithTse(pubKey, pubKeyLength, tse, tseLength);
		return getByteArrayResult(pubKey, pubKeyLength, status);
	}

	@Override
	public StringResult at_getSerialNumber() {
		PointerByReference serial = new PointerByReference();
		NativeInt32ByReference serialLength = new NativeInt32ByReference();
		int status = wrappedLibrary.at_getSerialNumber(serial, serialLength);
		return getByteArrayAsHexStringResult(serial, serialLength, status);
	}

	@Override
	public StringResult at_getSerialNumberWithTse(String tseId) {
		PointerByReference serial = new PointerByReference();
		NativeInt32ByReference serialLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int status = wrappedLibrary.at_getSerialNumberWithTse(serial, serialLength, tse, tseLength);
		return getByteArrayAsHexStringResult(serial, serialLength, status);
	}

	@Override
	public StringResult at_getSignatureAlgorithm() {
		PointerByReference signatureAlgorithm = new PointerByReference();
		NativeInt32ByReference signatureAlgorithmLength = new NativeInt32ByReference();
    	int res = wrappedLibrary.at_getSignatureAlgorithm(signatureAlgorithm, signatureAlgorithmLength);
    	return stringValueOrStatus(res, signatureAlgorithm, signatureAlgorithmLength);
	}

	@Override
	public StringResult at_getSignatureAlgorithmWithTse(String tseId) {
		PointerByReference signatureAlgorithm = new PointerByReference();
		NativeInt32ByReference signatureAlgorithmLength = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
    	int res = wrappedLibrary.at_getSignatureAlgorithmWithTse(signatureAlgorithm, signatureAlgorithmLength, tse, tseLength);
    	return stringValueOrStatus(res, signatureAlgorithm, signatureAlgorithmLength);
	}

	@Override
	public long at_getSignatureCounter() {
		NativeInt32ByReference counter = new NativeInt32ByReference();
		int res = wrappedLibrary.at_getSignatureCounter(counter);
		return int32ValueOrStatus(res, counter);
	}

	@Override
	public long at_getSignatureCounterWithTse(String tseId) {
		NativeInt32ByReference counter = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int res = wrappedLibrary.at_getSignatureCounterWithTse(counter, tse, tseLength);
		return int32ValueOrStatus(res, counter);
	}

	@Override
	public long at_getTransactionCounter() {
		NativeInt32ByReference counter = new NativeInt32ByReference();
		int res = wrappedLibrary.at_getTransactionCounter(counter);
		return int32ValueOrStatus(res, counter);
	}

	@Override
	public long at_getTransactionCounterWithTse(String tseId) {
		NativeInt32ByReference counter = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		int res = wrappedLibrary.at_getTransactionCounterWithTse(counter, tse, tseLength);
		return int32ValueOrStatus(res, counter);
	}

	@Override
	public int at_suspendSecureElement() {
		return wrappedLibrary.at_suspendSecureElement();
	}

	@Override
	public int at_suspendSecureElementWithTse(String tseId) {
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		return wrappedLibrary.at_suspendSecureElementWithTse(tse, tseLength);
	}

	@Override
	public int at_unsuspendSecureElement() {
		return wrappedLibrary.at_unsuspendSecureElement();
	}

	@Override
	public int at_unsuspendSecureElementWithTse(String tseId) {
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
		return wrappedLibrary.at_unsuspendSecureElementWithTse(tse, tseLength);
	}

	@Override
	public int at_registerClientId(String clientId) {
		ByteBuffer cid = NativeUtil.getAsciiByteBuffer(clientId);
		NativeInt32 cidLength = NativeUtil.getBufferLen(cid);
		return wrappedLibrary.at_registerClientId(cid, cidLength);
	}
	
	// ///////////////////////////////////
	//      Configuration functions
    // ///////////////////////////////////
	
	
	@Override
	public int cfgSetConfigFile(String configFile) {
		ByteBuffer path = NativeUtil.getAsciiByteBuffer(configFile);
		NativeInt32 pathLength = NativeUtil.getBufferLen(path);
    	return wrappedLibrary.cfgSetConfigFile(path, pathLength);
	}

	@Override
	public int cfgSetHttpProxy(String proxyUrl) {
		ByteBuffer url = NativeUtil.getAsciiByteBuffer(proxyUrl);
		NativeInt32 urlLength = NativeUtil.getBufferLen(url);
		return wrappedLibrary.cfgSetHttpProxy(url, urlLength);
	}

	@Override
	public int cfgSetLogAppend(boolean enabled) {
		return wrappedLibrary.cfgSetLogAppend(enabled);
	}

	@Override
	public int cfgSetLogColors(boolean enabled) {
		return wrappedLibrary.cfgSetLogColors(enabled);
	}

	@Override
	public int cfgSetLogDetails(boolean enabled) {
		return wrappedLibrary.cfgSetLogDetails(enabled);
	}

	@Override
	public int cfgSetLogDir(String dir) {
		ByteBuffer path = NativeUtil.getAsciiByteBuffer(dir);
		NativeInt32 pathLength = NativeUtil.getBufferLen(path);
    	return wrappedLibrary.cfgSetLogDir(path, pathLength);
	}

	@Override
	public int cfgSetLogLevel(ByteBuffer logLevel, NativeInt32 logLevelLength) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not implemented");
	}

	@Override
	public int cfgSetLogStderrColors(boolean enabled) {
		return wrappedLibrary.cfgSetLogStderrColors(enabled);
	}

	@Override
	public int cfgSetLoggingEnabled(boolean enabled) {
		return wrappedLibrary.cfgSetLoggingEnabled(enabled);
	}

	@Override
	public int cfgSetLoggingFile(boolean enabled) {
		return wrappedLibrary.cfgSetLoggingFile(enabled);
	}

	@Override
	public int cfgSetLoggingStderr(boolean enabled) {
		return wrappedLibrary.cfgSetLoggingStderr(enabled);
	}

	@Override
	public int cfgTseAdd(String tseID, int tseType, String connParam, String atrustTseID, String atrustApiKey,
			String timeAdminID, String timeAdminPwd) {
		// TODO add method
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public int cfgTseRemove(ByteBuffer tseID, NativeInt32 tseIDLength) {
		// TODO add method
		throw new RuntimeException("Not implemented yet");
	}
	
    //// /////////////////////////////////////////////
    //// New functions
    //// /////////////////////////////////////////////
    
    @Override
    public IntArrayResult getERSMappings() {
    	PointerByReference mappingData = new PointerByReference();
    	NativeInt32ByReference mappingDataLength = new NativeInt32ByReference();
    	int res = wrappedLibrary.getERSMappings(mappingData, mappingDataLength);
    	return intArrayOrStatus(res, mappingData, mappingDataLength);
    }

    @Override
    public IntArrayResult getERSMappingsWithTse(String tseId) {
    	PointerByReference mappingData = new PointerByReference();
    	NativeInt32ByReference mappingDataLength = new NativeInt32ByReference();
    	ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
    	int res = wrappedLibrary.getERSMappingsWithTse(mappingData, mappingDataLength, tse, tseLength);
    	return intArrayOrStatus(res, mappingData, mappingDataLength);
    }

    @Override
    public int mapERStoKey(String clientId, String keyId) {
    	ByteBuffer cid = NativeUtil.getAsciiByteBuffer(clientId);
		NativeInt32 cidLength = NativeUtil.getBufferLen(cid);
		ByteBuffer kid = NativeUtil.getAsciiByteBuffer(keyId);
		NativeInt32 kidLength = NativeUtil.getBufferLen(kid);
    	return wrappedLibrary.mapERStoKey(cid, cidLength, kid, kidLength);
    }

    @Override
    public int mapERStoKeyWithTse(String clientId, String keyId, String tseId) {
    	ByteBuffer cid = NativeUtil.getAsciiByteBuffer(clientId);
		NativeInt32 cidLength = NativeUtil.getBufferLen(cid);
		ByteBuffer kid = NativeUtil.getAsciiByteBuffer(keyId);
		NativeInt32 kidLength = NativeUtil.getBufferLen(kid);
    	ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
    	return wrappedLibrary.mapERStoKeyWithTse(cid, cidLength, kid, kidLength, tse, tseLength);
    }
    
    @Override
    public int initializePinValues(String pin, String puk, String timePin, String timePuk) {
    	ByteBuffer adminPin = NativeUtil.getAsciiByteBuffer(pin);
    	NativeInt32 adminPinLength = NativeUtil.getBufferLen(adminPin);
    	ByteBuffer adminPuk = NativeUtil.getAsciiByteBuffer(puk);
    	NativeInt32 adminPukLength = NativeUtil.getBufferLen(adminPuk);
    	ByteBuffer timeAdminPin = NativeUtil.getAsciiByteBuffer(timePin);
    	NativeInt32 timeAdminPinLength = NativeUtil.getBufferLen(timeAdminPin);
    	ByteBuffer timeAdminPuk = NativeUtil.getAsciiByteBuffer(timePuk);
    	NativeInt32 timeAdminPukLength = NativeUtil.getBufferLen(timeAdminPuk);
    	return wrappedLibrary.initializePinValues(adminPin, adminPinLength, adminPuk, adminPukLength, timeAdminPin, timeAdminPinLength, timeAdminPuk, timeAdminPukLength);
    }
    
    @Override
    public int initializePinValuesWithTse(String pin, String puk, String timePin, String timePuk, String tseId) {
    	ByteBuffer adminPin = NativeUtil.getAsciiByteBuffer(pin);
    	NativeInt32 adminPinLength = NativeUtil.getBufferLen(adminPin);
    	ByteBuffer adminPuk = NativeUtil.getAsciiByteBuffer(puk);
    	NativeInt32 adminPukLength = NativeUtil.getBufferLen(adminPuk);
    	ByteBuffer timeAdminPin = NativeUtil.getAsciiByteBuffer(timePin);
    	NativeInt32 timeAdminPinLength = NativeUtil.getBufferLen(timeAdminPin);
    	ByteBuffer timeAdminPuk = NativeUtil.getAsciiByteBuffer(timePuk);
    	NativeInt32 timeAdminPukLength = NativeUtil.getBufferLen(timeAdminPuk);
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
    	return wrappedLibrary.initializePinValuesWithTse(adminPin, adminPinLength, adminPuk, adminPukLength, timeAdminPin, timeAdminPinLength, timeAdminPuk, timeAdminPukLength, tse, tseLength);
    }
    
    @Override
    public int getPinStatus() {
    	NativeInt32ByReference pinState = new NativeInt32ByReference();
    	int res = wrappedLibrary.getPinStatus(pinState);
    	return (int) int32ValueOrStatus(res, pinState);
    }
    
    @Override
    public int getPinStatusWithTse(String tseId) {
    	NativeInt32ByReference pinState = new NativeInt32ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
    	int res = wrappedLibrary.getPinStatusWithTse(pinState, tse, tseLength);
    	return (int) int32ValueOrStatus(res, pinState);
    }
    
    @Override
    public int initializeNEW() {
    	return TseStatusCode.ERROR_FUNCTION_NOT_SUPPORTED;
    }
    
    @Override
    public int initializeNEWWithTse(String tseId) {
    	return TseStatusCode.ERROR_FUNCTION_NOT_SUPPORTED;
    }
    
    @Override
	public int activateTse() {
    	return wrappedLibrary.activateTse();
    }
    
    @Override
	public int activateTseWithTse(String tseId) {
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
    	return wrappedLibrary.activateTseWithTse(tse, tseLength);
    }
    
    @Override
	public int deactivateTse() {
    	return wrappedLibrary.deactivateTse();
    }
    
    @Override
	public int deactivateTseWithTse(String tseId) {
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
    	return wrappedLibrary.deactivateTseWithTse(tse, tseLength);
    }
    
    @Override
	public long getTotalLogMemory() {
    	NativeInt64ByReference sizeOfMemory = new NativeInt64ByReference();
    	int res = wrappedLibrary.getTotalLogMemory(sizeOfMemory);
    	return int64ValueOrStatus(res, sizeOfMemory);
    }
    
    @Override
	public long getTotalLogMemoryWithTse(String tseId) {
    	NativeInt64ByReference sizeOfMemory = new NativeInt64ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
    	int res = wrappedLibrary.getTotalLogMemoryWithTse(sizeOfMemory, tse, tseLength);
    	return int64ValueOrStatus(res, sizeOfMemory);
    }
    
    @Override
	public long getAvailableLogMemory() {
    	NativeInt64ByReference sizeOfMemory = new NativeInt64ByReference();
    	int res = wrappedLibrary.getAvailableLogMemory(sizeOfMemory);
    	return int64ValueOrStatus(res, sizeOfMemory);
    }
    
    @Override
	public long getAvailableLogMemoryWithTse(String tseId) {
    	NativeInt64ByReference sizeOfMemory = new NativeInt64ByReference();
		ByteBuffer tse = NativeUtil.getAsciiByteBuffer(tseId);
		NativeInt32 tseLength = NativeUtil.getBufferLen(tse);
    	int res = wrappedLibrary.getAvailableLogMemoryWithTse(sizeOfMemory, tse, tseLength);
    	return int64ValueOrStatus(res, sizeOfMemory);
    }
}
