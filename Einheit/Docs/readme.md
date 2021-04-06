# A-Sign TSE API Developer Manual (Einheit Supplement)

| Date              | Revision      | Author | Changes  |
| ----------------- |:-------------:|:--------:|------------- |
| 26/02/2020        | 0.9.0         | Robin Balean | API description. |
| 30/03/2020        | 0.9.7       | Robin Balean, Daniel Kovacic | Add COM function signatures. Add Appendix A - C function signatures. Add Appendix B - COM function signatures. |
| 04/03/2021        | 0.9.15       | Robin Balean | Add timeout |

## Introduction

This document is a supplement to the ASignTSE Developer manual and describes the additional features available in the ASignTSE Einheit solution.  This solution provides a wrapper around the SE-API implementations of other manufacturers to the ASignOnline API.  This allows developers who need to use both online and chip-based solutions to develop using a common interface.

At the time of writing of this document, the wrapper provides a wrapper over the Cryptovision API.  Other manufacturers may be included later.

Note that there are some features relevant to a chip-based solution that are not relevant to the online solution and vice versa.  We will endeavour to provide the most important functions from the chip implemenations, though some non-standard functions may only be accessible through the provider's library.

---

## Installation and Configuration

The A-Trust Premium library file and the Crytovision library file must be placed in the same directory.

The configuration file `asigntseonline.conf` will need to contain entries for `tss_type`, `conn_param` and optionally for `time_admin_pwd` (see [Automatic Time Management](#automatic-time-management)).
For Cryptovision, `tss_type` must have the value 2.  The entry `conn_param` specifies the device path where the Cryptovision chip is mounted.   The optional value `timeout` provides a timeout value (milliseconds) used for calls to the Cryptovision library.

Note that if you do not wish to use a configuration file then you can supply these configuration values programmatically using the functions `cfgTseAdd()` and `cfgSetTimeout()`.  These functions are described in the Developer Manual.

### Example configuration for a Cryptovision card

```C
[config]
timeout=5000

[default]
tss_type=2
conn_param=H:
time_admin_pwd=22222222
```

---



### Automatic Connection Management

The A-Sign TSE API manages the connection to the TSE chip automatically using the connection parameter value provided in the configuration file.  If your configuration file contains the optional value `timeout` then this parameter will be supplied as a timeout to the Cryptovision library.   If not a default value will be supplied.

If your connection fails after you have previously connected, the library will attempt to automatically reconnect.

---

### Automatic Time Management

If your configuration file contains the entries `time_admin_pwd`, then the A-Trust Wrapper will use these credentials to log in as the Time Administrator automatically and update the time from the local time source whenever it is required.  

Note that If you do not supply these details with your configuration then you can still perform this process manually.  In this case you would need to explicitly call `authenticateUser` and authenticate in the time administrator role, set the time using the function `updateTime`  and then call `logout` to logout of the time administrator role.  This sequence of commands would need to be performed on each startup, at regular intervals as recommended by the chip manufacturer (for Cryptovision see [`cv_getTimeSyncInterval`](#cvgettimesyncinterval)), and whenever time errors occur (see [updateTime](#updateTime)).

---

## Cryptovision provisioning example

The following example shows how to provision a Cryptovision token that is in the factory state.

To do this we first call the function `cv_initializePinValues` (which is a wraps the Cryptovision function `se_initializePinValues`) to create the initial PIN and PUK values.  Next we log in as the Admin user using the standard SE API function `authenticateUser`, followed by the SE API function `initializeDescriptionSet` to put the TSE into the initialized state.  

We then call the function `cv_registerClientId`.   Finally, we log out of the administrator role using the SE API function `logOut`.

Note that we did not explicitly require any calls to initialize the connection to the chip, nor to perform a time update.  These calls are performed automatically by the A-Sign TSE API.  See [Automatic Connection Management](#automatic-connection-management) and [Automatic Time Management](#automatic-time-management).

### Example: Set initial PINs, initialize TSE and register a ClientID

```C
    uint8_t adminPin[] = { 0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08 };
    uint8_t adminPuk[] = { 0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0a };
    uint8_t timeAdminPin[] = { 0x32,0x32,0x32,0x32,0x32,0x32,0x32,0x32 };                /* = "22222222" */
    uint8_t timeAdminPuk[] = { 0x73,0x6f,0x6d,0x65,0x74,0x68,0x69,0x6e,0x67,0x21 };    /* = "something!" */

    int32_t result = 0;

    /* Initialize PIN and PUK values for Admin and TimeAdmin */
    result = cv_initializePinValues(adminPin, sizeof(adminPin),
                                    adminPuk, sizeof(adminPuk),
                                    timeAdminPin, sizeof(timeAdminPin),
                                    timeAdminPuk, sizeof(timeAdminPuk));
    if (result != EXECUTION_OK)
    {
        (void)printf("cv_initializePinValues FAILED.  Error: %d\n", result);
        return result;
    }

    /* Authenticate the Admin role with the newly set Admin PIN in order to get the rights to initialize the TSE and register clients */
    AuthenticationResult authResult = 0;
    int16_t tries_remaining = 0;
    const char* adminUser = "Admin";
    result = authenticateUser(adminUser, strlen(adminUser), adminPin, sizeof(adminPin), &authResult, &tries_remaining);
    if (result != EXECUTION_OK || authResult != 0)
    {
        (void)printf("cv_authenticateUser FAILED.  Error: %d, AuthResult: %d, tries remaining: %d\n", result, authResult, tries_remaining);
        return result;
    }

    /* initialize TSE for first use */
    if ((result = initializeDescriptionSet()) != EXECUTION_OK )
    {
        (void)printf("initialize FAILED.  Error: %d\n", result);
        return result;
    }

    /* Register a client ID */
    const char* clientId = "ClientID-1";
    result = cv_registerClientId(clientId, strlen(clientId));
    if (result != EXECUTION_OK)
    {
        (void)printf("cv_registerClientId  Error: %d\n", result);
        return result;
    }

    /* Log out of administrator role */
    result = logOut(adminUser, strlen(adminUser));
    if (result != EXECUTION_OK)
    {
        (void)printf("cv_logOut FAILED.  Error: %d\n", result);
        return result;
    }
```

---

## Additional Functions

The following additional functions are provided.

- SE API Functions only implemented for the ASign Premium API
  - [updateTime](#updateTime)
- Cryptovision functions
  - [cv_exportData](#cv_exportData)
  - [cv_exportMoreData](#cv_exportMoreData)
  - [cv_getERSMappings](#cv_getERSMappings)
  - [cv_getWearIndicator](#cv_getWearIndicator)
  - [cv_getApiVersion](#cv_getApiVersion)
  - [cv_getApiVersionString](#cv_getApiVersionString)
  - [cv_getAvailableLogMemory](#cv_getAvailableLogMemory)
  - [cv_getCertificateExpirationDate](#cv_getCertificateExpirationDate)
  - [cv_getCertificationId](#cv_getCertificationId)
  - [cv_getFirmwareId](#cv_getFirmwareId)
  - [cv_getImplementationVersion](#cv_getApiVersion)
  - [cv_getImplementationVersionString](#cv_getApiVersionString)
  - [cv_getPinStatus](#cv_getPinStatus)
  - [cv_getTimeSyncInterval](#cv_getTimeSyncInterval)
  - [cv_getTimeSyncVariant](#cv_getTimeSyncVariant)
  - [cv_getTotalLogMemory](#cv_getTotalLogMemory)
  - [cv_initializePinValues](#cv_initializePinValues)
  - [cv_mapERStoKey](#cv_mapERStoKey)
  - [cv_registerClientId](#cv_registerClientId)
  - [cv_getUniqueId](#cv_getUniqueId)

<!--
-  Swissbit functions (these functions will only work in conjunction with the Swissbit library)
  - not yet implemented
-->
---

## SE API Functions

## updateTime

Updates the time on the chip from a trusted time source.  This is normally required at startup as well as at regular intervals during operation to prevent ERROR_TIME_NOT_SET errors.  If you ever receive this error, then you will need to update the time before you can perform any other operations.  If you are using [automatic time management](#Automatic-Time-Management) as described above then you should never need to call this function.

Note that to call this function you will need to be authenticated in the time administrator role.

**C**
```C
int32_t updateTime( uint64_t newDateTime);

int32_t updateTimeWithTse(uint64_t newDateTime
                          const char *tseId,
                          uint32_t  tseIdLength)
```
**COM**
```C
HRESULT UpdateTime(
	[in] DATE newDateTime,
	[out, retval] LONG* returnCode);

HRESULT UpdateTimeWithTimeSync(
	[out, retval] LONG* returnCode);
```

### Parameters

| Name          | In/Out      | Required? | Description  |
| ------------- |:-------------:|:----:|------------- |
| newDateTime | in | REQUIRED | The new 64-bit Unix Time value for the date/time maintained by the Secure Element |

### Return Codes

| Code          |  Description  |
|:------------- | ------------- |
| EXECUTION_OK | If the execution of the function has been successful, the return value EXECUTION_OK will be returned. |
| ERROR_UPDATE_TIME_FAILED | Failed to set time
| ERROR_SE_API_NOT_INITIALIZED | The SE API has not been initialized. |
| ERROR_USER_NOT_AUTHORIZED | The authenticated user is not authorized to set the time (i.e. does not have the time administrator role) |
| ERROR_USER_NOT_AUTHENTICATED | The user is not authenticated |
| ERROR_SECURE_ELEMENT_DISABLED | The Secure Element has been disabled. |

### Example (with default TSE)

```C
char *user = "timeAdmin";
char *password = "secret";
enum AuthenticationResult auth_result = 0;
int16_t retries = 0;
uint64_t time = 1583139600; // 03/02/2020 9:00am

if (authenticateUser(
    user, (uint32_t) sizeof(user),
    password, (uint32_t) sizeof(password)
    &auth_result,
    &retries) == EXECUTION_OK) {

    if (auth_result == auth_ok) {
        (void) updateTime(time);
        (void) logOut(user, (uint32_t) sizeof(user));
    }
}
```

---

## Cryptovision Functions

Functions specific to Cryptovision implementation begin with the prefix `_cv`.  These functions will only work in conjunction with the Cryptovision library.  
They are exposed by the A-Trust library for convenience and in most cases they correspond to an identical function in the Cryptovision library.

### Return codes

Crytovision return codes have been mapped back to A-Trust API return codes for consistency.  The original Cryptovision return codes are logged.

### Function versions "WithTse"

Most of the Cryptovision functions have also been provided in a `WithTse` version.  As in the online API, if called in this version the specified TSE (as determined by the configuration) will be called. The standard version of the function calls the TSE specified in the default section of the configuration file.

---

## cv_registerClientId

This function does not correspond to a Crytovision function.  It is a convenience function which combines the operations of the SE API function `exportSerialNumbers` and the Cryptovision function `se_mapERSToKey`.  

The first time it is called it internally calls `exportSerialNumbers` and parses the result to determine the serial number (=SHA256 hash of public key) for transaction log signing.  This value is then used in a call to `se_mapERSToKey` together with the client ID details in order to register the client.
Subsequent calls to `cv_registerClientId` use the key value from the previous call.

An example of the use of this function was given in [Cryptovision provisioning example](#cryptovision-provisioning-example)

**C**
```C
int32_t cv_registerClientId(const int8_t *client_id, uint32_t client_id_length);

int32_t cv_registerClientIdWithTse(const int8_t *client_id,
                               uint32_t client_id_length,
                               const int8_t *vtseId,
                               uint32_t vtseIdLength);
```
**COM**
```C
HRESULT CvRegisterClient(
	[in] BSTR clientID,
	[out, retval] LONG* returnCode);

HRESULT CvRegisterClientWithTse(
	[in] BSTR clientID,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);
```

---

## cv_mapERStoKey

Corresponds to the Cryptovision function `se_mapERStoKey`. See Cryptovision API for more details.  

Note that the convenience function [cv_registerClientId](#cv_registerClientId) provides a simpler interface to this function.

**C**
```C
int32_t cv_mapERStoKey(const int8_t *client_id,
                   uint32_t client_id_len,
                   const uint8_t *serial_number_key,
                   uint32_t serial_number_length);

int32_t cv_mapERStoKeyWithTse(const int8_t *client_id,
                          uint32_t client_id_len,
                          const uint8_t *serial_number_key,
                          uint32_t serial_number_length,
                          const int8_t *vtseId,
                          uint32_t vtseIdLength);
```
**COM**
```C
HRESULT CvMapErsToKey(
	[in] BSTR clientID,
	[in] SAFEARRAY(BYTE) serialNumberKey,
	[out, retval] LONG* returnCode);

HRESULT CvMapErsToKeyWithTse(
	[in] BSTR clientID,
	[in] SAFEARRAY(BYTE) serialNumberKey,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);
```

---

## cv_exportData

Corresponds to the Cryptovision function `se_exportData`. See Cryptovision API for more details.

**C**
```C
int32_t cv_exportData(const int8_t *client_id,
                  uint32_t client_id_length,
                  uint32_t transaction_number,
                  uint32_t start_transaction_number,
                  uint32_t end_transaction_number,
                  int64_t start_date,
                  int64_t end_date,
                  int32_t maximum_number_records,
                  uint8_t **exported_data,
                  uint32_t *exported_data_len);

int32_t cv_exportDataWithTse(const int8_t *client_id,
                         uint32_t client_id_length,
                         uint32_t transaction_number,
                         uint32_t start_transaction_number,
                         uint32_t end_transaction_number,
                         int64_t start_date,
                         int64_t end_date,
                         int32_t maximum_number_records,
                         uint8_t **exported_data,
                         uint32_t *exported_data_len,
                         const int8_t *tse_id,
                         uint32_t tse_id_len);
```
**COM**
```C
HRESULT CvExportData(
	[in] BSTR clientID,
	[in] LONG transactionNumber,
	[in] LONG startTransactionNumber,
	[in] LONG endTransactionNumber,
	[in] DATE startDate,
	[in] DATE endDate,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);

HRESULT CvExportDataWithTse(
	[in] BSTR clientID,
	[in] LONG transactionNumber,
	[in] LONG startTransactionNumber,
	[in] LONG endTransactionNumber,
	[in] DATE startDate,
	[in] DATE endDate,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);
```

---

## cv_exportMoreData

Corresponds to the Cryptovision function `se_exportMoreData`. See Cryptovision API for more details.

**C**
```C
int32_t cv_exportMoreData(const uint8_t *serial_number_key,
                      uint32_t serial_number_len,
                      uint32_t previous_sig_counter,
                      int32_t max_records,
                      uint8_t **exported_data,
                      uint32_t *exported_data_len);

int32_t cv_exportMoreDataWithTse(const uint8_t *serial_number_key,
                             uint32_t serial_number_len,
                             uint32_t previous_sig_counter,
                             int32_t max_records,
                             uint8_t **exported_data,
                             uint32_t *exported_data_len,
                             const int8_t *tse_id,
                             uint32_t tse_id_len);
```
**COM**
```C
HRESULT CvExportMoreData(
	[in] SAFEARRAY(BYTE) serialNumberKey,
	[in] LONG previousSigCounter,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);

HRESULT CvExportMoreDataWithTse(
	[in] SAFEARRAY(BYTE) serialNumberKey,
	[in] LONG previousSigCounter,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);
```

---

## cv_getApiVersion

Corresponds to the Cryptovision function `se_getApiVersion`. See Cryptovision API for more details.

Note that if you need the A-Trust version number you should use the function `at_getVersion`

**C**
```C
const uint8_t *cv_getApiVersion(uint32_t *len);
```
**COM**
```C
HRESULT CvGetApiVersion(
	[out] SAFEARRAY(BYTE)* version,
	[out, retval] LONG* returnCode);
```

---

## cv_getApiVersionString

Corresponds to the Cryptovision function `se_getApiVersionString`. See Cryptovision API for more details.

Note that if you need the A-Trust version number you should use the function `at_getVersion`

**C**
```C
const int8_t *cv_getApiVersionString(void);
```
**COM**
```C
HRESULT CvGetApiVersionString(
	[out] BSTR* version,
	[out, retval] LONG* returnCode);
```

---

## cv_getCertificateExpirationDate

Corresponds to the Cryptovision function `se_getCertificateExpirationDate`. See Cryptovision API for more details.

Note that the value for the serial number can be obtained using the function `at_getSerialNumber`.

**C**
```C
int32_t cv_getCertificateExpirationDate(const uint8_t *serial_number_key,
                                    uint32_t serial_number_len,
                                    int64_t *expiry_date);

int32_t cv_getCertificateExpirationDateWithTse(const uint8_t *serial_number_key,
                                           uint32_t serial_number_len,
                                           int64_t *expiry_date,
                                           const int8_t *tse_id,
                                           uint32_t tse_id_len);
```
**COM**
```C
HRESULT CvGetCertificateExpirationDate(
	[in] SAFEARRAY(BYTE) serialNumberKey,
	[out] DATE* expiryDate,
	[out, retval] LONG* returnCode);

HRESULT CvGetCertificateExpirationDateWithTse(
	[in] SAFEARRAY(BYTE) serialNumberKey,
	[out] DATE* expiryDate,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);
```

---

## cv_getCertificationId

Corresponds to the Cryptovision function `cv_getCertificationId`. See Cryptovision API for more details.

The memory allocated to the return parameter `certification_id` must be freed after use with the function `at_free`.

**C**
```C
int32_t cv_getCertificationId(int8_t **certification_id, uint32_t *certification_id_len);
```
**COM**
```C
HRESULT CvGetCertificationId(
	[out] BSTR* certificationID,
	[out, retval] LONG* returnCode);
```

---
## cv_getFirmwareId

Corresponds to the Cryptovision function `se_getFirmwareId`. See Cryptovision API for more details.

Note that if you need the A-Trust version number you should use the function `at_getVersion`

**C**
```C
const int8_t *cv_getFirmwareId(void);

const int8_t *cv_getFirmwareIdWithTse(const int8_t *vtseId, uint32_t vtseIdLength);
```
**COM**
```C
HRESULT CvGetFirmwareId(
	[out] BSTR* firmwareId,
	[out, retval] LONG* returnCode);

HRESULT CvGetFirmwareIdWithTse(
	[out] BSTR* firmwareId,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);
```

---

## cv_getImplementationVersion

Corresponds to the Cryptovision function `se_getImplementationVersion`. See Cryptovision API for more details.

Note that if you need the A-Trust version number you should use the function `at_getVersion`

**C**
```C
const uint8_t *cv_getImplementationVersion(uint32_t *len);
```
**COM**
```C
HRESULT CvGetImplementationVersion(
	[out] SAFEARRAY(BYTE)* version,
	[out, retval] LONG* returnCode);
```

---

## cv_getImplementationVersionString

Corresponds to the Cryptovision function `se_getImplementationVersionString`. See Cryptovision API for more details.

Note that if you need the A-Trust version number you should use the function `at_getVersion`

**C**
```C
const int8_t *cv_getImplementationVersionString(void);
```
**COM**
```C
HRESULT CvGetImplementationVersionString(
	[out] BSTR* version,
	[out, retval] LONG* returnCode);
```

---

## cv_getERSMappings

Corresponds to the Cryptovision function `se_getERSMappings`. See Cryptovision API for more details.

The memory allocated to the return parameter `mapping_data` must be freed after use with the function `at_free`.

**C**
```C
int32_t cv_getERSMappings(uint8_t **mapping_data,
                          uint32_t *mapping_data_length);

int32_t cv_getERSMappingsWithTse(uint8_t **mapping_data,
                             uint32_t *mapping_data_length,
                             const int8_t *vtseId,
                             uint32_t vtseIdLength);
```
**COM**
```C
HRESULT CvGetErsMappings(
	[out] SAFEARRAY(BYTE)* mappingData,
	[out, retval] LONG* returnCode);

HRESULT CvGetErsMappingsWithTse(
	[out] SAFEARRAY(BYTE)* mappingData,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);
```

---

## cv_getAvalableLogMemory

Corresponds to the Cryptovision function `se_getAvalableLogMemory`. See Cryptovision API for more details.

**C**
```C
int32_t cv_getAvailableLogMemory(uint64_t *size_of_memory);

int32_t cv_getAvailableLogMemoryWithTse(uint64_t *size_of_memory,
                                    const int8_t *vtseId,
                                    uint32_t vtseIdLength);
```
**COM**
```C
HRESULT CvGetAvailableLogMemory(
	[out] LONGLONG* sizeOfMemory,
	[out, retval] LONG* returnCode);

HRESULT CvGetAvailableLogMemoryWithTse(
	[out] LONGLONG* sizeOfMemory,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);
```

---

## cv_getTotalLogMemory

Corresponds to the Cryptovision function `se_getAvalableLogMemory`. See Cryptovision API for more details.

**C**
```C
int32_t cv_getTotalLogMemory(uint64_t *size_of_memory);

int32_t cv_getTotalLogMemoryWithTse(uint64_t *size_of_memory,
                                const int8_t *vtseId,
                                uint32_t vtseIdLength);
```
**COM**
```C
HRESULT CvGetTotalLogMemory(
	[out] LONGLONG* sizeOfMemory,
	[out, retval] LONG* returnCode);

HRESULT CvGetTotalLogMemoryWithTse(
	[out] LONGLONG* sizeOfMemory,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);
```

---

## cv_getTimeSyncInterval

Corresponds to the Cryptovision function `se_getTimeSyncInterval`. See Cryptovision API for more details.

**C**
```C
int32_t cv_getTimeSyncInterval(uint32_t *interval);

int32_t cv_getTimeSyncIntervalWithTse(uint32_t *interval,
                                  const int8_t *vtseId,
                                  uint32_t vtseIdLength);
```
**COM**
```C
HRESULT CvGetTimeSyncInterval(
	[out] LONG* interval,
	[out, retval] LONG* returnCode);

HRESULT CvGetTimeSyncIntervalWithTse(
	[out] LONG* interval,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);
```

---

## cv_getTimeSyncVariant

Corresponds to the Cryptovision function `se_getTimeSyncVariant`. See Cryptovision API for more details.

**C**
```C
int32_t cv_getTimeSyncVariant(SyncVariants *interval);

int32_t cv_getTimeSyncVariantWithTse(SyncVariants *variant,
                                 const int8_t *vtseId,
                                 uint32_t vtseIdLength);
```
**COM**
```C
HRESULT CvGetTimeSyncVariant(
	[out] LONG* variant,
	[out, retval] LONG* returnCode);

HRESULT CvGetTimeSyncVariantWithTse(
	[out] LONG* variant,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);
```

---

## cv_getWearIndicator

Corresponds to the Cryptovision function `se_getWearIndicator`. See Cryptovision API for more details.

**C**
```C
int32_t cv_getWearIndicator(uint32_t *wear_indicator);

int32_t cv_getWearIndicatorWithTse(uint32_t *wear_indicator,
                               const int8_t *vtseId,
                               uint32_t vtseIdLength);
```
**COM**
```C
HRESULT CvGetWearIndicator(
	[out] LONG* wearIndicator,
	[out, retval] LONG* returnCode);

HRESULT CvGetWearIndicatorWithTse(
	[out] LONG* wearIndicator,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);
```

---

## cv_initializePinValues

Corresponds to the Cryptovision function `se_initializePinValues`. See Cryptovision API for more details.

See also [Cryptovision provisioning example](#cryptovision-provisioning-example) for an example usage of this function.

**C**
```C
int32_t cv_initializePinValues(const uint8_t *admin_pin,
                           uint32_t admin_pin_length,
                           const uint8_t *admin_puk,
                           uint32_t admin_puk_length,
                           const uint8_t *time_admin_pin,
                           uint32_t time_admin_pin_length,
                           const uint8_t *time_admin_puk,
                           uint32_t time_admin_puk_length);

int32_t cv_initializePinValuesWithTse(const uint8_t *admin_pin,
                                  uint32_t admin_pin_length,
                                  const uint8_t *admin_puk,
                                  uint32_t admin_puk_length,
                                  const uint8_t *time_admin_pin,
                                  uint32_t time_admin_pin_length,
                                  const uint8_t *time_admin_puk,
                                  uint32_t time_admin_puk_length,
                                  const int8_t *vtseId,
                                  uint32_t vtseIdLength);
```
**COM**
```C
HRESULT CvInitializePinValues(
	[in] SAFEARRAY(BYTE) adminPin,
	[in] SAFEARRAY(BYTE) adminPuk,
	[in] SAFEARRAY(BYTE) timeAdminPin,
	[in] SAFEARRAY(BYTE) timeAdminPuk,
	[out, retval] LONG* returnCode);

HRESULT CvInitializePinValuesWithTse(
	[in] SAFEARRAY(BYTE) adminPin,
	[in] SAFEARRAY(BYTE) adminPuk,
	[in] SAFEARRAY(BYTE) timeAdminPin,
	[in] SAFEARRAY(BYTE) timeAdminPuk,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);
```

---

## cv_getPinStatus

Corresponds to the Cryptovision function `se_getPinStatus`. See Cryptovision API for more details.

**C**
```C
int32_t cv_getPinStatus(PinStateFlags *pin_state);

int32_t cv_getPinStatusWithTse(PinStateFlags *pin_state,
                           const int8_t *vtseId,
                           uint32_t vtseIdLength);
```
**COM**
```C
HRESULT CvGetPinStatus(
	[out] LONG* pinStatus,
	[out, retval] LONG* returnCode);

HRESULT CvGetPinStatusWithTse(
	[out] LONG* pinStatus,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);
```

---

## cv_getUniqueId

Corresponds to the Cryptovision function `se_getUniqueId`. See Cryptovision API for more details.

**C**
```C
const uint8_t *cv_getUniqueId(uint32_t *len);

const uint8_t *cv_getUniqueIdWithTse(uint32_t *len, const int8_t *vtseId, uint32_t vtseIdLength);
```
**COM**
```C
HRESULT CvGetUniqueId(
	[out] SAFEARRAY(BYTE)* id,
	[out, retval] LONG* returnCode);

HRESULT CvGetUniqueIdWithTse(
	[out] SAFEARRAY(BYTE)* version,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);
```

---

## Numeric Error values

| Constant | value |
| -------- | ----- |
| ERROR_ALLOCATION_FAILED | -3004 |
| ERROR_AUTHENTICATION_FAILED | -4000 |
| ERROR_BUFFER_TOO_SMALL | -3013 |
| ERROR_CERTIFICATE_EXPIRED | -5020 |
| ERROR_CONFIG_NOT_FOUND | -5038 |
| ERROR_DELETE_STORED_DATA_FAILED | -5032 |
| ERROR_DESCRIPTION_NOT_SET_BY_MANUFACTURER | -5024 |
| ERROR_DESCRIPTION_SET_BY_MANUFACTURER | -5025 |
| ERROR_DISABLE_SECURE_ELEMENT_FAILED | -5037 |
| ERROR_ERS_ALREADY_MAPPED | -3009 |
| ERROR_EXPORT_CERT_FAILED | -5014 |
| ERROR_EXPORT_SERIAL_NUMBERS_FAILED | -5026 |
| ERROR_FILE_NOT_FOUND | -3005 |
| ERROR_FINISH_TRANSACTION_FAILED | -5011 |
| ERROR_FUNCTION_NOT_SUPPORTED | -3001 |
| ERROR_GET_CURRENT_NUMBER_OF_CLIENTS_FAILED | -5028 |
| ERROR_GET_CURRENT_NUMBER_OF_TRANSACTIONS_FAILED | -5030 |
| ERROR_GET_MAX_NUMBER_OF_CLIENTS_FAILED | -5027 |
| ERROR_GET_MAX_NUMBER_TRANSACTIONS_FAILED | -5029 |
| ERROR_GET_SUPPORTED_UPDATE_VARIANTS_FAILED | -5031 |
| ERROR_ID_NOT_FOUND | -5005 |
| ERROR_INVALID_CONFIG | -5039 |
| ERROR_IO | -3002 |
| ERROR_MISSING_PARAMETER | -3000 |
| ERROR_NO_DATA_AVAILABLE | -5007 |
| ERROR_NO_ERS | -3010 |
| ERROR_NO_KEY | -3015 |
| ERROR_NO_LOG_MESSAGE | -5015 |
| ERROR_NO_SUCH_KEY | -3014 |
| ERROR_NO_TRANSACTION | -5017 |
| ERROR_PARAMETER_MISMATCH | -5004 |
| ERROR_READING_LOG_MESSAGE | -5016 |
| ERROR_RESTORE_FAILED | -5012 |
| ERROR_RETRIEVE_LOG_MESSAGE_FAILED | -5001 |
| ERROR_SECURE_ELEMENT_DISABLED | -5021 |
| ERROR_SE_API_DEACTIVATED | -3016 |
| ERROR_SE_API_NOT_DEACTIVATED | -3017 |
| ERROR_SE_API_NOT_INITIALIZED | -5018 |
| ERROR_SE_COMMUNICATION_FAILED | -3006 |
| ERROR_SIGNING_SYSTEM_OPERATION_DATA_FAILED | -5034 |
| ERROR_START_TRANSACTION_FAILED | -5009 |
| ERROR_STORAGE_FAILURE | -5002 |
| ERROR_STORING_INIT_DATA_FAILED | -5013 |
| ERROR_STREAM_WRITE | -3012 |
| ERROR_TIME_NOT_SET | -5019 |
| ERROR_TOO_MANY_RECORDS | -5008 |
| ERROR_TRANSACTION_NUMBER_NOT_FOUND | -5006 |
| ERROR_TSE_COMMAND_DATA_INVALID | -3007 |
| ERROR_TSE_RESPONSE_DATA_INVALID | -3008 |
| ERROR_TSE_TIMEOUT | -3003 |
| ERROR_TSE_UNKNOWN_ERROR | -3011 |
| ERROR_UNBLOCK_FAILED | -4001 |
| ERROR_UNEXPORTED_STORED_DATA | -5033 |
| ERROR_UNKNOWN | -3100 |
| ERROR_UPDATE_TIME_FAILED | -5003 |
| ERROR_UPDATE_TRANSACTION_FAILED | -5010 |
| ERROR_USER_ID_NOT_AUTHENTICATED | -5036 |
| ERROR_USER_ID_NOT_MANAGED | -5035 |
| ERROR_USER_NOT_AUTHENTICATED | -5023 |
| ERROR_USER_NOT_AUTHORIZED | -5022 |
| EXECUTION_OK | 0 |

## APPENDIX A: C function signatures
```C
int32_t cv_exportData(
   const int8_t *client_id,
   uint32_t client_id_length,
   uint32_t transaction_number,
   uint32_t start_transaction_number,
   uint32_t end_transaction_number,
   int64_t start_date,
   int64_t end_date,
   int32_t maximum_number_records,
   uint8_t **exported_data,
   uint32_t *exported_data_len);

int32_t cv_exportDataWithTse(
   const int8_t *client_id,
   uint32_t client_id_length,
   uint32_t transaction_number,
   uint32_t start_transaction_number,
   uint32_t end_transaction_number,
   int64_t start_date,
   int64_t end_date,
   int32_t maximum_number_records,
   uint8_t **exported_data,
   uint32_t *exported_data_len,
   const int8_t *tse_id,
   uint32_t tse_id_len);

int32_t cv_exportMoreData(
   const uint8_t *serial_number_key,
   uint32_t serial_number_len,
   uint32_t previous_sig_counter,
   int32_t max_records,
   uint8_t **exported_data,
   uint32_t *exported_data_len);

int32_t cv_exportMoreDataWithTse(
   const uint8_t *serial_number_key,
   uint32_t serial_number_len,
   uint32_t previous_sig_counter,
   int32_t max_records,
   uint8_t **exported_data,
   uint32_t *exported_data_len,
   const int8_t *tse_id,
   uint32_t tse_id_len);

const uint8_t *cv_getApiVersion(
   uint32_t *len);

const int8_t *cv_getApiVersionString(void);

int32_t cv_getAvailableLogMemory(
   uint64_t *size_of_memory);

int32_t cv_getAvailableLogMemoryWithTse(
   uint64_t *size_of_memory,
   const int8_t *vtseId,
   uint32_t vtseIdLength);

int32_t cv_getCertificateExpirationDate(
   const uint8_t *serial_number_key,
   uint32_t serial_number_len,
   int64_t *expiry_date);

int32_t cv_getCertificateExpirationDateWithTse(
   const uint8_t *serial_number_key,
   uint32_t serial_number_len,
   int64_t *expiry_date,
   const int8_t *tse_id,
   uint32_t tse_id_len);

int32_t cv_getCertificationId(
   int8_t **certification_id, 
   uint32_t *certification_id_len);

int32_t cv_getERSMappings(
   uint8_t **mapping_data,
   uint32_t *mapping_data_length);

int32_t cv_getERSMappingsWithTse(
   uint8_t **mapping_data,
   uint32_t *mapping_data_length,
   const int8_t *vtseId,
   uint32_t vtseIdLength);

const int8_t *cv_getFirmwareId(void);

const int8_t *cv_getFirmwareIdWithTse(
   const int8_t *tse_id, 
   uint32_t tse_len);

const uint8_t *cv_getImplementationVersion(
   uint32_t *len);

const int8_t *cv_getImplementationVersionString(void);

int32_t cv_getPinStatus(
   PinStateFlags *pin_state);

int32_t cv_getPinStatusWithTse(
   PinStateFlags *pin_state,
   const int8_t *vtseId,
   uint32_t vtseIdLength);

int32_t cv_getTimeSyncInterval(
   uint32_t *interval);

int32_t cv_getTimeSyncIntervalWithTse(
   uint32_t *interval,
   const int8_t *vtseId,
   uint32_t vtseIdLength);

int32_t cv_getTimeSyncVariant(
   SyncVariants *variant);

int32_t cv_getTimeSyncVariantWithTse(
   SyncVariants *variant,
   const int8_t *vtseId,
   uint32_t vtseIdLength);

int32_t cv_getTotalLogMemory(
   uint64_t *size_of_memory);

int32_t cv_getTotalLogMemoryWithTse(
   uint64_t *size_of_memory,
   const int8_t *vtseId,
   uint32_t vtseIdLength);

const uint8_t *cv_getUniqueId(
   uint32_t *len);

const uint8_t *cv_getUniqueIdWithTse(
   uint32_t *len, 
   const int8_t *tse_id, 
   uint32_t tse_len);

int32_t cv_getWearIndicator(
   uint32_t *wear_indicator);

int32_t cv_getWearIndicatorWithTse(
   uint32_t *wear_indicator,
   const int8_t *vtseId,
   uint32_t vtseIdLength);

int32_t cv_initializePinValues(
   const uint8_t *admin_pin,
   uint32_t admin_pin_length,
   const uint8_t *admin_puk,
   uint32_t admin_puk_length,
   const uint8_t *time_admin_pin,
   uint32_t time_admin_pin_length,
   const uint8_t *time_admin_puk,
   uint32_t time_admin_puk_length);

int32_t cv_initializePinValuesWithTse(
   const uint8_t *admin_pin,
   uint32_t admin_pin_length,
   const uint8_t *admin_puk,
   uint32_t admin_puk_length,
   const uint8_t *time_admin_pin,
   uint32_t time_admin_pin_length,
   const uint8_t *time_admin_puk,
   uint32_t time_admin_puk_length,
   const int8_t *vtseId,
   uint32_t vtseIdLength);

int32_t cv_mapERStoKey(
   const int8_t *client_id,
   uint32_t client_id_len,
   const uint8_t *serial_number_key,
   uint32_t serial_number_length);

int32_t cv_mapERStoKeyWithTse(
   const int8_t *client_id,
   uint32_t client_id_len,
   const uint8_t *serial_number_key,
   uint32_t serial_number_length,
   const int8_t *vtseId,
   uint32_t vtseIdLength);

int32_t cv_registerClientId(
   const int8_t *client_id, 
   uint32_t client_id_length);

int32_t cv_registerClientIdWithTse(
   const int8_t *client_id,
   uint32_t client_id_length,
   const int8_t *vtseId,
   uint32_t vtseIdLength);
```

## APPENDIX B: COM function signatures
```C
HRESULT CvGetPinStatus(
	[out] LONG* pinStatus,
	[out, retval] LONG* returnCode);

HRESULT CvGetPinStatusWithTse(
	[out] LONG* pinStatus,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvGetWearIndicator(
	[out] LONG* wearIndicator,
	[out, retval] LONG* returnCode);

HRESULT CvGetWearIndicatorWithTse(
	[out] LONG* wearIndicator,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvGetCertificateExpirationDate(
	[in] SAFEARRAY(BYTE) serialNumberKey,
	[out] DATE* expiryDate,
	[out, retval] LONG* returnCode);

HRESULT CvGetCertificateExpirationDateWithTse(
	[in] SAFEARRAY(BYTE) serialNumberKey,
	[out] DATE* expiryDate,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvGetCertificateExpirationDate_str(
	[in] SAFEARRAY(BYTE) serialNumberKey,
	[out] BSTR* expiryDate,
	[out, retval] LONG* returnCode);

HRESULT CvGetCertificateExpirationDateWithTse_str(
	[in] SAFEARRAY(BYTE) serialNumberKey,
	[out] BSTR* expiryDate,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvGetCertificationId(
	[out] BSTR* certificationID,
	[out, retval] LONG* returnCode);

HRESULT CvExportData(
	[in] BSTR clientID,
	[in] LONG transactionNumber,
	[in] LONG startTransactionNumber,
	[in] LONG endTransactionNumber,
	[in] DATE startDate,
	[in] DATE endDate,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);

HRESULT CvExportDataWithTse(
	[in] BSTR clientID,
	[in] LONG transactionNumber,
	[in] LONG startTransactionNumber,
	[in] LONG endTransactionNumber,
	[in] DATE startDate,
	[in] DATE endDate,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvExportData_str(
	[in] BSTR clientID,
	[in] LONG transactionNumber,
	[in] LONG startTransactionNumber,
	[in] LONG endTransactionNumber,
	[in] BSTR startDate,
	[in] BSTR endDate,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);

HRESULT CvExportDataWithTse_str(
	[in] BSTR clientID,
	[in] LONG transactionNumber,
	[in] LONG startTransactionNumber,
	[in] LONG endTransactionNumber,
	[in] BSTR startDate,
	[in] BSTR endDate,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvExportMoreData(
	[in] SAFEARRAY(BYTE) serialNumberKey,
	[in] LONG previousSigCounter,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[out, retval] LONG* returnCode);

HRESULT CvExportMoreDataWithTse(
	[in] SAFEARRAY(BYTE) serialNumberKey,
	[in] LONG previousSigCounter,
	[in] LONG maximumNumberRecords,
	[out] SAFEARRAY(BYTE)* exportedData,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvGetApiVersion(
	[out] SAFEARRAY(BYTE)* version,
	[out, retval] LONG* returnCode);

HRESULT CvGetApiVersionString(
	[out] BSTR* version,
	[out, retval] LONG* returnCode);

HRESULT CvGetImplementationVersion(
	[out] SAFEARRAY(BYTE)* version,
	[out, retval] LONG* returnCode);

HRESULT CvGetImplementationVersionString(
	[out] BSTR* version,
	[out, retval] LONG* returnCode);

HRESULT CvGetUniqueId(
	[out] SAFEARRAY(BYTE)* id,
	[out, retval] LONG* returnCode);

HRESULT CvGetUniqueIdWithTse(
	[out] SAFEARRAY(BYTE)* version,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvGetFirmwareId(
	[out] BSTR* firmwareId,
	[out, retval] LONG* returnCode);

HRESULT CvGetFirmwareIdWithTse(
	[out] BSTR* firmwareId,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvGetAvailableLogMemory(
	[out] LONGLONG* sizeOfMemory,
	[out, retval] LONG* returnCode);

HRESULT CvGetAvailableLogMemoryWithTse(
	[out] LONGLONG* sizeOfMemory,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvGetAvailableLogMemory_str(
	[out] BSTR* sizeOfMemory,
	[out, retval] LONG* returnCode);

HRESULT CvGetAvailableLogMemoryWithTse_str(
	[out] BSTR* sizeOfMemory,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvGetTotalLogMemory(
	[out] LONGLONG* sizeOfMemory,
	[out, retval] LONG* returnCode);

HRESULT CvGetTotalLogMemoryWithTse(
	[out] LONGLONG* sizeOfMemory,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvGetTotalLogMemory_str(
	[out] BSTR* sizeOfMemory,
	[out, retval] LONG* returnCode);

HRESULT CvGetTotalLogMemoryWithTse_str(
	[out] BSTR* sizeOfMemory,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvGetTimeSyncInterval(
	[out] LONG* interval,
	[out, retval] LONG* returnCode);

HRESULT CvGetTimeSyncIntervalWithTse(
	[out] LONG* interval,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvGetTimeSyncVariant(
	[out] LONG* variant,
	[out, retval] LONG* returnCode);

HRESULT CvGetTimeSyncVariantWithTse(
	[out] LONG* variant,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvGetErsMappings(
	[out] SAFEARRAY(BYTE)* mappingData,
	[out, retval] LONG* returnCode);

HRESULT CvGetErsMappingsWithTse(
	[out] SAFEARRAY(BYTE)* mappingData,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvMapErsToKey(
	[in] BSTR clientID,
	[in] SAFEARRAY(BYTE) serialNumberKey,
	[out, retval] LONG* returnCode);

HRESULT CvMapErsToKeyWithTse(
	[in] BSTR clientID,
	[in] SAFEARRAY(BYTE) serialNumberKey,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvRegisterClient(
	[in] BSTR clientID,
	[out, retval] LONG* returnCode);

HRESULT CvRegisterClientWithTse(
	[in] BSTR clientID,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);

HRESULT CvInitializePinValues(
	[in] SAFEARRAY(BYTE) adminPin,
	[in] SAFEARRAY(BYTE) adminPuk,
	[in] SAFEARRAY(BYTE) timeAdminPin,
	[in] SAFEARRAY(BYTE) timeAdminPuk,
	[out, retval] LONG* returnCode);

HRESULT CvInitializePinValuesWithTse(
	[in] SAFEARRAY(BYTE) adminPin,
	[in] SAFEARRAY(BYTE) adminPuk,
	[in] SAFEARRAY(BYTE) timeAdminPin,
	[in] SAFEARRAY(BYTE) timeAdminPuk,
	[in] BSTR vtseID,
	[out, retval] LONG* returnCode);
```
