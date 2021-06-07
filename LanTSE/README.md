# A-Trust LanTSE

## Introduction
A-Trust LanTSE is a middleware which allows to access one or many locally installed TSE using a REST WebService

## Prerequesites
A machine running the LanTSE service. This machine also needs to have the
locally installed TSE(s). There needs to be one configuration file containing
the sections for each installed TSE. You wil get this configuration after setup.

## Installation:
Either run the setup manually, you will hat to provide a unique instance-id for each run of the setup or use the silent version of the setup to install as many TSE as you want in one run: 

```
@echo off
"a.signTSE_v1.2.1.2_LAN25_Setup.exe" /S -test 1 -first 0 -last 1 -type basic -username 515569552 -password Fc1thYQfxCD -tsepin 12345678 -tsepuk 1234567890 -entropy "eastnHSEUHC##@*$(;,eushanoetushaoesunthaoei83249,lyoesnutihSNTEUISEUHstnehESNTIHOEIEHOIOESNTIH"
 
IF %ERRORLEVEL% == 0 (
    echo All instances installed successfully.  Check logs for details.
) ELSE (
    echo Install aborted after failure.  Check logs for details.
```
This will install two TSE (`-first 0 -last 1`) using the provided parameter connecting to the TEST system (`-test 1`)

### Configuration File

 **Beware** There needs to be one `[default]` section, the name for the following sections just needs to be unique.

The name of the section is required when usind the REST call: in this example the TSE referenced in section `[tse_2]` of the configuration file will be used.
http://127.0.0.1:8080/lantse/v1/tses/tse_2/lifecyclestate

### Example config
```
[config]
logging_enabled = true
logging_stderr = false
logging_file = true
log_details = true
log_stderr_colors = true
log_colors = false
log_append = true
log_level = trace
log_dir =.

[default]
tss_type=1
conn_param=https://test1.a-trust.at/asigntseonline;1
atrust_api_key=9264c...
atrust_vtss_id=69738...



[tse_2]
tss_type=1
conn_param=https://test1.a-trust.at/asigntseonline;2
atrust_api_key=98d66...
atrust_vtss_id=602647054...
```



## REST API

All API calls are appended to the following base URL: `http://172.0.0.1:8080/lantse/v1/tses/[section_name]/[API_CALL]`.

All API calls containing data are required to send header: `"Content-Type": "application/json"`.

---
### Initialize
Used to initialize a freshly set up TSE.

**API_CALL**: `/initializê/`

**Method**: `POST`

**Data constraints**:
```json
{
  "pin": "[pincode]",
  "description": "[description for TSE]" //optional
}
```

**Example**:
```json
{
  "pin": "12345",
  "description": "my cash register"
}
```
#### Response
**Code**: `200 OK`

---

### Register client
Used to register client ID using section name

**API_CALL**: `/atRegisterClientId/`

**Method**: `POST`

**Data constraint**:
```json
{
  "pin": "[pincode]",
  "clientid": "[section_name]"
}
```

**Example**:
```json
{
  "pin": "12345",
  "clientid": "tse_2"
}
```

#### Response
**Code**: `200 OK`

---

### GET requests

| Description                   | Request                               | Response                       | Example              |
| ----------------------------- | ------------------------------------- | ------------------------------ | -------------------- |
| Get timeformat                | `/atGetLogTimeFormat/`         | timeformat as text             | `Unixt`              |
| Get signature algorithm       | `/atGetSignatureAlgorithm/` | signature algorithm as text    | `ecdsa-plain-SHA256` |
| Get max client count          | `/getMaxNumberOfClients/` | max client count               | `50`                 |
| Get serial number             | `/atGetSerialNumbers/`       | serialnumber as hex           | -                    |
| Get transaction number        | `/atGetTransactionCounter/` | transaction number as integer     | `1`                  |
| Get signature counter         | `/atGetSignatureCounter`    | signature count as integer        | `2`                  |
| Get open transactions         | `/atGetOpenTransactions`        | list of open transactions                           | `{}`                 |
| Get public key                | `/atGetPublicKey/`          | public key as binary           | -                    |
| Get certificate used to sign  | `/atGetCertificate`         | certificate as binary DER encoded | -                    |
| Get all certificates in chain | `/atGetCertificates`        | all certificates as a tar file | -                    |
| Get log messages              | `/readLogMessage/`         | log file in .asn1 format       | -                    |

---

### Export data

Used to export data specified via parameters that can be combined to narrow down results

**API_CALL**: `/export`

**Method**: `GET`

**Parameters**:

| Name                                      | Description                                                                  |
| ----------------------------------------- | ---------------------------------------------------------------------------- |
| `max-records`                             | limit results                                                                |
| `transaction-number`                      | get single transaction using the transaction number                          |
| `client-id`                               | narrow down results to a client ID                                           |
| `start-transaction=[]&end-transaction=[]` | get transactions inside specified transaction number range                   |
| `start-time=[]&end-time=[]`               | get transactions inside specified time range (time format is unix timestamp) |

#### Response

**Examples**:
```json
//export all
/exports

//export transaction #42
/exports?transaction-number=42

//export transaction #42 from clientid tse_2
/exports?transaction-number=42&client-id=tse_2

//export transaction #42 from clientid tse_2 but limit result count to 10
/exports?start-transaction=1&end-transaction=42&&client-id=clientId&max-records=10

//export transactions between timestamps
/exports?start-time=1621417459&end-time=1621417460
```

---

### Start transaction

Used to start a transaction and retrieve transaction info

**API_CALL**: `/startTransaction/`

**Method**: `POST`

**Data constraint**:
```json
{
  "clientid": "[clientid]"
}
```

**Example**:
```json
{
  "clientid": "tse_2"
}
```

#### Response

**Code**: `200 OK`

**Content example**:
```json
{
  "status":0,
  "serialNumberB64": "FMhIaC6lyMFS0sA+2pYrS8WyRKBhmnlr6wuA3HPJw7w=",
  "signatureValueB64": "l2Jxe9tC3ka4gN1kbZLmvQ11HJZuoh+hgBnRHIqQ5QmbsfeLLPxY2xMHzS1PffenhcT7PUvJl1v5L1r8A4Bn1Q==",
  "unixtime": 1621600143,
  "transactionNumber": 1,
  "signatureCounter": 13
}
```

---

### Finish transaction

Used to finish transaction, send transaction details and tax values for processing

**API_CALL**: `/finishTransaction/[transaction_number]/`

**Method**: `POST`

**Data constraint**:
```json
{
  "clientid": "[clientid]",
  "transactionNumber": [transaction_number],
  "processData": "[transaction details]",
  "processType": "[type of transaction]"
}
```

**Example**:
```json
{
  "clientid": "default",
  "transactionNumber": 1, 
  "processData": "Beleg^75.33_7.99_0.0...4.30:Unbar",
  "processType": "Kassenbeleg-V1"}
```

#### Response

**Code**: `200 OK`

**Content example**:
```json
{
  "status":  0,
  "signatureValueB64": "gYvb4yY/oN7zBnpazV6/+hFlRHRLtBcE5Qpamo45ErqpznRcXQ5pGW1bHGy7oeaxRxwYrjvdILi1ZE4SwFL6Fw==",
  "unixtime": 1621601300,
  "signatureCounter": 14
}
```

---

### Error codes
For a list of error codes, please refer to [this link](https://github.com/A-Trust/KassenSichV/tree/main/Online#numeric-error-values).
