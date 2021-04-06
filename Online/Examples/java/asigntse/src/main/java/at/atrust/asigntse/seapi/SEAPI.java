package at.atrust.asigntse.seapi;

import java.nio.ByteBuffer;
import java.time.ZonedDateTime;

import at.atrust.asigntse.seapi.results.AuthenticationResult;
import at.atrust.asigntse.seapi.results.ByteArrayResult;
import at.atrust.asigntse.seapi.results.IntArrayResult;
import at.atrust.asigntse.seapi.results.StartTransactionResult;
import at.atrust.asigntse.seapi.results.StringResult;
import at.atrust.asigntse.seapi.results.TransactionResult;
import at.atrust.asigntse.seapi.results.UnblockResult;
import at.atrust.asigntse.seapi.results.UpdateVariantsResult;

public interface SEAPI {
	
	ByteArrayResult exportData(int maximumNumberRecords);
	ByteArrayResult exportDataWithTse(int maximumNumberRecords, String tse);
	
	ByteArrayResult exportDataFilteredByTransactionNumber(long transactionNumber);
	ByteArrayResult exportDataFilteredByTransactionNumberWithTse(long transactionNumber, String tse);

	ByteArrayResult exportDataFilteredByTransactionNumberAndClientId(int transactionNumber, String clientId);
	ByteArrayResult exportDataFilteredByTransactionNumberAndClientIdWithTse(int transactionNumber, String clientId, String tse);

	ByteArrayResult exportDataFilteredByTransactionNumberInterval(int startTransactionNumber, int endTransactionNumber, int maximumNumberRecords);
	ByteArrayResult exportDataFilteredByTransactionNumberIntervalWithTse(int startTransactionNumber, int endTransactionNumber, int maximumNumberRecords, String tse);

	ByteArrayResult exportDataFilteredByTransactionNumberIntervalAndClientId(int startTransactionNumber, int endTransactionNumber, String clientId, int maximumNumberRecords);
	ByteArrayResult exportDataFilteredByTransactionNumberIntervalAndClientIdWithTse(int startTransactionNumber, int endTransactionNumber, String clientId, int maximumNumberRecords, String tse);

	ByteArrayResult exportDataFilteredByPeriodOfTime(ZonedDateTime startDate, ZonedDateTime endDate, int maximumNumberRecords);
	ByteArrayResult exportDataFilteredByPeriodOfTimeWithTse(ZonedDateTime startDate, ZonedDateTime endDate, int maximumNumberRecords, String tse);

	ByteArrayResult exportDataFilteredByPeriodOfTimeAndClientId(ZonedDateTime startDate, ZonedDateTime endDate, String clientId, int maximumNumberRecords);
	ByteArrayResult exportDataFilteredByPeriodOfTimeAndClientIdWithTse(ZonedDateTime startDate, ZonedDateTime endDate, String clientId, int maximumNumberRecords, String tse);
	
	ByteArrayResult exportCertificates();
	ByteArrayResult exportCertificatesWithTse(String tse);

	ByteArrayResult readLogMessage();
	ByteArrayResult readLogMessageWithTse(String tse);

	ByteArrayResult exportSerialNumbers();
	ByteArrayResult exportSerialNumbersWithTse(String tse);
	
	ByteArrayResult exportPublicKey(String key);
	ByteArrayResult exportPublicKey(String key, String tseId);
	
	StartTransactionResult startTransaction(String clientId);
	StartTransactionResult startTransactionWithTse(String clientId, String tse);

	StartTransactionResult startTransaction(String clientId, byte[] processData, String processType, byte[] additionalData);
	StartTransactionResult startTransactionWithTse(String clientId, byte[] processData, String processType, byte[] additionalData, String tse);

	TransactionResult updateTransaction(String clientId, long transactionNumber, byte[] processData, String processType);
	TransactionResult updateTransactionWithTse(String clientId, long transactionNumber, byte[] processData, String processType, String tse);

	TransactionResult finishTransaction(String clientId, long transactionNumber, byte[] processData, String processType);
	TransactionResult finishTransactionWithTse(String clientId, long transactionNumber, byte[] processData, String processType, String tse);

	TransactionResult finishTransaction(String clientId, long transactionNumber, byte[] processData, String processType, byte[] additionalData);
	TransactionResult finishTransactionWithTse(String clientId, long transactionNumber, byte[] processData, String processType, byte[] additionalData, String tse);
	
	UpdateVariantsResult getSupportedTransactionUpdateVariants();
	UpdateVariantsResult getSupportedTransactionUpdateVariantsWithTse(String tse);

	AuthenticationResult authenticateUser(String userId, byte[] pin);
	AuthenticationResult authenticateUserWithTse(String userId, byte[] pin, String tse);

	UnblockResult unblockUser(String userId, byte[] puk, byte[] newPin);
	UnblockResult unblockUserWithTse(String userId, byte[] puk, byte[] newPin, String tse);
	
	long getMaxNumberOfClients();
	long getMaxNumberOfClientsWithTse(String tse);

	long getCurrentNumberOfClients();
	long getCurrentNumberOfClientsWithTse(String tse);

	long getMaxNumberOfTransactions();
	long getMaxNumberOfTransactionsWithTse(String tse);

	long getCurrentNumberOfTransactions();
	long getCurrentNumberOfTransactionsWithTse(String tse);

	int deleteStoredData();
	int deleteStoredDataWithTse(String tse);

	int logOut(String userId);
	int logOutWithTse(String userId, String tse);

	int initialize(String description);
	int initializeWithTse(String description, String tse);

	int initialize();
	int initializeWithTse(String tse);

	int updateTime(ZonedDateTime newDateTime);
	int updateTimeWithTse(ZonedDateTime newDateTime, String tse);

	int updateTime();
	int updateTimeWithTse(String tse);

	int disableSecureElement();
	int disableSecureElementWithTse(String tse);

	int restoreFromBackup(byte[] restoreData);
	int restoreFromBackupWithTse(byte[] restoreData, String tse);

	// ///////////////////////////////////
	//      A-Trust functions
    // ///////////////////////////////////
	
    int at_load();
    int at_unload();
 
	StringResult at_getVersion();

	ByteArrayResult at_getCertificate();
	ByteArrayResult at_getCertificateWithTse(String tseID);

	long  at_getLifecycleState();
	long  at_getLifecycleStateWithTse(String tseID);

	StringResult at_getLogTimeFormat();
	StringResult at_getLogTimeFormatWithTse(String tseID);

	IntArrayResult at_getOpenTransactions();
	IntArrayResult at_getOpenTransactionsWithTse(String tseID);

	ByteArrayResult at_getPublicKey();
	ByteArrayResult at_getPublicKeyWithTse(String tseID);

	StringResult at_getSerialNumber();
	StringResult at_getSerialNumberWithTse(String tseID);

	StringResult at_getSignatureAlgorithm();
	StringResult at_getSignatureAlgorithmWithTse(String tseID);

	long at_getSignatureCounter();
	long at_getSignatureCounterWithTse(String tseID);

	long at_getTransactionCounter();
	long at_getTransactionCounterWithTse(String tseID);

	int at_suspendSecureElement();
	int at_suspendSecureElementWithTse(String tseID);

	int at_unsuspendSecureElement();
	int at_unsuspendSecureElementWithTse(String tseID);
	
	int at_registerClientId(String clientId);

	// ///////////////////////////////////
	//      Configuration functions
    // ///////////////////////////////////
	
	int cfgSetConfigFile(String configFile);

	int cfgSetHttpProxy(String proxyUrl);

	int cfgSetLogAppend(boolean enabled);

	int cfgSetLogColors(boolean enabled);

	int cfgSetLogDetails(boolean enabled);

	int cfgSetLogDir(String path);

	int cfgSetLogLevel(ByteBuffer logLevel, NativeInt32 logLevelLength);

	int cfgSetLogStderrColors(boolean enabled);

	int cfgSetLoggingEnabled(boolean enabled);

	int cfgSetLoggingFile(boolean enabled);

	int cfgSetLoggingStderr(boolean enabled);

	int cfgTseAdd(String tseID, int tseType, String connParam, String atrustTseID, String atrustApiKey, String timeAdminID, String timeAdminPwd);

	int cfgTseRemove(ByteBuffer tseID, NativeInt32 tseIDLength);

	// ///////////////////////////////////
	//      New functions
    // ///////////////////////////////////
	
	public IntArrayResult getERSMappings();
    public IntArrayResult getERSMappingsWithTse(String tseId);
    
    public int mapERStoKey(String clientId, String keyId);
    public int mapERStoKeyWithTse(String clientId, String keyId, String tseId);
    
    public int initializePinValues(String pin, String puk, String timePin, String timePuk);
    public int initializePinValuesWithTse(String pin, String puk, String timePin, String timePuk, String tseId);
    
    public int getPinStatus();
    public int getPinStatusWithTse(String tseId);
    
    public int initializeNEW();
    public int initializeNEWWithTse(String tseId);
    
	public int activateTse();
	public int activateTseWithTse(String tseId);
    
	public int deactivateTse();
	public int deactivateTseWithTse(String tseId);

	public long getTotalLogMemory();
	public long getTotalLogMemoryWithTse(String tseId);
    
	public long getAvailableLogMemory();
	public long getAvailableLogMemoryWithTse(String tseId) ;

}