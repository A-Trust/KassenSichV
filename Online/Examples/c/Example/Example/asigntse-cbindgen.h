#ifdef __cplusplus
extern "C" {
#endif

#ifndef asigntse_h
#define asigntse_h


#define PARAM_BA(ba) ba, (uint32_t) sizeof(ba)
#define PARAM_STR(str) #str, (uint32_t) strlen(#str)
#define PARAM_STR2BA(str) (const uint8_t*)(#str), (uint32_t) strlen(#str)
#define PARAM_BUFFER(buf) &(buf), &(buf##Length)



#include <stdarg.h>
#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>

#define DEFAULT_TIMEOUT_VALUE 1500

#define DEFAULT_NUMBER_OF_RETRIES 1

#define EXECUTION_OK 0

#define ERROR_RETRIEVE_LOG_MESSAGE_FAILED -5001

#define ERROR_STORAGE_FAILURE -5002

#define ERROR_UPDATE_TIME_FAILED -5003

#define ERROR_PARAMETER_MISMATCH -5004

#define ERROR_ID_NOT_FOUND -5005

#define ERROR_TRANSACTION_NUMBER_NOT_FOUND -5006

#define ERROR_NO_DATA_AVAILABLE -5007

#define ERROR_TOO_MANY_RECORDS -5008

#define ERROR_START_TRANSACTION_FAILED -5009

#define ERROR_UPDATE_TRANSACTION_FAILED -5010

#define ERROR_FINISH_TRANSACTION_FAILED -5011

#define ERROR_RESTORE_FAILED -5012

#define ERROR_STORING_INIT_DATA_FAILED -5013

#define ERROR_EXPORT_CERT_FAILED -5014

#define ERROR_NO_LOG_MESSAGE -5015

#define ERROR_READING_LOG_MESSAGE -5016

#define ERROR_NO_TRANSACTION -5017

#define ERROR_SE_API_NOT_INITIALIZED -5018

#define ERROR_TIME_NOT_SET -5019

#define ERROR_CERTIFICATE_EXPIRED -5020

#define ERROR_SECURE_ELEMENT_DISABLED -5021

#define ERROR_USER_NOT_AUTHORIZED -5022

#define ERROR_USER_NOT_AUTHENTICATED -5023

#define ERROR_DESCRIPTION_NOT_SET_BY_MANUFACTURER -5024

#define ERROR_DESCRIPTION_SET_BY_MANUFACTURER -5025

#define ERROR_EXPORT_SERIAL_NUMBERS_FAILED -5026

#define ERROR_GET_MAX_NUMBER_OF_CLIENTS_FAILED -5027

#define ERROR_GET_CURRENT_NUMBER_OF_CLIENTS_FAILED -5028

#define ERROR_GET_MAX_NUMBER_TRANSACTIONS_FAILED -5029

#define ERROR_GET_CURRENT_NUMBER_OF_TRANSACTIONS_FAILED -5030

#define ERROR_GET_SUPPORTED_UPDATE_VARIANTS_FAILED -5031

#define ERROR_DELETE_STORED_DATA_FAILED -5032

#define ERROR_UNEXPORTED_STORED_DATA -5033

#define ERROR_SIGNING_SYSTEM_OPERATION_DATA_FAILED -5034

#define ERROR_USER_ID_NOT_MANAGED -5035

#define ERROR_USER_ID_NOT_AUTHENTICATED -5036

#define ERROR_DISABLE_SECURE_ELEMENT_FAILED -5037

#define ERROR_CONFIG_VALUE_NOT_FOUND -5038

#define ERROR_INVALID_CONFIG -5039

#define ERROR_UNSUSPEND_SECURE_ELEMENT_FAILED -5040

#define ERROR_SUSPEND_SECURE_ELEMENT_FAILED -5041

#define ERROR_GET_OPEN_TRANSACTIONS_FAILED -5042

#define ERROR_GET_LIFECYCLE_STATE_FAILED -5043

#define ERROR_GET_TRANSACTION_COUNTER_FAILED -5044

#define ERROR_GET_SIGNATURE_COUNTER_FAILED -5045

#define ERROR_GET_SIGNATURE_ALGORITHM_FAILED -5045

#define ERROR_GET_TOTAL_LOG_MEMORY -5046

#define ERROR_GET_LOG_TIME_FORMAT -5047

#define ERROR_EXPORT_PUBLIC_KEY_FAILED -5048

#define ERROR_EXPORT_CERTIFICATE_FAILED -5049

#define ERROR_TPM_CONNECT -5050

#define ERROR_INVALID_CLIENT_ID -5051

#define ERROR_CLIENT_ID_NOT_REGISTERED -5052

#define ERROR_CLIENT_ID_REGISTRATION_FAILED -5053

#define ERROR_CANNOT_RETRIEVE_REGISTERED_CLIENT_IDS -5054

#define ERROR_CORRUPTED_REGISTERED_CLIENT_IDS -5055

#define ERROR_CORRUPTED_APP_DATA -5056

#define ERROR_SET_PINS_FAILED -5057

#define ERROR_AUTHENTICATION_FAILED -4000

#define ERROR_UNBLOCK_FAILED -4001

/*
 a mandatory input parameter is NULL
 */
#define ERROR_MISSING_PARAMETER -3000

/*
 the function is not supported
 */
#define ERROR_FUNCTION_NOT_SUPPORTED -3001

/*
 connection error
 */
#define ERROR_IO -3002

/*
 timeout error
 */
#define ERROR_TSE_TIMEOUT -3003

/*
 the memory allocation for an output parameter failed
 */
#define ERROR_ALLOCATION_FAILED -3004

/*
 configuration file not found
 */
#define ERROR_CONFIG_FILE_NOT_FOUND -3005

/*
 transport I/O timeout error
 */
#define ERROR_SE_COMMUNICATION_FAILED -3006

/*
 invalid TSE command data
 */
#define ERROR_TSE_COMMAND_DATA_INVALID -3007

/*
 invalid TSE response data
 */
#define ERROR_TSE_RESPONSE_DATA_INVALID -3008

/*
 the serial number of an ERS is already mapped to a signature key
 */
#define ERROR_ERS_ALREADY_MAPPED -3009

/*
 unknown ERS
 */
#define ERROR_NO_ERS -3010

#define ERROR_TSE_UNKNOWN_ERROR -3011

#define ERROR_STREAM_WRITE -3012

#define ERROR_BUFFER_TOO_SMALL -3013

#define ERROR_NO_SUCH_KEY -3014

#define ERROR_NO_KEY -3015

#define ERROR_SE_API_DEACTIVATED -3016

#define ERROR_SE_API_NOT_DEACTIVATED -3017

#define ERROR_AT_LOAD_NOT_CALLED -3018

/*
 unknown error code
 */
#define ERROR_UNKNOWN -3100

/*
 unsupported premium feature
 */
#define ERROR_UNSUPPORTED_PREMIUM_FEATURE -6000

#define LIFECYCLESTATE_UNKNOWN 0

#define LIFECYCLESTATE_NOTINITIALIZED 1

#define LIFECYCLESTATE_ACTIVE 2

#define LIFECYCLESTATE_SUSPENDED 3

#define LIFECYCLESTATE_DISABLED 4

#define UPDATEVARIANTS_SIGNEDUPDATE 0

#define UPDATEVARIANTS_UNSIGNEDUPDATE 1

#define UPDATEVARIANTS_SIGNEDANDUNSIGNEDUPDATE 2

#define AUTHENTICATIONRESULT_AUTH_OK 0

#define AUTHENTICATIONRESULT_AUTH_FAILED 1

#define AUTHENTICATIONRESULT_AUTH_PINISBLOCKED 2

#define AUTHENTICATIONRESULT_AUTH_UNKNOWNUSERID 3

#define UNBLOCKRESULT_UNBL_OK 0

#define UNBLOCKRESULT_UNBL_FAILED 1

#define UNBLOCKRESULT_UNBL_UNKNOWNUSERID 2

#define UNBLOCKRESULT_UNBL_ERROR 3

#define PINSTATEFLAGS_STATEINITIALIZED 0

#define PINSTATEFLAGS_ADMINPINTRANSPORTSTATE 1

#define PINSTATEFLAGS_ADMINPUKTRANSPORTSTATE 2

#define PINSTATEFLAGS_TIMEADMINPINTRANSPORTSTATE 4

#define PINSTATEFLAGS_TIMEADMINPUKTRANSPORTSTATE 8

#define SYNCVARIANTS_NOINPUT 0

#define SYNCVARIANTS_UTCTIME 1

#define SYNCVARIANTS_GENERALIZEDTIME 2

#define SYNCVARIANTS_UNIXTIME 3

typedef struct TssType TssType;

/*
Represents the Life Cycle State of the TSE.
The value Unknown indicates an unknown Life Cycle State.
The value NotInitialized indicates that no initialize function, that is either
 @ref initializeDescriptionNotSet or @ref initializeDescriptionSet, has been called yet.
The value Active indicates that the TSE is ready for transactions.
The value Suspended indicates that @ref at_suspendSecureElement has been called.
The value Disabled indicates that @ref disableSecureElement has been called.

    Unknown        = 0
    NotInitialized = 1
    Active         = 2
    Suspended      = 3
    Disabled       = 4
 */
typedef int32_t LifecycleState;

/*
 Represents the variants that are supported by the Secure Element to update transactions.
    	signedUpdate					= 0
    	unsignedUpdate					= 1
    	signedAndUnsignedUpdate			= 2
 */
typedef int32_t UpdateVariants;

/*
 Represents the result of an authentication attempt.
 The value auth_ok indicates that the authentication has been successful.
 The value auth_failed indicates that the authentication has failed.
 The value auth_pinIsBlocked indicates that the PIN entry for the userId was blocked before the authentication attempt.
 The value auth_unknownUserId indicates that the passed userId is not managed by the SE API.
    	auth_ok					= 0
    	auth_failed				= 1
    	auth_pinIsBlocked		= 2
    	auth_unknownUserId		= 3
 */
typedef int32_t AuthenticationResult;

/*
 Represents the result of the unblock process.
 The value ok indicates that the unblocking has been successful.
 The value failed indicates that the unblocking has failed.
 The value unknownUserId indicates that the passed userId is not managed by the SE API.
 The value error indicates that an error has occurred during the execution of the function unblockUser.

    ok 		        = 0
    failed		        = 1
    pinBlocked 		= 2
    unknownUserId 		= 3
    error              = 4
 */
typedef int32_t UnblockResult;

#define AsignTseOnline 0

extern int32_t at_init(void);

extern int32_t _at_initSW(void);

extern int32_t _at_shutdown(void);

extern int32_t at_set(uint32_t index,
                      const uint8_t *pwd,
                      uint32_t pwdLen,
                      const uint8_t *data,
                      uint32_t dataLen);

extern int32_t at_clearSlot(uint32_t index);

extern int32_t at_isEmpty(uint32_t index);

extern int32_t at_getSize(uint32_t index, const uint8_t *pwd, uint32_t pwdLen, uint32_t *dataLen);

extern int32_t at_get(uint32_t index,
                      const uint8_t *pwd,
                      uint32_t pwdLen,
                      uint8_t *data,
                      uint32_t *dataLen);

extern int32_t at_sha256(const uint8_t *data,
                         uint32_t dataLen,
                         uint8_t *hashData,
                         uint32_t *hashDataLen);

extern int32_t at_random(uint32_t numBytes, uint8_t *randomBytes);

extern int32_t at_createCounter(uint32_t index, const uint8_t *pwd, uint32_t pwdLen);

extern int32_t at_increment(uint32_t index,
                            const uint8_t *pwd,
                            uint32_t pwdLen,
                            uint8_t *data,
                            uint32_t *dataLen);

extern int32_t at_findEmptySlotBlock(uint32_t searchMinIndex,
                                     uint32_t searchMaxIndex,
                                     uint32_t numFreeSlotsToFind,
                                     uint32_t *startIndex);

/*
 Gets the lifecycle state of the TSE.

         @param[out] state
             Life cycle state of TSE. [REQUIRED]

         If the execution of the function has failed, the appropriate error code is returned:
 @return @ref ERROR_SE_COMMUNICATION_FAILED    Secure Element communication failed
             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_TIME_NOT_SET
                the managed data/time in the Secure Element has not been updated after the initialization of the SE API
                or a period of absence of current for the Secure Element
 */
int32_t at_getLifecycleState(LifecycleState *state);

/*
see @ref at_getLifecycleState
 */
int32_t at_getLifecycleStateWithTse(LifecycleState *state,
                                    const int8_t *tseId,
                                    uint32_t tseIdLength);

/*
Unsuspends the secure element after a suspend operation. If this operation is successful the Life Cycle State will be set to Active.

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
 */
int32_t at_unsuspendSecureElement(void);

/*
see @ref at_unsuspendSecureElement
 */
int32_t at_unsuspendSecureElementWithTse(const int8_t *tseId, uint32_t tseIdLength);

/*
Temporarily suspends the secure element. If this operation is successful the lifecycle state will be set to Suspended.
             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
 */
int32_t at_suspendSecureElement(void);

/*
see @ref at_suspendSecureElement
 */
int32_t at_suspendSecureElementWithTse(const int8_t *tseId, uint32_t tseIdLength);

/*
 Export the certificate signing transaction logs.
 @param[out] cert
                array that contains the requested certificate. [REQUIRED]
                If successfully executed, the buffer has to freed by the function caller using the function @ref at_free.
 @param[out] certLength
                length of the array that represents the cert. [REQUIRED]

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
 */
int32_t at_getCertificate(uint8_t **cert,
                          uint32_t *certLength);

/*
see @ref at_getCertificate
 */
int32_t at_getCertificateWithTse(uint8_t **cert,
                                 uint32_t *certLength,
                                 const int8_t *tseId,
                                 uint32_t tseIdLength);

/*
 Export public key ot the certificate signing transaction logs.
 @param[out] publicKey
                Array that contains the requested public key. [REQUIRED]
                If successfully executed, the buffer has to freed by the function caller using the function @ref at_free.
 @param[out] publicKeyLength
                length of the array that represents the exportedPublicKey [REQUIRED]

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
 */
int32_t at_getPublicKey(uint8_t **pubKey,
                        uint32_t *pubKeyLength);

/*
see @ref at_getPublicKey
 */
int32_t at_getPublicKeyWithTse(uint8_t **pubKey,
                               uint32_t *pubKeyLength,
                               const int8_t *tseId,
                               uint32_t tseIdLength);

/*
 Get a list of dangling (open) SE transactions.
 @param[out] transactionNumbers
                Array that represents the list of transactions. [REQUIRED]
                If successfully executed, the buffer has to freed by the function caller using the function @ref at_free.
 @param[out] transactionNumbersLength
                Length of the array that represents the list of transactions.

 @return @ref EXECUTION_OK                                execution of the function has been successful
 @return @ref ERROR_AT_LOAD_NOT_CALLED                    @ref at_load has not been called
 @return @ref ERROR_SE_API_NOT_INITIALIZED                the SE API has not been initialized
 @return @ref ERROR_TIME_NOT_SET                          the managed data/time in the Secure Element has not been updated after the initialization of the SE API
                                                          or a period of absence of current for the Secure Element
 @return @ref ERROR_SECURE_ELEMENT_DISABLED               the Secure Element has been disabled

 */
int32_t at_getOpenTransactions(uint32_t **transactionNumbers,
                               uint32_t *transactionNumbersLength);

/*
see @ref at_getOpenTransactions
 */
int32_t at_getOpenTransactionsWithTse(uint32_t **transactionNumbers,
                                      uint32_t *transactionNumbersLength,
                                      const int8_t *tseId,
                                      uint32_t tseIdLength);

/*
 Supplies the current transaction counter (last used value).
 @param[out] transactionCounter
                The current transaction counter. [REQUIRED]

 @return @ref EXECUTION_OK                                  execution of the function has been successful
 @return @ref ERROR_AT_LOAD_NOT_CALLED                    @ref at_load has not been called
 @return @ref ERROR_SE_API_NOT_INITIALIZED                     the SE API has not been initialized
 @return @ref ERROR_SECURE_ELEMENT_DISABLED                   the Secure Element has been disabled
 */
int32_t at_getTransactionCounter(uint32_t *counter);

/*
see @ref at_getTransactionCounter
 */
int32_t at_getTransactionCounterWithTse(uint32_t *counter,
                                        const int8_t *tseId,
                                        uint32_t tseIdLength);

/*
 Supplies the current signature counter (last used value) for the certificate signing transaction logs.
 @param[out] signatureCounter
                The current signature counter. [REQUIRED]

 @return @ref EXECUTION_OK                                  execution of the function has been successful
 @return @ref ERROR_AT_LOAD_NOT_CALLED                       @ref at_load has not been called
 @return @ref ERROR_SE_API_NOT_INITIALIZED                     the SE API has not been initialized
 @return @ref ERROR_SECURE_ELEMENT_DISABLED                   the Secure Element has been disabled
 */
int32_t at_getSignatureCounter(uint32_t *counter);

/*
see @ref at_getSignatureCounter
 */
int32_t at_getSignatureCounterWithTse(uint32_t *counter, const int8_t *tseId, uint32_t tseIdLength);

/*
 Supplies the friendly name of the ASN.1 encoded signature algorithm OID encoded into signed data.
 @param[out] signatureAlgorithm
                Buffer for signature algorithm OID. [REQUIRED]
                If successfully executed, the buffer has to freed by the function caller using the function @ref at_free.
 @param[out] signatureAlgorithmLength
                Length of the signature algorithm OID buffer. [REQUIRED]

 @return @ref EXECUTION_OK                                  execution of the function has been successful
 @return @ref ERROR_AT_LOAD_NOT_CALLED                       @ref at_load has not been called
 @return @ref ERROR_SE_API_NOT_INITIALIZED                     the SE API has not been initialized
 @return @ref ERROR_SECURE_ELEMENT_DISABLED                   the Secure Element has been disabled
 */
int32_t at_getSignatureAlgorithm(int8_t **signatureAlgorithm,
                                 uint32_t *signatureAlgorithmLength);

/*
see @ref at_getSignatureAlgorithm
 */
int32_t at_getSignatureAlgorithmWithTse(int8_t **signatureAlgorithm,
                                        uint32_t *signatureAlgorithmLength,
                                        const int8_t *tseId,
                                        uint32_t tseIdLength);

/*
 Shows the the log time format of the underlying TSE library.
 @param[out] logTimeFormat
                Buffer for log time format information string. [REQUIRED]
                If successfully executed, the buffer has to freed by the function caller using the function @ref at_free.
 @param[out] logTimeFormatLength
                length of the log time format string buffer [REQUIRED]

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
 */
int32_t at_getLogTimeFormat(int8_t **logTimeFormat,
                            uint32_t *logTimeFormatLength);

/*
see @ref at_getLogTimeFormat
 */
int32_t at_getLogTimeFormatWithTse(int8_t **logTimeFormat,
                                   uint32_t *logTimeFormatLength,
                                   const int8_t *tseId,
                                   uint32_t tseIdLength);

/*
 Shows version number of the underlying TSE library.
 @param[out] version
                Buffer for version information string. [REQUIRED]
                If successfully executed, the buffer has to freed by the function caller using the function @ref at_free.
 @param[out] versionLength
                length of the version string buffer [REQUIRED]

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
 */
int32_t at_getVersion(int8_t **version,
                      uint32_t *versionLength);

/*
 Shows version details of the underlying TSE library.
 @param[out] versionDetails
                Buffer for version details information string. [REQUIRED]
                If successfully executed, the buffer has to freed by the function caller using the function @ref at_free.
 @param[out] versionDetailsLength
                length of the version details string buffer [REQUIRED]

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
 */
int32_t at_getVersionDetails(int8_t **versionDetails,
                             uint32_t *versionDetailsLength);

/*
 Gets the serial number (SHA256 hash of public key) of the transaction log signing key.
 @param[out] serial
                Buffer for serial number bytes. [REQUIRED]
                If successfully executed, the buffer has to freed by the function caller using the function @ref at_free.
 @param[out] serialLength
                Length of the serial number buffer. [REQUIRED]

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
 */
int32_t at_getSerialNumber(uint8_t **serial,
                           uint32_t *serialLength);

/*
see @ref at_getSerialNumber
 */
int32_t at_getSerialNumberWithTse(uint8_t **serial,
                                  uint32_t *serialLength,
                                  const int8_t *tseId,
                                  uint32_t tseIdLength);

/*
 Loads the underlying TSE library.

             ERROR_TPM_CONNECTION
                TPM connection failed
 */
int32_t at_load(void);

/*
 Unloads the underlying TSE library.

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
 */
int32_t at_unload(void);

/*
 Frees memory allocated by the TSE library.
 @param[in] ptr
                the pointer to free [REQUIRED]
 */
void at_free(void **ptr);

/*
 DEPRECATED (use at_free instead)
 Frees memory allocated by the TSE library
 @param[in] ptr
                the pointer to free [REQUIRED]
 */
void asigntse_free(void **ptr);

/*
 Registers a client ID.
 @param[in] clientId
                represents the ID of the application to be registered.
                It has to be of maximum length 25 and must not contain
                the following characters: / , < , > , : , " , \ , | , ? , *  [REQUIRED]
 @param[in] clientIdLength
                length of the clientId  [REQUIRED]

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_STORAGE_FAILURE
                some storage related error occured
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
             ERROR_TIME_NOT_SET
                the managed data/time in the Secure Element has not been updated after the initialization of the SE API
                or a period of absence of current for the Secure Element
             ERROR_SECURE_ELEMENT_DISABLED
                the Secure Element has been disabled
             ERROR_INVALID_CLIENT_ID
                clientId contains invalid characters or has invalid length
             ERROR_CLIENT_ID_REGISTRATION_FAILED
                clientId registration has failed
             ERROR_CORRUPTED_REGISTERED_CLIENT_IDS
                clientId registration failed due to corrupted registered clientIds data
             ERROR_CORRUPTED_APP_DATA
                clientId registration failed due to corrupted app data
 */
int32_t at_registerClientId(const int8_t *clientId,
                            uint32_t clientIdLength);

/*
see @ref at_registerClientId
 */
int32_t at_registerClientIdWithTse(const int8_t *clientId,
                                   uint32_t clientIdLength,
                                   const int8_t *tseId,
                                   uint32_t tseIdLength);

int32_t at_setPins(const uint8_t *adminPin,
                   uint32_t adminPinLength,
                   const uint8_t *adminPuk,
                   uint32_t adminPukLength);

int32_t at_setPinsWithTse(const uint8_t *adminPin,
                          uint32_t adminPinLength,
                          const uint8_t *adminPuk,
                          uint32_t adminPukLength,
                          const int8_t *tseId,
                          uint32_t tseIdLength);

/*
Set path to config file.
Note that this function has to be called before the library loads,
in order to have an effect.
 @param[in] path
                the path to the config file  [REQUIRED]
 @param[in] pathLength
                the length of the path  [REQUIRED]
             ERROR_INVALID_CONFIG
                the config file path does not exist
 */
int32_t cfgSetConfigFile(const int8_t *path, uint32_t pathLength);

/*
Add Tse config section.
 @param[in] tseID
                the ID of the Tse to add, that is the name of the config section  [REQUIRED]
 @param[in] tseIDLength
                the length of the ID of the Tse to add  [REQUIRED]
 @param[in] tseType
                the type of the Tse to add  [REQUIRED]
 @param[in] connParam
                the connection string  [REQUIRED]
 @param[in] connParamLength
                the length of the connection string  [REQUIRED]
 @param[in] atrustTseID
                the A-Trust ID of the Tse
 @param[in] atrustTseIDLength
                the length of the A-Trust ID of the Tse
 @param[in] atrustApiKey
                the A-Trust API key
 @param[in] atrustApiKeyLength
                the length of the A-Trust API key
 @param[in] timeAdminID
                the ID of the time admin
 @param[in] timeAdminIDLength
                the length of the ID of the time admin
 @param[in] timeAdminPwd
                the password of the time admin
 @param[in] timeAdminPwdLength
                the length of the password of the time admin
             ERROR_INVALID_CONFIG
                the config is invalid
 */
int32_t cfgTseAdd(const int8_t *tseID,
                  uint32_t tseIDLength,
                  uint32_t tseType,
                  const int8_t *connParam,
                  uint32_t connParamLength,
                  const int8_t *atrustTseID,
                  uint32_t atrustTseIDLength,
                  const int8_t *atrustApiKey,
                  uint32_t atrustApiKeyLength,
                  const int8_t *timeAdminID,
                  uint32_t timeAdminIDLength,
                  const int8_t *timeAdminPwd,
                  uint32_t timeAdminPwdLength);

/*
Remove Tse config section.
 @param[in] tseID
                the ID of the Tse to remove, that is the name of the config section [REQUIRED]
 @param[in] tseIDLength
                the length of the ID of the Tse to remove [REQUIRED]
             ERROR_CONFIG_VALUE_NOT_FOUND
                the config section could not be found
 */
int32_t cfgTseRemove(const int8_t *tseID, uint32_t tseIDLength);

/*
Enable logging.
Note that this function has to be called before the library loads,
in order to have an effect.
 @param[in] enabled
                the switch to enable or disable logging [REQUIRED]
 */
int32_t cfgSetLoggingEnabled(bool enabled);

/*
Enable logging to stderr.
Note that this function has to be called before the library loads,
in order to have an effect.
 @param[in] enabled
                the switch to enable or disable logging to stderr [REQUIRED]
 */
int32_t cfgSetLoggingStderr(bool enabled);

/*
Enable logging to a logfile.
Note that this function has to be called before the library loads,
in order to have an effect.
 @param[in] enabled
                the switch to enable or disable logging to file [REQUIRED]
 */
int32_t cfgSetLoggingFile(bool enabled);

/*
Set logfile target directory.
Note that this function has to be called before the library loads,
in order to have an effect.
 @param[in] path
                the path to the log directory [REQUIRED]
 @param[in] pathLength
                the length of the path to the log directory [REQUIRED]
 */
int32_t cfgSetLogDir(const int8_t *path, uint32_t pathLength);

/*
Set verbosity level of the logger.
Note that this function has to be called before the library loads,
in order to have an effect.
 @param[in] logLevel
                the log level [REQUIRED]
 @param[in] pathLength
                the length of the log level [REQUIRED]
 */
int32_t cfgSetLogLevel(const int8_t *logLevel, uint32_t logLevelLength);

/*
Makes the logger append to the specified output file, if it exists already; by default, the file would be truncated.
Note that this function has to be called before the library loads,
in order to have an effect.
 @param[in] enabled
                the switch to enable or disable appending [REQUIRED]
 */
int32_t cfgSetLogAppend(bool enabled);

/*
Enable a colored version of the logline-formatter.
Note that this function has to be called before the library loads,
in order to have an effect.
 @param[in] enabled
                the switch to enable or disable a colored version of the logline-formatter [REQUIRED]
 */
int32_t cfgSetLogColors(bool enabled);

/*
Enable more detailed log lines.
Note that this function has to be called before the library loads,
in order to have an effect.
 @param[in] enabled
                the switch to enable or disable more detailed log lines [REQUIRED]
 */
int32_t cfgSetLogDetails(bool enabled);

/*
Enable a colored version of the logline-formatter for stderr logging.
Note that this function has to be called before the library loads,
in order to have an effect.
 @param[in] enabled
                the switch to enable or disable a colored version of the logline-formatter for stderr logging [REQUIRED]
 */
int32_t cfgSetLogStderrColors(bool enabled);

/*
Set the http proxy.
 @param[in] proxyUrl
                the proxy url [REQUIRED]
 @param[in] proxyUrlLength
                the length of the proxy url [REQUIRED]
 */
int32_t cfgSetHttpProxy(const int8_t *proxyUrl, uint32_t proxyUrlLength);

/*
Set the http proxy with username and password.
 @param[in] proxyUrl
                the proxy url [REQUIRED]
 @param[in] proxyUrlLength
                the length of the proxy url [REQUIRED]
 @param[in] proxyUsername
                the proxy user name [REQUIRED]
 @param[in] proxyUsernameLength
                the length of the proxy user name [REQUIRED]
 @param[in] proxyPassword
                the proxy password [REQUIRED]
 @param[in] proxyPasswordLength
                the length of the proxy password [REQUIRED]
 */
int32_t cfgSetHttpProxyWithUsernameAndPassword(const int8_t *proxyUrl,
                                               uint32_t proxyUrlLength,
                                               const int8_t *proxyUsername,
                                               uint32_t proxyUsernameLength,
                                               const int8_t *proxyPassword,
                                               uint32_t proxyPasswordLength);

/*
Set the maximum time a request is allowed to take.
 @param[in] timeout
                the amount of milliseconds a request is allowed to take. [REQUIRED]
 */
int32_t cfgSetTimeout(uint64_t timeout);

/*
Set the number of allowed retries for a request.
 @param[in] retries
                the number of retries allowed for a request. [REQUIRED]
 */
int32_t cfgSetRetries(uint64_t retries);

/*
 The function initializeDescriptionNotSet starts the initialization of the SE API by the operator of the corresponding application.
 The initialization data in form of the description of the SE API is passed by the input parameter description.
 The description of the SE API MUST NOT have been set by the manufacturer.
 @param[in] description
                short description of the SE API.
                The parameter only be used if the description of the SE API has not been set by the manufacturer [REQUIRED].
 @param[in] descriptionLength
                length of the string representing the description of the SE API [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_SIGNING_SYSTEM_OPERATION_DATA_FAILED
                determination of the log message parts for the system operation data by the Secure Element failed
             ERROR_STORING_INIT_DATA_FAILED
                storing of the data for the description of the SE API failed
             ERROR_RETRIEVE_LOG_MESSAGE_FAILED
                execution of the Secure Element functionality to retrieve log message parts has failed
             ERROR_STORAGE_FAILURE
                storing of the log message has failed
             ERROR_CERTIFICATE_EXPIRED
                the certificate with the public key for the verification of the appropriate type of log messages is expired.
                Even if a certificate expired, the log message parts are created by the Secure Element and stored by the SE API.
             ERROR_SECURE_ELEMENT_DISABLED
                the Secure Element has been disabled
             ERROR_USER_NOT_AUTHORIZED
                the user who has invoked the function initializeDescriptionNotSet is not authorized to execute this function
             ERROR_USER_NOT_AUTHENTICATED
                the user who has invoked the function initializeDescriptionNotSet has not the status authenticated
             ERROR_DESCRIPTION_SET_BY_MANUFACTURER
                the function initializeDescriptionNotSet has been invoked with a value for the input parameter description
                although the description of the SE API has been set by the manufacturer
 */
int32_t initializeDescriptionNotSet(int8_t *description,
                                    uint32_t descriptionLength);

/*
see @ref initializeDescriptionNotSet
 */
int32_t initializeDescriptionNotSetWithTse(int8_t *description,
                                           uint32_t descriptionLength,
                                           const int8_t *tseId,
                                           uint32_t tseIdLength);

/*
 The function initializeDescriptionSet starts the initialization of the SE API by the operator of the corresponding application.
 The description of the SE API is set by the manufacturer.
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_SIGNING_SYSTEM_OPERATION_DATA_FAILED
                determination of the log message parts for the system operation data by the Secure Element failed
             ERROR_RETRIEVE_LOG_MESSAGE_FAILED
                execution of the Secure Element functionality to retrieve log message parts has failed
             ERROR_STORAGE_FAILURE
                storing of the log message has failed
             ERROR_CERTIFICATE_EXPIRED
                the certificate with the public key for the verification of the appropriate type of log messages is expired.
                Even if a certificate expired, the log message parts are created by the Secure Element and stored by the SE API.
             ERROR_SECURE_ELEMENT_DISABLED
                the Secure Element has been disabled
             ERROR_USER_NOT_AUTHORIZED
                the user who has invoked the function initializeDescriptionSet is not authorized to execute this function
             ERROR_USER_NOT_AUTHENTICATED
                the user who has invoked the function initializeDescriptionSet has not the status authenticated
             ERROR_DESCRIPTION_NOT_SET_BY_MANUFACTURER
                the function initializeDescriptionSet has been invoked without a value for the input parameter
                description although the description of the SE API has not been set by the manufacturer
 */
int32_t initializeDescriptionSet(void);

/*
see @ref initializeDescriptionSet
 */
int32_t initializeDescriptionSetWithTse(const int8_t *tseId, uint32_t tseIdLength);

/*
 The function updateTime updates the current date/time that is maintained by the Secure Element by passing a new date/time value
 @param[in] newDateTime
                new value for the date/time maintained by the Secure Element [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_UPDATE_TIME_FAILED
                execution of the Secure Element functionality to set the time has failed
             ERROR_RETRIEVE_LOG_MESSAGE_FAILED
                execution of the Secure Element functionality to retrieve log message parts has failed
             ERROR_STORAGE_FAILURE
                storing of the log message has failed
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
             ERROR_CERTIFICATE_EXPIRED
                the certificate with the public key for the verification of the appropriate type of log messages is expired.
                Even if a certificate expired, the log message parts are created by the Secure Element and stored by the SE API.
             ERROR_SECURE_ELEMENT_DISABLED
                the Secure Element has been disabled
             ERROR_USER_NOT_AUTHORIZED
                the user who has invoked the function updateTime is not authorized to execute this function
             ERROR_USER_NOT_AUTHENTICATED
                the user who has invoked the function updateTime has not the status authenticated
 */
int32_t updateTime(int64_t newDateTime);

/*
see @ref updateTime
 */
int32_t updateTimeWithTse(int64_t newDateTime, const int8_t *tseId, uint32_t tseIdLength);

/*
 The function updateTimeWithTimeSync updates the current date/time that is maintained by the Secure Element by
 using the functionality for time synchronization of the Secure Element
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_UPDATE_TIME_FAILED
                execution of the Secure Element functionality to update the time has failed
             ERROR_RETRIEVE_LOG_MESSAGE_FAILED
                execution of the Secure Element functionality to retrieve log message parts has failed
             ERROR_STORAGE_FAILURE
                storing of the log message has failed
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
             ERROR_CERTIFICATE_EXPIRED
                the certificate with the public key for the verification of the appropriate type of log messages is expired.
                Even if a certificate expired, the log message parts are created by the Secure Element and stored by the SE API.
             ERROR_SECURE_ELEMENT_DISABLED
                the Secure Element has been disabled
             ERROR_USER_NOT_AUTHORIZED
                the user who has invoked the function updateTimeWithTimeSync is not authorized to execute this function
             ERROR_USER_NOT_AUTHENTICATED
                the user who has invoked the function updateTimeWithTimeSync has not the status authenticated

 */
int32_t updateTimeWithTimeSync(void);

/*
see @ref updateTimeWithTimeSync
 */
int32_t updateTimeWithTimeSyncWithTse(const int8_t *tseId, uint32_t tseIdLength);

/*
 The function disableSecureElement disables the Secure Element in a way that none of its functionality can be used anymore
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_DISABLE_SECURE_ELEMENT_FAILED
                the deactivation of the Secure Element failed
             ERROR_TIME_NOT_SET
                the managed data/time in the Secure Element has not been updated after the initialization of the SE API
                or a period of absence of current for the Secure Element
             ERROR_RETRIEVE_LOG_MESSAGE_FAILED
                execution of the Secure Element functionality to retrieve log message parts has failed
             ERROR_STORAGE_FAILURE
                storing of the data of the log message has failed
             ERROR_CERTIFICATE_EXPIRED
                the certificate with the public key for the verification of the appropriate type of log messages is expired.
                Even if a certificate expired, the log message parts are created by the Secure Element and stored by the SE API.
             ERROR_SECURE_ELEMENT_DISABLED
                the Secure Element has been disabled
             ERROR_USER_NOT_AUTHORIZED
                the user who has invoked the function disableSecureElement is not authorized to execute this function
             ERROR_USER_NOT_AUTHENTICATED
                the user who has invoked the function disableSecureElement has not the status authenticated
 */
int32_t disableSecureElement(void);

/*
see @ref disableSecureElement
 */
int32_t disableSecureElementWithTse(const int8_t *tseId, uint32_t tseIdLength);

/*
 Starts a new transaction.
 @param[in] clientId
                represents the ID of the application that has invoked the function [REQUIRED]
 @param[in] clientIdLength
                length of the array that represents the clientId  [REQUIRED]
 @param[in] processData
                represents all the necessary information regarding the initial state of the process [REQUIRED]
 @param[in] processDataLength
                length of the array that represents the processData [REQUIRED]
 @param[in] processType
                identifies the type of the transaction as defined by the application.
                This string MUST NOT contain more than 100 characters [OPTIONAL]
 @param[in] processTypeLength
                length of the string that represents the processType.
                If a null pointer is passed for the parameter processType, the value for the length is 0 [REQUIRED]
 @param[in] additionalData
                reserved for future use [OPTIONAL]
 @param[in] additionalDataLength
                length of the array that represents the additionalData.
                If a null pointer is passed for the parameter additionalData, the value for the length is 0 [REQUIRED]
 @param[out] transactionNumber
                represents a transaction number that has been assigned by the Secure Element to the process [REQUIRED]
 @param[out] logTime
                represents the point in time of the Secure Element when the log message was created [REQUIRED]
 @param[out] serialNumber
                represents hash value over the public key of the key pair that is used for the creation of signature values
                in transaction log messages [REQUIRED]
 @param[out] serialNumberLength
                length of the array that represents the serialNumber [REQUIRED]
 @param[out] signatureCounter
                represents the current value of the signature counter [REQUIRED]
 @param[out] signatureValue
                represents the signature value [OPTIONAL]
 @param[out] signatureValueLength
                length of the array that represents the signature value
                if a value for the output parameter signatureValue is returned [OPTIONAL]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_START_TRANSACTION_FAILED
                the execution of the Secure Element functionality to start a transaction failed
             ERROR_RETRIEVE_LOG_MESSAGE_FAILED
                the execution of the Secure Element functionality to retrieve log message parts has failed
             ERROR_STORAGE_FAILURE
                storing of the log message failed
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
             ERROR_TIME_NOT_SET
                the managed data/time in the Secure Element has not been updated after the initialization of the SE API
                or a period of absence of current for the Secure Element
             ERROR_CERTIFICATE_EXPIRED
                the certificate with the public key for the verification of the appropriate type of log messages is expired.
                Even if a certificate expired, the log message parts are created by the Secure Element and stored by the SE API.
             ERROR_SECURE_ELEMENT_DISABLED
                the Secure Element has been disabled
             ERROR_CLIENT_ID_NOT_REGISTERED
                clientId is not registered
 */
int32_t startTransaction(const int8_t *clientId,
                         uint32_t clientIdLength,
                         const uint8_t *processData,
                         uint32_t processDataLength,
                         const int8_t *processType,
                         uint32_t processTypeLength,
                         const uint8_t *additionalData,
                         uint32_t additionalDataLength,
                         uint32_t *transactionNumber,
                         int64_t *logTime,
                         uint8_t **serialNumber,
                         uint32_t *serialNumberLength,
                         uint32_t *signatureCounter,
                         uint8_t **signatureValue,
                         uint32_t *signatureValueLength);

/*
see @ref startTransaction
 */
int32_t startTransactionWithTse(const int8_t *clientId,
                                uint32_t clientIdLength,
                                const uint8_t *processData,
                                uint32_t processDataLength,
                                const int8_t *processType,
                                uint32_t processTypeLength,
                                const uint8_t *additionalData,
                                uint32_t additionalDataLength,
                                uint32_t *transactionNumber,
                                int64_t *logTime,
                                uint8_t **serialNumber,
                                uint32_t *serialNumberLength,
                                uint32_t *signatureCounter,
                                uint8_t **signatureValue,
                                uint32_t *signatureValueLength,
                                const int8_t *tseId,
                                uint32_t tseIdLength);

/*
 Updates an open transaction.
 @param[in] clientId
                represents the ID of the application that has invoked the function [REQUIRED]
 @param[in] clientIdLength
                length of the array that represents the clientId  [REQUIRED]
 @param[in] transactionNumber
                parameter is used to unambiguously identify the current transaction [REQUIRED]
 @param[in] processData
                represents all the new information regarding the state of the process since the start
                of the corresponding transaction or its last update [REQUIRED]
 @param[in] processDataLength
                length of the array that represents the processData [REQUIRED]
 @param[in] processType
                identifies the type of the transaction as defined by the application.
                This string MUST NOT contain more than 100 characters [OPTIONAL]
 @param[in] processTypeLength
                length of the string that represents the processType.
                If a null pointer is passed for the parameter processType, the value for the length is 0 [REQUIRED]
 @param[out] logTime
                represents the point in time of the Secure Element when the log message was created [CONDITIONAL]
 @param[out] signatureValue
                represents the signature value [CONDITIONAL]
 @param[out] signatureValueLength
                length of the array that represents the signature value if a value for the
                output parameter signatureValue is returned [CONDITIONAL]
 @param[out] signatureCounter
                represents the current value of the signature counter [CONDITIONAL]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_UPDATE_TRANSACTION_FAILED
                the execution of the Secure Element functionality to update a transaction failed
             ERROR_RETRIEVE_LOG_MESSAGE_FAILED
                the execution of the Secure Element functionality to retrieve log message parts has failed
             ERROR_STORAGE_FAILURE
                storing of the log message failed
             ERROR_NO_TRANSACTION
                no transaction is known to be open under the provided transaction number
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
             ERROR_TIME_NOT_SET
                the managed data/time in the Secure Element has not been updated after the initialization of the SE API
                or a period of absence of current for the Secure Element
             ERROR_CERTIFICATE_EXPIRED
                the certificate with the public key for the verification of the appropriate type of log messages is expired.
                Even if a certificate expired, the log message parts are created by the Secure Element and stored by the SE API.
             ERROR_SECURE_ELEMENT_DISABLED
                the Secure Element has been disabled
             ERROR_CLIENT_ID_NOT_REGISTERED
                clientId is not registered

 */
int32_t updateTransaction(const int8_t *clientId,
                          uint32_t clientIdLength,
                          uint32_t transactionNumber,
                          const uint8_t *processData,
                          uint32_t processDataLength,
                          const int8_t *processType,
                          uint32_t processTypeLength,
                          int64_t *logTime,
                          uint8_t **signatureValue,
                          uint32_t *signatureValueLength,
                          uint32_t *signatureCounter);

/*
see @ref updateTransaction
 */
int32_t updateTransactionWithTse(const int8_t *clientId,
                                 uint32_t clientIdLength,
                                 uint32_t transactionNumber,
                                 const uint8_t *processData,
                                 uint32_t processDataLength,
                                 const int8_t *processType,
                                 uint32_t processTypeLength,
                                 int64_t *logTime,
                                 uint8_t **signatureValue,
                                 uint32_t *signatureValueLength,
                                 uint32_t *signatureCounter,
                                 const int8_t *tseId,
                                 uint32_t tseIdLength);

/*
 Finishes an open transaction.
 @param[in] clientId
                 represents the ID of the application that has invoked the function [REQUIRED]
 @param[in] clientIdLength
                 length of the array that represents the clientId  [REQUIRED]
 @param[in] transactionNumber
                 parameter is used to unambiguously identify the current transaction [REQUIRED]
 @param[in] processData
                 represents all the information regarding the final state of the process [REQUIRED]
 @param[in] processDataLength
                 length of the array that represents the processData [REQUIRED]
 @param[in] processType
                 identifies the type of the transaction as defined by the application
                 This string MUST NOT contain more than 100 characters [OPTIONAL]
 @param[in] processTypeLength
                 length of the string that represents the processType.
                 If a null pointer is passed for the parameter processType, the value for the length is 0 [REQUIRED]
 @param[in] additionalData   reserved for future use [OPTIONAL]
 @param[in] additionalDataLength
                 length of the array that represents the additionalData.
                 If a null pointer is passed for the parameter additionalData, the value for the length is 0 [REQUIRED]
 @param[out] logTime
                 represents the point in time of the Secure Element when the log message was created [REQUIRED]
 @param[out] signatureValue
                 represents the signature value [OPTIONAL]
 @param[out] signatureValueLength
                 length of the array that represents the signature value if a value for the
                 output parameter signatureValue is returned [OPTIONAL]
 @param[out] signatureCounter
                 represents the current value of the signature counter [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_FINISH_TRANSACTION_FAILED
                the execution of the Secure Element functionality to finish a transaction failed
             ERROR_RETRIEVE_LOG_MESSAGE_FAILED
                the execution of the Secure Element functionality to retrieve log message parts has failed
             ERROR_STORAGE_FAILURE
                storing of the log message failed
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
             ERROR_TIME_NOT_SET
                the managed data/time in the Secure Element has not been updated after the initialization
                of the SE API or a period of absence of current for the Secure Element
             ERROR_CERTIFICATE_EXPIRED
                the certificate with the public key for the verification of the appropriate type of log messages is expired.
                Even if a certificate expired, the log message parts are created by the Secure Element and stored by the SE API.
             ERROR_SECURE_ELEMENT_DISABLED   the Secure Element has been disabled
             ERROR_CLIENT_ID_NOT_REGISTERED
                clientId is not registered
 */
int32_t finishTransaction(const int8_t *clientId,
                          uint32_t clientIdLength,
                          uint32_t transactionNumber,
                          const uint8_t *processData,
                          uint32_t processDataLength,
                          const int8_t *processType,
                          uint32_t processTypeLength,
                          const uint8_t *additionalData,
                          uint32_t additionalDataLength,
                          int64_t *logTime,
                          uint8_t **signatureValue,
                          uint32_t *signatureValueLength,
                          uint32_t *signatureCounter);

/*
see @ref finishTransaction
 */
int32_t finishTransactionWithTse(const int8_t *clientId,
                                 uint32_t clientIdLength,
                                 uint32_t transactionNumber,
                                 const uint8_t *processData,
                                 uint32_t processDataLength,
                                 const int8_t *processType,
                                 uint32_t processTypeLength,
                                 const uint8_t *additionalData,
                                 uint32_t additionalDataLength,
                                 int64_t *logTime,
                                 uint8_t **signatureValue,
                                 uint32_t *signatureValueLength,
                                 uint32_t *signatureCounter,
                                 const int8_t *tseId,
                                 uint32_t tseIdLength);

/*
 Exports the transaction log messages, containing the process and protocol data, that correspond to a certain transaction
 and clientId. Additionally, the function exports all system log messages and audit log messages whose signature
 counters are contained in the following interval:
 	   Signature counter of the transaction log message for the start of the transaction and the signature
 	   counter of the transaction log message for the end of the transaction (inclusive).
 Furthermore, additional files that are needed to verify the signatures included in the log messages are exported
 @param[in] transactionNumber
                indicates the transaction whose corresponding log messages are relevant for the export [REQUIRED]
 @param[in] clientId
                ID of a client application that has used the API to log transactions.
                Only transaction log messages that correspond to the clientId are relevant for the export. [REQUIRED]
 @param[in] clientIdLength
                length of the array that represents the clientId  [REQUIRED]
 @param[out] exportedData
                selected log messages and additional files needed to verify the signatures included
                in the log messages [REQUIRED]
 @param[out] exportedDataLength
                length of the array that represents the exportedData [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_TRANSACTION_NUMBER_NOT_FOUND
                no data has been found for the provided transactionNumber
             ERROR_ID_NOT_FOUND
                no data has been found for the provided clientId
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
 */
int32_t exportDataFilteredByTransactionNumberAndClientId(uint32_t transactionNumber,
                                                         const int8_t *clientId,
                                                         uint32_t clientIdLength,
                                                         uint8_t **exportedData,
                                                         uint32_t *exportedDataLength);

/*
see @ref exportDataFilteredByTransactionNumberAndClientId
 */
int32_t exportDataFilteredByTransactionNumberAndClientIdWithTse(uint32_t transactionNumber,
                                                                const int8_t *clientId,
                                                                uint32_t clientIdLength,
                                                                uint8_t **exportedData,
                                                                uint32_t *exportedDataLength,
                                                                const int8_t *tseId,
                                                                uint32_t tseIdLength);

/*
 Exports the transaction log messages, containing the process and protocol data, that correspond to a certain transaction.
 Additionally, the function exports all system log messages and audit log messages whose signature
 counters are contained in the following interval:
 	   Signature counter of the transaction log message for the start of the transaction and the signature
 	   counter of the transaction log message for the end of the transaction (inclusive)
 Furthermore, additional files that are needed to verify the signatures, included in the log messages, are exported
 @param[in] transactionNumber
                indicates the transaction whose corresponding log messages are relevant for the export [REQUIRED]
 @param[out] exportedData
                selected log messages and additional files needed to verify the signatures included in the log messages [REQUIRED]
 @param[out] exportedDataLength
                length of the array that represents the exportedData [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_TRANSACTION_NUMBER_NOT_FOUND
                no data has been found for the provided transactionNumber
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
 */
int32_t exportDataFilteredByTransactionNumber(uint32_t transactionNumber,
                                              uint8_t **exportedData,
                                              uint32_t *exportedDataLength);

/*
see @ref exportDataFilteredByTransactionNumber
 */
int32_t exportDataFilteredByTransactionNumberWithTse(uint32_t transactionNumber,
                                                     uint8_t **exportedData,
                                                     uint32_t *exportedDataLength,
                                                     const int8_t *tseId,
                                                     uint32_t tseIdLength);

/*
 Exports the transaction log messages, containing the process and protocol data, that are relevant for a
 certain interval of transactions. Additionally, the function exports all system log messages and audit log
 messages whose signature counters are contained in this interval.
 Furthermore, additional files that are needed to verify the signatures, included in the log messages, are exported.
 @param[in] startTransactionNumber
                defines the transaction number (inclusive) regarding the start of the interval of relevant log messages [REQUIRED]
 @param[in] endTransactionNumber
                defines the transaction number (inclusive) regarding the end of the interval of relevant log messages [REQUIRED]
 @param[in] maximumNumberRecords
                if the value of this parameter is not 0, the function only returns the log messages if the number
                of relevant records is less or equal to the number of maximum records.
                If the value of the parameter is 0, the function returns all selected log messages [REQUIRED]
 @param[out] exportedData
                selected log messages and additional files needed to verify the signatures included in the log messages [REQUIRED]
 @param[out] exportedDataLength
                length of the array that represents the exportedData [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_PARAMETER_MISMATCH
                mismatch in parameters of the function
             ERROR_TRANSACTION_NUMBER_NOT_FOUND
                no data has been found for the provided transaction numbers
             ERROR_TOO_MANY_RECORDS
                the amount of requested records exceeds the parameter maximumNumberRecords
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
 */
int32_t exportDataFilteredByTransactionNumberInterval(uint32_t startTransactionNumber,
                                                      uint32_t endTransactionNumber,
                                                      uint32_t maximumNumberRecords,
                                                      uint8_t **exportedData,
                                                      uint32_t *exportedDataLength);

/*
see @ref exportDataFilteredByTransactionNumberInterval
 */
int32_t exportDataFilteredByTransactionNumberIntervalWithTse(uint32_t startTransactionNumber,
                                                             uint32_t endTransactionNumber,
                                                             uint32_t maximumNumberRecords,
                                                             uint8_t **exportedData,
                                                             uint32_t *exportedDataLength,
                                                             const int8_t *tseId,
                                                             uint32_t tseIdLength);

/*
 Exports the transaction log messages, containing the process and protocol data, that are relevant for a certain
 interval of transactions. The transaction log messages in this interval corresponds to the passed clientId.
 Additionally, the function exports all system log messages and audit log messages whose signature counters
 are contained in the interval.
 Furthermore, additional files that are needed to verify the signatures, included in the log messages, are exported.
 @param[in] startTransactionNumber
                defines the transaction number (inclusive) regarding the start of the interval of relevant log messages [REQUIRED]
 @param[in] endTransactionNumber
                defines the transaction number (inclusive) regarding the end of the interval of relevant log messages [REQUIRED]
 @param[in] clientId
                ID of a client application that has used the API to log transactions.
                Only transaction log messages that corresponds to the clientId are relevant for the export [REQUIRED]
 @param[in] clientIdLength
                length of the array that represents the clientId  [REQUIRED]
 @param[in] maximumNumberRecords
                if the value of this parameter is not 0, the function only returns the log messages if the number
                of relevant records is less or equal to the number of maximum records.
                If the value of the parameter is 0, the function returns all selected log messages [REQUIRED]
 @param[out] exportedData
                selected log messages and additional files needed to verify the signatures included in the log messages [REQUIRED]
 @param[out] exportedDataLength
                length of the array that represents the exportedData [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

  	   If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_PARAMETER_MISMATCH
                mismatch in parameters of the function
             ERROR_TRANSACTION_NUMBER_NOT_FOUND
                no data has been found for the provided transaction numbers
             ERROR_ID_NOT_FOUND
                no data has been found for the provided clientId
             ERROR_TOO_MANY_RECORDS
                the amount of requested records exceeds the parameter maximumNumberRecords
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized

 */
int32_t exportDataFilteredByTransactionNumberIntervalAndClientId(uint32_t startTransactionNumber,
                                                                 uint32_t endTransactionNumber,
                                                                 const int8_t *clientId,
                                                                 uint32_t clientIdLength,
                                                                 uint32_t maximumNumberRecords,
                                                                 uint8_t **exportedData,
                                                                 uint32_t *exportedDataLength);

/*
see @ref exportDataFilteredByTransactionNumberAndClientId
 */
int32_t exportDataFilteredByTransactionNumberIntervalAndClientIdWithTse(uint32_t startTransactionNumber,
                                                                        uint32_t endTransactionNumber,
                                                                        const int8_t *clientId,
                                                                        uint32_t clientIdLength,
                                                                        uint32_t maximumNumberRecords,
                                                                        uint8_t **exportedData,
                                                                        uint32_t *exportedDataLength,
                                                                        const int8_t *tseId,
                                                                        uint32_t tseIdLength);

/*
 Exports the transaction log messages, system log messages and audit log messages
 that have been created in a certain period of time.
 Furthermore, additional files that are needed to verify the signatures included in the log messages are exported.
 @param[in] startDate
                defines the starting time (inclusive) for the period in that the relevant log messages have been created.
                The value for the parameter is encoded in a format that conforms to BSI TR-03151.
                If a value for the input parameter endDate is passed, startDate is [OPTIONAL].
                If no value for the input parameter endDate is passed, startDate is [REQUIRED].
 @param[in] endDate
                defines the end time (inclusive) for the period in that relevant log messages have been created.
                The value for the parameter is encoded in a format that conforms to BSI TR-03151.
                If a value for the input parameter startDate is passed, endDate is [OPTIONAL].
                If no value for the input parameter startDate is passed, endDate is [REQUIRED].
 @param[in] maximumNumberRecords
                if the value of this parameter is not 0, the function only returns the log messages if the number
                of relevant records is less or equal to the number of maximum records.
                If the value of the parameter is 0, the function returns all selected log messages [REQUIRED]
 @param[out] exportedData
                selected log messages and additional files needed to verify the signatures included in the log messages [REQUIRED]
 @param[out] exportedDataLength
                length of the array that represents the exportedData [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_PARAMETER_MISMATCH
                mismatch in parameters of the function
             ERROR_NO_DATA_AVAILABLE
                no data has been found for the provided selection
             ERROR_TOO_MANY_RECORDS
                the amount of requested records exceeds the parameter maximumNumberRecords
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
 */
int32_t exportDataFilteredByPeriodOfTime(int64_t startDate,
                                         int64_t endDate,
                                         uint32_t maximumNumberRecords,
                                         uint8_t **exportedData,
                                         uint32_t *exportedDataLength);

/*
see @ref exportDataFilteredByPeriodOfTime
 */
int32_t exportDataFilteredByPeriodOfTimeWithTse(int64_t startDate,
                                                int64_t endDate,
                                                uint32_t maximumNumberRecords,
                                                uint8_t **exportedData,
                                                uint32_t *exportedDataLength,
                                                const int8_t *tseId,
                                                uint32_t tseIdLength);

/*
 Exports the transaction log messages, system log messages and audit log messages that have been created in a certain period of time.
 The transaction log messages in this period of time corresponds to the passed clientId.
 Furthermore, additional files that are needed to verify the signatures included in the log messages are exported
 @param[in] startDate
                defines the starting time (inclusive) for the period in that the relevant log messages have been created.
                The value for the parameter is encoded in a format that conforms to BSI TR-03151.
                If a value for the input parameter endDate is passed, startDate is [OPTIONAL].
                If no value for the input parameter endDate is passed, startDate is [REQUIRED].
 @param[in] endDate
                defines the end time (inclusive) for the period in that relevant log messages have been created.
                The value for the parameter is encoded in a format that conforms to BSI TR-03151.
                If a value for the input parameter startDate is passed, endDate is [OPTIONAL].
                If no value for the input parameter startDate is passed, endDate is [REQUIRED].
 @param[in] clientId
                ID of a client application that has used the API to log transactions.
Only transaction log messages that corresponds to the clientId are relevant for the export [REQUIRED]
 @param[in] clientIdLength
                length of the array that represents the clientId  [REQUIRED]
 @param[in] maximumNumberRecords
                if the value of this parameter is not 0, the function only returns the log messages if the number
                of relevant records is less or equal to the number of maximum records.
                If the value of the parameter is 0, the function returns all selected log messages [REQUIRED]
 @param[out] exportedData
                selected log messages and additional files needed to verify the signatures included in the log messages [REQUIRED]
 @param[out] exportedDataLength
                length of the array that represents the exportedData [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_PARAMETER_MISMATCH
                mismatch in parameters of the function
             ERROR_NO_DATA_AVAILABLE
                no data has been found for the provided selection
             ERROR_ID_NOT_FOUND
                no data has been found for the provided clientId
             ERROR_TOO_MANY_RECORDS
                the amount of requested records exceeds the parameter maximumNumberRecords
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized

 */
int32_t exportDataFilteredByPeriodOfTimeAndClientId(int64_t startDate,
                                                    int64_t endDate,
                                                    const int8_t *clientId,
                                                    uint32_t clientIdLength,
                                                    uint32_t maximumNumberRecords,
                                                    uint8_t **exportedData,
                                                    uint32_t *exportedDataLength);

/*
see @ref exportDataFilteredByPeriodOfTimeAndClientId
 */
int32_t exportDataFilteredByPeriodOfTimeAndClientIdWithTse(int64_t startDate,
                                                           int64_t endDate,
                                                           const int8_t *clientId,
                                                           uint32_t clientIdLength,
                                                           uint32_t maximumNumberRecords,
                                                           uint8_t **exportedData,
                                                           uint32_t *exportedDataLength,
                                                           const int8_t *tseId,
                                                           uint32_t tseIdLength);

/*
 Exports all stored transaction log messages, system log message and audit log messages.
 Furthermore, additional files that are needed to verify the signatures included in the log messages are exported.
 @param[in] maximumNumberRecords
                if the value of this parameter is not 0, the function only returns the log messages if the number
                of relevant records is less or equal to the number of maximum records.
                If the value of the parameter is 0, the function returns all stored log messages [REQUIRED]
 @param[out] exportedData
                all stored log messages and additional files needed to verify the signatures included in the log messages [REQUIRED]
 @param[out] exportedDataLength
                length of the array that represents the exportedData [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_TOO_MANY_RECORDS
                the amount of requested records exceeds the parameter maximumNumberRecords
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized

 */
int32_t exportData(uint32_t maximumNumberRecords,
                   uint8_t **exportedData,
                   uint32_t *exportedDataLength);

/*
see @ref exportData
 */
int32_t exportDataWithTse(uint32_t maximumNumberRecords,
                          uint8_t **exportedData,
                          uint32_t *exportedDataLength,
                          const int8_t *tseId,
                          uint32_t tseIdLength);

/*
 Exports the certificates of the certificate chains. These certificates belong to
 the public keys of the key pairs that are used for the creation of signature values in log messages
 @param[out] certificates
                the TAR archive that contains all certificates that are necessary for the verification of log messages.
                The format of the TAR archive and the contained certificates conforms to BSI TR-03151 [REQUIRED]
 @param[out] certificatesLength
                length of the array that represent the certificate(s) [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_EXPORT_CERT_FAILED
                the collection of the certificates for the export failed
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
 */
int32_t exportCertificates(uint8_t **certificates,
                           uint32_t *certificatesLength);

/*
see @ref exportCertificates
 */
int32_t exportCertificatesWithTse(uint8_t **certificates,
                                  uint32_t *certificatesLength,
                                  const int8_t *tseId,
                                  uint32_t tseIdLength);

/*
 Restores a backup in the SE API and storage. The backup data includes log messages and certificates
 that have been exported by using the exportData function.
 Log messages and certificates are passed in the TAR archive that has been returned during the export of the log messages and certificates.
 The function stores the data of the passed log messages in the storage. If an imported log message
 has a file name that already exists in the storage, a counter is appended to the file name of the imported log message.
 The function stores an imported certificate only if no certificate of the same name is managed by the SE API.
 @param[in] restoreData
                represents the TAR archive that contains the log messages and certificates for the restore process [REQUIRED]
 @param[in] restoreDataLength
                represents the length of the array that represents the restoreData [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_RESTORE_FAILED
                the restore process has failed
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
             ERROR_USER_NOT_AUTHORIZED
                the user who has invoked the function restoreFromBackup is not authorized to execute this function
             ERROR_USER_NOT_AUTHENTICATED
                the user who has invoked the function restoreFromBackup has not the status authenticated
 */
int32_t restoreFromBackup(uint8_t *restoreData,
                          uint32_t restoreDataLength);

/*
see @ref restoreFromBackup
 */
int32_t restoreFromBackupWithTse(uint8_t *restoreData,
                                 uint32_t restoreDataLength,
                                 const int8_t *tseId,
                                 uint32_t tseIdLength);

/*
 Reads a log message that bases on the last log message parts that have been produced and processed by the Secure Element
 @param[out] logMessage
                contains the last log message that the Secure Element has produced [REQUIRED]
 @param[out] logMessageLength
                length of the array that represents the last log message [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_NO_LOG_MESSAGE
                no log message parts are found
             ERROR_READING_LOG_MESSAGE
                error while retrieving log message parts
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
             ERROR_SECURE_ELEMENT_DISABLED
                the Secure Element has been disabled
 */
int32_t readLogMessage(uint8_t **logMessage,
                       uint32_t *logMessageLength);

/*
see @ref readLogMessage
 */
int32_t readLogMessageWithTse(uint8_t **logMessage,
                              uint32_t *logMessageLength,
                              const int8_t *tseId,
                              uint32_t tseIdLength);

/*
 Exports the serial number(s) of the SE API. A serial number is a hash value of a public key that belongs to a key pair
 whose private key is used to create signature values of log messages.
 @param[out] serialNumbers
                the serial number(s) of the SE API.
                The serial number(s) is encoded in the TLV structure defined in BSI TR-03151. [REQUIRED]
 @param[out] serialNumbersLength
                length of the array that represents the serial number(s) [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_EXPORT_SERIAL_NUMBERS_FAILED
                the collection of the serial number(s) failed
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
 */
int32_t exportSerialNumbers(uint8_t **serialNumbers,
                            uint32_t *serialNumbersLength);

/*
see @ref exportSerialNumbers
 */
int32_t exportSerialNumbersWithTse(uint8_t **serialNumbers,
                                   uint32_t *serialNumbersLength,
                                   const int8_t *tseId,
                                   uint32_t tseIdLength);

/*
 Supplies the maximal number of clients that can use the functionality to log transactions of the SE API simultaneously
 @param[out] maxNumberClients
                maximum number of clients that can use the functionality to log transactions of the SE API simultaneously [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
              ERROR_GET_MAX_NUMBER_OF_CLIENTS_FAILED
                 the determination of the maximum number of clients that could use the SE API simultaneously failed
              ERROR_SE_API_NOT_INITIALIZED
                 the SE API has not been initialized
              ERROR_SECURE_ELEMENT_DISABLED
                 the Secure Element has been disabled
 */
int32_t getMaxNumberOfClients(uint32_t *maxNumberClients);

/*
see @ref getMaxNumberOfClients
 */
int32_t getMaxNumberOfClientsWithTse(uint32_t *maxNumberClients,
                                     const int8_t *tseId,
                                     uint32_t tseIdLength);

/*
 Supplies the number of clients that are currently using the functionality to log transactions of the SE API.
 @param[out] currentNumberClients
                the number of clients that are currently using the functionality of the SE API [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_GET_CURRENT_NUMBER_OF_CLIENTS_FAILED
                the determination of the current number of clients using the SE API failed
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
             ERROR_SECURE_ELEMENT_DISABLED
                the Secure Element has been disabled
 */
int32_t getCurrentNumberOfClients(uint32_t *currentNumberClients);

/*
see @ref getCurrentNumberOfClients
 */
int32_t getCurrentNumberOfClientsWithTse(uint32_t *currentNumberClients,
                                         const int8_t *tseId,
                                         uint32_t tseIdLength);

/*
 Supplies the maximal number of simultaneously opened transactions that can be managed by the SE API
 @param[out] maxNumberTransactions
                 maximum number of simultaneously opened transactions that can be managed by the SE API [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_GET_MAX_NUMBER_TRANSACTIONS_FAILED
                the determination of the maximum number of transactions that can be managed simultaneously failed
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
             ERROR_SECURE_ELEMENT_DISABLED
                the Secure Element has been disabled
 */
int32_t getMaxNumberOfTransactions(uint32_t *maxNumberTransactions);

/*
see @ref getMaxNumberOfTransactions
 */
int32_t getMaxNumberOfTransactionsWithTse(uint32_t *maxNumberTransactions,
                                          const int8_t *tseId,
                                          uint32_t tseIdLength);

/*
 Supplies the number of open transactions that are currently managed by the SE API
 @param[out] currentNumberTransactions
                the number of open transactions that are currently managed by the SE API [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_GET_CURRENT_NUMBER_OF_TRANSACTIONS_FAILED
                the determination of the number of open transactions that are currently managed by the SE API failed
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
             ERROR_SECURE_ELEMENT_DISABLED
                the Secure Element has been disabled
 */
int32_t getCurrentNumberOfTransactions(uint32_t *currentNumberTransactions);

/*
see @ref getCurrentNumberOfTransactions
 */
int32_t getCurrentNumberOfTransactionsWithTse(uint32_t *currentNumberTransactions,
                                              const int8_t *tseId,
                                              uint32_t tseIdLength);

/*
 Supplies the supported variants to update transactions
 @param[out] supportedUpdateVariants
                the supported variant(s) to update a transaction [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_GET_SUPPORTED_UPDATE_VARIANTS_FAILED
                the identification of the supported variant(s) to update transactions failed
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
             ERROR_SECURE_ELEMENT_DISABLED
                the Secure Element has been disabled
 */
int32_t getSupportedTransactionUpdateVariants(UpdateVariants *supportedUpdateVariants);

/*
see @ref getSupportedTransactionUpdateVariants
 */
int32_t getSupportedTransactionUpdateVariantsWithTse(UpdateVariants *supportedUpdateVariants,
                                                     const int8_t *tseId,
                                                     uint32_t tseIdLength);

/*
 Deletes all data that is stored in the storage. The function deletes
 only data that has been exported.
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_DELETE_STORED_DATA_FAILED
                the deletion of the data from the storage failed
             ERROR_UNEXPORTED_STORED_DATA
                the deletion of data from the storage failed because the storage contains data that has not been exported
             ERROR_SE_API_NOT_INITIALIZED
                the SE API has not been initialized
             ERROR_USER_NOT_AUTHORIZED
                the user who has invoked the function deleteStoredData is not authorized to execute this function
             ERROR_USER_NOT_AUTHENTICATED
                the user who has invoked the function deleteStoredData has not the status authenticated
 */
int32_t deleteStoredData(void);

/*
see @ref deleteStoredData
 */
int32_t deleteStoredDataWithTse(const int8_t *tseId, uint32_t tseIdLength);

/*
 Enables an authorized user or application to authenticate to the SE API for the usage of restricted SE API functions
 @param[in] userId
                the ID of the user who or application that wants to be authenticated [REQUIRED]
 @param[in] userIdLength
                the length of the array that represents the userId [REQUIRED]
 @param[in] pin
                the PIN for the authentication [REQUIRED]
 @param[in] pinLength
                the length of the array that represents the pin [REQUIRED]
 @param[out] authenticationResult
                the result of the authentication [REQUIRED]
 @param[out] remainingRetries
                the number of remaining retries to enter a PIN [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the authentication attempt has failed, the return value AUTHENTICATION_FAILED is returned.

         If an error occurs during the processing the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_SIGNING_SYSTEM_OPERATION_DATA_FAILED
                the determination of the log message parts for the system operation data by the Secure Element failed
             ERROR_RETRIEVE_LOG_MESSAGE_FAILED
                the execution of the Secure Element functionality to retrieve log message parts has failed
             ERROR_STORAGE_FAILURE
                storing of the data of the log message failed
             ERROR_SECURE_ELEMENT_DISABLED
                the Secure Element has been disabled

 */
int32_t authenticateUser(const int8_t *userId,
                         uint32_t userIdLength,
                         const uint8_t *pin,
                         uint32_t pinLength,
                         AuthenticationResult *authenticationResult,
                         int16_t *remainingRetries);

/*
see @ref authenticateUser
 */
int32_t authenticateUserWithTse(const int8_t *userId,
                                uint32_t userIdLength,
                                const uint8_t *pin,
                                uint32_t pinLength,
                                AuthenticationResult *authenticationResult,
                                int16_t *remainingRetries,
                                const int8_t *tseId,
                                uint32_t tseIdLength);

/*
 Enables the log out of an authenticated user or application from the SE API
 @param[in] userId
                the ID of the user who or application that wants to log out from the SE API [REQUIRED]
 @param[in] userIdLength
                the length of the array that represents the userId [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of the function has failed, the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_USER_ID_NOT_MANAGED
                the passed userId is not managed by the SE API
             ERROR_SIGNING_SYSTEM_OPERATION_DATA_FAILED
                the determination of the log message parts for the system operation data by the Secure Element failed
             ERROR_USER_ID_NOT_AUTHENTICATED
                the passed userId has not the status authenticated
             ERROR_RETRIEVE_LOG_MESSAGE_FAILED
                the execution of the Secure Element functionality to retrieve log message parts has failed
             ERROR_STORAGE_FAILURE
                storing of the data of the log message failed
             ERROR_SECURE_ELEMENT_DISABLED
                the Secure Element has been disabled
 */
int32_t logOut(const int8_t *userId,
               uint32_t userIdLength);

/*
see @ref logOut
 */
int32_t logOutWithTse(const int8_t *userId,
                      uint32_t userIdLength,
                      const int8_t *tseId,
                      uint32_t tseIdLength);

/*
 Enables the unblocking for the entry of a PIN and the definition of a new PIN
 for the authentication of authorized users or applications
 @param[in] userId
                the ID of the user who or application that wants to unblock the corresponding PIN entry [REQUIRED]
 @param[in] userIdLength
                the length of the array that represents the userId [REQUIRED]
 @param[in] puk
                the PUK of the user/application [REQUIRED]
 @param[in] pukLength
                the length of the array that represents the puk [REQUIRED]
 @param[in] newPin
                the new PIN for the user/application [REQUIRED]
 @param[in] newPinLength
                the length of the array that represents the newPin [REQUIRED]
 @param[out] unblockResult
                the result of the unblock procedure [REQUIRED]
 @return if the execution of the function has been successful, the return value EXECUTION_OK is returned.

         If the execution of attempt to unblock a PIN entry has failed, the return value UNBLOCK_FAILED is returned.

         If an error occurs during the processing the appropriate error code is returned:

             ERROR_AT_LOAD_NOT_CALLED
                @ref at_load has not been called
             ERROR_SIGNING_SYSTEM_OPERATION_DATA_FAILED
                the determination of the log message parts for the system operation data by the Secure Element failed
             ERROR_RETRIEVE_LOG_MESSAGE_FAILED
                the execution of the Secure Element functionality to retrieve log message parts has failed
             ERROR_STORAGE_FAILURE
                storing of the data of the log message failed
             ERROR_SECURE_ELEMENT_DISABLED
                the Secure Element has already been disabled
 */
int32_t unblockUser(const int8_t *userId,
                    uint32_t userIdLength,
                    const int8_t *puk,
                    uint32_t pukLength,
                    const int8_t *newPin,
                    uint32_t newPinLength,
                    UnblockResult *unblockResult);

/*
see @ref unblockUser
 */
int32_t unblockUserWithTse(const int8_t *userId,
                           uint32_t userIdLength,
                           const int8_t *puk,
                           uint32_t pukLength,
                           const int8_t *newPin,
                           uint32_t newPinLength,
                           UnblockResult *unblockResult,
                           const int8_t *tseId,
                           uint32_t tseIdLength);

extern void init_pace(int32_t alg, int32_t curve);

extern void set_pace_password(const int8_t *pace_password, int32_t len);

extern const int8_t *pace_step1(const int8_t *enc_nonce_b64, int32_t len);

extern const int8_t *pace_step2(const int8_t *server_pub1_b64, int32_t len);

extern const int8_t *pace_step3(const int8_t *server_pub2_b64, int32_t len);

extern const int8_t *pace_step4(const int8_t *server_token_b64, int32_t len);

extern void free_pace(void);

#endif /* asigntse_h */

#ifdef __cplusplus
}
#endif
