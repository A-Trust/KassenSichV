using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

////////////////////////////////////////////////////////////////////////////////
//
// DEMO - Just a proof of concept
//
////////////////////////////////////////////////////////////////////////////////

namespace LibAsignTse
{
    public class SEApiImpl : NativeInterface
    {

        ////////////////////////////////////////////////////////////////////////////////
        // convenience functions
        public static Int32 StartTransaction(string clientId
                                             , string processData
                                             , string processType
                                             , string additionalData
                                             , out UInt32 transactionNumber
                                             , out DateTime tm
                                             , out byte[] serialNumber
                                             , out UInt32 signatureCounter
                                             , out byte[] signatureValue)
        {
            transactionNumber = 0;
            signatureCounter = 0;

            byte[] _clientId = System.Text.Encoding.UTF8.GetBytes(clientId);
            byte[] _processData = System.Text.Encoding.UTF8.GetBytes(processData);
            byte[] _processType = System.Text.Encoding.UTF8.GetBytes(processType);
            byte[] _additionalData = System.Text.Encoding.UTF8.GetBytes(additionalData);
            IntPtr _serialNumber = IntPtr.Zero;
            UInt32 _serialNumberLength = 0;
            IntPtr _signatureValue = IntPtr.Zero;
            UInt32 _signatureValueLength = 0;
            //SEApiImpl.cpp_tm _tm = new SEApiImpl.cpp_tm();

            Int64 unixtime = 0;

            Int32 ret = SEApiImpl.startTransaction(_clientId, (UInt32)_clientId.Length,
                                       _processData, (UInt32)_processData.Length,
                                       _processType, (UInt32)_processType.Length,
                                       _additionalData, (UInt32)_additionalData.Length,
                                       ref transactionNumber,
                                       ref unixtime,
                                       ref _serialNumber, ref _serialNumberLength,
                                       ref signatureCounter, ref _signatureValue, ref _signatureValueLength);
            tm = DateTimeOffset.FromUnixTimeSeconds((long)unixtime).UtcDateTime;  //Util.cpp_tm2DateTime(_tm);
            serialNumber = NativeConverter.CharStarStarToByteArray(ref _serialNumber, _serialNumberLength);
            signatureValue = NativeConverter.CharStarStarToByteArray(ref _signatureValue, _signatureValueLength);
            return ret;
        }

        public static Int32 UpdateTransaction(string clientId
                                                      , UInt32 transactionNumber
                                                      , string processData
                                                      , string processType
                                                      , out DateTime tm
                                                      , out byte[] signatureValue
                                                      , out UInt32 signatureCounter)
        {
            signatureCounter = 0;
            byte[] _clientId = System.Text.Encoding.UTF8.GetBytes(clientId);
            byte[] _processData = System.Text.Encoding.UTF8.GetBytes(processData);
            byte[] _processType = System.Text.Encoding.UTF8.GetBytes(processType);
            Int64 unix_tm = 0;
            IntPtr _signatureValue = IntPtr.Zero;
            UInt32 _signatureValueLength = 0;

            Int32 ret = updateTransaction(_clientId, (UInt32)_clientId.Length,
                                        transactionNumber,
                                        _processData, (UInt32)_processData.Length,
                                        _processType, (UInt32)_processType.Length,
                                        ref unix_tm,
                                        ref _signatureValue, ref _signatureValueLength,
                                        ref signatureCounter);
            tm = NativeConverter.UnixTimeStampToDateTime(unix_tm);
            signatureValue = NativeConverter.CharStarStarToByteArray(ref _signatureValue, _signatureValueLength);

            return ret;
        }
        public static Int32 FinishTransaction(string clientId
                                             , UInt32 transactionNumber
                                             , string processData
                                             , string processType
                                             , string additionalData
                                             , out DateTime tm
                                             , out byte[] signatureValue
                                             , out UInt32 signatureCounter)
        {
            signatureCounter = 0;
            byte[] _clientId = System.Text.Encoding.UTF8.GetBytes(clientId);
            byte[] _processData = System.Text.Encoding.UTF8.GetBytes(processData);
            byte[] _processType = System.Text.Encoding.UTF8.GetBytes(processType);
            byte[] _additionalData = System.Text.Encoding.UTF8.GetBytes(additionalData);
            //SEApiImpl.cpp_tm _tm = new SEApiImpl.cpp_tm();
            IntPtr _signatureValue = IntPtr.Zero;
            UInt32 _signatureValueLength = 0;

            Int64 unixtime = 0;
            Int32 ret = SEApiImpl.finishTransaction(_clientId, (UInt32)_clientId.Length,
                                    transactionNumber,
                                    _processData, (UInt32)_processData.Length,
                                    _processType, (UInt32)_processType.Length,
                                    _additionalData, (UInt32)_additionalData.Length,
                                    ref unixtime,
                                    ref _signatureValue, ref _signatureValueLength,
                                    ref signatureCounter);

            tm = DateTimeOffset.FromUnixTimeSeconds((long)unixtime).UtcDateTime;//Util.cpp_tm2DateTime(_tm);
            signatureValue = NativeConverter.CharStarStarToByteArray(ref _signatureValue, _signatureValueLength);

            return ret;
        }

        public static Int32 ExportData(UInt32 maximumNumberRecords, out byte[] exportedData)
        {
            IntPtr _exportedData = IntPtr.Zero;
            UInt32 _exportedDataLength = 0;
            Int32 ret = SEApiImpl.exportData(maximumNumberRecords, ref _exportedData, ref _exportedDataLength);
            exportedData = NativeConverter.CharStarStarToByteArray(ref _exportedData, _exportedDataLength);
            return ret;
        }

        public static Int32 setConfigFile(string configFile)
        {
            byte[] _configFile = Encoding.UTF8.GetBytes(configFile);
            return cfgSetConfigFile(_configFile, (uint)_configFile.Length);
        }
    }
}
