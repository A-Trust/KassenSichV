using System;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.Text;

namespace LibAsignTse
{
    //////////////////////////////////////////////////////////////////////////////// 
    ///
    // Mapping for the native asigntse.dll 
    //
    //////////////////////////////////////////////////////////////////////////////// 
    public class NativeInterface
    {
        public enum UpdateVariants
        {
            signedUpdate, unsignedUpdate, signedAndUnsignedUpdate
        };

        public static Dictionary<uint, string> LifeCycle = new Dictionary<uint, string>()
        {
            { 0, "Unknown"} ,
            { 1, "Not Initialized"} ,
            { 2, "Active"} ,
            { 3, "Suspended"} ,
            { 4, "Disabled"} ,
        };

        //////////////////////////////////////////////////////////////////////////////// 
        // authenticateUser
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 authenticateUser(byte[] userId, UInt32 userIdLength
                                                   , byte[] pin, UInt32 pinLength
                                                   , ref Int32 authenticationResult
                                                   , ref Int16 remainingRetries);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 authenticateUserWithTse(byte[] userId, UInt32 userIdLength
                                                   , byte[] pin, UInt32 pinLength
                                                   , ref Int32 authenticationResult
                                                   , ref Int32 remainingRetries
                                                   , byte[] tseId, UInt32 tseIdLength);

        //////////////////////////////////////////////////////////////////////////////// 
        // logOut
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 logOut(byte[] userId, UInt32 userIdLength);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 logOutWithTse(byte[] userId, UInt32 userIdLength
                                                , byte[] tseId, UInt32 tseIdLength);

        //////////////////////////////////////////////////////////////////////////////// 
        // unblockUser
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 unblockUser(byte[] userId, UInt32 userIdLength
                                                   , byte[] puk, UInt32 pukLength
                                                   , byte[] newPin, UInt32 newPinLength
                                                   , ref Int32 unblockResult);
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 unblockUserWithTse(byte[] userId, UInt32 userIdLength
                                                   , byte[] puk, UInt32 pukLength
                                                   , byte[] newPin, UInt32 newPinLength
                                                   , ref Int32 unblockResult
                                                   , byte[] tseId, UInt32 tseIdLength);


        //////////////////////////////////////////////////////////////////////////////// 
        // updateTime
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 updateTime(long newTime);


        //////////////////////////////////////////////////////////////////////////////// 
        // startTransaction
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 startTransaction(byte[] clientId, UInt32 clientIdLength
                                                   , byte[] processData, UInt32 processDataLength
                                                   , byte[] processType, UInt32 processTypeLength
                                                   , byte[] additionalData, UInt32 additionalDataLength
                                                   , ref UInt32 transactionNumber
                                                   , ref Int64 tm
                                                   , ref IntPtr serialNumber, ref UInt32 serialNumberLength
                                                   , ref UInt32 signatureCounter
                                                   , ref IntPtr signatureValue, ref UInt32 signatureValueLength);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 startTransactionWithTse(byte[] clientId, UInt32 clientIdLength
                                                          , byte[] processData, UInt32 processDataLength
                                                          , byte[] processType, UInt32 processTypeLength
                                                          , byte[] additionalData, UInt32 additionalDataLength
                                                          , ref UInt32 transactionNumber
                                                          , ref Int64 tm
                                                          , ref IntPtr serialNumber, ref UInt32 serialNumberLength
                                                          , ref UInt32 signatureCounter
                                                          , ref IntPtr signatureValue, ref UInt32 signatureValueLength
                                                          , byte[] tseId, UInt32 tseIdLength);


        //////////////////////////////////////////////////////////////////////////////// 
        // updateTransaction
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 updateTransaction(byte[] clientId, UInt32 clientIdLength
                                                    , UInt32 transactionNumber
                                                    , byte[] processData, UInt32 processDataLength
                                                    , byte[] processType, UInt32 processTypeLength
                                                    , ref Int64 tm
                                                    , ref IntPtr signatureValue, ref UInt32 signatureValueLength
                                                    , ref UInt32 signatureCounter);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 updateTransactionWithTse(byte[] clientId, UInt32 clientIdLength
                                                           , UInt32 transactionNumber
                                                           , byte[] processData, UInt32 processDataLength
                                                           , byte[] processType, UInt32 processTypeLength
                                                           , ref Int64 tm
                                                           , ref IntPtr signatureValue, ref UInt32 signatureValueLength
                                                           , ref UInt32 signatureCounter
                                                           , byte[] tseId, UInt32 tseIdLength);



        //////////////////////////////////////////////////////////////////////////////// 
        // finishTransaction
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 finishTransaction(byte[] clientId, UInt32 clientIdLength
                                                    , UInt32 transactionNumber
                                                    , byte[] processData, UInt32 processDataLength
                                                    , byte[] processType, UInt32 processTypeLength
                                                    , byte[] additionalData, UInt32 additionalDataLength
                                                    , ref Int64 tm
                                                    , ref IntPtr signatureValue, ref UInt32 signatureValueLength
                                                    , ref UInt32 signatureCounter);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 finishTransactionWithTse(byte[] clientId, UInt32 clientIdLength
                                                           , UInt32 transactionNumber
                                                           , byte[] processData, UInt32 processDataLength
                                                           , byte[] processType, UInt32 processTypeLength
                                                           , byte[] additionalData, UInt32 additionalDataLength
                                                           , ref Int64 tm
                                                           , ref IntPtr signatureValue, ref UInt32 signatureValueLength
                                                           , ref UInt32 signatureCounter
                                                           , byte[] tseId, UInt32 tseIdLength);

        //////////////////////////////////////////////////////////////////////////////// 
        // exportDataFilteredByTransactionNumberAndClientId
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByTransactionNumberAndClientId(UInt32 transactionNumber,
                                                        byte[] clientId, UInt32 clientIdLength,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength);
        public static int exportDataFilteredByTransactionNumberAndClientId(uint transactionNumber, string clientId,
                                                         out byte[] exportedData)
        {
            exportedData = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            byte[] clientIdBytes = System.Text.Encoding.UTF8.GetBytes(clientId);
            int ret = SEApiImpl.exportDataFilteredByTransactionNumberAndClientId(transactionNumber, clientIdBytes, (uint)clientIdBytes.Length,
                ref ptr, ref ptrLength);
            if (ret == 0)
            {
                exportedData = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByTransactionNumberAndClientIdWithTse(UInt32 transactionNumber,
                                                        byte[] clientId, UInt32 clientIdLength,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength,
                                                        byte[] tseId, UInt32 tseIdLength);
        public static int exportDataFilteredByTransactionNumberAndClientIdWithTse(uint transactionNumber, string clientId,
                                                         out byte[] exportedData, string tseId)
        {
            exportedData = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            byte[] clientIdBytes = System.Text.Encoding.UTF8.GetBytes(clientId);
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseId);
            int ret = SEApiImpl.exportDataFilteredByTransactionNumberAndClientIdWithTse(transactionNumber, clientIdBytes, (uint)clientIdBytes.Length, 
                ref ptr, ref ptrLength, tseIdBytes, (uint)tseIdBytes.Length);
            if (ret == 0)
            {
                exportedData = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        //////////////////////////////////////////////////////////////////////////////// 
        // exportDataFilteredByTransactionNumber
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByTransactionNumber(UInt32 transactionNumber,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength);

        public static int exportDataFilteredByTransactionNumber(uint transactionNumber, out byte[] exportedData)
        {
            exportedData = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            int ret = SEApiImpl.exportDataFilteredByTransactionNumber(transactionNumber, ref ptr, ref ptrLength);
            if (ret == 0)
            {
                exportedData = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByTransactionNumberWithTse(UInt32 transactionNumber,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength,
                                                        byte[] tseId, UInt32 tseIdLength);

        public static int exportDataFilteredByTransactionNumberWithTse(uint transactionNumber,
                                                         out byte[] exportedData, string tseId)
        {
            exportedData = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseId);
            int ret = SEApiImpl.exportDataFilteredByTransactionNumberWithTse(transactionNumber, 
                ref ptr, ref ptrLength, tseIdBytes, (uint)tseIdBytes.Length);
            if (ret == 0)
            {
                exportedData = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        //////////////////////////////////////////////////////////////////////////////// 
        // exportDataFilteredByTransactionNumberInterval
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByTransactionNumberInterval(UInt32 startTransactionNumber,
                                                        UInt32 endTransactionNumber,
                                                        UInt32 maximumNumberRecords,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength);
        public static int exportDataFilteredByTransactionNumberInterval(uint startTransactionNumber,
                                                        uint endTransactionNumber,
                                                        uint maximumNumberRecords, out byte[] exportedData)
        {
            exportedData = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            int ret = SEApiImpl.exportDataFilteredByTransactionNumberInterval(startTransactionNumber, endTransactionNumber,
                maximumNumberRecords, ref ptr, ref ptrLength);
            if (ret == 0)
            {
                exportedData = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByTransactionNumberIntervalWithTse(UInt32 startTransactionNumber,
                                                        UInt32 endTransactionNumber,
                                                        UInt32 maximumNumberRecords,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength,
                                                        byte[] tseId, UInt32 tseIdLength);
        public static int exportDataFilteredByTransactionNumberIntervalWithTse(uint startTransactionNumber,
                                                        uint endTransactionNumber, 
                                                        uint maximumNumberRecords, out byte[] exportedData, string tseId)
        {
            exportedData = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseId);
            int ret = SEApiImpl.exportDataFilteredByTransactionNumberIntervalWithTse(startTransactionNumber, endTransactionNumber,
                maximumNumberRecords, ref ptr, ref ptrLength, tseIdBytes, (uint)tseIdBytes.Length);
            if (ret == 0)
            {
                exportedData = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        //////////////////////////////////////////////////////////////////////////////// 
        // exportDataFilteredByTransactionNumberIntervalAndClientId
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByTransactionNumberIntervalAndClientId(UInt32 startTransactionNumber,
                                                        UInt32 endTransactionNumber,
                                                        byte[] clientId, UInt32 clientIdLength,
                                                        UInt32 maximumNumberRecords,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength);
        public static int exportDataFilteredByTransactionNumberIntervalAndClientId(uint startTransactionNumber,
                                                        uint endTransactionNumber, string clientId,
                                                        uint maximumNumberRecords, out byte[] exportedData)
        {
            exportedData = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            byte[] clientIdBytes = System.Text.Encoding.UTF8.GetBytes(clientId);
            int ret = SEApiImpl.exportDataFilteredByTransactionNumberIntervalAndClientId(startTransactionNumber, endTransactionNumber,
                clientIdBytes, (uint)clientIdBytes.Length,
                maximumNumberRecords, ref ptr, ref ptrLength);
            if (ret == 0)
            {
                exportedData = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByTransactionNumberIntervalAndClientIdWithTse(UInt32 startTransactionNumber,
                                                        UInt32 endTransactionNumber,
                                                        byte[] clientId, UInt32 clientIdLength,
                                                        UInt32 maximumNumberRecords,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength,
                                                        byte[] tseId, UInt32 tseIdLength);
        public static int exportDataFilteredByTransactionNumberIntervalAndClientIdWithTse(uint startTransactionNumber,
                                                        uint endTransactionNumber, string clientId,
                                                        uint maximumNumberRecords, out byte[] exportedData, string tseId)
        {
            exportedData = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            byte[] clientIdBytes = System.Text.Encoding.UTF8.GetBytes(clientId);
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseId);
            int ret = SEApiImpl.exportDataFilteredByTransactionNumberIntervalAndClientIdWithTse(startTransactionNumber, endTransactionNumber,
                clientIdBytes, (uint)clientIdBytes.Length, 
                maximumNumberRecords, ref ptr, ref ptrLength, tseIdBytes, (uint)tseIdBytes.Length);
            if (ret == 0)
            {
                exportedData = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }


        //////////////////////////////////////////////////////////////////////////////// 
        // exportDataFilteredByPeriodOfTime
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByPeriodOfTime(Int64 startDate,
                                                        Int64 endDate,
                                                        UInt32 maximumNumberRecords,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength);
        public static int exportDataFilteredByPeriodOfTime(DateTime startDate, DateTime endDate,
            uint maximumNumberRecords, out byte[] exportedData)
        {
            exportedData = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            var startDateUnix = NativeConverter.DateTimeUtcToUnixTimeStamp(startDate);
            var endDateUnix = NativeConverter.DateTimeUtcToUnixTimeStamp(endDate);
            int ret = SEApiImpl.exportDataFilteredByPeriodOfTime(startDateUnix, endDateUnix,
                maximumNumberRecords, ref ptr, ref ptrLength);
            if (ret == 0)
            {
                exportedData = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByPeriodOfTimeWithTse(Int64 startDate,
                                                        Int64 endDate,
                                                        UInt32 maximumNumberRecords,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength,
                                                        byte[] tseId, UInt32 tseIdLength);
        public static int exportDataFilteredByPeriodOfTimeWithTse(DateTime startDate, DateTime endDate,
            uint maximumNumberRecords, out byte[] exportedData, string tseId)
        {
            exportedData = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            var startDateUnix = NativeConverter.DateTimeUtcToUnixTimeStamp(startDate);
            var endDateUnix = NativeConverter.DateTimeUtcToUnixTimeStamp(endDate);
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseId);
            int ret = SEApiImpl.exportDataFilteredByPeriodOfTimeWithTse(startDateUnix, endDateUnix,
                maximumNumberRecords, ref ptr, ref ptrLength, tseIdBytes, (uint)tseIdBytes.Length);
            if (ret == 0)
            {
                exportedData = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }



        //////////////////////////////////////////////////////////////////////////////// 
        // exportDataFilteredByPeriodOfTimeAndClientId
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByPeriodOfTimeAndClientId(Int64 startDate,
                                                        Int64 endDate,
                                                        byte[] clientId, UInt32 clientIdLength,
                                                        UInt32 maximumNumberRecords,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength);

        public static int exportDataFilteredByPeriodOfTimeAndClientId(DateTime startDate, DateTime endDate, string clientId,
            uint maximumNumberRecords, out byte[] exportedData)
        {
            exportedData = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            var startDateUnix = NativeConverter.DateTimeUtcToUnixTimeStamp(startDate);
            var endDateUnix = NativeConverter.DateTimeUtcToUnixTimeStamp(endDate);
            byte[] clientIdBytes = System.Text.Encoding.UTF8.GetBytes(clientId);
            int ret = SEApiImpl.exportDataFilteredByPeriodOfTimeAndClientId(startDateUnix, endDateUnix, clientIdBytes,
                (uint)clientIdBytes.Length, maximumNumberRecords, ref ptr, ref ptrLength);
            if (ret == 0)
            {
                exportedData = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataFilteredByPeriodOfTimeAndClientIdWithTse(Int64 startDate,
                                                        Int64 endDate,
                                                        byte[] clientId, UInt32 clientIdLength,
                                                        UInt32 maximumNumberRecords,
                                                        ref IntPtr exportedData, ref UInt32 exportedDataLength,
                                                        byte[] tseId, UInt32 tseIdLength);
        public static int exportDataFilteredByPeriodOfTimeAndClientIdWithTse(DateTime startDate, DateTime endDate, string clientId,
            uint maximumNumberRecords, out byte[] exportedData, string tseId)
        {
            exportedData = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            var startDateUnix = NativeConverter.DateTimeUtcToUnixTimeStamp(startDate);
            var endDateUnix = NativeConverter.DateTimeUtcToUnixTimeStamp(endDate);
            byte[] clientIdBytes = System.Text.Encoding.UTF8.GetBytes(clientId);
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseId);
            int ret = SEApiImpl.exportDataFilteredByPeriodOfTimeAndClientIdWithTse(startDateUnix, endDateUnix, clientIdBytes, (uint)clientIdBytes.Length,
                maximumNumberRecords, ref ptr, ref ptrLength, tseIdBytes, (uint)tseIdBytes.Length);
            if (ret == 0)
            {
                exportedData = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        //////////////////////////////////////////////////////////////////////////////// 
        // exportData
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportData(UInt32 maximumNumberRecords
                                             , ref IntPtr exportedData
                                             , ref UInt32 exportedDataLength);
        public static int exportData(uint maximumNumberRecords, out byte[] exportedData)
        {
            exportedData = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            int ret = SEApiImpl.exportData(maximumNumberRecords, ref ptr, ref ptrLength);
            if (ret == 0)
            {
                exportedData = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportDataWithTse(UInt32 maximumNumberRecords
                                             , ref IntPtr exportedData
                                             , ref UInt32 exportedDataLength
                                             , byte[] tseId, UInt32 tseIdLength);
        public static int exportDataWithTse(uint maximumNumberRecords, out byte[] exportedData, string tseId)
        {
            exportedData = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseId);
            int ret = SEApiImpl.exportDataWithTse(maximumNumberRecords, ref ptr, ref ptrLength, tseIdBytes, (uint)tseIdBytes.Length);
            if (ret == 0)
            {
                exportedData = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        //////////////////////////////////////////////////////////////////////////////// 
        // readLogMessage
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 readLogMessage(ref IntPtr exportedData, ref UInt32 exportedDataLength);
        public static int readLogMessage(out byte[] exportedData)
        {
            exportedData = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            int ret = SEApiImpl.readLogMessage(ref ptr, ref ptrLength);
            if (ret == 0)
            {
                exportedData = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 readLogMessageWithTse(ref IntPtr exportedData, ref UInt32 exportedDataLength, byte[] tseId, UInt32 tseIdLength);
        public static int readLogMessageWithTse(out byte[] exportedData, string tseId)
        {
            exportedData = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseId);
            int ret = SEApiImpl.readLogMessageWithTse(ref ptr, ref ptrLength, tseIdBytes, (uint)tseIdBytes.Length);
            if (ret == 0)
            {
                exportedData = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        //////////////////////////////////////////////////////////////////////////////// 
        // exportCertificates
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportCertificates(ref IntPtr certificates
                                                     , ref UInt32 certificatesLength);
        public static int exportCertificates(out byte[] certificates)
        {
            certificates = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            int ret = SEApiImpl.exportCertificates(ref ptr, ref ptrLength);
            if (ret == 0)
            {
                certificates = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportCertificatesWithTse(ref IntPtr certificates
                                                            , ref UInt32 certificatesLength
                                                            , byte[] tseId, UInt32 tseIdLength);
        public static int exportCertificatesWithTse(out byte[] certificates, string tseId)
        {
            certificates = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseId);
            int ret = SEApiImpl.exportCertificatesWithTse(ref ptr, ref ptrLength, tseIdBytes, (uint)tseIdBytes.Length);
            if (ret == 0)
            {
                certificates = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }



        //////////////////////////////////////////////////////////////////////////////// 
        // exportSerialNumbers
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportSerialNumbers(ref IntPtr serialNumbers
                                                      , ref UInt32 serialNumbersLength);
        public static int exportSerialNumbers(out byte[] serialNumbers)
        {
            serialNumbers = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            int ret = SEApiImpl.exportSerialNumbers(ref ptr, ref ptrLength);
            if (ret == 0)
            {
                serialNumbers = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 exportSerialNumbersWithTse(ref IntPtr serialNumbers
                                                             , ref UInt32 serialNumbersLength
                                                             , byte[] tseId, UInt32 tseIdLength);

        public static int exportSerialNumbersWithTse(out byte[] serialNumbers, string tseId)
        {
            serialNumbers = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseId);
            int ret = SEApiImpl.exportSerialNumbersWithTse(ref ptr, ref ptrLength, tseIdBytes, (uint)tseIdBytes.Length);
            if (ret == 0)
            {
                serialNumbers = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        //////////////////////////////////////////////////////////////////////////////// 
        // getMaxNumberOfClients
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 getMaxNumberOfClients(ref UInt32 maxNumberClients);
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 getMaxNumberOfClientsWithTse(ref UInt32 maxNumberClients
                                                               , byte[] tseId, UInt32 tseIdLength);
        //////////////////////////////////////////////////////////////////////////////// 
        // getCurrentNumberOfClients
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 getCurrentNumberOfClients(ref UInt32 currentNumberClients);
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 getCurrentNumberOfClientsWithTse(ref UInt32 currentNumberClients
                                                                   , byte[] tseId, UInt32 tseIdLength);

        //////////////////////////////////////////////////////////////////////////////// 
        // getMaxNumberOfTransactions
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 getMaxNumberOfTransactions(ref UInt32 maxNumberTransactions);
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 getMaxNumberOfTransactionsWithTse(ref UInt32 maxNumberTransactions
                                                                    , byte[] tseId, UInt32 tseIdLength);
        //////////////////////////////////////////////////////////////////////////////// 
        // getCurrentNumberOfTransactions
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 getCurrentNumberOfTransactions(ref UInt32 currentNumberTransactions);
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 getCurrentNumberOfTransactionsWithTse(ref UInt32 currentNumberTransactions
                                                                        , byte[] tseId, UInt32 tseIdLength);

        //////////////////////////////////////////////////////////////////////////////// 
        // getSupportedTransactionUpdateVariants
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 getSupportedTransactionUpdateVariants(ref UpdateVariants supportedUpdateVariants);
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 getSupportedTransactionUpdateVariantsWithTse(ref UpdateVariants supportedUpdateVariants
                                                                               , byte[] tseId, UInt32 tseIdLength);



        //////////////////////////////////////////////////////////////////////////////// 
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getPublicKey(ref IntPtr pub_key, ref UInt32 pub_key_len);
        public static int at_getPublicKey(out byte[] pubKeyBytes)
        {
            pubKeyBytes = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            int ret = SEApiImpl.at_getPublicKey(ref ptr, ref ptrLength);
            if (ret == 0)
            {
                pubKeyBytes = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }


        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getPublicKeyWithTse(ref IntPtr pub_key, ref UInt32 pub_key_len, byte[] tseId, UInt32 tseIdLength);
        public static int at_getPublicKeyWithTse(out byte[] pubKeyBytes, string tseID)
        {
            pubKeyBytes = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseID);
            int ret = SEApiImpl.at_getPublicKeyWithTse(ref ptr, ref ptrLength, tseIdBytes, (uint)tseIdBytes.Length);
            if (ret == 0)
            {
                pubKeyBytes = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }
            return ret;
        }


        //////////////////////////////////////////////////////////////////////////////// 
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getSignatureAlgorithm(ref IntPtr signature_algorithm, ref UInt32 signature_algorithm_length);
        public static int at_getSignatureAlgorithm(out string sigAlg)
        {
            sigAlg = "";
            byte[] sigAlgBytes = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            int ret = SEApiImpl.at_getSignatureAlgorithm(ref ptr, ref ptrLength);
            if (ret == 0)
            {
                sigAlgBytes = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                sigAlg = Encoding.Default.GetString(sigAlgBytes);
                at_free(ref ptr);
            }

            return ret;
        }

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getSignatureAlgorithmWithTse(ref IntPtr signature_algorithm, ref UInt32 signature_algorithm_length, byte[] tseId, UInt32 tseIdLength);
        public static int at_getSignatureAlgorithmWithTse(out string sigAlg, string tseId)
        {
            sigAlg = "";
            byte[] sigAlgBytes = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseId);
            int ret = SEApiImpl.at_getSignatureAlgorithmWithTse(ref ptr, ref ptrLength, tseIdBytes, (uint)tseIdBytes.Length);
            if (ret == 0)
            {
                sigAlgBytes = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                sigAlg = Encoding.Default.GetString(sigAlgBytes);
                at_free(ref ptr);
            }

            return ret;
        }

        //////////////////////////////////////////////////////////////////////////////// 
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getLogTimeFormat(ref IntPtr log_time_format, ref UInt32 log_time_format_length);
        public static int at_getLogTimeFormat(out string logTimeFormat)
        {
            logTimeFormat = "";
            byte[] bytes = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            int ret = SEApiImpl.at_getSignatureAlgorithm(ref ptr, ref ptrLength);
            if (ret == 0)
            {
                bytes = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                logTimeFormat = Encoding.Default.GetString(bytes);
                at_free(ref ptr);
            }

            return ret;
        }

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getLogTimeFormatWithTse(ref IntPtr log_time_format, ref UInt32 log_time_format_length, byte[] tseId, UInt32 tseIdLength);
        public static int at_getLogTimeFormatWithTse(out string logTimeFormat, string tseId)
        {
            logTimeFormat = "";
            byte[] bytes = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseId);
            int ret = SEApiImpl.at_getSignatureAlgorithmWithTse(ref ptr, ref ptrLength, tseIdBytes, (uint)tseIdBytes.Length);
            if (ret == 0)
            {
                bytes = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                logTimeFormat = Encoding.Default.GetString(bytes);
                at_free(ref ptr);
            }

            return ret;
        }

        //////////////////////////////////////////////////////////////////////////////// 
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getSerialNumber(ref IntPtr serial, ref UInt32 serial_len);
        public static int at_getSerialNumber(out byte[] serial)
        {
            serial = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            int ret = SEApiImpl.at_getSerialNumber(ref ptr, ref ptrLength);
            if (ret == 0)
            {
                serial = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getSerialNumberWithTse(ref IntPtr serial, ref UInt32 serial_len, byte[] tseId, UInt32 tseIdLength);
        public static int at_getSerialNumberWithTse(out byte[] serial, string tseId)
        {
            serial = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseId);
            int ret = SEApiImpl.at_getSerialNumberWithTse(ref ptr, ref ptrLength, tseIdBytes, (uint)tseIdBytes.Length);
            if (ret == 0)
            {
                serial = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        //////////////////////////////////////////////////////////////////////////////// 
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getSignatureCounter(ref UInt32 counter);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getSignatureCounterWithTse(ref UInt32 counter, byte[] tseId, UInt32 tseIdLength);

        //////////////////////////////////////////////////////////////////////////////// 
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getTransactionCounter(ref UInt32 counter);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getTransactionCounterWithTse(ref UInt32 counter, byte[] tseId, UInt32 tseIdLength);

        //////////////////////////////////////////////////////////////////////////////// 
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getLifecycleState(ref UInt32 state);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getLifecycleStateWithTse(ref UInt32 state, byte[] tseId, UInt32 tseIdLength);

        //////////////////////////////////////////////////////////////////////////////// 
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_unsuspendSecureElement();

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_unsuspendSecureElementWithTse(byte[] tseId, UInt32 tseIdLength);
        public static int at_unsuspendSecureElementWithTse(string tseId)
        {
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseId);
            return SEApiImpl.at_unsuspendSecureElementWithTse(tseIdBytes, (uint)tseIdBytes.Length);
        }

        //////////////////////////////////////////////////////////////////////////////// 
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_suspendSecureElement();

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_suspendSecureElementWithTse(byte[] tseId, UInt32 tseIdLength);
        public static int at_suspendSecureElementWithTse(string tseId)
        {
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseId);
            return SEApiImpl.at_suspendSecureElementWithTse(tseIdBytes, (uint)tseIdBytes.Length);
        }

        //////////////////////////////////////////////////////////////////////////////// 
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getCertificate(ref IntPtr cert, ref UInt32 certLength);
        public static int at_getCertificate(out byte[] cert)
        {
            cert = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            int ret = SEApiImpl.at_getCertificate(ref ptr, ref ptrLength);
            if (ret == 0)
            {
                cert = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getCertificateWithTse(ref IntPtr cert, ref UInt32 certLength, byte[] tseId, UInt32 tseIdLength);
        public static int at_getCertificateWithTse(out byte[] cert, string tseId)
        {
            cert = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseId);
            int ret = SEApiImpl.at_getCertificateWithTse(ref ptr, ref ptrLength, tseIdBytes, (uint)tseIdBytes.Length);
            if (ret == 0)
            {
                cert = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        //////////////////////////////////////////////////////////////////////////////// 
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getOpenTransactions(ref IntPtr transaction_numbers, ref UInt32 transaction_numbers_length);
        public static int at_getOpenTransactions(out int[] transactions)
        {
            transactions = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            int ret = SEApiImpl.at_getOpenTransactions(ref ptr, ref ptrLength);
            if (ret == 0)
            {
                transactions = NativeConverter.CharStarStarToInt32Array(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getOpenTransactionsWithTse(ref IntPtr transaction_numbers, ref UInt32 transaction_numbers_length, byte[] tseId, UInt32 tseIdLength);
        public static int at_getOpenTransactionsWithTse(out int[] transactions, string tseId)
        {
            transactions = null;
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            byte[] tseIdBytes = System.Text.Encoding.UTF8.GetBytes(tseId);
            int ret = SEApiImpl.at_getOpenTransactionsWithTse(ref ptr, ref ptrLength, tseIdBytes, (uint)tseIdBytes.Length);
            if (ret == 0)
            {
                transactions = NativeConverter.CharStarStarToInt32Array(ref ptr, ptrLength);
                at_free(ref ptr);
            }

            return ret;
        }

        //////////////////////////////////////////////////////////////////////////////// 
        // asigntse_free
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern void at_free(ref IntPtr ptr);
        // DEPRECATED!
        public static void asigntse_free(ref IntPtr ptr) { at_free(ref ptr); }

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_load();

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_unload();

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 at_getVersion(ref IntPtr version, ref UInt32 versionLength);
        public static int at_getVersion(out string version)
        {
            IntPtr ptr = IntPtr.Zero;
            UInt32 ptrLength = 0;
            version = "";
            var ret = SEApiImpl.at_getVersion(ref ptr, ref ptrLength);
            if (ret == 0)
            {
                byte[] buf = NativeConverter.CharStarStarToByteArray(ref ptr, ptrLength);
                var version_string = Encoding.Default.GetString(buf);
                SEApiImpl.at_free(ref ptr);
            }
            return ret;
        }

        ////////////////////////////////////////////////////////////////////////////////
        // CFG commands
        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetConfigFile(byte[] filename, UInt32 filenameLength);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgTseAdd(   byte[] vtseID, UInt32 vtseIDLength,
                                                UInt32 tssType,
                                                byte[] connParam, UInt32 connParamLength,
                                                byte[] atrustVtssID, UInt32 atrustVtssIDLength,
                                                byte[] atrustApiKey, UInt32 atrustApiKeyLength,
                                                byte[] timeAdminID, UInt32 timeAdminIDLength,
                                                byte[] timeAdminPwd, UInt32 timeAdminPwdLength);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgTseRemove(byte[] vtseID, UInt32 vtseIDLength);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLoggingEnabled(bool enabled);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLoggingStderr(bool enabled);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLoggingFile(bool enabled);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLogDir(byte[] path, UInt32 pathLength);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLogLevel(byte[] logLevel, UInt32 logLevelLength);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLogAppend(bool enabled);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLogColors(bool enabled);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLogDetails(bool enabled);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetLogStderrColors(bool enabled);

        [DllImport(@"asigntse.dll", CallingConvention = CallingConvention.Cdecl)]
        public static extern Int32 cfgSetHttpProxy(byte[] proxyUrl, UInt32 proxyUrlLength);

    }
}
