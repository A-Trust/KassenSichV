@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

SET clientID=client
SET clientID2=client2
SET clientID3=client3
SET clientID4=client4
SET tseId="TSE_1"
SET processType="Kassenbeleg-V1"
SET processData="Beleg^75.33_7.99_0.00_0.00_0.00^10.00:Bar_5.00:Bar:CHF_5.00:Bar:USD_64.30:Unbar"

SET exportData_file="exportData.tar"
SET exportCert_file="exportCertificate.tar"
SET exportSerNr_file="exportSerialNumber.tar"
SET exportTNCI_file="exportDataTransNrClientId.tar"
SET exportTN_file="exportDataTransNr.tar"
SET exportTNI_file="exportDataTransNrInterval.tar"
SET exportTNICl_file="exportDataTransNrIntervalClientId.tar"
SET exportPT_file="exportDataPeriodTime.tar"
SET exportLM_file="LogMessage.asn1"

SET output_file="asigntseEXE_out.txt"
SET log_file="asigntseEXE_log.txt"

SET bat_version=1.0.0.3

CALL :WriteNew "Begin execute ASignTseExe.bat"
CALL :NewLine

CALL :Write "asigntseBAT-Premium.bat file - Version : %bat_version%"
asigntseEXE.exe version > %output_file%
CALL :ReadOutAndWrite
asigntseEXE.exe at_getVersion > %output_file%
set /p lib_version=<asigntseEXE_out.txt
CALL :Write "asigntse.dll - Version : %lib_version%"
CALL :NextFunctionSpacer 

:: CV Functions

CHOICE /C YN /N /M "Logging [Y]yes or [N]No"
   GOTO log_%ERRORLEVEL% 
   
:log_1 
	SET logging=--cfgsetloggingenabled true --cfgsetloggingstderr true
	CALL :Write "Logging"
GOTO runtests 

:log_2
	SET logging=""
	CALL :Write "No logging"
GOTO runtests

:runtests
CALL :premiuminitialize
CALL :premiummapclients
CALL :premiumtests
CALL :seapitrans
CALL :seapiexport
CALL :seapiadmin
CALL :atrustapi

:end
CALL :Write "FINISHED"
EXIT /B

:premiuminitialize
CALL :Write "Initializing..."

CALL :ExecuteCommand "asigntseEXE.exe" "at_getLifecycleState" ""
CALL :NextFunctionSpacer 
for /f "delims==" %%a in (asigntseEXE_out.txt) do set lcs=%%a
CALL :Write "Lifecycle state:%lcs%"
CALL :NextFunctionSpacer 

IF NOT %lcs%==1 (
    CALL :Write "Already initialized"
    goto :eof 
)

CALL :ExecuteCommand "asigntseEXE.exe" "cv_getPinStatus" "%logging%"
for /f "delims==" %%a in (asigntseEXE_out.txt) do set pinStatus=%%a
CALL :Write "PinStatus:%pinStatus%"
CALL :NextFunctionSpacer 

IF %pinStatus%==0 (
    CALL :Write "PIN already set"
) ELSE (
    CALL :Write "Setting PIN..."
	CALL :ExecuteCommand "asigntseEXE.exe" "cv_initializePinValues" "11111111 something1 22222222 something2 %logging%"
	CALL :NextFunctionSpacer 
)

CALL :ExecuteCommand "asigntseEXE.exe" "initializeDescriptionSet" "%logging% --authenticateuser Admin 11111111"
CALL :NextFunctionSpacer 
goto :eof 

:premiummapclients
CALL :Write "Mapping clients..."
CALL :ExecuteCommand "asigntseEXE.exe" "at_getSerialNumber" "%logging%"
for /f "delims==" %%a in (asigntseEXE_out.txt) do set serialHex=%%a
CALL :Write "serial:%serialHex%"

CALL :ExecuteCommand "asigntseEXE.exe" "cv_getCertificateExpirationDate" "%serialHex% %logging%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cv_getErsMappings" "%logging%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cv_registerClientId" "%clientID%  %logging% --authenticateuser Admin 11111111"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cv_mapErsToKey" "%clientID2% %serialHex%  %logging% --authenticateuser Admin 11111111"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cv_getErsMappings" "%logging%"
goto :eof 



:premiumtests
CALL :Write "Premium tests..."

CALL :ExecuteCommand "asigntseEXE.exe" "cv_getFirmwareId" "%logging%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cv_getImplementationVersionString" "%logging%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cv_getImplementationVersion" "%logging%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cv_getApiVersionString" "%logging%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cv_getApiVersion" "%logging%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cv_getUniqueId" "%logging%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cv_getCertificationId" "%logging%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cv_getAvailableLogMemory" "%logging%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cv_getTotalLogMemory" "%logging%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cv_getPinStatus" "%logging%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cv_getWearIndicator" "%logging%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cv_getTimeSyncInterval" "%logging%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cv_getTimeSyncVariant" "%logging%"
CALL :NextFunctionSpacer 

SET client="%clientID%"
SET trans=NULL
SET startTrans=NULL
SET endTrans=NULL
SET startTime=NULL
SET endTime=NULL
SET maxRec=0
SET file=export_data_cv.tar

CALL :ExecuteCommand "asigntseEXE.exe" "cv_exportData" "%client% %trans% %startTrans% %endTrans% %startTime% %endTime% %maxRec% %file% %logging%"
CALL :NextFunctionSpacer 
goto :eof 


:seapitrans
::SE-API Functions -- Transaction
CALL :Write "-- SE-API Functions -- Transaction --"
CALL :NewLine

CALL :ExecuteCommand "asigntseEXE.exe" "StartTransaction" "%clientID%"
SET /p transactionNumber=< asigntseEXE_out.txt
CALL :Write "TransactionNumber:%transactionNumber%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "FinishTransaction" "%clientID% %transactionNumber% %processData% %processType%"
CALL :NextFunctionSpacer 

goto :eof 


:seapiexport

::SE-API - dataexportcalls
CALL :Write "-- SE-API Functions -- dataexportcalls --"
CALL :NewLine

CALL :ExecuteCommand "asigntseEXE.exe" "exportData" "0 %exportData_file%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "exportDataFilteredByTransactionNumberAndClientId" "%transactionNumber% %clientID% %exportTNCI_file%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "exportDataFilteredByTransactionNumber" "%transactionNumber% %exportTN_file%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "exportDataFilteredByTransactionNumberInterval" "%transactionNumber% %transactionNumber% 0 %exportTNI_file%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "exportDataFilteredByTransactionNumberIntervalAndClientId" "%transactionNumber% %transactionNumber% %clientID% 0 %exportTNCI_file%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "exportDataFilteredByPeriodOfTime" "2019-01-15_00:00:00 2019-21-01_00:00:00 0 %exportPT_file%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "exportDataFilteredByPeriodOfTimeAndClientId" "2019-01-15_00:00:00 2019-21-01_00:00:00 %clientID% 0 %exportPT_file%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "exportCertificates" "%exportCert_file%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "readLogMessage" "%exportLM_file%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "deleteStoredData" ""
CALL :NextFunctionSpacer 

goto :eof 



:seapiadmin
::SE-API - Administrative Calls
CALL :Write "-- SE-API Functions -- Administrative Calls --"
CALL :NewLine


CALL :ExecuteCommand "asigntseEXE.exe" "getMaxNumberOfClients" ""
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "getCurrentNumberOfClients" ""
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "getMaxNumberOfTransactions" ""
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "getCurrentNumberOfTransactions" ""
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "getSupportedTransactionUpdateVariants" ""
CALL :NextFunctionSpacer 

goto :eof 


:atrustapi
::A-Trust API Functions
CALL :Write "-- A-Trust API Functions --"
CALL :NewLine

CALL :ExecuteCommand "asigntseEXE.exe" "at_getPublicKey" ""
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "at_getLogTimeFormat" ""
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "at_getOpenTransactions" ""
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "at_getSignatureAlgorithm" ""
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "at_getSignatureCounter" ""
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "at_getTransactionCounter" ""
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "at_getVersion" ""
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "at_getLifecycleState" ""
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "at_getSerialNumber" ""
CALL :NextFunctionSpacer 

goto :eof 

:configfunctions
::Configuration Functions
CALL :ExecuteCommand "asigntseEXE.exe" "cfgSetConfigFile" "%CD%\asigntseonline.conf"
CALL :NextFunctionSpacer 

::CALL :ExecuteCommand "asigntseEXE.exe" "cfgTseAdd" "TSE_1 1 https://hs-abnahme.a-trust.at/asigntseonline/v1 u0000000000212xx test test"
CALL :ExecuteCommand "asigntseEXE.exe" "cfgTseAdd" "TSE_1 2 H: test TimeAdmin 22222222"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cfgTseRemove" "TSE_1"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cfgSetLoggingStderr" "true"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cfgSetLoggingFile" "true"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cfgSetLogDir" "%CD%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cfgSetLogLevel" "true"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cfgSetLogAppend" "true"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cfgSetLogColors" "true"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cfgSetLogDetails" "true"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "cfgSetLogStderrColors" "true"
CALL :NextFunctionSpacer 

goto :eof 


:qrcode
::QR-CODE
CALL :ExecuteCommand "asigntseEXE.exe" "genqrcode" "955002-00 Kassenbeleg-V1 Beleg^0.00_2.55_0.00_0.00_0.00_^2.55Bar . qrcode.bmp"
CALL :NextFunctionSpacer 

goto :eof 








echo FINISH ...
PAUSE
EXIT /B 2



:ExecuteCommand
SET exe=%~1
SET functionname=%~2
SET parameter=%3
SET parameter=%parameter:"=%

CALL :ConsoleBeginFunction %functionname%
CALL :Write "Command: %exe% %functionname% %parameter%"
%exe% %functionname% %parameter% > asigntseEXE_out.txt
CALL :ReadResponseCodeWithData %functionname% %errorlevel% 
goto :eof

:ReadOutAndWrite
::for /f "tokens=* delims=" %%x in (asigntseEXE_out.txt) do echo %%x    
::for /f "tokens=* delims=" %%x in (asigntseEXE_out.txt) do echo %%x >> %log_file%    
type asigntseEXE_out.txt
type asigntseEXE_out.txt >> %log_file%
goto :eof

:Write
echo %~1
echo %~1 >> %log_file%
goto :eof  

:WriteNew
echo %~1
echo %~1 > %log_file%
goto :eof  

:NewLine
echo.
echo.>>%log_file%
goto :eof

:NextFunctionSpacer
CALL :NewLine
CALL :NewLine
goto :eof

:ReadResponseCode
IF "%2"=="0" ( 
    echo AsignTseEXE ReponseCode : 0 - EXECUTION_OK
    echo.     
    goto :eof
)
for /f "tokens=*" %%a in (errortext.txt) do (
  SET std=%%a
  IF "!std:~0,5!"=="%2" ( 
    echo AsignTseEXE ReponseCode : %1 - !std!
    goto :eof  
  )
)
echo ExitCode for %1 :: %2
echo An error has occurred! Continue?
PAUSE
goto :eof  

:ReadResponseCodeWithData
IF "%2"=="0" ( 
  CALL :Write "AsignTseEXE ResponseCode : 0 - EXECUTION_OK"   
  CALL :PrintOutput  
  goto :eof  
)

for /f "tokens=*" %%a in (errortext.txt) do (
  SET std=%%a
  IF "!std:~0,5!"=="%2" ( 
    echo AsignTseEXE ReponseCode : %1 - !std!
    PAUSE
    goto :eof  
  )
)
echo ExitCode for %1 :: %2
echo An error has occurred! Continue?
PAUSE
goto :eof 

:ConsoleBeginFunction
set funcname=%1
set spacer=
CALL :strLen funcname strlen
set /A steps=75-%strlen%
for /l %%a in (1,1,%steps%) do CALL set "spacer=%%spacer%%-"
echo|set /p=----%funcname%%spacer%
echo|set /p=----%funcname%%spacer% >> %log_file%
CALL :NewLine
goto :eof

:ConsoleEndFunction
set spacer=
for /l %%a in (1,1,79) do CALL set "spacer=%%spacer%%-"
echo|set /p=%spacer%
goto :eof

:strLen
setlocal enabledelayedexpansion
:strLen_Loop
   if not "!%1:~%len%!"=="" set /A len+=1 & goto :strLen_Loop
(endlocal & set %2=%len%)
goto :eof


:PrintOutput
  CALL :Write "AsignTseEXE ResponseData :"
  CALL :Write "------------------------" 
  CALL :ReadOutAndWrite
  CALL :Write "------------------------"  
goto :eof