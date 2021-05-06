# A-Sign TSE API Developer Manual (Standard Edition)

| Date              | Revision      | Author | Changes  |
| ----------------- |:-------------:|:--------:|------------- |
| 09/09/2019        | 0.9.0         | DK | API description. |
| 25/10/2019        | 0.9.2         | DK | API description. |
| 29/10/2019        | 0.9.3         | DK | API description. |
| 03/01/2020        | 0.9.4         | RS | Cleanup, withTse |
| 16/03/2020        | 0.9.7         | DK | Library initialization, A-Trust API, Cfg API. |
| 30/03/2020        | 0.9.7_1       | DK | Add COM function signatures. Add Appendix for COM function signatures. |
| 02/04/2020        | 0.9.7_2       | DK | Add Appendix for C function signatures. |
| 07/04/2020        | 0.9.7_3       | JC | Provisioning description.  |
| 23/04/2020        | 1.0.0         | DK | Fix cfg-Function descriptions.  |
| 23/04/2020        | 1.0.0_1       | JC | Add error codes |

# Introduction

A-Trust provides a shared library for using **a.sign TSE Online**, which provides the neccessary functionality required for German Kassensicherheitsverordnung.

## Installation

The library must be placed in the same directory as the executable. The provided configuration file must be placed in the working directory of the executable.

## Library Initialization
Since version **1.0.0** the library must be initialized with a call to `at_load`. The function `at_load` must be called exactly once at startup and before any other library call.

The function `at_unload` must be called before closing the application. After `at_unload` has been called, no other function call to the library is allowed.

## Provisioning
The following example shows how to provision a TSE using SE API functions.

First of all we shall check whether the TSE's `lifeCycleState` is `NotInitialized` (=1) by using the function [at_getLifecycleState](#at_getLifecycleState).
Contrary to earlier versions, PIN and PUK are set during the installation process.
Next we log in as the admin user using the function [authenticateUser](#authenticateUser), followed by the function [initializeDescriptionSet](#initializeDescriptionSet) to put the TSE into the initialized state.
We then call the function [at_registerClientId](#at_registerClientId). Finally, we log out of the administrator role using the function  [logOut](#logOut).

Note that we did not explicitly require any calls to initialize the
connection to the security module, nor to perform a time update. These calls
are performed automatically by the A-Sign TSE API.
### Example: Check LifeCycleState, set initial PINs, initialize TSE and register a ClientID

```C
    uint8_t adminPin[] = { 0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08 };
    uint8_t adminPuk[] = { 0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0a };

    int32_t result = 0;

    /* Load TSE */
    if ((result = at_load()) != EXECUTION_OK) {
        (void)printf("at_load FAILED.  Error: %d\n", result);
        return result;
    }

    /* Check whether the TSE is not initialized */
    LifeCycleState state = 0;
    int32_t result = at_getLifecycleState(&state);

    if (result == EXECUTION_OK && state == 1) {

        /* Authenticate the Admin role with the newly set Admin PIN in order
         to get the rights to initialize the TSE and register clients */
        AuthenticationResult authResult = 0;
        int16_t tries_remaining = 0;
        const char* adminUser = "Admin";

        result = authenticateUser(
            adminUser, strlen(adminUser), 
            adminPin, sizeof(adminPin), 
            &authResult, 
            &tries_remaining
        );

        if (result != EXECUTION_OK || authResult != 0) {
            (void)printf("authenticateUser FAILED.  Error: %d, AuthResult: %d, tries remaining: %d\n", result, authResult, tries_remaining);
            return result;
        }

        /* initialize TSE for first use */
        if ((result = initializeDescriptionSet()) != EXECUTION_OK ) {
            (void)printf("initializeDesriptionSet FAILED.  Error: %d\n", result);
            return result;
        }

        /* Register a client ID */
        const char* clientId = "ClientID-1";
        result = at_registerClientId(clientId, strlen(clientId));

        if (result != EXECUTION_OK) {
            (void)printf("at_registerClientId  Error: %d\n", result);
            return result;
        }

        /* Log out of administrator role */
        result = logOut(adminUser, strlen(adminUser));

        if (result != EXECUTION_OK) {
            (void)printf("logOut FAILED.  Error: %d\n", result);
            return result;
        }
    } else {
        (void)printf("at_getLifecycleState FAILED.  Error: %d\n", result);
    } 
```
## Overview

[DSFinV-K 2.0](https://www.bzst.de/DE/Unternehmen/Aussenpruefungen/DigitaleSchnittstelleFinV/digitaleschnittstellefinv_node.html) defines required data and formats cash registers have to use. DSFinV-K defines three fundamental processes:

- [Beleg erzeugen](#beleg)
- [Bestellung](#bestellung)
- [Sonstiger Vorgang](#sonstigervorgang)

All of these operations must be performed in a transaction as described in [make transaction](#makeTransaction) providing different values for the parameters.

### Make Transaction
The function [startTransaction](#startTransaction) must be called at the beginning of each transaction and [finishTransaction](#finishTransaction) is called when the transaction is finished.  Note that the function [updateTransaction](#updateTransaction) should no longer be used.


- call [startTransaction](#startTransaction)
  - provide just the `clientID`
  - receive `transactionNumber` and `logTime`
- call [finishTransaction](#finishtransaction) and provide:
  - `clientID`
  - `transactionNumber` received from startTransaction
  - `processType` = depends on the type of transaction eg. Beleg -> processType = `Kassenbeleg-V1`
  - `processData` = depends on the type of transaction see Beleg or Bestellung or SonstigerVorgang
- [finishTransaction](#finishTransaction) returns:
  - `logTime` (eg.: 2019-07-20T09:11:24:000Z) - all dateTime values UTC
  - `signatureValue` (binary)
  - `signatureCounter` (eg. 4711)

### Beleg erzeugen (create receipt)

The following values must be provided to [makeTransaction](#makeTransaction)

- processType = `Kassenbeleg-V1`
- processData = consists of three values separated by `^`
  - `Transaktionstyp^Brutto-Steuerumsätze^Zahlungen`
  - `Transaktionstyp` = `Beleg`
  - `Brutto-Steuerumsätze`
    - The following values separated by `_`
      - Allgemeiner Steuersatz (19%)
      - Ermäßigter Steuersatz (7%)
      - Durchnittsatz (§24(1)Nr.3 UStG) (10.7%)
      - Durchnittsatz (§24(1)Nr.1 UStG) (5.5%)
      - 0%
    - eg.: €10 Allgemeiner Steuersatz -> `10.00_0.00_0.00_0.00_0.00`
  - `Zahlungen`: three values separated by `:` zB.: `10:Bar:EUR`
    - `Betrag:Zahlungsart:Währung`
      - `Betrag`: value
      - `Zahlungsart`: Bar or Unbar
      - `Währung`: Currency
      - ordered: Bar, then Unbar, EUR then foreign currencies alphabetically

#### Example

Please note, that this example omits proper error handling for sake of clarity.

```C
char *clientId = "Kasse_1";
char *tseId = "TSE_1";
char *processType = "Kassenbeleg-V1";
unsigned char *processData = "Beleg^75.33_7.99_0.00_0.00_0.00^10.00:Bar_5.00:Bar:CHF_5.00:Bar:USD_64.30:Unbar";
uint32_t transaction_number = 0;
unsigned char *serial_number = NULL;
uint32_t serial_number_len = 0;
uint32_t signature_counter = 0;
unsigned char *signature_value = NULL;
uint32_t signature_value_len = 0;
int64_t log_time = 0;

if (startTransactionWithTse(
    clientId, (uint32_t) strlen(clientId),
    NULL, 0,
    NULL, 0,
    NULL, 0,
    &transaction_number,
    &log_time,
    &serial_number,
    &serial_number_len,
    &signature_counter,
    &signature_value,
    &signature_value_len,
    tseId, (uint32_t) strlen(tseId)) == EXECUTION_OK) {

    /* some code */

    at_free(&signature_value);

    if (finishTransactionWithTse(
        clientId, (uint32_t) strlen(clientId),
        transaction_number,
        (const uint8_t *) processData, (uint32_t) strlen(processData),
        processType, (uint32_t) strlen(processType),
        NULL, 0,
        &log_time,
        &signature_value,
        &signature_value_len,
        &signature_counter,
        tseId, (uint32_t) strlen(tseId))) == EXECUTION_OK) {

        /* some code */

        at_free(&signature_value);
	}  
}
```
### Bestellung (order)
The following values must be provided to [makeTransaction](#makeTransaction)
 - processType = `Bestellung-V1`
 - processData: 
   - values separated by `;`
   - lines separated by `\n`
   - `Menge;"Bezeichnung";Preis`
     - Menge: minimal amound of decimal places eg. 1 or 0.6 or 3.1415
     - Bezeichnung: quoted description
       - `"Wiener Schnitzel"`
       - Schnitzel "Wiener Art" -> `"Schnitzel ""Wiener Art"""` 
     - Preis: gross price of single article, 2 decimal places eg: 2.00 or 2.99
   - example: 
      ```
      2;"Eisbecher ""Himbeere""";3.99
      1;"Eiskaffee";2.99
      ```

#### Example

This example corresponds to the order above.

```C
const char* process_type = "Bestellung-V1";
uint32_t* process_data = 
    "2;\"Eisbecher \"\"Himbeere"\"\"\;3.99\n"
    "1;\"Eiskaffee\";2.99";
```

### Sonstiger Vorgang (other transaction)

The following values must be provided to [makeTransaction](#makeTransaction)
This transaction can be used for everything which is not an order or receipt.

- processType = `SonstigerVorgang`
- processData: free, should be readable

# SE-API Functions

BSI TR-03151 specifies the following functions:

Transactions:

- [startTransaction](#startTransaction)
- [updateTransaction](#updateTransaction)
- [finishTransaction](#finishTransaction)

Data export Calls:

- [exportData](#exportData)
- [exportDataFilteredByTransactionNumberAndClientId](#exportDataFilteredByTransactionNumberAndClientId)
- [exportDataFilteredByTransactionNumber](#exportDataFilteredByTransactionNumber)
- [exportDataFilteredByTransactionNumberInterval](#exportDataFilteredByTransactionNumberInterval)
- [exportDataFilteredByTransactionNumberIntervalAndClientId](#exportDataFilteredByTransactionNumberIntervalAndClientId)
- [exportDataFilteredByPeriodOfTime](#exportDataFilteredByPeriodOfTime)
- [exportDataFilteredByPeriodOfTimeAndClientId](#exportDataFilteredByPeriodOfTimeAndClientId)
- [exportCertificates](#exportCertificates)
- [exportSerialNumbers](#exportSerialNumbers)
- [restoreFromBackup](#restoreFromBackup)
- [readLogMessage](#readLogMessage)
  
Administrative Calls:

- Time Management
  - [updateTime](#updateTime)
  - [updateTimeWithTimeSync](#updateTimeWithTimeSync)
- User Management
  - [authenticateUser](#authenticateUser)
  - [logout](#logout)
  - [unblockUser](#unblockUser)
- Lifecycle
  - [Initialize](#initializeDescriptionNotSet)
    - [initializeDescriptionNotSet](#initializeDescriptionNotSet)
    - [initializeDescriptionSet](#initializeDescriptionSet)
  - [disableSecureElement](#disableSecureElement)
  - [restoreFromBackup](#restoreFromBackup)
  - [deleteStoredData](#deleteStoredData)
- Statistics
  - [getMaxNumberOfClients](#GetMaxNumberOfClients)
  - [getCurrentNumberOfClients](#GetCurrentNumberOfClients)
  - [getMaxNumberOfTransactions](#GetMaxNumberOfTransactions)
  - [getCurrentNumberOfTransactions](#GetCurrentNumberOfTransactions)
  - [getSupportedTransactionUpdateVariants](#GetSupportedTransactionUpdateVariants)

# A-Trust API Functions

The following functions are not defined in the SE-API specification.  They are additional functions specific to the A-Trust implementation.

- Management functions
  - [at_getVersion](#at_getVersion)
  - [at_getSignatureAlgorithm](#at_getSignatureAlgorithm)
  - [at_getPublicKey](#at_getPublicKey)
  - [at_getOpenTransactions](#at_getOpenTransactions)
  - [at_getSignatureCounter](#at_getSignatureCounter)
  - [at_getTransactionCounter](#at_getTransactionCounter)
  - [at_getLifecycleState](#at_getLifecycleState)
  - [at_getSerialNumber](#at_getSerialNumber)
  - [at_suspendSecureElement](#at_suspendSecureElement)
  - [at_unsuspendSecureElement](#at_unsuspendSecureElement)
<!-- THESE FUNCTIONS ARE NOT EXPOSED YET  
  - [at_registerClient](#at_registerClient)
  - [at_getClientMappings](#at_getClientMappings) 
  -->
  
- Startup/shutdown
  - [at_load](#at_load)
  - [at_unload](#:at_unload)
- Utility
  - [at_free](#at_free)

## Config API Functions

The following functions are also specific to the A-Trust implementation and are used to set the configuration of the library.

- [cfgSetConfigFile](#cfgSetConfigFile)
- [cfgTseAdd](#cfgTseAdd)
- [cfgTseRemove](#cfgTseRemove)
- [cfgSetLoggingEnabled](#cfgSetLoggingEnabled)
- [cfgSetLoggingStderr](#cfgSetLoggingStderr)
- [cfgSetLoggingFile](#cfgSetLoggingFile)
- [cfgSetLogDir](#cfgSetLogDir)
- [cfgSetLogLevel](#cfgSetLogLevel)
- [cfgSetLogAppend](#cfgSetLogAppend)
- [cfgSetLogColors](#cfgSetLogColors)
- [cfgSetLogDetails](#cfgSetLogDetails)
- [cfgSetLogStderrColors](#cfgSetLogStderrColors)
- [cfgSetHttpProxy](#cfgSetHttpProxy)


## WithTse function family

Most functions have a WithTse version with additional parameters `tseId` and `tseIdLength`. This parameter specifies the config entries, the library should use for the call. Functions without the `tseId` parameter use the `[default]` section. To see all function variants, have a look at Appendix A.

# Date-Format

If not specified otherwise date formats for function parameters are to be provided in [Epoch Time](https://www.epochconverter.com/)

For example `startDate` and `endDate` in [exportDataFilteredByPeriodOfTime](#exportdatafilteredbyperiodoftime).
 
# Transactions

## startTransaction

Starts a new transaction.

**C**
```C
int32_t startTransaction(const char *clientId,
                         uint32_t clientIdLength,
                         const uint8_t *processData,
                         uint32_t processDataLength,
                         const char *processType,
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
```

```C
int32_t startTransactionWithTse(const char *clientId,
                                uint32_t clientIdLength,
                                const uint8_t *processData,
                                uint32_t processDataLength,
                                const char *processType,
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
                                const char *tseId,
                                uint32_t  tseIdLength)
```
**COM**
```C
HRESULT StartTransaction([in] BSTR clientId,
	[in] BSTR processData,
	[in] BSTR processType,
	[in] BSTR additionalData,
	[out] LONG* transactionNumber,
	[out] DATE* logTime,
	[out] SAFEARRAY(BYTE)* serialNumber,
	[out] LONG* signatureCounter,
	[out] SAFEARRAY(BYTE)* signaturetvalue,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| clientId | in | REQUIRED | Represents the ID of the application that has invoked the function. |
| clientIdLength | in | REQUIRED | Length of the array that represents the clientId. |
| processData | in | REQUIRED| Represents all the necessary information regarding the initial state of the process. | 
| processDataLength | in | REQUIRED | Length of the array that represents the processData. |
| processType | in | OPTIONAL | Identifies the type of the transaction as defined by the application.This string MUST NOT contain more than 100 characters |
| processTypeLength | in | REQUIRED | Length of the string that represents the processType. If a null pointer is passed for the parameter processType, the value for the length SHALL be 0. |
| additionalData | in | OPTIONAL | Reserved for future use. |
| additionalDataLength | in | REQUIRED | Length of the array that represents the additionalData. If a null pointer is passed for the parameter additionalData, the value for the length SHALL be 0. |
| transactionNumber | out | REQUIRED | Represents a transaction number that has been assigned by the Secure Element to the process. |
| logTime | out | REQUIRED | Represents the point in time of the Secure Element when the log message was created. Time is given in UTC. |
| serialNumber | out | REQUIRED | Represents hash value over the public key of the key pair that is used for the creation of signature values in transaction log messages. |
| serialNumberLength | out | REQUIRED | Length of the array that represents the serialNumber. |
| signatureCounter | out | REQUIRED | Represents the current value of the signature counter. |
| signatureValue | out | OPTIONAL | Represents the signature value. |
| signatureValueLength | out | OPTIONAL | Length of the array that represents the signature value if a value for the output parameter signatureValue is returned. |
| tseId | in | (REQUIRED) | ID of the TSE to use in a multi-TSE environment. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_START_TRANSACTION_FAILED | The execution of the Secure Element functionality to start a transaction failed. |
| ERROR_RETRIEVE_LOG_MESSAGE_FAILED | The execution of the Secure Element functionality to retrieve log message parts has failed. |
| ERROR_STORAGE_FAILURE | Storing of the log message failed. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |
| ERROR_TIME_NOT_SET | The managed data/time in the Secure Element has not been updated after the initialization of the SE API or a period of absence of current for the Secure Element. |
| ERROR_CERTIFICATE_EXPIRED | The certificate with the public key for the verification of the appropriate type of log messages is expired. Even if a certificate expired, the log message parts are created by the Secure Element and stored by the SE API. |
| ERROR_SECURE_ELEMENT_DISABLED | The Secure Element has been disabled. |

### Example 1 (with default TSE)

```C
char *clientId = "Kasse_1";
char *tseId = "TSE_1";
uint32_t transaction_number = 0;
unsigned char *serial_number = NULL;
uint32_t serial_number_len = 0;
uint32_t signature_counter = 0;
unsigned char *signature_value = NULL;
uint32_t signature_value_len = 0;
int64_t log_time = 0;

if (startTransaction(
    clientId, (uint32_t) strlen(clientId),
    NULL, 0,	
    NULL, 0,
    NULL, 0,
    &transaction_number,
    &log_time,
    &serial_number,
    &serial_number_len,
    &signature_counter,
    &signature_value,
    &signature_value_len) == EXECUTION_OK) {

    struct tm *timeinfo = gmtime(&log_time);
    (void) strftime(buffer, 80, "%FT%T%z", timeinfo);
    (void) printf("start_transaction: \n  log_time: %s \n  "
    	"transaction_number: %d \n  signature_counter: %d \n  ",
    	buffer, transaction_number,
    	signature_counter);

    (void) printf("signature_value: ");
    for (uint32_t i = 0; i < signature_value_len; ++i) { (void) printf("%02x", signature_value[i]); }
    (void) printf("\n");

    (void) printf("serial_number: ");
    for (uint32_t i = 0; i < serial_number_len; ++i) { (void) printf("%02x", serial_number[i]); }
    (void) printf("\n\n");

    at_free(&serial_number);
    at_free(&signature_value);;
}
```

### Example 2 (with TSE)

```C
char *clientId = "Kasse_1";
uint32_t transaction_number = 0;
unsigned char *serial_number = NULL;
uint32_t serial_number_len = 0;
uint32_t signature_counter = 0;
unsigned char *signature_value = NULL;
uint32_t signature_value_len = 0;
int64_t log_time = 0;

if (startTransactionWithTse(
    clientId, (uint32_t) strlen(clientId),
    NULL, 0,	
    NULL, 0,
    NULL, 0,
    &transaction_number,
    &log_time,
    &serial_number,
    &serial_number_len,
    &signature_counter,
    &signature_value,
    &signature_value_len,
    tseId, (uint32_t) strlen(tseId))) == EXECUTION_OK) {

    struct tm *timeinfo = gmtime(&log_time);
    (void) strftime(buffer, 80, "%FT%T%z", timeinfo);
    (void) printf("start_transaction: \n  log_time: %s \n  "
    	"transaction_number: %d \n  signature_counter: %d \n  ",
    	buffer, transaction_number,
    	signature_counter);

    (void) printf("signature_value: ");
    for (uint32_t i = 0; i < signature_value_len; ++i) { (void) printf("%02x", signature_value[i]); }
    (void) printf("\n");

    (void) printf("serial_number: ");
    for (uint32_t i = 0; i < serial_number_len; ++i) { (void) printf("%02x", serial_number[i]); }
    (void) printf("\n\n");

    at_free(&serial_number);
    at_free(&signature_value);;
}
```

## updateTransaction

Updates an open transaction.

**[DSFinV-K 2.0](https://www.bzst.de/DE/Unternehmen/Aussenpruefungen/DigitaleSchnittstelleFinV/digitaleschnittstellefinv_node.html) specifies, that the updateTransaction call is not used in order to make a transaction.**

**C**
```C
int32_t updateTransaction(const char *clientId,
                          uint32_t clientIdLength,
                          uint32_t transactionNumber,
                          const uint8_t *processData,
                          uint32_t processDataLength,
                          const char *processType,
                          uint32_t processTypeLength,
                          int64_t *logTime,
                          uint8_t **signatureValue,
                          uint32_t *signatureValueLength,
                          uint32_t *signatureCounter);
```

```C
int32_t updateTransactionWithTse(const char *clientId,
                                 uint32_t clientIdLength,
                                 uint32_t transactionNumber,
                                 const uint8_t *processData,
                                 uint32_t processDataLength,
                                 const char *processType,
                                 uint32_t processTypeLength,
                                 int64_t *logTime,
                                 uint8_t **signatureValue,
                                 uint32_t *signatureValueLength,
                                 uint32_t *signatureCounter,
                                 const char *tseId,
                                 uint32_t  tseIdLength);
```
**COM**
```C
HRESULT UpdateTransaction([in] BSTR clientId,
	[in] LONG transactionNumber,
	[in] BSTR processData,
	[in] BSTR processType,
	[out] DATE* logTime,
	[out] SAFEARRAY(BYTE)* signaturetvalue,
	[out] LONG* signatureCounter,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| clientId | in | REQUIRED | Represents the ID of the application that has invoked the function. |
| clientIdLength | in | REQUIRED | Length of the array that represents the clientId. |
| transactionNumber | in | REQUIRED | Parameter is used to unambiguously identify the current transaction. |
| processData | in | REQUIRED | Represents all the new information regarding the state of the process since the start of the corresponding transaction or its last update. |
| processDataLength | in | REQUIRED | Length of the array that represents the processData. |
| processType | in | OPTIONAL | Identifies the type of the transaction as defined by the application. This string MUST NOT contain more than 100 characters. |
| processTypeLength | in | REQUIRED | Length of the string that represents the processType. If a null pointer is passed for the parameter processType, the value for the length SHALL be 0. |
| logTime | out | CONDITIONAL | Represents the point in time of the Secure Element when the log message was created. |
| signatureValue | out | CONDITIONAL | Represents the signature value. |
| signatureValueLength | out | CONDITIONAL | Length of the array that represents the signature value if a value for the output parameter signatureValue is returned. |
| signatureCounter | out | CONDITIONAL | Represents the current value of the signature counter. |
| tseId | in | (REQUIRED) | ID of the TSE to use in a multi-TSE environment. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UPDATE_TRANSACTION_FAILED | The execution of the Secure Element functionality to update a transaction failed. |
| ERROR_RETRIEVE_LOG_MESSAGE_FAILED | The execution of the Secure Element functionality to retrieve log message parts has failed. |
| ERROR_STORAGE_FAILURE | Storing of the log message failed. |
| ERROR_NO_TRANSACTION | No transaction is known to be open under the provided transaction number. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |
| ERROR_TIME_NOT_SET | The managed data/time in the Secure Element has not been updated after the initialization of the SE API. or a period of absence of current for the Secure Element. |
| ERROR_CERTIFICATE_EXPIRED | The certificate with the public key for the verification of the appropriate type of log messages is expired. Even if a certificate expired, the log message parts are created by the Secure Element and stored by the SE API. |
| ERROR_SECURE_ELEMENT_DISABLED | The Secure Element has been disabled. |

## finishTransaction

Finishes an open transaction.

**C**
```C
int32_t finishTransaction(const char *clientId,
                          uint32_t  clientIdLength,
                          uint32_t transactionNumber,
                          const uint8_t *processData,
                          uint32_t processDataLength,
                          const char *processType,
                          uint32_t processTypeLength,
                          const uint8_t *additionalData,
                          uint32_t additionalDataLength,
                          int64_t *logTime,
                          uint8_t **signatureValue,
                          uint32_t *signatureValueLength,
                          uint32_t *signatureCounter);
```

```C
int32_t finishTransactionWithTse(const char *clientId,
                                 uint32_t  clientIdLength,
                                 uint32_t transactionNumber,
                                 const uint8_t *processData,
                                 uint32_t processDataLength,
                                 const char *processType,
                                 uint32_t processTypeLength,
                                 const uint8_t *additionalData,
                                 uint32_t additionalDataLength,
                                 int64_t *logTime,
                                 uint8_t **signatureValue,
                                 uint32_t *signatureValueLength,
                                 uint32_t *signatureCounter
                                 const char *tseId,
                                 uint32_t  tseIdLength);
```
**COM**
```C
HRESULT FinishTransaction([in] BSTR clientId,
	[in] LONG transactionNumber,
	[in] BSTR processData,
	[in] BSTR processType,
	[in] BSTR additionalData,
	[out] DATE* logTime,
	[out] LONG* signatureCounter,
	[out] SAFEARRAY(BYTE)* signaturetvalue,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| clientId | in | REQUIRED | Represents the ID of the application that has invoked the function. |
| clientIdLength | in | REQUIRED | Length of the array that represents the clientId. |
| transactionNumber | in | REQUIRED | Parameter is used to unambiguously identify the current transaction. |
| processData | in | REQUIRED | Represents all the information regarding the final state of the process. |
| processDataLength | in | REQUIRED | Length of the array that represents the processData. |
| processType | in | OPTIONAL | Identifies the type of the transaction as defined by the application. This string MUST NOT contain more than 100 characters. |
| processTypeLength | in | REQUIRED | Length of the string that represents the processType. If a null pointer is passed for the parameter processType, the value for the length SHALL be 0. |
| additionalData | in | OPTIONAL | Reserved for future use. |
| additionalDataLength | in | REQUIRED | Length of the array that represents the additionalData. If a null pointer is passed for the parameter additionalData, the value for the length SHALL be 0. |
| logTime | out | REQUIRED | Represents the point in time of the Secure Element when the log message was created. |
| signatureValue | out | OPTIONAL | Represents the signature value. |
| signatureValueLength | out | OPTIONAL | Length of the array that represents the signature value if a value for the output parameter signatureValue is returned. |
| signatureCounter | out | REQUIRED | Represents the current value of the signature counter. |
| tseId | in | (REQUIRED) | ID of the TSE to use in a multi-TSE environment. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_FINISH_TRANSACTION_FAILED | The execution of the Secure Element functionality to finish a transaction failed. |
| ERROR_RETRIEVE_LOG_MESSAGE_FAILED | The execution of the Secure Element functionality to retrieve log message parts has failed. |
| ERROR_STORAGE_FAILURE | Storing of the log message failed. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |
| ERROR_TIME_NOT_SET | The managed data/time in the Secure Element has not been updated after the initialization of the SE API or a period of absence of current for the Secure Element. |
| ERROR_CERTIFICATE_EXPIRED | The certificate with the public key for the verification of the appropriate type of log messages is expired. Even if a certificate expired, the log message parts are created by the Secure Element and stored by the SE API. |
| ERROR_SECURE_ELEMENT_DISABLED | The Secure Element has been disabled. |

### Example 

```C
char *clientId = "Kasse_1";
char *processType = "Kassenbeleg-V1";
unsigned char *processData = "Beleg^75.33_7.99_0.00_0.00_0.00^10.00:Bar_5.00:Bar:CHF_5.00:Bar:USD_64.30:Unbar";
uint32_t signature_counter = 0;
unsigned char *signature_value = NULL;
uint32_t signature_value_len = 0;
int64_t log_time = 0;

uint32_t transaction_number = 1234;    /* should be a valid transaction number */

if (finishTransaction(
    clientId, (uint32_t) strlen(clientId),
    transaction_number,
    (const uint8_t *) processData, (uint32_t) strlen(processData),
    processType, (uint32_t) strlen(processType),
    NULL, 0,
    &log_time,
    &signature_value,
    &signature_value_len,
    &signature_counter) == EXECUTION_OK) {

    struct tm *timeinfo = gmtime(&log_time);
    (void) strftime(buffer, 80, "%FT%T%z", timeinfo);
    (void) printf("finish_transaction: \n  log_time: %s \n  "
        "transaction_number: %d \n  signature_counter: %d \n  ",
        buffer, transaction_number, 
        signature_counter);

    (void) printf("signature_value: ");
    for (unsigned long i = 0; i < signature_value_len; ++i) { (void) printf("%02x", signature_value[i]); }
    (void) printf("\n\n");

    at_free(&signature_value);
}

```

# exportDataFilteredByTransactionNumberAndClientId

Exports the transaction log messages, containing the process and protocol data, that are relevant for a certain interval of transactions. The transaction log messages in this interval correspond to the passed clientId.

Additionally, the function exports all system log messages and audit log messages whose signature counters are contained in the interval.

Furthermore, additional files are exported that are needed to verify the signatures in the log messages.

**C**
```C
int32_t exportDataFilteredByTransactionNumberAndClientId(uint32_t transactionNumber,
                                                         const char *clientId,
                                                         uint32_t clientIdLength,
                                                         uint8_t **exportedData,
                                                         uint32_t *exportedDataLength);
```

```C
int32_t exportDataFilteredByTransactionNumberAndClientIdWithTse(uint32_t transactionNumber,
                                                                const char *clientId,
                                                                uint32_t clientIdLength,
                                                                uint8_t **exportedData,
                                                                uint32_t *exportedDataLength,
                                                                const char *tseId,
                                                                uint32_t  tseIdLength)
```
**COM**
```C
HRESULT ExportDataFilteredByTransactionNumberAndClientId([in] LONG transactionNumber,
	[in] BSTR clientId,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| transactionNumber | in | REQUIRED | Defines the transaction number (inclusive) regarding the start of the interval of relevant log messages. |
| clientId | in | REQUIRED | ID of a client application that has used the API to log transactions. Only transaction log messages that corresponds to the clientId are relevant for the export. |
| clientIdLength | in | REQUIRED | Length of the array that represents the clientId. |
| maximumNumberRecords | in | REQUIRED | If the value of this parameter is not 0, the function only returns the log messages if the number of relevant records is less or equal to the number of maximum records. If the value of the parameter is 0, the function returns all selected log messages. |
| exportedData | out | REQUIRED | Selected log messages and additional files needed to verify the signatures included in the log messages. |
| exportedDataLength | out | REQUIRED | Length of the array that represents the exportedData. |
| tseId | in | (REQUIRED) | ID of the TSE to use in a multi-TSE environment. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_PARAMETER_MISMATCH | Mismatch in parameters of the function. |
| ERROR_TRANSACTION_NUMBER_NOT_FOUND | No data has been found for the provided transaction numbers. |
| ERROR_ID_NOT_FOUND | No data has been found for the provided clientId. |
| ERROR_TOO_MANY_RECORDS | The amount of requested records exceeds the parameter maximumNumberRecords. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |

### Example

```C
char *clientId = "Kasse_1";
uint8_t *export_data = NULL;
uint32_t export_data_len = 0;

uint32_t transaction_number = 1234;    /* should be a valid transaction number */

if (exportDataFilteredByTransactionNumberAndClientId(
    transaction_number,
    clientId, (uint32_t) strlen(clientId),
    &export_data,
    &export_data_len) == EXECUTION_OK) {

    /* some code */

    at_free(&export_data);
    export_data_len = 0;
}
```

## exportDataFilteredByTransactionNumber

Exports the transaction log messages, containing the process and protocol data, that correspond to a certain transaction.

Additionally, the function exports all system log messages and audit log messages whose signature counters are contained in the following interval:
Signature counter of the transaction log message for the start of the transaction and the signature counter of the transaction log message for the end of the transaction (inclusive)

Furthermore, additional files are exported that are needed to verify the signatures in the log messages.

**C**
```C
int32_t exportDataFilteredByTransactionNumber(uint32_t transactionNumber,
                                              uint8_t **exportedData,
                                              uint32_t *exportedDataLength);;
```

**COM**
```C
HRESULT ExportDataFilteredByTransactionNumber([in] LONG transactionNumber,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);
```


This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| transactionNumber | in | REQUIRED | Indicates the transaction whose corresponding log messages are relevant for the export. |
| exportedData | out | REQUIRED | Selected log messages and additional files needed to verify the signatures included in the log messages. |
| exportedDataLength | out | REQUIRED | Length of the array that represents the exportedData. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_TRANSACTION_NUMBER_NOT_FOUND | No data has been found for the provided transactionNumber. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |

### Example 

```C
uint8_t *export_data = NULL;
uint32_t export_data_len = 0;

uint32_t transaction_number = 1234;    /* should be a valid transaction number */

if (exportDataFilteredByTransactionNumber(
    transaction_number,
    &export_data,
    &export_data_len) == EXECUTION_OK) {

    /* some code */

    at_free(&export_data);
    export_data_len = 0;
}
```

## exportDataFilteredByTransactionNumberInterval

Exports the transaction log messages, containing the process and protocol data, that are relevant for a certain interval of transactions.

Additionally, the function exports all system log messages and audit log messages whose signature counters are contained in this interval.

Furthermore, additional files are exported that are needed to verify the signatures in the log messages.

**C**
```C
int32_t exportDataFilteredByTransactionNumberInterval(uint32_t startTransactionNumber,
                                                      uint32_t endTransactionNumber,
                                                      uint32_t maximumNumberRecords,
                                                      uint8_t **exportedData,
                                                      uint32_t *exportedDataLength);
```
**COM**
```C
HRESULT ExportDataFilteredByTransactionNumberInterval([in] LONG startTransactionNumber,
	[in] LONG endTransactionNumber,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);
```

This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| startTransactionNumber | in | REQUIRED | Defines the transaction number (inclusive) regarding the start of the interval of relevant log messages. |
| endTransactionNumber | in | REQUIRED | Defines the transaction number (inclusive) regarding the end of the interval of relevant log messages. |
| maximumNumberRecords | in | REQUIRED | If the value of this parameter is not 0, the function only returns the log messages if the number of relevant records is less or equal to the number of maximum records. If the value of the parameter is 0, the function returns all selected log messages. |
| exportedData | out | REQUIRED | Selected log messages and additional files needed to verify the signatures included in the log messages. |
| exportedDataLength | out | REQUIRED | Length of the array that represents the exportedData. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_PARAMETER_MISMATCH | Mismatch in parameters of the function. |
| ERROR_TRANSACTION_NUMBER_NOT_FOUND | No data has been found for the provided transaction numbers. |
| ERROR_TOO_MANY_RECORDS | The amount of requested records exceeds the parameter maximumNumberRecords. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |

### Example

```C
uint8_t *export_data = NULL;
uint32_t export_data_len = 0;

uint32_t transaction_number = 1234;    /* should be a valid transaction number */

if (exportDataFilteredByTransactionNumberInterval(
    start_transaction_number,
    end_transaction_number,
    0,
    &export_data,
    &export_data_len) == EXECUTION_OK) {

    /* some code */

    at_free(&export_data);
    export_data_len = 0;
}
```

## exportDataFilteredByTransactionNumberIntervalAndClientId

Exports the transaction log messages, containing the process and protocol data, that are relevant for a certain interval of transactions. The transaction log messages in this interval corresponds to the passed clientId.

Additionally, the function SHALL export all system log messages and audit log messages whose signature counters are contained in the interval.

Furthermore, additional files are exported that are needed to verify the signatures in the log messages.

**C**
```C
int32_t exportDataFilteredByTransactionNumberIntervalAndClientId(uint32_t startTransactionNumber,
                                                                 uint32_t endTransactionNumber,
                                                                 const int8_t *clientId,
                                                                 uint32_t clientIdLength,
                                                                 uint32_t maximumNumberRecords,
                                                                 uint8_t **exportedData,
                                                                 uint32_t *exportedDataLength);
```
**COM**
```C
HRESULT ExportDataFilteredByTransactionNumberIntervalAndClientId([in] LONG startTransactionNumber,
	[in] LONG endTransactionNumber,
	[in] BSTR clientId,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);
```


This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| startTransactionNumber | in | REQUIRED | Defines the transaction number (inclusive) regarding the start of the interval of relevant log messages. |
| endTransactionNumber | in | REQUIRED | Defines the transaction number (inclusive) regarding the end of the interval of relevant log messages. |
| clientId | in | REQUIRED | ID of a client application that has used the API to log transactions. Only transaction log messages that corresponds to the clientId are relevant for the export. |
| clientIdLength | in | REQUIRED | Length of the array that represents the clientId. |
| maximumNumberRecords | in | REQUIRED | If the value of this parameter is not 0, the function only returns the log messages if the number of relevant records is less or equal to the number of maximum records. If the value of the parameter is 0, the function SHALL return all selected log messages. |
| exportedData | out | REQUIRED | Selected log messages and additional files needed to verify the signatures included in the log messages. |
| exportedDataLength | out | REQUIRED | Length of the array that represents the exportedData. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_PARAMETER_MISMATCH | Mismatch in parameters of the function. |
| ERROR_TRANSACTION_NUMBER_NOT_FOUND | No data has been found for the provided transaction numbers. |
| ERROR_ID_NOT_FOUND | No data has been found for the provided clientId. |
| ERROR_TOO_MANY_RECORDS | The amount of requested records exceeds the parameter maximumNumberRecords. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |

### Example

```C
char *clientId = "Kasse_1";
uint8_t *export_data = NULL;
uint32_t export_data_len = 0;

uint32_t start_transaction_number = 1234;    /* should be a valid transaction number */
uint32_t end_transaction_number   = 1238;    /* should be a valid transaction number */

if (exportDataFilteredByTransactionNumberIntervalAndClientId(
    start_transaction_number, 
    end_transaction_number,
    clientId, (uint32_t) strlen(clientId),
    0,
    &export_data, 
    &export_data_len) == EXECUTION_OK) {

    /* some code */

    at_free(&export_data);
    export_data_len = 0;
}
```

## exportDataFilteredByPeriodOfTime

Exports the transaction log messages, system log messages and audit log messages that have been created in a certain period of time.

Furthermore, additional files are exported that are needed to verify the signatures in the log messages.

**C**
```C
int32_t exportDataFilteredByPeriodOfTime(int64_t startDate,
                                         int64_t endDate,
                                         uint32_t maximumNumberRecords,
                                         uint8_t **exportedData,
                                         uint32_t *exportedDataLength);
```
**COM**
```C
HRESULT ExportDataFilteredByPeriodOfTime([in] DATE startDate,
	[in] DATE endDate,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);
```

This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| startDate | in | CONDITIONAL | Defines the starting time (inclusive) for the period in that the relevant log messages have been created. See [DateFormat](#date-format). If a value for the input parameter endDate is passed, startDate is [OPTIONAL]. If no value for the input parameter endDate is passed, startDate is [REQUIRED].
| endDate | in | CONDITIONAL | Defines the end time (inclusive) for the period in that relevant log messages have been created. See [DateFormat](#date-format). If a value for the input parameter startDate is passed, endDate is [OPTIONAL]. If no value for the input parameter startDate is passed, endDate is [REQUIRED]. |
| maximumNumberRecords | in | REQUIRED | If the value of this parameter is not 0, the function only returns the log messages if the number of relevant records is less or equal to the number of maximum records. If the value of the parameter is 0, the function returns all selected log messages. |
| exportedData | out | REQUIRED | Selected log messages and additional files needed to verify the signatures included in the log messages. |
| exportedDataLength | out | REQUIRED | Length of the array that represents the exportedData. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_PARAMETER_MISMATCH | Mismatch in parameters of the function. |
| ERROR_NO_DATA_AVAILABLE | No data has been found for the provided selection. |
| ERROR_TOO_MANY_RECORDS | The amount of requested records exceeds the parameter maximumNumberRecords. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |

### Example 

```C
uint8_t *export_data = NULL;
uint32_t export_data_len = 0;

int64_t start_time = 0;
time(&start_time);
int64_t end_time = start_time + 59;

if (exportDataFilteredByPeriodOfTime(
    start_time,
    end_time,
    100,
    &export_data,
    &export_data_len) == EXECUTION_OK) {

    /* some code */

    at_free(&export_data);
    export_data_len = 0;
}
```

## exportDataFilteredByPeriodOfTimeAndClientId

Exports the transaction log messages, system log messages and audit log messages that have been created in a certain period of time.

The transaction log messages in this period of time corresponds to the passed clientId.

Furthermore, additional files are exported that are needed to verify the signatures in the log messages.

**C**
```C
int32_t exportDataFilteredByPeriodOfTimeAndClientId(int64_t startDate,
                                                    int64_t endDate,
                                                    const int8_t *clientId,
                                                    uint32_t clientIdLength,
                                                    uint32_t maximumNumberRecords,
                                                    uint8_t **exportedData,
                                                    uint32_t *exportedDataLength);
```
**COM**
```C
HRESULT ExportDataFilteredByPeriodOfTimeAndClientId([in] DATE startDate,
	[in] DATE endDate,
	[in] BSTR clientId,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);
```

This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| startDate | in | CONDITIONAL | Defines the starting time (inclusive) for the period in that the relevant log messages have been created. See [DateFormat](#date-format). If a value for the input parameter endDate is passed, startDate is [OPTIONAL]. If no value for the input parameter endDate is passed, startDate is [REQUIRED]. |
| endDate | in | CONDITIONAL | Defines the end time (inclusive) for the period in that relevant log messages have been created. See [DateFormat](#date-format). If a value for the input parameter startDate is passed, endDate is [OPTIONAL]. If no value for the input parameter startDate is passed, endDate is [REQUIRED]. |
| clientId | in | REQUIRED | ID of a client application that has used the API to log transactions. Only transaction log messages that corresponds to the clientId are relevant for the export. |
| clientIdLength | in | Length of the array that represents the clientId. |
| maximumNumberRecords | in | REQUIRED | If the value of this parameter is not 0, the function only returns the log messages if the number of relevant records is less or equal to the number of maximum records. If the value of the parameter is 0, the function SHALL return all selected log messages. | 
| exportedData | out | REQUIRED | Selected log messages and additional files needed to verify the signatures included in the log messages. |
| exportedDataLength | out | REQUIRED|  Length of the array that represents the exportedData. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_PARAMETER_MISMATCH | Mismatch in parameters of the function. |
| ERROR_NO_DATA_AVAILABLE | No data has been found for the provided selection. |
| ERROR_ID_NOT_FOUND | No data has been found for the provided clientId. |
| ERROR_TOO_MANY_RECORDS | The amount of requested records exceeds the parameter maximumNumberRecords. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |

### Example 

```C
char *clientId = "Kasse_1";
uint8_t *export_data = NULL;
uint32_t export_data_len = 0;

int64_t start_time = 0;
time(&start_time);
int64_t end_time = start_time + 59;

if (exportDataFilteredByPeriodOfTimeAndClientId(
    start_time,
    end_time,
    clientId, (uint32_t) strlen(clientId),
    0,
    &export_data,
    &export_data_len) == EXECUTION_OK) {

    /* some code */

    at_free(&export_data);
    export_data_len = 0;
}
```

## exportData

Exports all stored transaction log messages, system log message and audit log messages.
Furthermore, additional files are exported that are needed to verify the signatures in the log messages.

**C**
```C
int32_t exportData(uint32_t maximumNumberRecords,
                   uint8_t **exportedData,
                   uint32_t *exportedDataLength);
```
**COM**
```C
HRESULT ExportData([in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);
```

This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| maximumNumberRecords | in | REQUIRED | If the value of this parameter is not 0, the function only returns the log messages if the number of relevant records is less or equal to the number of maximum records. If the value of the parameter is 0, the function returns all stored log messages. |
| exportedData | out | REQUIRED | All stored log messages and additional files needed to verify the signatures included in the log messages. |
| exportedDataLength | out | REQUIRED | Length of the array that represents the exportedData. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_TOO_MANY_RECORDS | The amount of requested records exceeds the parameter maximumNumberRecords. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |

### Example 

```C
uint8_t *export_data = NULL;
uint32_t export_data_len = 0;

if (exportData(
    0,
    &export_data,
    &export_data_len) == EXECUTION_OK) {

    /* some code */
    at_free(&export_data);
    export_data_len = 0;
}
```

## exportCertificates

Exports the certificates of the certificate chains. These certificates belong to
the public keys of the key pairs that are used for the creation of signature values in log messages.

**C**
```C
int32_t exportCertificates(uint8_t **certificates,
                           uint32_t *certificatesLength);
```
**COM**
```C
HRESULT ExportCertificates([out] SAFEARRAY(BYTE)* certificate, [out, retval] LONG* returnCode);
```

This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| certificates | out | REQUIRED | The TAR archive that contains all certificates that are necessary for the verification of log messages. The format of the TAR archive and the contained certificates SHALL conform to BSI TR-03151. |
| certificatesLength | out | Length of the array that represent the certificate(s). |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_EXPORT_CERT_FAILED | The collection of the certificates for the export failed. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |

### Example

```C
uint8_t *certificates = NULL;
uint32_t certificates_len = 0;

if (exportCertificates(
    &certificates,
    &certificates_len) == EXECUTION_OK) {

    /* some code */

    at_free(&certificates);
    certificates_len = 0;
}
```

## readLogMessage

Reads a log message that bases on the last log message parts that have been produced and processed by the Secure Element.

**C**
```C
int32_t readLogMessage(uint8_t **logMessage,
                       uint32_t *logMessageLength);
```
**COM**
```C
HRESULT ReadLogMessage(
	[out] SAFEARRAY(BYTE)* logMessage,
	[out, retval] LONG* returnCode);
```

This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| logMessage | out | REQUIRED | Contains the last log message that the Secure Element has produced. |
| logMessageLength | out | REQUIRED | Length of the array that represents the last log message. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_NO_LOG_MESSAGE | No log message parts are found. |
| ERROR_READING_LOG_MESSAGE | Error while retrieving log message parts. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |
| ERROR_SECURE_ELEMENT_DISABLED | The Secure Element has been disabled. |

### Example

```C
uint8_t *log_rec = NULL;
uint32_t log_rec_len = 0;

if (readLogMessage(
    &log_rec,
    &log_rec_len) == EXECUTION_OK) {

    /* some code */

    at_free(&log_rec);
}
```

## exportSerialNumbers

Exports the serial number(s) of the SE API. A serial number is a hash value of a public key that belongs to a key pair whose private key is used to create signature values of log messages.

**C**
```C
int32_t exportSerialNumbers(uint8_t **serialNumbers,
                            uint32_t *serialNumbersLength);
```
**COM**
```C
HRESULT ExportSerialNumbers([out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);
```

This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| serialNumbers | out | REQUIRED | The serial number(s) of the SE API. The serial number(s) SHALL be encoded in the TLV structure defined in BSI TR-03151. |
| serialNumbersLength | out | REQUIRED | Length of the array that represents the serial number(s). |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_EXPORT_SERIAL_NUMBERS_FAILED | The collection of the serial number(s) failed. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |

### Example

```C
uint8_t *serial_numbers = NULL;
uint32_t serial_numbers_len = 0;

if (exportSerialNumbers(
    &serial_numbers,
    &serial_numbers_len) == EXECUTION_OK) {

    /* some code */

    at_free(&serial_numbers);
    serial_numbers_len = 0;
}
```

# User Management

## updateTime

 The function updateTime updates the current date/time that is maintained by the Secure Element by passing a new date/time value.

**Since the TSE runs on a HSM in the A-Trust data center the time is managed by A-Trust.**

**This function is not supported**

**C**
```C
int32_t updateTime(int64_t newDateTime);
```
**COM**
```C
HRESULT UpdateTime(
	[in] DATE newDateTime,
	[out, retval] LONG* returnCode);
```

## updateTimeWithTimeSync

The function updateTimeWithTimeSync updates the current date/time that is maintained by the Secure Element by using the functionality for time synchronization of the Secure Element.

**Since the TSE runs on a HSM in the A-Trust data center the time is managed by A-Trust.**

**This function is not supported**

**C**
```C
int32_t updateTimeWithTimeSync(void);
```
**COM**
```C
HRESULT UpdateTimeWithTimeSync(
	[out, retval] LONG* returnCode);
```

## initializeDescriptionNotSet

**C**
```C
int32_t initializeDescriptionNotSet(char *description,
                                    uint32_t descriptionLength);
```
**COM**
```C
HRESULT InitializeDescriptionNotSet(
	[in] BSTR description,
	[out, retval] LONG* returnCode);
```

This function also exists with ```withTse```.

The function initializeDescriptionNotSet starts the initialization of the SE API by the operator of the corresponding application.

The initialization data in form of the description of the SE API is passed by the input parameter description.

The description of the SE API MUST NOT have been set by the manufacturer.



## initializeDescriptionSet

The function initializeDescriptionSet starts the initialization of the SE API by the operator of the corresponding application.

The description of the SE API SHALL have been set by the manufacturer.



**C**
```C
int32_t initializeDescriptionSet(void);
```
**COM**
```C
HRESULT InitializeDescriptionSet(
	[out, retval] LONG* returnCode);
```

This function also exists with ```withTse```.

## disableSecureElement

 The function disableSecureElement disables the Secure Element in a way that none of its functionality can be used anymore.

**C**
```C
int32_t disableSecureElement(void);
```
**COM**
```C
HRESULT DisableSecureElement([out, retval] LONG* returnCode);
```


This function also exists with ```withTse```.

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_DISABLE_SECURE_ELEMENT_FAILED | The deactivation of the Secure Element failed. |
| ERROR_TIME_NOT_SET | The managed data/time in the Secure Element has not been updated after the initialization of the SE API or a period of absence of current for the Secure Element. |
| ERROR_RETRIEVE_LOG_MESSAGE_FAILED | Execution of the Secure Element functionality to retrieve log message parts has failed. |
| ERROR_STORAGE_FAILURE | Storing of the data of the log message has failed. |
| ERROR_CERTIFICATE_EXPIRED | The certificate with the public key for the verification of the appropriate type of log messages is expired. Even if a certificate expired, the log message parts are created by the Secure Element and stored by the SE API. |
| ERROR_SECURE_ELEMENT_DISABLED | The Secure Element has been disabled. |
| ERROR_USER_NOT_AUTHORIZED | The user who has invoked the function disableSecureElement is not authorized to execute this function. |
| ERROR_USER_NOT_AUTHENTICATED | The user who has invoked the function disableSecureElement has not the status authenticated. |

### Example

```C
enum AuthenticationResult auth_result = 0;
int16_t retries = 0;
char *password = "Geheim";

if (authenticateUser(
    NULL, 0,
    password, (uint32_t) sizeof(password)
    &auth_result,
    &retries) == EXECUTION_OK) {

    if (auth_result == auth_ok) {
        (void) disableSecureElement();
        (void) logOut(NULL, 0);
    }
}
```

## restoreFromBackup

Restores a backup in the SE API and storage. The backup data includes log messages and certificates that have been exported by using the exportData function.
Log messages and certificates are passed in the TAR archive that has been returned during the export of the log messages and certificates.

**This function is not supported.**

**C**
```C
int32_t restoreFromBackup(uint8_t *restoreData,
                          uint32_t restoreDataLength);
```

## deleteStoredData

Deletes all data that is stored in the storage. The function deletes only data that has been exported.

**C**
```C
int32_t deleteStoredData(void);
```
**COM**
```C
HRESULT DeleteStoredData([out, retval] LONG* returnCode);
```

This function also exists with ```withTse```.

**This function is currently not supported.**

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_DELETE_STORED_DATA_FAILED | The deletion of the data from the storage failed. |
| ERROR_UNEXPORTED_STORED_DATA | The deletion of data from the storage failed because the storage contains data that has not been exported. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |
| ERROR_USER_NOT_AUTHORIZED | The user who has invoked the function deleteStoredData is not authorized to execute this function. | 
| ERROR_USER_NOT_AUTHENTICATED | The user who has invoked the function deleteStoredData has not the status authenticated. | 

## getMaxNumberOfClients

Supplies the maximal number of clients that can use the functionality to log transactions of the SE API simultaneously.

**C**
```C
int32_t getMaxNumberOfClients(uint32_t *maxNumberClients);
```
**COM**
```C
HRESULT GetMaxNumberOfClients(
	[out] LONG* maxNumberClients,
	[out, retval] LONG* returnCode);

```

This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| maxNumberClients | out | REQUIRED | Maximum number of clients that can use the functionality to log transactions of the SE API simultaneously. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_GET_MAX_NUMBER_OF_CLIENTS_FAILED | The determination of the maximum number of clients that could use the SE API simultaneously failed. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |
| ERROR_SECURE_ELEMENT_DISABLED | The Secure Element has been disabled. |

### Example

```C
uint32_t max_number_of_clients = 0;

if (getMaxNumberOfClients(
    &max_number_of_clients) == EXECUTION_OK) {
    (void) printf("max_number_of_clients: %ld \n", 
        max_number_of_clients);
}
```

## getCurrentNumberOfClients

Supplies the number of clients that are currently using the functionality to log transactions of the SE API.

**C**
```C
int32_t getCurrentNumberOfClients(uint32_t *currentNumberClients)
```
**COM**
```C
HRESULT GetCurrentNumberOfClients(
	[out] LONG* currentNumberClients,
	[out, retval] LONG* returnCode);
```

This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| currentNumberClients | out | REQUIRED | The number of clients that are currently using the functionality of the SE API. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_GET_CURRENT_NUMBER_OF_CLIENTS_FAILED | The determination of the current number of clients using the SE API failed. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |
| ERROR_SECURE_ELEMENT_DISABLED | The Secure Element has been disabled. |

### Example

```C
uint32_t current_number_of_clients = 0;

if (getCurrentNumberOfClients(
    &current_number_of_clients) == EXECUTION_OK) {
    (void) printf("current_number_of_clients: %ld \n", 
        current_number_of_clients);
}
```

## getMaxNumberOfTransactions

Supplies the maximal number of simultaneously opened transactions that can be managed by the SE API.

**C**
```C
int32_t getMaxNumberOfTransactions(uint32_t *maxNumberTransactions);
```
**COM**
```C
HRESULT GetMaxNumberOfTransactions(
	[out] LONG* maxNumberTransactions,
	[out, retval] LONG* returnCode);
```
This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| maxNumberTransactions | out | REQUIRED | Maximum number of simultaneously opened transactions that can be managed by the SE API. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_GET_MAX_NUMBER_TRANSACTIONS_FAILED | The determination of the maximum number of transactions that can be managed simultaneously failed. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |
| ERROR_SECURE_ELEMENT_DISABLED | The Secure Element has been disabled. |

### Example

```C
uint32_t max_number_of_transactions = 0;

if (getMaxNumberOfTransactions(
    &max_number_of_transactions) == EXECUTION_OK) {
    (void) printf("max_number_of_transactions: %ld \n", 
        max_number_of_transactions);
}
```

## getCurrentNumberOfTransactions

Supplies the number of open transactions that are currently managed by the SE API.

**C**
```C
int32_t getCurrentNumberOfTransactions(uint32_t *currentNumberTransactions);
```
**COM**
```C
HRESULT GetCurrentNumberOfTransactions(
	[out] LONG* currentNumberTransactions,
	[out, retval] LONG* returnCode);
```
This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| currentNumberTransactions | out | REQUIRED | The number of open transactions that are currently managed by the SE API. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_GET_CURRENT_NUMBER_OF_TRANSACTIONS_FAILED | The determination of the number of open transactions that are currently managed by the SE API failed. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |
| ERROR_SECURE_ELEMENT_DISABLED | The Secure Element has been disabled. |

### Example

```C
uint32_t current_number_of_transactions = 0;

if (getCurrentNumberOfTransactions(
    &current_number_of_transactions) == EXECUTION_OK) {
    (void) printf("current_number_of_transactions: %ld \n", 
        current_number_of_transactions);
}
```

## getSupportedTransactionUpdateVariants

Supplies the supported variants to update transactions.

**C**
```C
int32_t getSupportedTransactionUpdateVariants(UpdateVariants *supportedUpdateVariants);
```
**COM**
```C
HRESULT GetSupportedTransactionUpdateVariants(
	[out] LONG* supportedUpdateVariants,
	[out, retval] LONG* returnCode);
```

This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| supportedUpdateVariants | out | REQUIRED | The supported variant(s) to update a transaction. | 

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_GET_SUPPORTED_UPDATE_VARIANTS_FAILED | The identification of the supported variant(s) to update transactions failed. |
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |
| ERROR_SECURE_ELEMENT_DISABLED | The Secure Element has been disabled. |

### Example

```C
enum UpdateVariants supported_update_variants = 0;

if (getSupportedTransactionUpdateVariants(
    &supported_update_variants) == EXECUTION_OK) {
    (void) printf("supported_update_variants: %ld \n", 
        supported_update_variants);

}
```

### UpdateVariants
Represents the variants that are supported by the Secure Element to update transactions.

```C
enum UpdateVariants {
    signedUpdate, unsignedUpdate, signedAndUnsignedUpdate
};
```
This function also exists with ```withTse```.

## authenticateUser

Enables an authorized user or application to authenticate to the SE API for the usage of restricted SE API functions.

**C**
```C
int32_t authenticateUser(const int8_t *userId,
                         uint32_t userIdLength,
                         const uint8_t *pin,
                         uint32_t pinLength,
                         AuthenticationResult *authenticationResult,
                         int16_t *remainingRetries);
```
**COM**
```C
HRESULT AuthenticateUser([in] BSTR userId,
	[in] SAFEARRAY(BYTE) pin,
	[out] LONG* authenticationResult,
	[out] LONG* remainingRetries,
	[out, retval] LONG* returnCode);
```

This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| userId | in | REQUIRED | The ID of the user who or application that wants to be authenticated. |
| userIdLength | in | REQUIRED | The length of the array that represents the userId. |
| pin | in | REQUIRED | The PIN for the authentication. |
| pinLength | in | REQUIRED | The length of the array that represents the pin. |
| authenticationResult | out | REQUIRED | The result of the authentication. |
| remainingRetries | out | REQUIRED | The number of remaining retries to enter a PIN. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_SIGNING_SYSTEM_OPERATION_DATA_FAILED | The determination of the log message parts for the system operation data by the Secure Element failed. |
| ERROR_RETRIEVE_LOG_MESSAGE_FAILED | The execution of the Secure Element functionality to retrieve log message parts has failed. |
| ERROR_STORAGE_FAILURE | Storing of the data of the log message failed. |
| ERROR_SECURE_ELEMENT_DISABLED | The Secure Element has been disabled. |

### Example

```C
enum AuthenticationResult auth_result = 0;
int16_t retries = 0;
char *password = "Geheim";

if (authenticateUser(
    NULL, 0,
    password, (uint32_t) sizeof(password)
    &auth_result,
    &retries) == EXECUTION_OK) {

    if (auth_result == auth_ok) {
        (void) disableSecureElement();
        (void) logOut(NULL, 0);
    }
}
```


### Authentication Result

Represents the result of an authentication attempt.
 * The value ok indicates that the authentication has been successful.
 * The value failed indicates that the authentication has failed.
 * The value pinIsBlocked indicates that the PIN entry for the userId was blocked before the authentication attempt.
 * The value unknownUserId indicates that the passed userId is not managed by the SE API.

```C
enum AuthenticationResult {
    auth_ok, auth_failed, auth_pinIsBlocked, auth_unknownUserId
};
```

## logOut

Enables the log out of an authenticated user or application from the SE API.

**C**
```C
int32_t logOut(const int8_t *userId,
               uint32_t userIdLength);

```
**COM**
```C
HRESULT LogOut(
	[in] BSTR userId,
	[out, retval] LONG* returnCode);
```
This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| userId | in | REQUIRED | The ID of the user who or application that wants to log out from the SE API.
| userIdLength | in | REQUIRED | The length of the array that represents the userId. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_USER_ID_NOT_MANAGED | The passed userId is not managed by the SE API. |
| ERROR_SIGNING_SYSTEM_OPERATION_DATA_FAILED | The determination of the log message parts for the system operation data by the Secure Element failed. |
| ERROR_USER_ID_NOT_AUTHENTICATED | The passed userId has not the status authenticated. |
| ERROR_RETRIEVE_LOG_MESSAGE_FAILED | The execution of the Secure Element functionality to retrieve log message parts has failed. |
| ERROR_STORAGE_FAILURE | Storing of the data of the log message failed. |
| ERROR_SECURE_ELEMENT_DISABLED | The Secure Element has been disabled. |

### Example

```C
enum AuthenticationResult auth_result = 0;
int16_t retries = 0;
char *password = "Geheim";

if (authenticateUser(
    NULL, 0,
    password, (uint32_t) sizeof(password)
    &auth_result,
    &retries) == EXECUTION_OK) {

    if (auth_result == auth_ok) {
        (void) logOut(NULL, 0);
    }

}
```

## unblockUser

Enables the unblocking for the entry of a PIN and the definition of a new PIN for the authentication of authorized users or applications.

**C**
```C
int32_t unblockUser(const int8_t *userId,
                    uint32_t userIdLength,
                    const int8_t *puk,
                    uint32_t pukLength,
                    const int8_t *newPin,
                    uint32_t newPinLength,
                    UnblockResult *unblockResult);
```
**COM**
```C
HRESULT UnblockUser(
	[in] BSTR userId,
	[in] BSTR puk,
	[in] BSTR newPin,
	[out] LONG* unblockResult,
	[out, retval] LONG* returnCode);
```

This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| userId | in | REQUIRED | The ID of the user who or application that wants to log out from the SE API.
| userIdLength | in | REQUIRED | The length of the array that represents the userId. |
| puk | in | REQUIRED | The PUK of the user/application. |
| pukLength | in | REQUIRED | The length of the array that represents the puk. |
| newPin | in | REQUIRED | The new PIN for the user/application. |
| newPinLength | in | REQUIRED | The length of the array that represents the newPin. |
| unblockResult | out | REQUIRED | The result of the unblock procedure. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| UNBLOCK_FAILED | If the execution of attempt to unblock a PIN entry has failed, the return value UNBLOCK_FAILED SHALL be returned. |
| ERROR_SIGNING_SYSTEM_OPERATION_DATA_FAILED | The determination of the log message parts for the system operation data by the Secure Element failed. |
| ERROR_RETRIEVE_LOG_MESSAGE_FAILED | The execution of the Secure Element functionality to retrieve log message parts has failed. |
| ERROR_STORAGE_FAILURE | Storing of the data of the log message failed. |
| ERROR_SECURE_ELEMENT_DISABLED | The Secure Element has already been disabled. |

### UnblockUser Result

Represents the result of the unblock process.

 * The value ok SHALL indicate that the unblocking has been successful.
 * The value failed SHALL indicate that the unblocking has failed.
 * The value unknownUserId SHALL indicate that the passed userId is not managed by the SE API.
 * The value error SHALL indicate that an error has occurred during the execution of the function unblockUser.

```C
enum UnblockResult {
    unbl_ok, unbl_failed, unbl_unknownUserId, unbl_error
};
```

## A-Trust API Functions

The following functions are not defined in the SE-API specification.  They are additional functions specific to our implementation.

## at_getVersion

Shows version information of the underlying TSE library.

**C**
```C
const int8_t *at_getVersion(void)
```
**COM**
```C
HRESULT AtGetVersion(
	[out] BSTR* version,
	[out, retval] LONG* returnCode);

```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

### Example

```C
```

## at_getSignatureAlgorithm

Supplies the friendly name of the ASN.1 encoded signature algorithm OID encoded into signed data.

**C**
```C
int32_t at_getSignatureAlgorithm(int8_t **signature_algorithm,
                                 uint32_t *signature_algorithm_length)
```
**COM**
```C
HRESULT AtGetSignatureAlgorithm(
	[out] BSTR* signatureAlgorithm,
	[out, retval] LONG* returnCode);

```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| signatureAlgorithm | out | REQUIRED | Buffer for signature algorithm. If successfully executed, the buffer has to freed by the function caller using the function `at_free`. |
| signatureAlgorithmLength | out | REQUIRED | Length of the signature algorithm buffer. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

### Example

```C
```
## at_getPublicKey

Export public key ot the certificate signing transaction logs.

**C**
```C
int32_t at_getPublicKey(uint8_t **pub_key,
                        uint32_t *pub_key_len)
```
**COM**
```C
HRESULT AtGetPublicKey(
	[out] SAFEARRAY(BYTE)* publicKey,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| publicKey | out | REQUIRED | Array that contains the requested public key. If successfully executed, the buffer has to freed by the function caller using the function `at_free`. |
| publicKeyLength | out | REQUIRED | The length of the array that represents the publicKey. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |


## at_getOpenTransactions

Returns a list of transactions which have been started with `startTransaction` but not yet been completed with `finishTransaction`.

**C**
```C
int32_t at_getOpenTransactions(uint32_t **transaction_numbers,
                               uint32_t *transaction_numbers_length)
```
**COM**
```C
HRESULT AtGetOpenTransactions(
	[out] SAFEARRAY(LONG)* transactions,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| transactionNumbers | out | REQUIRED | Array that represents the list of transactions. If successfully executed, the buffer has to freed by the function caller using the function `at_free`.|
| transactionNumbersLength | out | REQUIRED | Length of the array that represents the list of transactions. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## at_getSignatureCounter

Supplies the current signature counter (last used value) for the certificate signing transaction logs.

**C**
```C
int32_t at_getSignatureCounter(uint32_t *counter)
```
**COM**
```C
HRESULT AtGetSignatureCounter(
	[out] LONG* signatureCounter,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| signatureCounter | out | REQUIRED | The current signature counter. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## at_getTransactionCounter

Supplies the current transaction counter (last used value).

**C**
```C
int32_t at_getTransactionCounter(uint32_t *counter)
```
**COM**
```C
HRESULT AtGetTransactionCounter(
	[out] LONG* transactionCounter,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| transactionCounter | out | REQUIRED | The current transaction counter. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## at_getLifecycleState

Gets the lifecycle state of the TSE.

**C**
```C
int32_t at_getLifecycleState(LifecycleState *state)
```
**COM**
```C
HRESULT AtGetLifeCycleState(
	[out] LONG* lifecycleState,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| state | out | REQUIRED | The lifecycle state: 0=unknown, 1=not initialized, 2=active, 3=deactivated, 4=disabled |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## at_getSerialNumber

Gets the serial number (SHA256 hash of public key) of the transaction log signing key.

**C**
```C
int32_t at_getSerialNumber(uint8_t **serial,
                           uint32_t *serialLength);
```
**COM**
```C
HRESULT AtGetSerialNumber(
	[out] SAFEARRAY(BYTE)* serialNumber,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| serial | out | REQUIRED | Buffer for serial number bytes.  |
| serialLength | out | REQUIRED | Length of the serial number buffer. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## at_suspendSecureElement

Temporarily suspend the secure element.

**C**
```C
int32_t at_suspendSecureElement(void);
```
**COM**
```C
HRESULT AtSuspendSecureElement(
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## at_unsuspendSecureElement

Unsuspend the secure element.

**C**
```C
int32_t at_unsuspendSecureElement(void);
```
**COM**
```C
HRESULT AtUnsuspendSecureElement(
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## at_load

Initializes the library.  This function must be called exactly once on startup and before any other library call is done.

**C**
```C
int32_t at_load(void)
```
**COM**
```C
HRESULT AtLoad(
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## at_unload

The function `at_unload` must be called when closing the library. After `at_unload` has been called, no other function call to the library is allowed.

**C**
```C
int32_t at_unload(void)
```
**COM**
```C
HRESULT AtUnload(
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## at_free

Frees memory allocated by the asigntse shared memory.

```C
void at_free(void **ptr);
```
This function also exists with ```withTse```.

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| ptr | in | REQUIRED | Pointer to a Pointer to memory allocated by the asigntse shared library. |


## Configuration Functions

## cfgSetConfigFile

Set path to the config file.

**C**
```C
int32_t cfgSetConfigFile(const int8_t *path,
                         uint32_t pathLength)
```
**COM**
```C
HRESULT CfgSetConfigFile([in] BSTR path,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| path | in | REQUIRED | Path to a asigntseonline.conf |
| pathLength | in | REQUIRED | The length of the array that represents the path. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## cfgTseAdd

Add Tse config section. The Section will not be written to the config file.

**C**
```C
int32_t cfgTseAdd(const int8_t *tseId,
                  uint32_t tseIdLength,
                  uint32_t tseType,
                  const int8_t *connParam,
                  uint32_t connParamLength,
                  const int8_t *atrustTseID,
                  uint32_t atrustTseIDLength,
                  const int8_t *timeAdminID,
                  uint32_t timeAdminIDLength,
                  const int8_t *timeAdminPwd,
                  uint32_t timeAdminPwdLength)
```
**COM**
```C
HRESULT CfgTseAdd([in] BSTR tseId,
	[in] LONG tseType,
	[in] BSTR connParam,
	[in] BSTR atrustVtssID,
	[in] BSTR atrustApiKey,
	[in] BSTR timeAdminID,
	[in] BSTR timeAdminPwd,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| tseId | in | REQUIRED | Name of the tse config section. |
| tseIdLength | in | REQUIRED | The length of the array that represents the tseId. |
| tseType | in| REQUIRED| Has to be 1 for a.sign TSE or 2 for Cryptovision.|
| connParam | in | REQUIRED | URL to the asignTSE webserver. |
| connParamLength | in | REQUIRED | The length of the array that represents the connParam. |
| atrustTseID | in | REQUIRED | TSE Identification Number. |
| atrustTseIDLength | in | REQUIRED | The length of the array that represents the atrustTseID. |
| timeAdminID | in | REQUIRED | In case of asignTSE this is always `NULL`. |
| timeAdminIDLength | in | REQUIRED | The length of the array that represents the timeAdminID. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

### Example

```C
    const char* connParam = "https://hs-abnahme.a-trust.at/asigntseonline/v1";
    const char* username = "u0000000000212xx";
    const char* api_key = "testapikey45345123foe58392abb2cf3267ebc9cf2abf700a50e1a8bb623e4013844d0";
	cfgTseAdd(
		"default", strlen("default"),
		1,
		connParam, strlen(connParam),
		username, strlen(username),
		api_key, strlen(api_key),
		NULL, 0, NULL, 0
	);
```

## cfgTseRemove

Remove Tse config section.

**C**
```C
int32_t cfgTseRemove(const int8_t *tseId,
                     uint32_t tseIdLength)
```
**COM**
```C
HRESULT CfgTseRemove([in] BSTR tseId,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| tseId | in | REQUIRED | Name of the tse config section. |
| tseIdLength | in | REQUIRED | The length of the array that represents the tseId. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

### Example

```C
char* config_entry = "tse1";
cfgTseRemove(config_entry, strlen(config_entry));
```

## cfgSetLoggingEnabled

Enable logging.

**C**
```C
int32_t cfgSetLoggingEnabled(bool enabled)
```
**COM**
```C
HRESULT CfgSetLoggingEnabled([in] BOOL enabled,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| enabled | in | REQUIRED | Set the value of this property to `true` to enable the option or `false` to disable it. Default: `false`. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## cfgSetLoggingStderr

Enable logging to stderr.

**C**
```C
int32_t cfgSetLoggingStderr(bool enabled)
```
**COM**
```C
HRESULT CfgSetLoggingStderr([in] BOOL enabled,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| enabled | in | REQUIRED | Set the value of this property to `true` to enable the option or `false` to disable it. Default: `false`. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## cfgSetLoggingFile

Enable logging to a logfile.

**C**
```C
int32_t cfgSetLoggingFile(bool enabled)
```
**COM**
```C
HRESULT CfgSetLoggingFile([in] BOOL enabled,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| enabled | in | REQUIRED | Set the value of this property to `true` to enable the option or `false` to disable it. Default: `false` |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## cfgSetLogDir

Set logfile target directory.

**C**
```C
int32_t cfgSetLogDir(const int8_t *path,
                     uint32_t pathLength)
```
**COM**
```C
HRESULT CfgSetLogDir([in] BSTR path,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| path | in | REQUIRED | Target Directory for logfiles. Default: `.`. |
| pathLength | in | REQUIRED | The length of the array that represents the path. |


### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## cfgSetLogLevel

Set verbosity level of the logger.

**C**
```C
int32_t cfgSetLogLevel(const int8_t *logLevel, uint32_t logLevelLength)
```
**COM**
```C
HRESULT CfgSetLogLevel([in] BSTR logLevel,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| logLevel | in | REQUIRED | A string representing the available verbosity levels of the logger. Allowed values are: `error`, `warn`, `info`, `debug` and `trace`. Default: `trace`. |
| logLevelLength | in | REQUIRED | The length of the string that represents the logLevel. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## cfgSetLogAppend

The output file will be created if it does not exist.

If the log append flag is set to true, the logger will append to the output file.  
If not set (default), the output file will be truncated to zero length before the logger starts writing to it.

**C**
```C
int32_t cfgSetLogAppend(bool enabled)
```
**COM**
```C
HRESULT CfgSetLogAppend([in] BOOL enabled,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| enabled | in | REQUIRED | Set the value of this property to `true` to enable the option or `false` to disable it. Default: `true`. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## cfgSetLogColors

Enable a colored version of the logline-formatter.

**C**
```C
int32_t cfgSetLogColors(bool enabled)
```
**COM**
```C
HRESULT CfgSetLogColors([in] BOOL enabled,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| enabled | in | REQUIRED | Set the value of this property to `true` to enable the option or `false` to disable it. Default: `false`. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## cfgSetLogDetails

Enable more detailed log lines.

**C**
```C
int32_t cfgSetLogDetails(bool enabled)
```
**COM**
```C
HRESULT CfgSetLogDetails([in] BOOL enabled,
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| enabled | in | REQUIRED | Set the value of this property to `true` to enable the option or `false` to disable it. Default: `true`. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## cfgSetLogStderrColors

Enable a colored version of the logline-formatter.

**C**
```C
int32_t cfgSetLogStderrColors(bool enabled)
```
**COM**
```C
HRESULT CfgSetLogStderrColors([in] BOOL enabled,
	[out, retval] LONG* returnCode);
``` 

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| enabled | in | REQUIRED | Set the value of this property to `true` to enable the option or `false` to disable it. Default: `false`. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

## cfgSetHttpProxy

Set the http proxy.

```C
int32_t cfgSetHttpProxy(const int8_t *proxyUrl,
                        uint32_t proxyUrlLength);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| proxyUrl | in | REQUIRED | Proxy Url and Port. This overwrites the HTTP_PROXY environment variable. |
| proxyUrlLength | in | REQUIRED | The length of the array that represents the proxyUrl. |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UNKNOWN | If the execution fails, the return value ERROR_UNKNOWN will be returned. |

### Example

**C**
```C
char* proxy = "https://proxy.example.com:3128";
cfgSetHttpProxy(proxy, (uint32_t) strlen(proxy));
```
**COM**
```C
HRESULT CfgSetHttpProxy([in] BSTR proxyUrl,
	[out, retval] LONG* returnCode);
```

## Numeric Error values
| Constant | value |
| -------- | ----- |
| ERROR_MISSING_PARAMETER | -3000; |
| ERROR_FUNCTION_NOT_SUPPORTED | -3001 |
| ERROR_IO | -3002 |
| ERROR_TSE_TIMEOUT  | -3003 |
| ERROR_ALLOCATION_FAILED  | -3004; |
| ERROR_CONFIG_FILE_NOT_FOUND  | -3005; |
| ERROR_SE_COMMUNICATION_FAILED  | -3006; |
| ERROR_TSE_COMMAND_DATA_INVALID  | -3007 |
| ERROR_TSE_RESPONSE_DATA_INVALID  | -3008 |
| ERROR_ERS_ALREADY_MAPPED   | -3009 |
| ERROR_NO_ERS  | -3010 |
| ERROR_TSE_UNKNOWN_ERROR  | -3011 |
| ERROR_STREAM_WRITE  | -3012 |
| ERROR_BUFFER_TOO_SMALL  | -3013 |
| ERROR_NO_SUCH_KEY  | -3014 |
| ERROR_NO_KEY  | -3015 |
| ERROR_SE_API_DEACTIVATED  | -3016 |
| ERROR_SE_API_NOT_DEACTIVATED  | -3017 |
| ERROR_AT_LOAD_NOT_CALLED | -3018 |
| ERROR_SE_IPC_PROTOCOL_ERROR | -3019 |
| ERROR_AT_SET_PINS_FAILED | -3020|
| ERROR_SE_NOT_PROVISIONED_ERROR | -3021 |
| ERROR_SE_INSUFFICIENT_USER_ENTROPY_ERROR | -3021 |
| ERROR_SE_ALREADY_PROVISIONED | -3022 |
| ERROR_SE_IN_SECURE_STATE | -3023 |
| ERROR_UNKNOWN  | -3100 |
| ERROR_AUTHENTICATION_FAILED | -4000 |
| ERROR_UNBLOCK_FAILED | -4001 |
| ERROR_RETRIEVE_LOG_MESSAGE_FAILED | -5001 |
| ERROR_STORAGE_FAILURE | -5002 |
| ERROR_UPDATE_TIME_FAILED | -5003 |
| ERROR_PARAMETER_MISMATCH | -5004 |
| ERROR_ID_NOT_FOUND | -5005 |
| ERROR_TRANSACTION_NUMBER_NOT_FOUND | -5006 |
| ERROR_NO_DATA_AVAILABLE | -5007 |
| ERROR_TOO_MANY_RECORDS | -5008 |
| ERROR_START_TRANSACTION_FAILED | -5009 |
| ERROR_UPDATE_TRANSACTION_FAILED | -5010 |
| ERROR_FINISH_TRANSACTION_FAILED | -5011 |
| ERROR_RESTORE_FAILED | -5012 |
| ERROR_STORING_INIT_DATA_FAILED | -5013 |
| ERROR_EXPORT_CERT_FAILED | -5014 |
| ERROR_NO_LOG_MESSAGE | -5015 |
| ERROR_READING_LOG_MESSAGE | -5016 |
| ERROR_NO_TRANSACTION | -5017 |
| ERROR_SE_API_NOT_INITIALIZED | -5018 |
| ERROR_TIME_NOT_SET | -5019 |
| ERROR_CERTIFICATE_EXPIRED | -5020 |
| ERROR_SECURE_ELEMENT_DISABLED | -5021 |
| ERROR_USER_NOT_AUTHORIZED | -5022 |
| ERROR_USER_NOT_AUTHENTICATED | -5023 |
| ERROR_DESCRIPTION_NOT_SET_BY_MANUFACTURER | -5024 |
| ERROR_DESCRIPTION_SET_BY_MANUFACTURER | -5025 |
| ERROR_EXPORT_SERIAL_NUMBERS_FAILED | -5026 |
| ERROR_GET_MAX_NUMBER_OF_CLIENTS_FAILED | -5027 |
| ERROR_GET_CURRENT_NUMBER_OF_CLIENTS_FAILED | -5028 |
| ERROR_GET_MAX_NUMBER_TRANSACTIONS_FAILED | -5029 |
| ERROR_GET_CURRENT_NUMBER_OF_TRANSACTIONS_FAILED | -5030 |
| ERROR_GET_SUPPORTED_UPDATE_VARIANTS_FAILED | -5031 |
| ERROR_DELETE_STORED_DATA_FAILED | -5032 |
| ERROR_UNEXPORTED_STORED_DATA | -5033 |
| ERROR_SIGNING_SYSTEM_OPERATION_DATA_FAILED | -5034 |
| ERROR_USER_ID_NOT_MANAGED | -5035 |
| ERROR_USER_ID_NOT_AUTHENTICATED | -5036 |
| ERROR_DISABLE_SECURE_ELEMENT_FAILED | -5037 |
| ERROR_CONFIG_VALUE_NOT_FOUND | -5038 |
| ERROR_INVALID_CONFIG | -5039 |
| ERROR_SUSPEND_SECURE_ELEMENT_FAILED | -5040 |
| ERROR_UNSUSPEND_SECURE_ELEMENT_FAILED | -5041 |
| ERROR_GET_OPEN_TRANSACTIONS_FAILED | -5042 |
| ERROR_GET_LIFECYCLE_STATE_FAILED | -5043 |
| ERROR_GET_TRANSACTION_COUNTER_FAILED | -5044 |
| ERROR_GET_SIGNATURE_ALGORITHM_FAILED | -5045 |
| ERROR_GET_SIGNATURE_COUNTER_FAILED | -5045 |
| ERROR_GET_TOTAL_LOG_MEMORY | -5046 |
| ERROR_GET_LOG_TIME_FORMAT | -5047 |
| ERROR_EXPORT_PUBLIC_KEY_FAILED | -5048 |
| ERROR_EXPORT_CERTIFICATE_FAILED | -5049 |
| ERROR_UNSUPPORTED_PREMIUM_FEATURE  | -6000 |
| EXECUTION_OK | 0 ||

## Configuration File

### Configuring Technical Secure Systems

Every config file must have a `[default]` section. If a SEAPI function without the `WithTse` postfix gets called, the `[default]` tse is used. Here tss and tse are used synonymously.

| Constant | value |
| -------- | ----- |
| tss_type | Reserved for future use. |
| conn_param | SMAERS Server Url. |
| atrust_api_key | TSS api key. |
| atrust_vtss_id | TSS id. |

#### Example
```Ini
[default]
tss_type=1
conn_param=https://hs-abnahme.a-trust.at/asigntseonline/v1
atrust_api_key=880f02caddcd6bad2102aa9962d0fa7f1c42c6618635f8fd07cbfabb131f0b14
atrust_vtss_id=u000000000010123

[tse_1]
tss_type=1
conn_param=https://hs-abnahme.a-trust.at/asigntseonline/v1
atrust_api_key=880f02caddcd6bad2102aa9962d0fa7f1c42c6618635f8fd07cbfabb131f0b14
atrust_vtss_id=u000000000010123

[tse_2]
tss_type=1
conn_param=https://hs-abnahme.a-trust.at/asigntseonline/v1
atrust_api_key=5c0e7104194bea87f203ecd39f6f7c87a49403384d65f82612c95cf263720345
atrust_vtss_id=u000000000010124
```

### General Configuration
In order to set general configuration parameters, there is an optional `[config]` section. The following table shows configuration options and switches.

| Option | Type | Description |
| ------ | ---- | ----------- |
| logging_enabled | Boolean | Enable logging. |
| logging_stderr  | Boolean | Enable logging to stderr. |
| logging_file | Boolean | Enable logging to a logfile. |
| log_dir | Path | Set logfile directory. |
| log_details | Boolean | Enable more detailed log lines. |
| log_level | error \| warn \| info \| debug \| trace | Set verbosity level of the logger. | 
| log_append | Boolean |  true: append to the output file, if it exists; false: the output file will be truncated to zero length before the logger starts writing to it. |
| log_colors | Boolean | Enable a colored version of the logline-formatter. |
| log_stderr_colors | Boolean | Enable a colored version of the logline-formatter. |
| http_proxy | URL | Set the http proxy. |

## APPENDIX A: C Function Signatures
```C
void asigntse_free(
    void **ptr);

void at_free(
    void **ptr);

int32_t at_getCertificate(
    uint8_t **cert,
    uint32_t *certLength);

int32_t at_getCertificateWithTse(
    uint8_t **cert,
    uint32_t *certLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t at_getLifecycleState(
    LifecycleState *state);

int32_t at_getLifecycleStateWithTse(
    LifecycleState *state,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t at_getLogTimeFormat(
    int8_t **logTimeFormat, 
    uint32_t *logTimeFormatLength);

int32_t at_getLogTimeFormatWithTse(
    int8_t **logTimeFormat,
    uint32_t *logTimeFormatLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t at_getOpenTransactions(
    uint32_t **transactionNumbers,
    uint32_t *transactionNumbersLength);

int32_t at_getOpenTransactionsWithTse(
    uint32_t **transactionNumbers,
    uint32_t *transactionNumbersLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t at_getPublicKey(
    uint8_t **pubKey,
    uint32_t *pubKeyLength);

int32_t at_getPublicKeyWithTse(
    uint8_t **pubKey,
    uint32_t *pubKeyLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t at_getSerialNumber(
    uint8_t **serial,
    uint32_t *serialLength);

int32_t at_getSerialNumberWithTse(
    uint8_t **serial,
    uint32_t *serialLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t at_getSignatureAlgorithm(
    int8_t **signatureAlgorithm,
    uint32_t *signatureAlgorithmLength);

int32_t at_getSignatureAlgorithmWithTse(
    int8_t **signatureAlgorithm,
    uint32_t *signatureAlgorithmLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t at_getSignatureCounter(
    uint32_t *counter);

int32_t at_getSignatureCounterWithTse(
    uint32_t *counter, 
    const int8_t *tseId, 
    uint32_t tseIdLength);

int32_t at_getTransactionCounter(
    uint32_t *counter);

int32_t at_getTransactionCounterWithTse(
    uint32_t *counter,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t at_getVersion(
    int8_t **version,
    uint32_t *versionLength);

int32_t at_load(void);

int32_t at_suspendSecureElement(void);

int32_t at_suspendSecureElementWithTse(
    const int8_t *tseId, 
    uint32_t tseIdLength);

int32_t at_unload(void);

int32_t at_unsuspendSecureElement(void);

int32_t at_unsuspendSecureElementWithTse(
    const int8_t *tseId, 
    uint32_t tseIdLength);

int32_t authenticateUser(
    const int8_t *userId,
    uint32_t userIdLength,
    const uint8_t *pin,
    uint32_t pinLength,
    AuthenticationResult *authenticationResult,
    int16_t *remainingRetries);

int32_t authenticateUserWithTse(
    const int8_t *userId,
    uint32_t userIdLength,
    const uint8_t *pin,
    uint32_t pinLength,
    AuthenticationResult *authenticationResult,
    int16_t *remainingRetries,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t cfgSetConfigFile(
    const int8_t *path, 
    uint32_t pathLength);

int32_t cfgSetHttpProxy(
    const int8_t *proxyUrl,
    uint32_t proxyUrlLength);

int32_t cfgSetLogAppend(
    bool enabled);

int32_t cfgSetLogColors(
    bool enabled);

int32_t cfgSetLogDetails(
    bool enabled);

int32_t cfgSetLogDir(
    const int8_t *path, 
    uint32_t pathLength);

int32_t cfgSetLogLevel(
    const int8_t *logLevel, 
    uint32_t logLevelLength);

int32_t cfgSetLogStderrColors(
    bool enabled);

int32_t cfgSetLoggingEnabled(
    bool enabled);

int32_t cfgSetLoggingFile(
    bool enabled);

int32_t cfgSetLoggingStderr(
    bool enabled);

int32_t cfgTseAdd(
    const int8_t *tseID,
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

int32_t cfgTseRemove(
    const int8_t *tseID, 
    uint32_t tseIDLength);

int32_t deleteStoredData(void);

int32_t deleteStoredDataWithTse(
    const int8_t *tseId, 
    uint32_t tseIdLength);

int32_t disableSecureElement(void);

int32_t disableSecureElementWithTse(
    const int8_t *tseId, 
    uint32_t tseIdLength);

int32_t exportCertificates(
    uint8_t **certificates,
    uint32_t *certificatesLength);

int32_t exportCertificatesWithTse(
    uint8_t **certificates,
    uint32_t *certificatesLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t exportData(
    uint32_t maximumNumberRecords,
    uint8_t **exportedData,
    uint32_t *exportedDataLength);

int32_t exportDataFilteredByPeriodOfTime(
    int64_t startDate,
    int64_t endDate,
    uint32_t maximumNumberRecords,
    uint8_t **exportedData,
    uint32_t *exportedDataLength);

int32_t exportDataFilteredByPeriodOfTimeAndClientId(
    int64_t startDate,
    int64_t endDate,
    const int8_t *clientId,
    uint32_t clientIdLength,
    uint32_t maximumNumberRecords,
    uint8_t **exportedData,
    uint32_t *exportedDataLength);

int32_t exportDataFilteredByPeriodOfTimeAndClientIdWithTse(
    int64_t startDate,
    int64_t endDate,
    const int8_t *clientId,
    uint32_t clientIdLength,
    uint32_t maximumNumberRecords,
    uint8_t **exportedData,
    uint32_t *exportedDataLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t exportDataFilteredByPeriodOfTimeWithTse(
    int64_t startDate,
    int64_t endDate,
    uint32_t maximumNumberRecords,
    uint8_t **exportedData,
    uint32_t *exportedDataLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t exportDataFilteredByTransactionNumber(
    uint32_t transactionNumber,
    uint8_t **exportedData,
    uint32_t *exportedDataLength);

int32_t exportDataFilteredByTransactionNumberAndClientId(
    uint32_t transactionNumber,
    const int8_t *clientId,
    uint32_t clientIdLength,
    uint8_t **exportedData,
    uint32_t *exportedDataLength);

int32_t exportDataFilteredByTransactionNumberAndClientIdWithTse(
    uint32_t transactionNumber,
    const int8_t *clientId,
    uint32_t clientIdLength,
    uint8_t **exportedData,
    uint32_t *exportedDataLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t exportDataFilteredByTransactionNumberInterval(
    uint32_t startTransactionNumber,
    uint32_t endTransactionNumber,
    uint32_t maximumNumberRecords,
    uint8_t **exportedData,
    uint32_t *exportedDataLength);

int32_t exportDataFilteredByTransactionNumberIntervalAndClientId(
    uint32_t startTransactionNumber,
    uint32_t endTransactionNumber,
    const int8_t *clientId,
    uint32_t clientIdLength,
    uint32_t maximumNumberRecords,
    uint8_t **exportedData,
    uint32_t *exportedDataLength);

int32_t exportDataFilteredByTransactionNumberIntervalAndClientIdWithTse(
    uint32_t startTransactionNumber,
    uint32_t endTransactionNumber,
    const int8_t *clientId,
    uint32_t clientIdLength,
    uint32_t maximumNumberRecords,
    uint8_t **exportedData,
    uint32_t *exportedDataLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t exportDataFilteredByTransactionNumberIntervalWithTse(
    uint32_t startTransactionNumber,
    uint32_t endTransactionNumber,
    uint32_t maximumNumberRecords,
    uint8_t **exportedData,
    uint32_t *exportedDataLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t exportDataFilteredByTransactionNumberWithTse(
    uint32_t transactionNumber,
    uint8_t **exportedData,
    uint32_t *exportedDataLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t exportDataWithTse(
    uint32_t maximumNumberRecords,
    uint8_t **exportedData,
    uint32_t *exportedDataLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t exportSerialNumbers(
    uint8_t **serialNumbers,
    uint32_t *serialNumbersLength);

int32_t exportSerialNumbersWithTse(
    uint8_t **serialNumbers,
    uint32_t *serialNumbersLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t finishTransaction(
    const int8_t *clientId,
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

int32_t finishTransactionWithTse(
    const int8_t *clientId,
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

int32_t getCurrentNumberOfClients(
    uint32_t *currentNumberClients);

int32_t getCurrentNumberOfClientsWithTse(
    uint32_t *currentNumberClients,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t getCurrentNumberOfTransactions(
    uint32_t *currentNumberTransactions);

int32_t getCurrentNumberOfTransactionsWithTse(
    uint32_t *currentNumberTransactions,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t getMaxNumberOfClients(
    uint32_t *maxNumberClients);

int32_t getMaxNumberOfClientsWithTse(
    uint32_t *maxNumberClients,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t getMaxNumberOfTransactions(
    uint32_t *maxNumberTransactions);

int32_t getMaxNumberOfTransactionsWithTse(
    uint32_t *maxNumberTransactions,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t getSupportedTransactionUpdateVariants(
    UpdateVariants *supportedUpdateVariants);

int32_t getSupportedTransactionUpdateVariantsWithTse(
    UpdateVariants *supportedUpdateVariants,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t initializeDescriptionNotSet(
    int8_t *description,
    uint32_t description_length);

int32_t initializeDescriptionNotSetWithTse(
    int8_t *description,
    uint32_t description_length,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t initializeDescriptionSet(void);

int32_t initializeDescriptionSetWithTse(
    const int8_t *tseId, 
    uint32_t tseIdLength);

int32_t logOut(
    const int8_t *userId,
    uint32_t userIdLength);

int32_t logOutWithTse(
    const int8_t *userId,
    uint32_t userIdLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t readLogMessage(
    uint8_t **logMessage,
    uint32_t *logMessageLength);

int32_t readLogMessageWithTse(
    uint8_t **logMessage,
    uint32_t *logMessageLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t restoreFromBackup(
    uint8_t *restoreData,
    uint32_t restoreDataLength);

int32_t restoreFromBackupWithTse(
    uint8_t *restoreData,
    uint32_t restoreDataLength,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t startTransaction(
    const int8_t *clientId,
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

int32_t startTransactionWithTse(
    const int8_t *clientId,
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

int32_t unblockUser(
    const int8_t *userId,
    uint32_t userIdLength,
    const int8_t *puk,
    uint32_t pukLength,
    const int8_t *newPin,
    uint32_t newPinLength,
    UnblockResult *unblockResult);

int32_t unblockUserWithTse(
    const int8_t *userId,
    uint32_t userIdLength,
    const int8_t *puk,
    uint32_t pukLength,
    const int8_t *newPin,
    uint32_t newPinLength,
    UnblockResult *unblockResult,
    const int8_t *tseId,
    uint32_t tseIdLength);

int32_t updateTime(
    int64_t newDateTime);

int32_t updateTimeWithTimeSync(void);

int32_t updateTimeWithTimeSyncWithTse(
    const int8_t *tseId, 
    uint32_t tseIdLength);

int32_t updateTimeWithTse(
    int64_t newDateTime, 
    const int8_t *tseId, 
    uint32_t tseIdLength);

int32_t updateTransaction(
    const int8_t *clientId,
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

int32_t updateTransactionWithTse(
    const int8_t *clientId,
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
```

## APPENDIX B: COM Function Signatures
```C
HRESULT AuthenticateUser([in] BSTR userId,
	[in] SAFEARRAY(BYTE) pin,
	[out] LONG* authenticationResult,
	[out] LONG* remainingRetries,
	[out, retval] LONG* returnCode);

HRESULT AuthenticateUserWithTse([in] BSTR userId,
	[in] SAFEARRAY(BYTE) pin,
	[out] LONG* authenticationResult,
	[out] LONG* remainingRetries,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT AuthenticateUser_str([in] BSTR userId,
	[in] BSTR pin,
	[out] LONG* authenticationResult,
	[out] LONG* remainingRetries,
	[out, retval] LONG* returnCode);

HRESULT AuthenticateUserWithTse_str([in] BSTR userId,
	[in] BSTR pin,
	[out] LONG* authenticationResult,
	[out] LONG* remainingRetries,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT DeleteStoredData([out, retval] LONG* returnCode);

HRESULT DeleteStoredDataWithTse([in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT DisableSecureElement([out, retval] LONG* returnCode);

HRESULT DisableSecureElementWithTse([in] BSTR tseId, [out, retval] LONG* returnCode);

HRESULT ExportCertificates([out] SAFEARRAY(BYTE)* certificate, [out, retval] LONG* returnCode);

HRESULT ExportCertificatesWithTse([out] SAFEARRAY(BYTE)* certificate, [in] BSTR tseId, [out, retval] LONG* returnCode);

HRESULT ExportData([in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);

HRESULT ExportDataFilteredByPeriodOfTime([in] DATE startDate,
	[in] DATE endDate,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);

HRESULT ExportDataFilteredByPeriodOfTimeAndClientId([in] DATE startDate,
	[in] DATE endDate,
	[in] BSTR clientId,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);

HRESULT ExportDataFilteredByPeriodOfTimeAndClientIdWithTse([in] DATE startDate,
	[in] DATE endDate,
	[in] BSTR clientId,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT ExportDataFilteredByPeriodOfTimeWithTse([in] DATE startDate,
	[in] DATE endDate,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT ExportDataFilteredByPeriodOfTime_str([in] BSTR startDate,
	[in] BSTR endDate,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);

HRESULT ExportDataFilteredByPeriodOfTimeAndClientId_str([in] BSTR startDate,
	[in] BSTR endDate,
	[in] BSTR clientId,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);

HRESULT ExportDataFilteredByPeriodOfTimeAndClientIdWithTse_str([in] BSTR startDate,
	[in] BSTR endDate,
	[in] BSTR clientId,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT ExportDataFilteredByPeriodOfTimeWithTse_str([in] BSTR startDate,
	[in] BSTR endDate,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT ExportDataFilteredByTransactionNumber([in] LONG transactionNumber,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);

HRESULT ExportDataFilteredByTransactionNumberAndClientId([in] LONG transactionNumber,
	[in] BSTR clientId,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);

HRESULT ExportDataFilteredByTransactionNumberAndClientIdWithTse([in] LONG transactionNumber,
	[in] BSTR clientId,
	[out] SAFEARRAY(BYTE)* exportedData,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT ExportDataFilteredByTransactionNumberInterval([in] LONG startTransactionNumber,
	[in] LONG endTransactionNumber,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);

HRESULT ExportDataFilteredByTransactionNumberIntervalAndClientId([in] LONG startTransactionNumber,
	[in] LONG endTransactionNumber,
	[in] BSTR clientId,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);

HRESULT ExportDataFilteredByTransactionNumberIntervalAndClientIdWithTse([in] LONG startTransactionNumber,
	[in] LONG endTransactionNumber,
	[in] BSTR clientId,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT ExportDataFilteredByTransactionNumberIntervalWithTse([in] LONG startTransactionNumber,
	[in] LONG endTransactionNumber,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT ExportDataFilteredByTransactionNumberWithTse([in] LONG transactionNumber,
	[out] SAFEARRAY(BYTE)* exportedData,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT ExportDataWithTse([in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT ExportSerialNumbers([out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);

HRESULT ExportSerialNumbersWithTse([out] SAFEARRAY(BYTE)* exportedData,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT StartTransaction([in] BSTR clientId,
	[in] BSTR processData,
	[in] BSTR processType,
	[in] BSTR additionalData,
	[out] LONG* transactionNumber,
	[out] DATE* logTime,
	[out] SAFEARRAY(BYTE)* serialNumber,
	[out] LONG* signatureCounter,
	[out] SAFEARRAY(BYTE)* signaturetvalue,
	[out, retval] LONG* returnCode);

HRESULT StartTransactionWithTse([in] BSTR clientId,
	[in] BSTR processData,
	[in] BSTR processType,
	[in] BSTR additionalData,
	[out] LONG* transactionNumber,
	[out] DATE* logTime,
	[out] SAFEARRAY(BYTE)* serialNumber,
	[out] LONG* signatureCounter,
	[out] SAFEARRAY(BYTE)* signaturetvalue,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT StartTransaction_str([in] BSTR clientId,
	[in] BSTR processData,
	[in] BSTR processType,
	[in] BSTR additionalData,
	[out] LONG* transactionNumber,
	[out] BSTR* logTime,
	[out] SAFEARRAY(BYTE)* serialNumber,
	[out] LONG* signatureCounter,
	[out] SAFEARRAY(BYTE)* signaturetvalue,
	[out, retval] LONG* returnCode);

HRESULT StartTransactionWithTse_str([in] BSTR clientId,
	[in] BSTR processData,
	[in] BSTR processType,
	[in] BSTR additionalData,
	[out] LONG* transactionNumber,
	[out] BSTR* logTime,
	[out] SAFEARRAY(BYTE)* serialNumber,
	[out] LONG* signatureCounter,
	[out] SAFEARRAY(BYTE)* signaturetvalue,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT FinishTransaction([in] BSTR clientId,
	[in] LONG transactionNumber,
	[in] BSTR processData,
	[in] BSTR processType,
	[in] BSTR additionalData,
	[out] DATE* logTime,
	[out] LONG* signatureCounter,
	[out] SAFEARRAY(BYTE)* signaturetvalue,
	[out, retval] LONG* returnCode);

HRESULT FinishTransactionWithTse([in] BSTR clientId,
	[in] LONG transactionNumber,
	[in] BSTR processData,
	[in] BSTR processType,
	[in] BSTR additionalData,
	[out] DATE* logTime,
	[out] LONG* signatureCounter,
	[out] SAFEARRAY(BYTE)* signaturetvalue,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT FinishTransaction_str([in] BSTR clientId,
	[in] LONG transactionNumber,
	[in] BSTR processData,
	[in] BSTR processType,
	[in] BSTR additionalData,
	[out] BSTR* logTime,
	[out] LONG* signatureCounter,
	[out] SAFEARRAY(BYTE)* signaturetvalue,
	[out, retval] LONG* returnCode);

HRESULT FinishTransactionWithTse_str([in] BSTR clientId,
	[in] LONG transactionNumber,
	[in] BSTR processData,
	[in] BSTR processType,
	[in] BSTR additionalData,
	[out] BSTR* logTime,
	[out] LONG* signatureCounter,
	[out] SAFEARRAY(BYTE)* signaturetvalue,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT GetCurrentNumberOfClients(
	[out] LONG* currentNumberClients,
	[out, retval] LONG* returnCode);

HRESULT GetCurrentNumberOfClientsWithTse(
	[out] LONG* currentNumberClients,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT GetCurrentNumberOfTransactions(
	[out] LONG* currentNumberTransactions,
	[out, retval] LONG* returnCode);

HRESULT GetCurrentNumberOfTransactionsWithTse(
	[out] LONG* currentNumberTransactions,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT GetMaxNumberOfClients(
	[out] LONG* maxNumberClients,
	[out, retval] LONG* returnCode);

HRESULT GetMaxNumberOfClientsWithTse(
	[out] LONG* maxNumberClients,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT GetMaxNumberOfTransactions(
	[out] LONG* maxNumberTransactions,
	[out, retval] LONG* returnCode);

HRESULT GetMaxNumberOfTransactionsWithTse(
	[out] LONG* maxNumberTransactions,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT GetSupportedTransactionUpdateVariants(
	[out] LONG* supportedUpdateVariants,
	[out, retval] LONG* returnCode);

HRESULT GetSupportedTransactionUpdateVariantsWithTse(
	[out] LONG* supportedUpdateVariants,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT InitializeDescriptionNotSet(
	[in] BSTR description,
	[out, retval] LONG* returnCode);

HRESULT InitializeDescriptionSet(
	[out, retval] LONG* returnCode);

HRESULT LogOut(
	[in] BSTR userId,
	[out, retval] LONG* returnCode);

HRESULT LogOutWithTse(
	[in] BSTR userId,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT ReadLogMessage(
	[out] SAFEARRAY(BYTE)* logMessage,
	[out, retval] LONG* returnCode);

HRESULT ReadLogMessageWithTse(
	[out] SAFEARRAY(BYTE)* logMessage,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT RestoreFromBackup(
	[in] SAFEARRAY(BYTE) restoreData,
	[out, retval] LONG* returnCode);

HRESULT UnblockUser(
	[in] BSTR userId,
	[in] BSTR puk,
	[in] BSTR newPin,
	[out] LONG* unblockResult,
	[out, retval] LONG* returnCode);

HRESULT UnblockUserWithTse(
	[in] BSTR userId,
	[in] BSTR puk,
	[in] BSTR newPin,
	[out] LONG* unblockResult,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT AsignTSELoad(
	[out, retval] LONG* returnCode);

HRESULT UpdateTime(
	[in] DATE newDateTime,
	[out, retval] LONG* returnCode);

HRESULT UpdateTime_str(
	[in] BSTR newDateTime,
	[out, retval] LONG* returnCode);

HRESULT UpdateTimeWithTimeSync(
	[out, retval] LONG* returnCode);

HRESULT UpdateTransaction([in] BSTR clientId,
	[in] LONG transactionNumber,
	[in] BSTR processData,
	[in] BSTR processType,
	[out] DATE* logTime,
	[out] SAFEARRAY(BYTE)* signaturetvalue,
	[out] LONG* signatureCounter,
	[out, retval] LONG* returnCode);

HRESULT UpdateTransactionWithTse([in] BSTR clientId,
	[in] LONG transactionNumber,
	[in] BSTR processData,
	[in] BSTR processType,
	[out] DATE* logTime,
	[out] SAFEARRAY(BYTE)* signaturetvalue,
	[out] LONG* signatureCounter,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT UpdateTransaction_str([in] BSTR clientId,
	[in] LONG transactionNumber,
	[in] BSTR processData,
	[in] BSTR processType,
	[out] BSTR* logTime,
	[out] SAFEARRAY(BYTE)* signaturetvalue,
	[out] LONG* signatureCounter,
	[out, retval] LONG* returnCode);

HRESULT UpdateTransactionWithTse_str([in] BSTR clientId,
	[in] LONG transactionNumber,
	[in] BSTR processData,
	[in] BSTR processType,
	[out] BSTR* logTime,
	[out] SAFEARRAY(BYTE)* signaturetvalue,
	[out] LONG* signatureCounter,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT CfgSetConfigFile([in] BSTR path,
	[out, retval] LONG* returnCode);

HRESULT CfgTseAdd([in] BSTR tseId,
	[in] LONG tssType,
	[in] BSTR connParam,
	[in] BSTR atrustVtssID,
	[in] BSTR atrustApiKey,
	[in] BSTR timeAdminID,
	[in] BSTR timeAdminPwd,
	[out, retval] LONG* returnCode);

HRESULT CfgTseRemove([in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT CfgSetLoggingEnabled([in] BOOL enabled,
	[out, retval] LONG* returnCode);

HRESULT CfgSetLoggingStderr([in] BOOL enabled,
	[out, retval] LONG* returnCode);

HRESULT CfgSetLoggingFile([in] BOOL enabled,
	[out, retval] LONG* returnCode);

HRESULT CfgSetLogDir([in] BSTR path,
	[out, retval] LONG* returnCode);

HRESULT CfgSetLogLevel([in] BSTR logLevel,
	[out, retval] LONG* returnCode);

HRESULT CfgSetLogAppend([in] BOOL enabled,
	[out, retval] LONG* returnCode);

HRESULT CfgSetLogColors([in] BOOL enabled,
	[out, retval] LONG* returnCode);

HRESULT CfgSetLogDetails([in] BOOL enabled,
	[out, retval] LONG* returnCode);

HRESULT CfgSetLogStderrColors([in] BOOL enabled,
	[out, retval] LONG* returnCode);

HRESULT CfgSetHttpProxy([in] BSTR proxyUrl,
	[out, retval] LONG* returnCode);

HRESULT AtLoad(
	[out, retval] LONG* returnCode);

HRESULT AtUnload(
	[out, retval] LONG* returnCode);

HRESULT AtGetLifeCycleState(
	[out] LONG* lifecycleState,
	[out, retval] LONG* returnCode);

HRESULT AtGetLifeCycleStateWithTse(
	[out] LONG* lifecycleState,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT AtUnsuspendSecureElement(
	[out, retval] LONG* returnCode);

HRESULT AtUnsuspendSecureElementWithTse([in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT AtSuspendSecureElement(
	[out, retval] LONG* returnCode);

HRESULT AtSuspendSecureElementWithTse([in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT AtGetCertificate(
	[out] SAFEARRAY(BYTE)* certificate,
	[out, retval] LONG* returnCode);

HRESULT AtGetCertificateWithTse(
	[out] SAFEARRAY(BYTE)* certificate,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT AtGetPublicKey(
	[out] SAFEARRAY(BYTE)* publicKey,
	[out, retval] LONG* returnCode);

HRESULT AtGetPublicKeyWithTse(
	[out] SAFEARRAY(BYTE)* publicKey,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT AtGetOpenTransactions(
	[out] SAFEARRAY(LONG)* transactions,
	[out, retval] LONG* returnCode);

HRESULT AtGetOpenTransactionsWithTse(
	[out] SAFEARRAY(LONG)* transactions,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT AtGetTransactionCounter(
	[out] LONG* transactionCounter,
	[out, retval] LONG* returnCode);

HRESULT AtGetTransactionCounterWithTse(
	[out] LONG* transactionCounter,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT AtGetSignatureCounter(
	[out] LONG* signatureCounter,
	[out, retval] LONG* returnCode);

HRESULT AtGetSignatureCounterWithTse(
	[out] LONG* signatureCounter,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT AtGetSignatureAlgorithm(
	[out] BSTR* signatureAlgorithm,
	[out, retval] LONG* returnCode);

HRESULT AtGetSignatureAlgorithmWithTse(
	[out] BSTR* signatureAlgorithm,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT AtGetLogTimeFormat(
	[out] BSTR* logTimeFormat,
	[out, retval] LONG* returnCode);

HRESULT AtLogTimeFormatWithTse(
	[out] BSTR* logTimeFormat,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);

HRESULT AtGetVersion(
	[out] BSTR* version,
	[out, retval] LONG* returnCode);

HRESULT AtGetSerialNumber(
	[out] SAFEARRAY(BYTE)* serialNumber,
	[out, retval] LONG* returnCode);

HRESULT AtGetSerialNumberWithTse(
	[out] SAFEARRAY(BYTE)* serialNumber,
	[in] BSTR tseId,
	[out, retval] LONG* returnCode);
```
