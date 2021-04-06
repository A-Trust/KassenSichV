package at.atrust.asigntse.seapi;

public class TseStatusCode {
	
	
    /**
     * This constant defines the return value of the functions of the SE API that
     * indicates that the execution of a function has been successful
     */
    public static final int OK = 0;
    public static final int EXECUTION_OK = 0;
    
    /// a mandatory input parameter is NULL
    public static final int ERROR_MISSING_PARAMETER = -3000; 
    /// the function is not supported
    public static final int ERROR_UNSUPPORTED_COMMAND = -3001;
    public static final int ERROR_FUNCTION_NOT_SUPPORTED = -3001;
    /// connection error
    public static final int ERROR_IO = -3002;
    /// timeout error
    public static final int ERROR_TSE_TIMEOUT = -3003;
    /// the memory allocation for an output parameter failed
    public static final int ERROR_ALLOCATION_FAILED = -3004; 
    /// configuration file not found
    public static final int ERROR_CONFIG_FILE_NOT_FOUND = -3005;
    /// transport I/O timeout error
    public static final int ERROR_SE_COMMUNICATION_FAILED = -3006; 
    /// invalid TSE command data
    public static final int ERROR_TSE_COMMAND_DATA_INVALID = -3007;
    /// invalid TSE response data
    public static final int ERROR_TSE_RESPONSE_DATA_INVALID = -3008;
    /// the serial number of an ERS is already mapped to a signature key
    public static final int ERROR_ERS_ALREADY_MAPPED  = -3009;
    /// unknown ERS
    public static final int ERROR_NO_ERS = -3010;
    public static final int ERROR_TSE_UNKNOWN_ERROR = -3011;
    public static final int ERROR_STREAM_WRITE = -3012;
    public static final int ERROR_BUFFER_TOO_SMALL = -3013;
    public static final int ERROR_NO_SUCH_KEY = -3014;
    public static final int ERROR_NO_KEY = -3015;
    public static final int ERROR_SE_API_DEACTIVATED = -3016;
    public static final int ERROR_SE_API_NOT_DEACTIVATED = -3017;

    /// unknown error code
    public static final int ERROR_UNKNOWN = -3100;
   
    /**
     * Constant that defines a return value for the SE API function
     * authenticateUser. This return value indicates that the authentication attempt
     * has failed
     */
    public static final int AUTHENTICATION_FAILED = -4000;

    /**
     * Constant that defines a return value for the SE API function unblockUser.
     * This return value indicates that the attempt to unblock a PIN entry has
     * failed.
     */
    public static final int UNBLOCK_FAILED = -4001;
	
	public static final int ERROR_RETRIEVE_LOG_MESSAGE_FAILED = -5001;
	public static final int ERROR_STORAGE_FAILURE = -5002;
	public static final int ERROR_UPDATE_TIME_FAILED = -5003;
	public static final int ERROR_PARAMETER_MISMATCH = -5004;
	public static final int ERROR_ID_NOT_FOUND = -5005;
	public static final int ERROR_TRANSACTION_NUMBER_NOT_FOUND = -5006;
	public static final int ERROR_NO_DATA_AVAILABLE = -5007;
	public static final int ERROR_TOO_MANY_RECORDS = -5008;
	public static final int ERROR_START_TRANSACTION_FAILED = -5009;
	public static final int ERROR_UPDATE_TRANSACTION_FAILED = -5010;
	public static final int ERROR_FINISH_TRANSACTION_FAILED = -5011;
	public static final int ERROR_RESTORE_FAILED = -5012;
	public static final int ERROR_STORING_INIT_DATA_FAILED = -5013;
	public static final int ERROR_EXPORT_CERT_FAILED = -5014;
	public static final int ERROR_NO_LOG_MESSAGE = -5015;
	public static final int ERROR_READING_LOG_MESSAGE = -5016;
	public static final int ERROR_NO_TRANSACTION = -5017;
	public static final int ERROR_SE_API_NOT_INITIALIZED = -5018;
	public static final int ERROR_TIME_NOT_SET = -5019;
	public static final int ERROR_CERTIFICATE_EXPIRED = -5020;
	public static final int ERROR_SECURE_ELEMENT_DISABLED = -5021;
	public static final int ERROR_USER_NOT_AUTHORIZED = -5022;
	public static final int ERROR_USER_NOT_AUTHENTICATED = -5023;
	public static final int ERROR_DESCRIPTION_NOT_SET_BY_MANUFACTURER = -5024;
	public static final int ERROR_DESCRIPTION_SET_BY_MANUFACTURER = -5025;
	public static final int ERROR_EXPORT_SERIAL_NUMBERS_FAILED = -5026;
	public static final int ERROR_GET_MAX_NUMBER_OF_CLIENTS_FAILED = -5027;
	public static final int ERROR_GET_CURRENT_NUMBER_OF_CLIENTS_FAILED = -5028;
	public static final int ERROR_GET_MAX_NUMBER_TRANSACTIONS_FAILED = -5029;
	public static final int ERROR_GET_CURRENT_NUMBER_OF_TRANSACTIONS_FAILED = -5030;
	public static final int ERROR_GET_SUPPORTED_UPDATE_VARIANTS_FAILED = -5031;
	public static final int ERROR_DELETE_STORED_DATA_FAILED = -5032;
	public static final int ERROR_UNEXPORTED_STORED_DATA = -5033;
	public static final int ERROR_SIGNING_SYSTEM_OPERATION_DATA_FAILED = -5034;
	public static final int ERROR_USER_ID_NOT_MANAGED = -5035;
	public static final int ERROR_USER_ID_NOT_AUTHENTICATED = -5036;
	public static final int ERROR_DISABLE_SECURE_ELEMENT_FAILED = -5037;
	public static final int ERROR_CONFIG_VALUE_NOT_FOUND = -5038;
	public static final int ERROR_INVALID_CONFIG= -5039;
	public static final int ERROR_SUSPEND_SECURE_ELEMENT_FAILED = -5040;
    public static final int ERROR_UNSUSPEND_SECURE_ELEMENT_FAILED = -5041;
    public static final int ERROR_GET_OPEN_TRANSACTIONS_FAILED = -5042;
    public static final int ERROR_GET_LIFECYCLE_STATE_FAILED = -5043;
    public static final int ERROR_GET_TRANSACTION_COUNTER_FAILED = -5044;
    public static final int ERROR_GET_SIGNATURE_ALGORITHM_FAILED = -5045;
    public static final int ERROR_GET_SIGNATURE_COUNTER_FAILED = -5045;
    public static final int ERROR_GET_TOTAL_LOG_MEMORY = -5046;
    public static final int ERROR_GET_LOG_TIME_FORMAT = -5047;
    public static final int ERROR_EXPORT_PUBLIC_KEY_FAILED = -5048;
    public static final int ERROR_EXPORT_CERTIFICATE_FAILED = -5049;

    /// unsupported premium feature
    public static final int ERROR_UNSUPPORTED_PREMIUM_FEATURE = -6000;
}
