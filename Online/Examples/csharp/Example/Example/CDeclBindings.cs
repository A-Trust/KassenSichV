using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace Example
{
    public class CDeclBindings
    {
        const string PATH_TO_DLL = @"asigntse.dll";

        public enum UpdateVariants
        {
            signedUpdate, unsignedUpdate, signedAndUnsignedUpdate
        };

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 authenticateUser(byte[] userId, UInt32 userIdLength
                                                   , byte[] pin, UInt32 pinLength
                                                   , ref Int32 authenticationResult
                                                   , ref Int16 remainingRetries);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 authenticateUserWithTse(byte[] userId, UInt32 userIdLength
                                                   , byte[] pin, UInt32 pinLength
                                                   , ref Int32 authenticationResult
                                                   , ref Int32 remainingRetries
                                                   , byte[] tseId, UInt32 tseIdLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 logOut(byte[] userId, UInt32 userIdLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 updateTime(long newTime);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 startTransaction(byte[] clientId, UInt32 clientIdLength
                                                   , byte[] processData, UInt32 processDataLength
                                                   , byte[] processType, UInt32 processTypeLength
                                                   , byte[] additionalData, UInt32 additionalDataLength
                                                   , ref UInt32 transactionNumber
                                                   , ref Int64 tm
                                                   , ref IntPtr serialNumber, ref UInt32 serialNumberLength
                                                   , ref UInt32 signatureCounter
                                                   , ref IntPtr signatureValue, ref UInt32 signatureValueLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 updateTransaction(byte[] clientId, UInt32 clientIdLength
                                                    , UInt32 transactionNumber
                                                    , byte[] processData, UInt32 processDataLength
                                                    , byte[] processType, UInt32 processTypeLength
                                                    , ref Int64 tm
                                                    , ref IntPtr signatureValue, ref UInt32 signatureValueLength
                                                    , ref UInt32 signatureCounter);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 finishTransaction(byte[] clientId, UInt32 clientIdLength
                                                    , UInt32 transactionNumber
                                                    , byte[] processData, UInt32 processDataLength
                                                    , byte[] processType, UInt32 processTypeLength
                                                    , byte[] additionalData, UInt32 additionalDataLength
                                                    , ref Int64 tm
                                                    , ref IntPtr signatureValue, ref UInt32 signatureValueLength
                                                    , ref UInt32 signatureCounter);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern void at_free(ref IntPtr ptr);

        //public static extern void asigntse_free(ref IntPtr ptr);
        public static void asigntse_free(ref IntPtr ptr) { at_free(ref ptr); }


        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByTransactionNumberAndClientId(UInt32 transactionNumber,
                                                        byte[] clientId, UInt32 clientIdLength,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength);
        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByTransactionNumber(UInt32 transactionNumber,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByTransactionNumberInterval(UInt32 startTransactionNumber,
                                                        UInt32 endTransactionNumber,
                                                        UInt32 maximumNumberRecords,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByTransactionNumberIntervalAndClientId(UInt32 startTransactionNumber,
                                                        UInt32 endTransactionNumber,
                                                        byte[] clientId, UInt32 clientIdLength,
                                                        UInt32 maximumNumberRecords,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByPeriodOfTime(Int64 startDate,
                                                        Int64 endDate,
                                                        UInt32 maximumNumberRecords,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByPeriodOfTimeAndClientId(Int64 startDate,
                                                        Int64 endDate,
                                                        byte[] clientId, UInt32 clientIdLength,
                                                        UInt32 maximumNumberRecords,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportData(UInt32 maximumNumberRecords
                                             , ref IntPtr exportedData
                                             , ref UInt32 exportedDataLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportCertificates(ref IntPtr certificates
                                                     , ref UInt32 certificatesLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportSerialNumbers(ref IntPtr serialNumbers
                                                      , ref UInt32 serialNumbersLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 getMaxNumberOfClients(ref UInt32 maxNumberClients);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 getCurrentNumberOfClients(ref UInt32 currentNumberClients);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 getMaxNumberOfTransactions(ref UInt32 maxNumberTransactions);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 getCurrentNumberOfTransactions(ref UInt32 currentNumberTransactions);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 getSupportedTransactionUpdateVariants(ref UpdateVariants supportedUpdateVariants);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getPublicKey(ref IntPtr pub_key, ref UInt32 pub_key_len);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getVersion(ref IntPtr version, ref UInt32 versionLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_load();

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_unload();

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getSignatureAlgorithm(ref IntPtr signature_algorithm, ref UInt32 signature_algorithm_length);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getLogTimeFormat(ref IntPtr log_time_format, ref UInt32 log_time_format_length);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getSerialNumber(ref IntPtr serial, ref UInt32 serial_len);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getSignatureCounter(ref UInt32 counter);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getTransactionCounter(ref UInt32 counter);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getLifecycleState(ref UInt32 state);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_unsuspendSecureElement();
        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_suspendSecureElement();

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getCertificate(ref IntPtr cert, ref UInt32 certLenght);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getOpenTransactions(ref IntPtr transaction_numbers, ref UInt32 transaction_numbers_length);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetConfigFile(byte[] path, UInt32 pathLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetHttpProxy(byte[] proxyUrl, UInt32 proxyUrlLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetHttpProxyWithUsernameAndPassword(byte[] proxyUrl, UInt32 proxyUrlLength
                                                                         , byte[] proxyUsername, UInt32 proxyUsernameLength
                                                                         , byte[] proxyPassword, UInt32 proxyPasswordLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLogAppend(bool enabled);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLogColors(bool enabled);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLogDetails(bool enabled);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLogDir(byte[] path, UInt32 pathLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLogLevel(byte[] logLevel, UInt32 logLevelLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLogStderrColors(bool enabled);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLoggingEnabled(bool enabled);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLoggingFile(bool enabled);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLogStderr(bool enabled);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetRetries(UInt64 retries);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetTimeout(UInt64 timeout);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetHttpProxyWithUsernameAndPassword(byte[] tseID, UInt32 tseIDLength
                                                                         , UInt32 tseType
                                                                         , byte[] connParam, UInt32 connParamLength
                                                                         , byte[] atrustTseID, UInt32 atrustTseIDLength
                                                                         , byte[] atrustApiKey, UInt32 atrustApiKeyLength
                                                                         , byte[] timeAdminID, UInt32 timAdminIDLength
                                                                         , byte[] timeAdminPwd, UInt32 timAdminPwdLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgTseRemove(byte[] tseID, UInt32 tseIDLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 initializeDescriptionNotSet(byte[] description, UInt32 desriptionLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 initializeDescriptionSet();

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_setPins(byte[] adminPin, UInt32 adminPinLength
                                             , byte[] adminPuk, UInt32 adminPukLength);

        [DllImport(PATH_TO_DLL, CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_registerClientId(byte[] clientId, UInt32 clientIdLength);


        public static Dictionary<uint, string> LifeCycle = new Dictionary<uint, string>()
        {
            { 0, "Unknown"} ,
            { 1, "NotInitialized"} ,
            { 2, "Active"} ,
            { 3, "Suspended"} ,
            { 4, "Disabled"} ,
        };
    }
}
