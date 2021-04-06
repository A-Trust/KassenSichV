package at.atrust.asigntse.seapi;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.ptr.PointerByReference;

public interface NativeLibraryWrapper extends Library {
	
	static String LIBRARY_NAME_WINDOWS = "asigntse"; //$NON-NLS-1$
	static String LIBRARY_NAME_LINUX = "libasigntse.so"; //$NON-NLS-1$

	public static final NativeLibraryWrapper INSTANCE = Native.load((Platform.isWindows() || Platform.isAndroid() ? LIBRARY_NAME_WINDOWS : LIBRARY_NAME_LINUX), NativeLibraryWrapper.class);

	int initializeDescriptionNotSet       (ByteBuffer description, NativeInt32 descriptionLength);
	int initializeDescriptionNotSetWithTse(ByteBuffer description, NativeInt32 descriptionLength, ByteBuffer tseId, NativeInt32 tseIdLength);
	
	int initializeDescriptionSet();
	int initializeDescriptionSetWithTse(ByteBuffer tseId, NativeInt32 tseIdLength);
	
	int updateTime       (long newDateTime);
	int updateTimeWithTse(long newDateTime, ByteBuffer tseId, NativeInt32 tseIdLength);
	
	int updateTimeWithTimeSync();
	int updateTimeWithTimeSynWithTse(ByteBuffer tseId, NativeInt32 tseIdLength);

	int disableSecureElement();
	int disableSecureElementWithTse(ByteBuffer tseId, NativeInt32 tseIdLength);

	int startTransaction       (ByteBuffer clientId, NativeInt32 clientIdLength, ByteBuffer processData, NativeInt32 processDataLength, ByteBuffer processType, NativeInt32 processTypeLength, ByteBuffer additionalData, NativeInt32 additionalDataLength, NativeInt32ByReference transactionNumber, NativeInt64ByReference logTime, PointerByReference serialNumber, NativeInt32ByReference serialNumberLength, NativeInt32ByReference signatureCounter, PointerByReference signatureValue, NativeInt32ByReference signatureValueLength);
	int startTransactionWithTse(ByteBuffer clientId, NativeInt32 clientIdLength, ByteBuffer processData, NativeInt32 processDataLength, ByteBuffer processType, NativeInt32 processTypeLength, ByteBuffer additionalData, NativeInt32 additionalDataLength, NativeInt32ByReference transactionNumber, NativeInt64ByReference logTime, PointerByReference serialNumber, NativeInt32ByReference serialNumberLength, NativeInt32ByReference signatureCounter, PointerByReference signatureValue, NativeInt32ByReference signatureValueLength, ByteBuffer tseId, NativeInt32 tseIdLength);

	int updateTransaction       (ByteBuffer clientId, NativeInt32 clientIdLength, NativeInt32 transactionNumber, ByteBuffer processData, NativeInt32 processDataLength, ByteBuffer processType, NativeInt32 processTypeLength, NativeInt64ByReference logTime, PointerByReference signatureValue, NativeInt32ByReference signatureValueLength, NativeInt32ByReference signatureCounter);
	int updateTransactionWithTse(ByteBuffer clientId, NativeInt32 clientIdLength, NativeInt32 transactionNumber, ByteBuffer processData, NativeInt32 processDataLength, ByteBuffer processType, NativeInt32 processTypeLength, NativeInt64ByReference logTime, PointerByReference signatureValue, NativeInt32ByReference signatureValueLength, NativeInt32ByReference signatureCounter, ByteBuffer tseId, NativeInt32 tseIdLength);
	
	int finishTransaction       (ByteBuffer clientId, NativeInt32 clientIdLength, NativeInt32 transactionNumber, ByteBuffer processData, NativeInt32 processDataLength, ByteBuffer processType, NativeInt32 processTypeLength, ByteBuffer additionalData, NativeInt32 additionalDataLength, NativeInt64ByReference logTime, PointerByReference signatureValue, NativeInt32ByReference signatureValueLength, NativeInt32ByReference signatureCounter);
	int finishTransactionWithTse(ByteBuffer clientId, NativeInt32 clientIdLength, NativeInt32 transactionNumber, ByteBuffer processData, NativeInt32 processDataLength, ByteBuffer processType, NativeInt32 processTypeLength, ByteBuffer additionalData, NativeInt32 additionalDataLength, NativeInt64ByReference logTime, PointerByReference signatureValue, NativeInt32ByReference signatureValueLength, NativeInt32ByReference signatureCounter, ByteBuffer tseId, NativeInt32 tseIdLength);

	int exportDataFilteredByTransactionNumberAndClientId       (NativeInt32 transactionNumber, ByteBuffer clientId, NativeInt32 clientIdLength, PointerByReference exportedData, NativeInt32ByReference exportedDataLength);
	int exportDataFilteredByTransactionNumberAndClientIdWithTse(NativeInt32 transactionNumber, ByteBuffer clientId, NativeInt32 clientIdLength, PointerByReference exportedData, NativeInt32ByReference exportedDataLength, ByteBuffer tseId, NativeInt32 tseIdLength);
	
	int exportDataFilteredByTransactionNumber       (NativeInt32 transactionNumber, PointerByReference exportedData, NativeInt32ByReference exportedDataLength);
	int exportDataFilteredByTransactionNumberWithTse(NativeInt32 transactionNumber, PointerByReference exportedData, NativeInt32ByReference exportedDataLength, ByteBuffer tseId, NativeInt32 tseIdLength);
	
	int exportDataFilteredByTransactionNumberInterval       (NativeInt32 startTransactionNumber, NativeInt32 endTransactionNumber, NativeInt32 maximumNumberRecords, PointerByReference exportedData, NativeInt32ByReference exportedDataLength);
	int exportDataFilteredByTransactionNumberIntervalWithTse(NativeInt32 startTransactionNumber, NativeInt32 endTransactionNumber, NativeInt32 maximumNumberRecords, PointerByReference exportedData, NativeInt32ByReference exportedDataLength, ByteBuffer tseId, NativeInt32 tseIdLength);
	
	int exportDataFilteredByTransactionNumberIntervalAndClientId       (NativeInt32 startTransactionNumber, NativeInt32 endTransactionNumber, ByteBuffer clientId, NativeInt32 clientIdLength, NativeInt32 maximumNumberRecords, PointerByReference exportedData, NativeInt32ByReference exportedDataLength);
	int exportDataFilteredByTransactionNumberIntervalAndClientIdWithTse(NativeInt32 startTransactionNumber, NativeInt32 endTransactionNumber, ByteBuffer clientId, NativeInt32 clientIdLength, NativeInt32 maximumNumberRecords, PointerByReference exportedData, NativeInt32ByReference exportedDataLength, ByteBuffer tseId, NativeInt32 tseIdLength);
	
	int exportDataFilteredByPeriodOfTime       (long startDate, long endDate, NativeInt32 maximumNumberRecords, PointerByReference exportedData, NativeInt32ByReference exportedDataLength);
	int exportDataFilteredByPeriodOfTimeWithTse(long startDate, long endDate, NativeInt32 maximumNumberRecords, PointerByReference exportedData, NativeInt32ByReference exportedDataLength, ByteBuffer tseId, NativeInt32 tseIdLength);

	int exportDataFilteredByPeriodOfTimeAndClientId       (long startDate, long endDate, ByteBuffer clientId, NativeInt32 clientIdLength, NativeInt32 maximumNumberRecords, PointerByReference exportedData, NativeInt32ByReference exportedDataLength);
	int exportDataFilteredByPeriodOfTimeAndClientIdWithTse(long startDate, long endDate, ByteBuffer clientId, NativeInt32 clientIdLength, NativeInt32 maximumNumberRecords, PointerByReference exportedData, NativeInt32ByReference exportedDataLength, ByteBuffer tseId, NativeInt32 tseIdLength);

	int exportData       (NativeInt32 maximumNumberRecords, PointerByReference exportedData, NativeInt32ByReference exportedDataLength);
	int exportDataWithTse(NativeInt32 maximumNumberRecords, PointerByReference exportedData, NativeInt32ByReference exportedDataLength, ByteBuffer tseId, NativeInt32 tseIdLength);

	int exportCertificates       (PointerByReference certificates, NativeInt32ByReference certificatesLength);
	int exportCertificatesWithTse(PointerByReference certificates, NativeInt32ByReference certificatesLength, ByteBuffer tseId, NativeInt32 tseIdLength);

	int exportPublicKey       (ByteBuffer keyId, NativeInt32 keyIdLength, PointerByReference exportedPubKey, NativeInt32ByReference exportedPubKeyLength);
	int exportPublicKeyWithTse(ByteBuffer keyId, NativeInt32 keyIdLength, PointerByReference exportedPubKey, NativeInt32ByReference exportedPubKeyLength, ByteBuffer tseId, NativeInt32 tseIdLength);
	
	int restoreFromBackup       (ByteBuffer restoreData, NativeInt32 restoreDataLength);
	int restoreFromBackupWithTse(ByteBuffer restoreData, NativeInt32 restoreDataLength, ByteBuffer tseId, NativeInt32 tseIdLength);
	
	int readLogMessage       (PointerByReference logMessage, NativeInt32ByReference logMessageLength);
	int readLogMessageWithTse(PointerByReference logMessage, NativeInt32ByReference logMessageLength, ByteBuffer tseId, NativeInt32 tseIdLength);
	
	int exportSerialNumbers       (PointerByReference serialNumbers, NativeInt32ByReference serialNumbersLength);
	int exportSerialNumbersWithTse(PointerByReference serialNumbers, NativeInt32ByReference serialNumbersLength, ByteBuffer tseId, NativeInt32 tseIdLength);

	int getMaxNumberOfClients       (NativeInt32ByReference maxNumberClients);
	int getMaxNumberOfClientsWithTse(NativeInt32ByReference maxNumberClients, ByteBuffer tseId, NativeInt32 tseIdLength);

	int getCurrentNumberOfClients       (NativeInt32ByReference currentNumberClients);
	int getCurrentNumberOfClientsWithTse(NativeInt32ByReference currentNumberClients, ByteBuffer tseId, NativeInt32 tseIdLength);

	int getMaxNumberOfTransactions       (NativeInt32ByReference maxNumberTransactions);
	int getMaxNumberOfTransactionsWithTse(NativeInt32ByReference maxNumberTransactions, ByteBuffer tseId, NativeInt32 tseIdLength);

	int getCurrentNumberOfTransactions       (NativeInt32ByReference currentNumberTransactions);
	int getCurrentNumberOfTransactionsWithTse(NativeInt32ByReference currentNumberTransactions, ByteBuffer tseId, NativeInt32 tseIdLength);

	int getSupportedTransactionUpdateVariants       (IntBuffer supportedUpdateVariants);
	int getSupportedTransactionUpdateVariantsWithTse(IntBuffer supportedUpdateVariants, ByteBuffer tseId, NativeInt32 tseIdLength);

	int deleteStoredData();
	int deleteStoredDataWithTse(ByteBuffer tseId, NativeInt32 tseIdLength);

	int authenticateUser       (ByteBuffer userId, NativeInt32 userIdLength, ByteBuffer pin, NativeInt32 pinLength, IntBuffer authenticationResult, ShortBuffer remainingRetries);
	int authenticateUserWithTse(ByteBuffer userId, NativeInt32 userIdLength, ByteBuffer pin, NativeInt32 pinLength, IntBuffer authenticationResult, ShortBuffer remainingRetries, ByteBuffer tseId, NativeInt32 tseIdLength);
	
	int logOut       (ByteBuffer userId, NativeInt32 userIdLength);
	int logOutWithTse(ByteBuffer userId, NativeInt32 userIdLength, ByteBuffer tseId, NativeInt32 tseIdLength);

	int unblockUser       (ByteBuffer userId, NativeInt32 userIdLength, ByteBuffer puk, NativeInt32 pukLength, ByteBuffer newPin, NativeInt32 newPinLength, IntBuffer unblockResult);
	int unblockUserWithTse(ByteBuffer userId, NativeInt32 userIdLength, ByteBuffer puk, NativeInt32 pukLength, ByteBuffer newPin, NativeInt32 newPinLength, IntBuffer unblockResult, ByteBuffer tseId, NativeInt32 tseIdLength);

    int getOpenTransactions       (PointerByReference transactionNumbers, NativeInt32ByReference transactionNumbersLength);
    int getOpenTransactionsWithTse(PointerByReference transactionNumbers, NativeInt32ByReference transactionNumbersLength, ByteBuffer tseId, NativeInt32 tseIdLength);

    int getERSMappings       (PointerByReference mappingData, NativeInt32ByReference mappingDataLength);
    int getERSMappingsWithTse(PointerByReference mappingData, NativeInt32ByReference mappingDataLength, ByteBuffer tseId, NativeInt32 tseIdLength);

    int mapERStoKey       (ByteBuffer clientId, NativeInt32 clientIdLength, ByteBuffer keyId, NativeInt32 keyIdLength);
    int mapERStoKeyWithTse(ByteBuffer clientId, NativeInt32 clientIdLength, ByteBuffer keyId, NativeInt32 keyIdLength, ByteBuffer tseId, NativeInt32 tseIdLength);

    int initializePinValues       (ByteBuffer adminPin, NativeInt32 adminPinLength, ByteBuffer adminPuk, NativeInt32 adminPukLength, ByteBuffer timeAdminPin, NativeInt32 timeAdminPinLength, ByteBuffer timeAdminPuk, NativeInt32 timeAdminPukLength);
    int initializePinValuesWithTse(ByteBuffer adminPin, NativeInt32 adminPinLength, ByteBuffer adminPuk, NativeInt32 adminPukLength, ByteBuffer timeAdminPin, NativeInt32 timeAdminPinLength, ByteBuffer timeAdminPuk, NativeInt32 timeAdminPukLength, ByteBuffer tseId, NativeInt32 tseIdLength);
    
    int getPinStatus       (NativeInt32ByReference pinState);
    int getPinStatusWithTse(NativeInt32ByReference pinState, ByteBuffer tseId, NativeInt32 tseIdLength);
    
    int initialize       ();
    int initializeWithTse(ByteBuffer tseId, NativeInt32 tseIdLength);
    
    int activateTse       ();
    int activateTseWithTse(ByteBuffer tseId, NativeInt32 tseIdLength);
    
    int deactivateTse       ();
    int deactivateTseWithTse(ByteBuffer tseId, NativeInt32 tseIdLength);
    
    int getTotalLogMemory       (NativeInt64ByReference sizeOfMemory);
    int getTotalLogMemoryWithTse(NativeInt64ByReference sizeOfMemory, ByteBuffer tseId, NativeInt32 tseIdLength);
  
    // XXX without TSE is missing in RUST 
    int getAvailableLogMemory       (NativeInt64ByReference sizeOfMemory);
    int getAvailableLogMemoryWithTse(NativeInt64ByReference sizeOfMemory, ByteBuffer tseId, NativeInt32 tseIdLength);

    // ///////////////////////////////////
	//      A-Trust functions
    // ///////////////////////////////////
    int at_getVersion       (PointerByReference version, NativeInt32ByReference versionLength);

	void at_free(PointerByReference ptr);

	int at_getCertificate(PointerByReference cert, NativeInt32ByReference certLength);
	int at_getCertificateWithTse(PointerByReference cert, NativeInt32ByReference certLength, ByteBuffer tseId, NativeInt32 tseIdLength);

	int at_getLifecycleState(NativeInt32ByReference state);
	int at_getLifecycleStateWithTse(NativeInt32ByReference state, ByteBuffer tseId, NativeInt32 tseIdLength);

	int at_getLogTimeFormat(PointerByReference logTimeFormat, NativeInt32ByReference logTimeFormatLength);
	int at_getLogTimeFormatWithTse(PointerByReference logTimeFormat, NativeInt32ByReference logTimeFormatLength, ByteBuffer tseId, NativeInt32 tseIdLength);

	int at_getOpenTransactions(PointerByReference transactionNumbers, NativeInt32ByReference transactionNumbersLength);
	int at_getOpenTransactionsWithTse(PointerByReference transactionNumbers, NativeInt32ByReference transactionNumbersLength, ByteBuffer tseId, NativeInt32 tseIdLength);

	int at_getPublicKey(PointerByReference pubKey, NativeInt32ByReference pubKeyLength);
	int at_getPublicKeyWithTse(PointerByReference pubKey, NativeInt32ByReference pubKeyLength, ByteBuffer tseId, NativeInt32 tseIdLength);

	int at_getSerialNumber(PointerByReference serial, NativeInt32ByReference serialLength);
	int at_getSerialNumberWithTse(PointerByReference serial, NativeInt32ByReference serialLength, ByteBuffer tseId, NativeInt32 tseIdLength);

	int at_getSignatureAlgorithm(PointerByReference signatureAlgorithm, NativeInt32ByReference signatureAlgorithmLength);
	int at_getSignatureAlgorithmWithTse(PointerByReference signatureAlgorithm, NativeInt32ByReference signatureAlgorithmLength, ByteBuffer tseId, NativeInt32 tseIdLength);

	int at_getSignatureCounter(NativeInt32ByReference counter);
	int at_getSignatureCounterWithTse(NativeInt32ByReference counter, ByteBuffer tseId, NativeInt32 tseIdLength);

	int at_getTransactionCounter(NativeInt32ByReference counter);
	int at_getTransactionCounterWithTse(NativeInt32ByReference counter, ByteBuffer tseId, NativeInt32 tseIdLength);

	int at_getVersion(ByteBuffer version, NativeInt32ByReference versionLength);

	int at_load();

	int at_suspendSecureElement();
	int at_suspendSecureElementWithTse(ByteBuffer tseId, NativeInt32 tseIdLength);

	int at_unload();

	int at_unsuspendSecureElement();
	int at_unsuspendSecureElementWithTse(ByteBuffer tseId, NativeInt32 tseIdLength);
	
	int at_registerClientId(ByteBuffer clientId, NativeInt32 clientIdLength);
	
	// ///////////////////////////////////
	//      Configuration functions
    // ///////////////////////////////////
	int cfgSetConfigFile(ByteBuffer path, NativeInt32 pathLength);

	int cfgSetHttpProxy(ByteBuffer proxyUrl, NativeInt32 proxyUrlLength);

	int cfgSetLogAppend(boolean enabled);

	int cfgSetLogColors(boolean enabled);

	int cfgSetLogDetails(boolean enabled);

	int cfgSetLogDir(ByteBuffer path, NativeInt32 pathLength);

	int cfgSetLogLevel(ByteBuffer logLevel, NativeInt32 logLevelLength);

	int cfgSetLogStderrColors(boolean enabled);

	int cfgSetLoggingEnabled(boolean enabled);

	int cfgSetLoggingFile(boolean enabled);

	int cfgSetLoggingStderr(boolean enabled);

	int cfgTseAdd(ByteBuffer tseID,
		    NativeInt32 tseIDLength,
		    NativeInt32 tseType,
		    ByteBuffer connParam,
		    NativeInt32 connParamLength,
		    ByteBuffer atrustTseID,
		    NativeInt32 atrustTseIDLength,
		    ByteBuffer atrustApiKey,
		    NativeInt32 atrustApiKeyLength,
		    ByteBuffer timeAdminID,
		    NativeInt32 timeAdminIDLength,
		    ByteBuffer timeAdminPwd,
		    NativeInt32 timeAdminPwdLength);

	int cfgTseRemove(ByteBuffer tseID, NativeInt32 tseIDLength);
}
