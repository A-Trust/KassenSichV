@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

SET clientID=12
SET processType="Kassenbeleg-V1"

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

SET bat_version=1.1.0.0

CALL :WriteNew "Begin execute ASignTseExe.bat"
CALL :NewLine

CALL :Write "asigntseBAT.bat file - Version : %bat_version%"
asigntseEXE.exe version > %output_file%
CALL :ReadOutAndWrite
asigntseEXE.exe at_getVersion > %output_file%
set /p lib_version=<asigntseEXE_out.txt
CALL :Write "asigntse.dll - Version : %lib_version%"
CALL :NextFunctionSpacer 


::Re
CALL :ExecuteCommand "asigntseEXE.exe" "at_registerclientid" "%clientID%"
CALL :NextFunctionSpacer 


::SE-API Functions -- Transaction
CALL :Write "-- SE-API Functions -- Transaction --"
CALL :NewLine

CALL :ExecuteCommand "asigntseEXE.exe" "StartTransaction" "%clientID%"
SET /p transactionNumber=< asigntseEXE_out.txt
CALL :Write "TransactionNumber:%transactionNumber%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "FinishTransaction" "%clientID% %transactionNumber% %processType% Beleg^^0.00_2.55_0.00_0.00_0.00_^^2.55Bar"
CALL :NextFunctionSpacer 

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

CALL :ExecuteCommand "asigntseEXE.exe" "exportDataFilteredByTransactionNumberIntervalAndClientId" "%transactionNumber% %transactionNumber% %clientID% 0 %exportTNICl_file%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "exportDataFilteredByPeriodOfTime" "2019.01.15_00:00:00 2019.21.01_00:00:00 0 %exportPT_file%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "exportDataFilteredByPeriodOfTimeAndClientId" "2019.01.15_00:00:00 2019.21.01_00:00:00 %clientID% 0 %exportPT_file%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "exportCertificates" "%exportCert_file%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "readLogMessage" "%exportLM_file%"
CALL :NextFunctionSpacer 


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




::Configuration example
CALL :Write "-- cfg with Start/FinishTransaction --"
CALL :Write "-- To use the configuration functions: asigntseEXE.exe functionname parameter --cfgxxxxxxxxxx cfgparameter --cfgxxxxxxxxxx cfgparameter ..."
CALL :ExecuteCommand "asigntseEXE.exe" "StartTransaction" "%clientID% --cfgsetloggingenabled true --cfgsetloggingstderr true"
SET /p transactionNumber=< asigntseEXE_out.txt
CALL :Write "TransactionNumber:%transactionNumber%"
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "FinishTransaction" "%clientID% %transactionNumber% %processType% Beleg^^0.00_2.55_0.00_0.00_0.00_^^2.55Bar --cfgsetloggingenabled true --cfgsetloggingstderr true"
CALL :NextFunctionSpacer 


::QR-CODE
CALL :Write "-- QR-CODE --"

CALL :ExecuteCommand "asigntseEXE.exe" "StartTransaction" "%clientID%"
CALL :ReadDataFromOutput 1 transactionNumber
CALL :ReadDataFromOutput 2 start_time
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "FinishTransaction" "%clientID% %transactionNumber% %processType% Beleg^^0.00_2.55_0.00_0.00_0.00_^^2.55Bar" 
CALL :ReadDataFromOutput 1 finishtransactionNumber
CALL :ReadDataFromOutput 2 finish_time
CALL :ReadDataFromOutput 3 var_signature
CALL :ReadDataFromOutput 4 var_serialnumber
CALL :ReadDataFromOutput 5 var_sigcounter
CALL :NextFunctionSpacer 

CALL :ExecuteCommand "asigntseEXE.exe" "convertstringtobase64" %var_signature%
CALL :ReadDataFromOutput 1 var_signatureb64

CALL :ExecuteCommand "asigntseEXE.exe" "genqrcode" "%clientID% %processType% Beleg^^0.00_2.55_0.00_0.00_0.00_^^2.55Bar %finishtransactionNumber% %var_sigcounter% %start_time% %finish_time% %var_signatureb64% ."
CALL :NextFunctionSpacer 

echo FINISH ...
PAUSE
EXIT /B 2




::Execute and parse data
::-----------------------------------------------------
:ExecuteCommand
SET exe=%~1
SET functionname=%~2
SET parameter=%3
SET parameter=%parameter:"=%

CALL :ConsoleBeginFunction %functionname%
CALL :Write "Command: %exe% %functionname% %parameter%"

%exe% %functionname% %parameter% > asigntseEXE_out.txt
IF %errorlevel%==0 ( 
  CALL :Write "AsignTseEXE ResponseCode : 0 - EXECUTION_OK"   
   
  CALL :Write "AsignTseEXE ResponseData :"
  CALL :Write "------------------------" 
  CALL :ReadOutAndWrite
) ELSE (
	for /f "tokens=*" %%a in (errortext.txt) do (
	SET std=%%a
	IF "!std:~0,5!"=="%2" ( 
		echo AsignTseEXE ReponseCode : %1 - !std!
		)
	)
)
CALL :Write "------------------------"  
goto :eof 


:ReadDataFromOutput
set "xprvar="

SET /A skip_ = %1% - 1 

IF "%1"=="1" (
	for /F "delims=" %%i in (asigntseEXE_out.txt) do if not defined xprvar set "xprvar=%%i"

) ELSE (
	for /F "skip=%skip_% delims=" %%i in (asigntseEXE_out.txt) do if not defined xprvar set "xprvar=%%i"
)
set %2=%xprvar%
goto :eof

:ReadOutAndWrite
type asigntseEXE_out.txt
type asigntseEXE_out.txt >> %log_file%
goto :eof
::-----------------------------------------------------




::logging
::-----------------------------------------------------
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

:strLen
setlocal enabledelayedexpansion
:strLen_Loop
   if not "!%1:~%len%!"=="" set /A len+=1 & goto :strLen_Loop
(endlocal & set %2=%len%)
goto :eof
::-----------------------------------------------------




