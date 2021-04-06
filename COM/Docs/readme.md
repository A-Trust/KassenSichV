# asign TSE COM Object

This COM Object is a COM wrapper for the SMAERS Module `asigntse.dll` adding required features like BASE64 En/Decode for operating a cash register. 

## Delivery
- `asigntsecom.dll` The asign TSE COM Object
- `VBTester`: a Test Program including source showing the basic use of the COM Object using VB6 (StartTransaction, FinishTransaction and ExportData - write tar file)
 
## Installation

- download the newest version of the [SMAERS Module](https://www.a-trust.at/TsePartner/KassenSichV/) (`asigntse.dll`) after logging in with your partner account
- read `DeveloperManual`: the developer manual for the SMAERS module explaining the exported functions and their parameters 
- place the SMAERS Module (`asigntse.dll`) in the same folder as the COM Object
- place `asigntseonline.conf` in the same folder as the executable. This is the configuration file for the test system, which has to be replaced by the production version
- register (`regsvr32`) the COM Object
```DOS
regsv32 asigntsecom.dll
```

### Example using the COM Object
please refer to the VBTester folder for the full example
```VB
'Registering the components
Set comobj = CreateObject("asigntsecom.tse")
Set comobjBase64 = CreateObject("asigntsecom.Base64")
```

```VB
' perform StartTransaction
result = comobj.StartTransaction(clientId, proccessData, processType, additionalData, transactionNumber, logTime, serialNumber, signatureCounter, signatureValue)
```

```VB
' convert binary value to Base64
result = comobjBase64.Encode(signatureValue, temp)
```

## FAQ
- Run-time-error 429 ActiveX component can't create object:

  The COM Component hasn't been registered using `regsvr32`.
- Parameters of regsvr32:
[MSDN](https://docs.microsoft.com/en-us/previous-versions/windows/it-pro/windows-xp/bb490985(v=technet.10)?redirectedfrom=MSDN)
- Can the DLL be registered silently?
asigntsecom.dll
```DOS
regsvr32 /s asigntsecom.dll
```
- How do I unregister the DLL:
```DOS
regsvr32 /u asigntsecom.dll
```
- Is there an explanation of the functions?

  Please refer to the [DeveloperManual](https://www.a-trust.at/TsePartner/KassenSichV/DeveloperManual.aspx) of the SMAERS Module
- TEST Version

  The test Version of the COM Object stops working end of 2020, please contact vertrieb@a-trust.at for further details