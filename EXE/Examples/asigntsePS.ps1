$clientID = 12
$exeFile = ".\asigntseEXE.exe"
$processType = "Kassenbeleg-V1"
$processData = "Beleg^75.33_7.99_0.00_0.00_0.00^10.00:Bar_5.00:Bar:CHF_5.00:Bar:USD_64.30:Unbar"
$tseId = "TSE_1"

$exportData_file="exportData.tar"
$exportDataWithTse_file="exportDataWithTse.tar"

$exportCert_file="exportCertificate.tar"
$exportCertWithTse_file="exportCertificateWithTse.tar"

$exportSerNr_file="exportSerialNumber.tar"
$exportSerNrWithTse_file="exportSerialNumberWithTse.tar"

$exportTNCI_file="exportDataTransNrClientId.tar"
$exportTNCIWithTse_file="exportDataTransNrClientIdWithTse.tar"

$exportTN_file="exportDataTransNr.tar"
$exportTNWithTse_file="exportDataTransNrWithTse.tar"

$exportTNI_file="exportDataTransNrInterval.tar"
$exportTNIWithTse_file="exportDataTransNrIntervalWithTse.tar"

$exportTNICl_file="exportDataTransNrIntervalClientId.tar"
$exportTNIClWithTse_file="exportDataTransNrIntervalClientIdWithTse.tar"

$exportPT_file="exportDataPeriodTime.tar"
$exportPTWithTse_file="exportDataPeriodTimeWithTse.tar"

$exportLM_file="LogMessage.asn1"
$exportLMWithTse_file="LogMessageWithTse.asn1"

$output_file="asigntseEXE_out.txt"
$log_file="asigntseEXE_log.txt"

$errorDict = @{
    -3004 = "ERROR_ALLOCATION_FAILED"
    -4000 = "ERROR_AUTHENTICATION_FAILED"
    -3013 = "ERROR_BUFFER_TOO_SMALL"
    -5020 = "ERROR_CERTIFICATE_EXPIRED"
    -5038 = "ERROR_CONFIG_NOT_FOUND"
    -5032 = "ERROR_DELETE_STORED_DATA_FAILED"
    -5024 = "ERROR_DESCRIPTION_NOT_SET_BY_MANUFACTURER"
    -5025 = "ERROR_DESCRIPTION_SET_BY_MANUFACTURER"
    -5037 = "ERROR_DISABLE_SECURE_ELEMENT_FAILED"
    -3009 = "ERROR_ERS_ALREADY_MAPPED"
    -5014 = "ERROR_EXPORT_CERT_FAILED"
    -5026 = "ERROR_EXPORT_SERIAL_NUMBERS_FAILED"
    -3005 = "ERROR_FILE_NOT_FOUND"
    -5011 = "ERROR_FINISH_TRANSACTION_FAILED"
    -3001 = "ERROR_FUNCTION_NOT_SUPPORTED"
    -5028 = "ERROR_GET_CURRENT_NUMBER_OF_CLIENTS_FAILED"
    -5030 = "ERROR_GET_CURRENT_NUMBER_OF_TRANSACTIONS_FAILED"
    -5027 = "ERROR_GET_MAX_NUMBER_OF_CLIENTS_FAILED"
    -5029 = "ERROR_GET_MAX_NUMBER_TRANSACTIONS_FAILED"
    -5031 = "ERROR_GET_SUPPORTED_UPDATE_VARIANTS_FAILED"
    -5005 = "ERROR_ID_NOT_FOUND"
    -5039 = "ERROR_INVALID_CONFIG"
    -3002 = "ERROR_IO"
    -3000 = "ERROR_MISSING_PARAMETER"
    -5007 = "ERROR_NO_DATA_AVAILABLE"
    -3010 = "ERROR_NO_ERS"
    -3015 = "ERROR_NO_KEY"
    -5015 = "ERROR_NO_LOG_MESSAGE"
    -3014 = "ERROR_NO_SUCH_KEY"
    -5017 = "ERROR_NO_TRANSACTION"
    -5004 = "ERROR_PARAMETER_MISMATCH"
    -5016 = "ERROR_READING_LOG_MESSAGE"
    -5012 = "ERROR_RESTORE_FAILED"
    -5001 = "ERROR_RETRIEVE_LOG_MESSAGE_FAILED"
    -5021 = "ERROR_SECURE_ELEMENT_DISABLED"
    -3016 = "ERROR_SE_API_DEACTIVATED"
    -3017 = "ERROR_SE_API_NOT_DEACTIVATED"
    -5018 = "ERROR_SE_API_NOT_INITIALIZED"
    -3006 = "ERROR_SE_COMMUNICATION_FAILED"
    -5034 = "ERROR_SIGNING_SYSTEM_OPERATION_DATA_FAILED"
    -5009 = "ERROR_START_TRANSACTION_FAILED"
    -5002 = "ERROR_STORAGE_FAILURE"
    -5013 = "ERROR_STORING_INIT_DATA_FAILED"
    -3012 = "ERROR_STREAM_WRITE"
    -5019 = "ERROR_TIME_NOT_SET"
    -5008 = "ERROR_TOO_MANY_RECORDS"
    -5006 = "ERROR_TRANSACTION_NUMBER_NOT_FOUND"
    -3007 = "ERROR_TSE_COMMAND_DATA_INVALID"
    -3008 = "ERROR_TSE_RESPONSE_DATA_INVALID"
    -3003 = "ERROR_TSE_TIMEOUT"
    -3011 = "ERROR_TSE_UNKNOWN_ERROR"
    -4001 = "ERROR_UNBLOCK_FAILED"
    -5033 = "ERROR_UNEXPORTED_STORED_DATA"
    -3100 = "ERROR_UNKNOWN"
    -5003 = "ERROR_UPDATE_TIME_FAILED"
    -5010 = "ERROR_UPDATE_TRANSACTION_FAILED"
    -5036 = "ERROR_USER_ID_NOT_AUTHENTICATED"
    -5035 = "ERROR_USER_ID_NOT_MANAGED"
    -5023 = "ERROR_USER_NOT_AUTHENTICATED"
    -5022 = "ERROR_USER_NOT_AUTHORIZED"
    0     = "EXECUTION_OK"
}

function WriteExitCode {  
    Param ($FuncName, $ExitCode )
    $x = $errorDict[$ExitCode]
    Write-Host "$FuncName returns: $ExitCode ($x)"
}


function Invoke-Transaction { 
    # StartTransaction
    Write-Host -NoNewline "-----StartTransaction"
    Write-Host $("-" * (75-"StartTransaction".Length))

    $process = Start-Process -NoNewWindow -PassThru -Wait -FilePath $exeFile -ArgumentList "StartTransaction $clientID" -RedirectStandardError "stderr.txt" -RedirectStandardOutput "stdout.txt"
    $ret = $process.ExitCode
    WriteExitCode "StartTransaction"  $ret
    # $err = get-content stderr.txt
    $out = get-content stdout.txt
    $arr = $out -split '\n'
    $transactionNumber = $arr[0]
    $StartLogTime = $arr[1]
    $StartSignatureValue = $arr[2]
    $serialNumber = $arr[3]
    $startSignatureCounter = $arr[4]

    Write-Host `n

    ################################################################################
    
    # FinishTransaction
    Write-Host -NoNewline "-----FinishTransaction"
    Write-Host $("-" * (75-"FinishTransaction".Length))    
    
    $process = Start-Process -NoNewWindow -PassThru -Wait -FilePath $exeFile -ArgumentList "FinishTransaction $clientID $transactionNumber $processType $processData" -RedirectStandardError "stderr.txt" -RedirectStandardOutput "stdout.txt"
    $ret = $process.ExitCode
    WriteExitCode "FinishTransaction"  $ret
    $out = get-content stdout.txt
    $arr = $out -split '\n'
    $FinishLogTime = $arr[1]
    $FinishSignatureValue = $arr[2]
    $FinishSignatureCounter = $arr[3]

    Write-Host "Transaction Number: $transactionNumber"
    Write-Host "log time: start: $StartLogTime - finsish: $FinishLogTime"
    Write-Host "Start-SignatureValue: $StartSignatureValue"
    Write-Host "Finish-SignatureValue: $FinishSignatureValue"
    Write-Host "Start-SignatureCounter: $StartSignatureCounter"
    Write-Host "Finish-SignatureCounter: $FinishSignatureCounter"

    Write-Host `n

    return $transactionNumber
}

function Invoke-Transaction_WithTse { 
    # StartTransaction
    Write-Host -NoNewline "-----StartTransactionWithTse"
    Write-Host $("-" * (75-"StartTransactionWithTse".Length))

    $process = Start-Process -NoNewWindow -PassThru -Wait -FilePath $exeFile -ArgumentList "StartTransactionWithTse $clientID $tseId" -RedirectStandardError "stderr.txt" -RedirectStandardOutput "stdout.txt"
    $ret = $process.ExitCode
    WriteExitCode "StartTransactionWithTse"  $ret
    # $err = get-content stderr.txt
    $out = get-content stdout.txt
    $arr = $out -split '\n'
    $transactionNumberWithTse = $arr[0]
    $StartLogTime = $arr[1]
    $StartSignatureValue = $arr[2]
    $serialNumber = $arr[3]
    $startSignatureCounter = $arr[4]
    Write-Host `n

    ################################################################################
    
    # FinishTransaction
    Write-Host -NoNewline "-----FinishTransactionWithTse"
    Write-Host $("-" * (75-"FinishTransactionWithTse".Length))    
    
    $process = Start-Process -NoNewWindow -PassThru -Wait -FilePath $exeFile -ArgumentList "FinishTransactionWithTse $clientID $transactionNumberWithTse $processType $processData $tseId" -RedirectStandardError "stderr.txt" -RedirectStandardOutput "stdout.txt"
    $ret = $process.ExitCode
    WriteExitCode "FinishTransactionWithTse"  $ret
    $out = get-content stdout.txt
    $arr = $out -split '\n'
    $FinishLogTime = $arr[1]
    $FinishSignatureValue = $arr[2]
    $FinishSignatureCounter = $arr[3]

    Write-Host "Transaction Number: $transactionNumberWithTse"
    Write-Host "log time: start: $StartLogTime - finsish: $FinishLogTime"
    Write-Host "Start-SignatureValue: $StartSignatureValue"
    Write-Host "Finish-SignatureValue: $FinishSignatureValue"
    Write-Host "Start-SignatureCounter: $StartSignatureCounter"
    Write-Host "Finish-SignatureCounter: $FinishSignatureCounter"

    Write-Host `n

    return $transactionNumberWithTse
}

function ExecuteCommand
{
    PARAM(
        [string]$functionname,
        [string]$parameter
    )    
    #WriteOutputHeader
    Write-Host -NoNewline "-----$functionname"
    Write-Host $("-" * (75-$functionname.Length))
       
    $argument = "$functionname $parameter"
    Write-Host "Command: asigntseEXE.exe $argument"

    $process = Start-Process -NoNewWindow -PassThru -Wait -FilePath $exeFile -ArgumentList $argument -RedirectStandardError "stderr.txt" -RedirectStandardOutput "stdout.txt"
    $ret = $process.ExitCode
    WriteExitCode $function $ret    
    
    Write-Host `n
}

function ExecuteCommandPrintOutput
{
    PARAM(
        [string]$functionname,
        [string]$parameter
    ) 
    ExecuteCommand $functionname $parameter
    ReadDataFromOutput
}

function ReadDataFromOutput
{
    $out = get-content stdout.txt
    $arr = $out -split '\n'
    foreach($data in $arr)
    {
        Write-Host $data
    }

    Write-Host `n    
}

function Readstderr
{
    Write-Host `n    

    $out = get-content stderr.txt
    $arr = $out -split '\n'
    foreach($data in $arr)
    {
        Write-Host $data
    }

    Write-Host `n    
}




#StartTransaction / FinishTransaction
$transactionNumber = Invoke-Transaction
$transactionNumberWithTse = Invoke-Transaction_WithTse

#SE-API - dataexportcalls
Write-Host "`n-- SE-API Functions -- dataexportcalls --"

ExecuteCommand "exportData" "0 $exportData_file"
ExecuteCommand "exportDataWithTse" "0 $exportDataWithTse_file $tseId"

ExecuteCommand "exportDataFilteredByTransactionNumberAndClientId" "$transactionNumber $clientID $exportTNCI_file"
ExecuteCommand "exportDataFilteredByTransactionNumberAndClientIdWithTse" "$transactionNumberWithTse $clientID $exportTNCIWithTse_file $tseId"

ExecuteCommand "exportDataFilteredByTransactionNumber" "$transactionNumber $exportTN_file"
ExecuteCommand "exportDataFilteredByTransactionNumberWithTse" "$transactionNumberWithTse $exportTNWithTse_file $tseId"
 
ExecuteCommand "exportDataFilteredByTransactionNumberInterval" "$transactionNumber $transactionNumber 0 $exportTNI_file" 
ExecuteCommand "exportDataFilteredByTransactionNumberIntervalWithTse" "$transactionNumberWithTse $transactionNumberWithTse 0 $exportTNIWithTse_file $tseId"
 
ExecuteCommand "exportDataFilteredByTransactionNumberIntervalAndClientId" "$transactionNumber $transactionNumber $clientID 0 $exportTNICl_file"
ExecuteCommand "exportDataFilteredByTransactionNumberIntervalAndClientIdWithTse" "$transactionNumberWithTse $transactionNumberWithTse $clientID 0 $exportTNIClWithTse_file $tseId"
 
ExecuteCommand "exportDataFilteredByPeriodOfTime" "2019.01.15_00:00:00 2019.21.01_00:00:00 0 $exportPT_file"
ExecuteCommand "exportDataFilteredByPeriodOfTimeWithTse" "2019.01.15_00:00:00 2019.21.01_00:00:00 0 $exportPTWithTse_file $tseId"

ExecuteCommand "exportDataFilteredByPeriodOfTimeAndClientId" "2019.01.15_00:00:00 2019.21.01_00:00:00 $clientID 0 $exportPT_file"
ExecuteCommand "exportDataFilteredByPeriodOfTimeAndClientIdWithTse" "2019.01.15_00:00:00 2019.21.01_00:00:00 $clientID 0 $exportPTWithTse_file $tseId"

ExecuteCommand "exportCertificates" "$exportCert_file"
ExecuteCommand "exportCertificatesWithTse" "$exportCertWithTse_file $tseId"

ExecuteCommand "readLogMessage" "$exportLM_file"
ExecuteCommand "readLogMessageWithTse" "$exportLMWithTse_file $tseId"
 

#SE-API - Administrative Calls
Write-Host "`n-- SE-API Functions -- Administrative Calls --"
ExecuteCommandPrintOutput "getMaxNumberOfClients" ""
ExecuteCommandPrintOutput "getMaxNumberOfClientsWithTse" "$tseId"

ExecuteCommandPrintOutput "getCurrentNumberOfClients" ""
ExecuteCommandPrintOutput "getCurrentNumberOfClientsWithTse" "$tseId"

ExecuteCommandPrintOutput "getMaxNumberOfTransactions" ""
ExecuteCommandPrintOutput "getMaxNumberOfTransactionsWithTse" "$tseId"

ExecuteCommandPrintOutput "getCurrentNumberOfTransactions" ""
ExecuteCommandPrintOutput "getCurrentNumberOfTransactionsWithTse" "$tseId"

ExecuteCommandPrintOutput "getSupportedTransactionUpdateVariants" ""
ExecuteCommandPrintOutput "getSupportedTransactionUpdateVariantsWithTse" "$tseId"


#A-Trust API Functions
Write-Host "`n-- A-Trust API Functions --"
ExecuteCommandPrintOutput "at_getPublicKey" ""
ExecuteCommandPrintOutput "at_getPublicKeyWithTse" "$tseId"

ExecuteCommandPrintOutput "at_getLogTimeFormat" ""
ExecuteCommandPrintOutput "at_getLogTimeFormatWithTse" "$tseId"

ExecuteCommandPrintOutput "at_getOpenTransactions" ""
ExecuteCommandPrintOutput "at_getOpenTransactionsWithTse" "$tseId"

ExecuteCommandPrintOutput "at_getSignatureAlgorithm" ""
ExecuteCommandPrintOutput "at_getSignatureAlgorithmWithTse" "$tseId"

ExecuteCommandPrintOutput "at_getSignatureCounter" ""
ExecuteCommandPrintOutput "at_getSignatureCounterWithTse" "$tseId"

ExecuteCommandPrintOutput "at_getTransactionCounter" ""
ExecuteCommandPrintOutput "at_getTransactionCounterWithTse" "$tseId"

ExecuteCommandPrintOutput "at_getVersion" ""

ExecuteCommandPrintOutput "at_getLifecycleState" ""
ExecuteCommandPrintOutput "at_getLifecycleStateWithTse" "$tseId"

ExecuteCommandPrintOutput "at_getSerialNumber" ""
ExecuteCommandPrintOutput "at_getSerialNumberWithTse" "$tseId"


#QR-CODE
Write-Host "`n-- QR-CODE --"
ExecuteCommandPrintOutput "genqrcode" "955002-00 Kassenbeleg-V1 Beleg^0.00_2.55_0.00_0.00_0.00_^2.55Bar . qrcode.bmp"

#cfg Parameter
Write-Host "`n-- cfg configuration parameter --"
Write-Host "`n-- append --cfgfunction cfgparmeter as parameter --"
ExecuteCommandPrintOutput "at_getPublicKey" "--cfgsetloggingenabled true --cfgsetloggingstderr true"
Readstderr