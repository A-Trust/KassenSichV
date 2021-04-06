using System;
using System.Text;
using System.IO;
using System.Runtime.InteropServices;

namespace Example
{
    class ExampleCode
    {       
        const string USER_ID = "admin";
        const string CLIENT_ID = "CSharpExample";
        const string PROCESS_DATA = "Beleg^75.33_7.99_0.00_0.00_0.00^10.00:Bar_5.00:Bar:CHF_5.00:Bar:USD_64.30:Unbar";
        const string PROCESS_TYPE = "Kassenbeleg";

        const int EXECUTION_OK = 0;
        const int EXIT_FAILURE = -1;
        
        const int AUTH_OK = 0;


        public static byte[] CharStarStarToByteArray(ref IntPtr in_ptr, ulong in_length)
        {
            byte[] arrayRes = new byte[in_length];
            if (in_length > 0)
            {
                Marshal.Copy(in_ptr, arrayRes, 0, (int)in_length);
            }
            return arrayRes;
        }

        public static int InitializeTse(string pin, string puk)
        {
            int result = 0;

            byte[] pinBytes = Encoding.UTF8.GetBytes(pin);
            byte[] pukBytes = Encoding.UTF8.GetBytes(puk);

            if ((result = CDeclBindings.at_setPins(
            pinBytes, (UInt32)(pinBytes.Length),
            pukBytes, (UInt32)(pukBytes.Length))) != EXECUTION_OK)
            {
                Console.WriteLine($"at_setPins failed: {result}");

                return result;
            }

            byte[] userIdBytes = Encoding.UTF8.GetBytes(USER_ID);
            Int32 authResult = 0;
            Int16 remainingRetries = 0;
            if ((result = CDeclBindings.authenticateUser(
                userIdBytes, (UInt32)(userIdBytes.Length),
                pinBytes, (UInt32)(pinBytes.Length),
                ref authResult, ref remainingRetries)) != EXECUTION_OK)
            {
                Console.WriteLine($"authenticateUser failed: {result} " +
                    $"\tauthResult: {authResult} \tremainingRetries{remainingRetries}");

                return result;
            }

            if (authResult != AUTH_OK)
            {
                Console.WriteLine($"authResult not ok: {authResult}");

                return result;
            }

            if ((result = CDeclBindings.initializeDescriptionSet()) != EXECUTION_OK)
            {
                Console.WriteLine($"initializeDescriptionSet failed: {result}");

                return result;
            }
            
            if ((result = CDeclBindings.logOut(
                userIdBytes, (UInt32)(userIdBytes.Length))) != EXECUTION_OK)
            {
                Console.WriteLine($"logOut failed: {result}");

                return result;
            }

            Console.WriteLine("TSE initialization successful.\n");

            return EXECUTION_OK;
        }

        public static int RegisterClientId(string pin)
        {
            int result = 0;

            byte[] pinBytes = Encoding.UTF8.GetBytes(pin);

            byte[] userIdBytes = Encoding.UTF8.GetBytes(USER_ID);
            Int32 authResult = 0;
            Int16 remainingRetries = 0;
            if ((result = CDeclBindings.authenticateUser(
                userIdBytes, (UInt32)(userIdBytes.Length),
                pinBytes, (UInt32)(pinBytes.Length),
                ref authResult, ref remainingRetries)) != EXECUTION_OK)
            {
                Console.WriteLine($"authenticateUser failed: {result} " +
                    $"\tauthResult: {authResult} \tremainingRetries{remainingRetries}");

                return result;
            }

            if (authResult != AUTH_OK)
            {
                Console.WriteLine($"authResult not ok: {authResult}");

                return result;
            }
            
            // Client id registration
            byte[] clientIdBytes = Encoding.UTF8.GetBytes(CLIENT_ID);
            if ((result = CDeclBindings.at_registerClientId(clientIdBytes, 
                (UInt32)(clientIdBytes.Length))) != EXECUTION_OK)
            {
                Console.WriteLine($"at_registerClientId failed: {result}\n");

                return result;
            }

            if ((result = CDeclBindings.logOut(
                userIdBytes, (UInt32)(userIdBytes.Length))) != EXECUTION_OK)
            {
                Console.WriteLine($"logOut failed: {result}");

                return result;
            }

            return EXECUTION_OK;
        }


        static void Main(string[] args)
        {
            int result = 0;

            var pin = "geheimpin";
            var puk = "geheimpuk";

            // uncomment for logging
            /*
            if ((result = CDeclBindings.cfgSetLoggingEnabled(true)) != EXECUTION_OK)
            {
                Console.WriteLine($"cfgSetLoggingEnabled failed: {result}\n");
            }
            */

            if ((result = CDeclBindings.at_load()) != EXECUTION_OK)
            {
                Console.WriteLine($"at_load failed: {result}\n");
            }

            IntPtr version = IntPtr.Zero;
            UInt32 versionLen = 0;
            if ((result = CDeclBindings.at_getVersion(ref version, ref versionLen)) != EXECUTION_OK) {
                Console.WriteLine($"at_getVersion failed: {result}\n");
            } else
            {
                byte[] versionByteArray = CharStarStarToByteArray(ref version, versionLen);
                string versionStr = Encoding.Default.GetString(versionByteArray);
                Console.WriteLine($"a.sign TSE v{versionStr}\n");
            }

            UInt32 state = 0;
            if ((result = CDeclBindings.at_getLifecycleState(ref state)) != EXECUTION_OK)
            {
                Console.WriteLine($"at_getLifecycleState failed: {result}\n");
            }
            string stateStr = CDeclBindings.LifeCycle[state];


            /* TSE initialization if necessary */
            if (stateStr == "NotInitialized")
            {
                if (InitializeTse(pin, puk) != EXECUTION_OK)
                {
                    Console.WriteLine("TSE initialization failed.\n\n");

                    goto AtUnload;
                }
            }

            /* Client id registration */
            if (RegisterClientId(pin) != EXECUTION_OK)
            {
                Console.WriteLine("Client id registration failed.\n\n");
            }

            /* Make transaction */
            byte[] clientIdBytes = Encoding.UTF8.GetBytes(CLIENT_ID);
            byte[] processDataBytes = System.Text.Encoding.UTF8.GetBytes(PROCESS_DATA);
            byte[] processTypeBytes = System.Text.Encoding.UTF8.GetBytes(PROCESS_TYPE);
            IntPtr serialNumber = IntPtr.Zero;
            UInt32 serialNumberLen = 0;
            UInt32 signatureCounter = 0;
            UInt32 transactionNumber = 0;
            UInt32 signatureValueLen = 0;
            IntPtr signatureValue = IntPtr.Zero;
            Int64 logTime = 0;

            if ((result = CDeclBindings.startTransaction(
                clientIdBytes, (UInt32)(clientIdBytes.Length),
                null, 0,
                null, 0,
                null, 0,
                ref transactionNumber,
                ref logTime,
                ref serialNumber, ref serialNumberLen,
                ref signatureCounter,
                ref signatureValue, ref signatureValueLen)) != EXECUTION_OK)
            {
                Console.WriteLine($"startTransaction failed: {result}\n");

                goto AtUnload;
            }
            else
            {
                DateTimeOffset LogTimeStr = DateTimeOffset.FromUnixTimeSeconds(logTime);

                Console.WriteLine($"startTransaction\n " +
                    $" logTime: {LogTimeStr.ToString("o")}\n " +
                    $" transactionNumber: {transactionNumber}\n " +
                    $" signatureCounter: {signatureCounter}\n");

                CDeclBindings.at_free(ref serialNumber);
                CDeclBindings.at_free(ref signatureValue);
            }

            if ((result = CDeclBindings.finishTransaction(
                clientIdBytes, (UInt32)(clientIdBytes.Length),
                transactionNumber,
                processDataBytes, (UInt32)(processDataBytes.Length),
                processTypeBytes, (UInt32)(processTypeBytes.Length),
                null, 0,
                ref logTime,
                ref signatureValue, ref signatureValueLen,
                ref signatureCounter)) != EXECUTION_OK)
            {
                Console.WriteLine($"finishTransaction failed: {result}\n");
            } else
            {
                DateTimeOffset LogTimeStr = DateTimeOffset.FromUnixTimeSeconds(logTime);

                Console.WriteLine($"finishTransaction\n " +
                    $" logTime: {LogTimeStr.ToString("o")}\n " +
                    $" transactionNumber: {transactionNumber}\n  " +
                    $"signatureCounter: {signatureCounter}\n");

                CDeclBindings.at_free(ref signatureValue);
            }

            /* Export data */
            UInt32 maximumNumberRecords = 0;
            IntPtr data = IntPtr.Zero;
            UInt32 dataLen = 0;
            if ((result = CDeclBindings.exportData(
                maximumNumberRecords, ref data, ref dataLen)) != EXECUTION_OK)
            {
                Console.WriteLine($"exportData failed: {result}\n");
            }
            else
            {
                byte[] bytes = CharStarStarToByteArray(ref data, dataLen);
                File.WriteAllBytes("export_data.tar", bytes);
            }

            AtUnload:
            if ((result = CDeclBindings.at_unload()) != EXECUTION_OK)
            {
                Console.WriteLine($"at_unload failed: {result}\n");

                Environment.Exit(EXIT_FAILURE);
            }
        }
    }
}
