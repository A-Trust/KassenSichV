using System;
using System.IO;
using System.Linq;
using System.Text;
using System.Drawing;
using LibAsignTse;

using ZXing;
using ZXing.QrCode;
using ZXing.QrCode.Internal;

////////////////////////////////////////////////////////////////////////////////
//
// DEMO - Just a proof of concept
//
////////////////////////////////////////////////////////////////////////////////

namespace AsignTseExampleClient
{

    class ExampleClient
    {
        static void Main(string[] args)
        {
            var ret = SEApiImpl.cfgSetLoggingEnabled(true);
            ret = SEApiImpl.cfgSetLoggingStderr(false);
            byte[] dir = Encoding.UTF8.GetBytes(".");
            ret = SEApiImpl.cfgSetLogDir(dir, (UInt32)dir.Length);
            ret = SEApiImpl.cfgSetLoggingFile(true);

            ret = SEApiImpl.at_load();

            if (ret != 0) { Console.WriteLine("Error initialising library: {0}", ret); }
            if (args.Length > 0)
            {
                string cmd = args[0].ToLower();
                if (new[] { "?", "-?", "/?", "-h" }.Contains(cmd))
                { help(); }
                else if (cmd == "export") { do_export(); }
                else if (cmd == "open") { do_open(); }
                else if (cmd == "cert") { do_export_cert("neu2"); }
                else if (cmd == "pubkey") { do_export_pubkey(); }
                else if (cmd == "serial") { do_export_serial(); }
                else if (cmd == "params") { do_params(); }
                else if (cmd == "receipt")
                {
                    UInt32 transactionNumber = 0;
                    do_receipt(out transactionNumber);
                }
                else if (cmd == "genqrcode") { do_qrcode(args); }
                else
                {
                    Console.WriteLine("Unknown Argument: {0}", cmd);
                    help();
                }
            }
            else
            {
                do_export();
                UInt32 transactionNumber = 0;
                do_receipt(out transactionNumber);
            }
            SEApiImpl.at_unload();
        }
        private static void help()
        {
            var ret = SEApiImpl.at_getVersion(out string version_string);

            Console.WriteLine("using asign TSE v{0}", version_string);
            Console.WriteLine("\nArguments:");
            Console.WriteLine("  ?       ... help");
            Console.WriteLine("  export  ... test all data export functions");
            Console.WriteLine("  cert    ... test certificate export functions");
            Console.WriteLine("  pubkey  ... test public key export functions");
            Console.WriteLine("  serial  ... test serial export functions");
            Console.WriteLine("  receipt ... create a receipt");
            Console.WriteLine("  params  ... print Parameters");
            Console.WriteLine("  open    ... get open (dangling) transactions");
            Console.WriteLine("  genqrcode  ... generate QR-Code");
        }
        
        private static void do_params()
        {
            UInt32 maxNumberClients = 0;
            UInt32 currentNumberClients = 0;
            UInt32 maxNumberTransactions = 0;
            UInt32 currentNumberTransactions = 0;
            SEApiImpl.UpdateVariants uv=new SEApiImpl.UpdateVariants();

            SEApiImpl.getMaxNumberOfClients(ref maxNumberClients);
            SEApiImpl.getCurrentNumberOfClients(ref currentNumberClients);
            SEApiImpl.getMaxNumberOfTransactions(ref maxNumberTransactions);
            SEApiImpl.getCurrentNumberOfTransactions(ref currentNumberTransactions);
            SEApiImpl.getSupportedTransactionUpdateVariants(ref uv);
            
            Console.WriteLine("maxNumberClients: {0}", maxNumberClients);
            Console.WriteLine("currentNumberClients: {0}", currentNumberClients);
            Console.WriteLine("maxNumberTransactions: {0}", maxNumberTransactions);
            Console.WriteLine("currentNumberTransactions: {0}", currentNumberTransactions);
            Console.Write("getSupportedTransactionUpdateVariants: ");
            switch (uv) {
                case SEApiImpl.UpdateVariants.signedAndUnsignedUpdate:
                    Console.WriteLine("signedAndUnsignedUpdate");
                    break;
                case SEApiImpl.UpdateVariants.signedUpdate:
                    Console.WriteLine("signedUpdate");
                    break;
                case SEApiImpl.UpdateVariants.unsignedUpdate:
                    Console.WriteLine("unsignedUpdate");
                    break;
            }

            int ret = SEApiImpl.at_getSignatureAlgorithm(out string sigAlg);
            Console.WriteLine("Algorithms: {0}", sigAlg);

            ret = SEApiImpl.at_getLogTimeFormat(out string logTimeFormat);
            Console.WriteLine("LogTimeFormat: {0}", logTimeFormat);

            ret = SEApiImpl.at_getSerialNumber(out byte[] serial);
            Console.WriteLine("TSE Serialnumber: {0}",Convert.ToBase64String(serial));

            UInt32 signature_counter=0;
            ret = SEApiImpl.at_getSignatureCounter(ref signature_counter);
            Console.WriteLine("Signature Counter: {0}",signature_counter);

            UInt32 transaction_counter=0;
            ret = SEApiImpl.at_getTransactionCounter(ref transaction_counter);
            Console.WriteLine("Transaction Counter: {0}",transaction_counter);

            UInt32 lifecycle=0;
            ret = SEApiImpl.at_getLifecycleState(ref lifecycle);
            Console.WriteLine("Lifecycle state: {0} ({1})", lifecycle, SEApiImpl.LifeCycle[lifecycle]);
        }
        private static void do_export_serial()
        {
            string filename = "serials.bin";
            int ret = SEApiImpl.exportSerialNumbers(out byte[] serialNumbers);
            Console.WriteLine("exportSerialNumbers returns {0} ", ret);
            if (ret != 0) return;
            File.WriteAllBytes(filename, serialNumbers);
            Console.WriteLine("file {0} written: {1} bytes.", filename, serialNumbers.Length);
        }
        private static void do_export_cert(String tseID="")
        {
            string filename = "certs.tar";
            byte[] certificates = null;
            int ret;
            if (tseID == "")
            {
                ret = SEApiImpl.exportCertificates(out certificates);
            }
            else
            {
                ret = SEApiImpl.exportCertificatesWithTse(out certificates, tseID);
            }
            Console.WriteLine("exportCertificates returns {0} ", ret);
            if (ret != 0) return;

            File.WriteAllBytes(filename, certificates);
            Console.WriteLine("file {0} written: {1} bytes.", filename, certificates.Length);

            ret = SEApiImpl.at_getCertificate(out byte[] cert);
            Console.WriteLine("at_getCertificate returns {0} ", ret);
            if (ret != 0) return;
            filename = "cert_x509.cer";
            File.WriteAllBytes(filename, cert);
            Console.WriteLine("file {0} written: {1} bytes.", filename, cert.Length);
        }
        private static void export_all()
        {
            string filename = "test_export_all.tar";
            uint maximumNumberRecords = 0;
            int ret = SEApiImpl.exportData(maximumNumberRecords, out byte[] exportedData);
            Console.WriteLine("\nExportData returns: {0}", ret);
            if (ret != 0) return;
            File.WriteAllBytes(filename, exportedData);
            Console.WriteLine("file {0} written: {1} bytes.", filename, exportedData.Length);
        }
        private static void export_last_minute()
        {
            string filename = "test_export_last_minute.tar";
            uint maximumNumberRecords = 0;
            DateTime startDate = DateTime.UtcNow.AddMinutes(-1);
            DateTime endDate = DateTime.UtcNow;
            Console.WriteLine("export last min - start: {0}", startDate.ToString());
            Console.WriteLine("export last min - end  : {0}", endDate.ToString());

            Int64 unix_start = NativeConverter.DateTimeUtcToUnixTimeStamp(startDate);
            Int64 unix_end = NativeConverter.DateTimeUtcToUnixTimeStamp(endDate);
            Console.WriteLine("export last min - start: {0}", unix_start);
            Console.WriteLine("export last min - end  : {0}", unix_end);

            var ret = SEApiImpl.exportDataFilteredByPeriodOfTime(startDate, endDate, maximumNumberRecords, out byte[] exportedData);
            Console.WriteLine("Return:{0}", ret);
            if (ret != 0) return;
            File.WriteAllBytes(filename, exportedData);
            Console.WriteLine("file {0} written: {1} bytes.", filename, exportedData.Length);
        }
        private static void export_single_tn()
        {
            uint transactionNumber = 0;
            do_receipt(out transactionNumber);
            string filename = String.Format("test_export_{0}.tar", transactionNumber);
            var ret = SEApiImpl.exportDataFilteredByTransactionNumber(transactionNumber, out byte[] exportedData);
            Console.WriteLine("Return:{0}", ret);
            if (ret != 0) return;
            File.WriteAllBytes(filename, exportedData);
            Console.WriteLine("file {0} written: {1} bytes.", filename, exportedData.Length);
        }
        private static void do_export_pubkey() {
            var filename = "pubKey.bin";
            int ret = SEApiImpl.at_getPublicKey(out byte[] pubKey);
            Console.WriteLine("export PublicKey returns {0} ", ret);
            if (ret != 0) return;

            File.WriteAllBytes(filename, pubKey);
            Console.WriteLine("file {0} written: {1} bytes.", filename, pubKey.Length);
        }
        private static void do_export()
        {
            export_all();
            export_last_minute();
            export_single_tn();
        }

        private static void do_open()
        { 
            int ret = SEApiImpl.at_getOpenTransactions(out int[] openTN);
            Console.WriteLine("Open Transactions returned: {0} ", ret);
            if (ret != 0) return;

            foreach (var entry in openTN) { Console.WriteLine(entry); }
        
        }

        private static void do_receipt(out uint transactionNumber)
        {
            string processTypeString = "Kassenbeleg-V1";
            string clientIdString = "clientId";

            get_receipt(out transactionNumber, processTypeString, clientIdString,
            out uint signatureCounter, out string signatureValue, out string public_keyB64, out DateTime starttime, out DateTime finishtime);

        }

        #region generateQrCode
        private static void do_qrcode(string[] args)
        {
            string filename = "QR_Code.png";

            if (args.Length < 4)
            {
                Console.WriteLine("Check Parameter: \n\t AsignTseExampleClient.exe do_qrcode clientId processType processData filename(optional)");
                return;
            }
            if (args.Length == 5)
            {
                filename = args[4];
            }

            string clientId = args[1];
            string processType = args[2];
            string processData = args[3];

            //---> StartTransaction && FinsihTransaction 
            byte[] bclientId = System.Text.Encoding.UTF8.GetBytes(clientId);
            byte[] bprocessData = System.Text.Encoding.UTF8.GetBytes(processData);
            byte[] bprocessType = System.Text.Encoding.UTF8.GetBytes(processType);

            IntPtr serialNumber = IntPtr.Zero;
            UInt32 serialNumberLength = 0;
            UInt32 signatureCounter = 0;
            UInt32 transactionNumber = 0;
            IntPtr ptr_signatureValue = IntPtr.Zero;
            UInt32 signatureValueLength = 0;
            Int64 time_int64_starttransaction = 0;

            int ret = SEApiImpl.startTransaction(bclientId, (UInt32)clientId.Length,
                                       bprocessData, (UInt32)processData.Length,
                                       bprocessType, (UInt32)processType.Length,
                                       null, 0,
                                       ref transactionNumber,
                                       ref time_int64_starttransaction,
                                       ref serialNumber, ref serialNumberLength,
                                       ref signatureCounter,
                                       ref ptr_signatureValue, ref signatureValueLength);

            IntPtr ptr_signatureValue_finish = IntPtr.Zero;
            UInt32 signatureValueLength_finish = 0;
            UInt32 signatureCounter_finish = 0;

            Int64 time_int64_finishtransaction = 0;

            ret = SEApiImpl.finishTransaction(bclientId, (UInt32)clientId.Length,
                                    transactionNumber,
                                    bprocessData, (UInt32)processData.Length,
                                    bprocessType, (UInt32)processType.Length,
                                    null, 0,
                                    ref time_int64_finishtransaction,
                                    ref ptr_signatureValue_finish, ref signatureValueLength_finish,
                                    ref signatureCounter_finish);

            DateTime epoch = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);
            DateTime start_time = epoch.AddSeconds(time_int64_starttransaction);
            DateTime finish_time = epoch.AddSeconds(time_int64_finishtransaction);

            string qr_signaturevalue = NativeConverter.CharStarStarToB64String(ref ptr_signatureValue_finish, signatureValueLength_finish);

            string logtimeformat = "";
            if (ret == 0)
            {
                IntPtr tf = IntPtr.Zero;
                UInt32 tfLength = 0;
                ret = SEApiImpl.at_getLogTimeFormat(ref tf, ref tfLength);
                byte[] f = NativeConverter.CharStarStarToByteArray(ref tf, tfLength);
                logtimeformat = Encoding.Default.GetString(f);
                SEApiImpl.at_free(ref tf);
            }

            string publickey = "";
            if (ret == 0)
            {
                IntPtr pubKey = IntPtr.Zero;
                UInt32 pubKeyLength = 0;
                ret = SEApiImpl.at_getPublicKey(ref pubKey, ref pubKeyLength);
                publickey = NativeConverter.CharStarStarToB64String(ref pubKey, pubKeyLength);
                SEApiImpl.asigntse_free(ref pubKey);
            }

            string signaturealgorithm = "";
            if (ret == 0)
            {
                IntPtr algs = IntPtr.Zero;
                UInt32 algsLength = 0;
                ret = SEApiImpl.at_getSignatureAlgorithm(ref algs, ref algsLength);
                byte[] f = NativeConverter.CharStarStarToByteArray(ref algs, algsLength);
                signaturealgorithm = Encoding.Default.GetString(f);
                SEApiImpl.at_free(ref algs);
            }

            string str_qrcode = "V0;" + clientId + "," + processType + "," + processData + "," + transactionNumber + "," +
                signatureCounter + "," + start_time.ToString("yyyy-MM-ddThh:mm:ss.fffZ") + "," + finish_time.ToString("yyyy-MM-ddThh:mm:ss.fffZ") + "," +
                signaturealgorithm + ";" + logtimeformat + ";" + qr_signaturevalue + ";" + publickey;

            SEApiImpl.asigntse_free(ref ptr_signatureValue);
            SEApiImpl.asigntse_free(ref serialNumber);
            SEApiImpl.asigntse_free(ref ptr_signatureValue_finish);

            if (GenerateQrCode(str_qrcode, 500, filename) == null)
            {
                Console.WriteLine("Generate QR-Code -> error");
                return;
            }
            Console.WriteLine("Finish generate QR-Code -> QR saved as " + filename);
        }


        private static string get_receipt(out uint transactionNumber, string processTypeString, string clientIdString, 
            out uint signatureCounter, out string signatureValue, out string public_keyB64, out DateTime starttime, out DateTime finishtime)
        {
            Receipt receipt = new Receipt(7533, 799, 0, 0, 0);
            receipt.addPayment(1000, true);
            receipt.addPayment(500, true, "CHF");
            receipt.addPayment(500, true, "USD");
            receipt.addPayment(6430, false);
            var processData = receipt.getFormatedReceipt();
            Console.WriteLine($"Created receipt: {processData}");

            makeTransaction(clientIdString, processTypeString, processData, 
                out transactionNumber, out signatureCounter, out signatureValue, out public_keyB64, out starttime, out finishtime);

            return processData;
        }

        private static void makeTransaction(string clientIdString, string processTypeString, string processDataString, out uint transactionNumber,
           out uint signatureCounter, out string signatureValue_string, out string public_key, out DateTime starttime, out DateTime finishtime)
        {
            byte[] clientId = System.Text.Encoding.UTF8.GetBytes(clientIdString);
            byte[] processData = System.Text.Encoding.UTF8.GetBytes(processDataString);
            byte[] processType = System.Text.Encoding.UTF8.GetBytes(processTypeString);
            byte[] additionalData = System.Text.Encoding.UTF8.GetBytes("");

            IntPtr serialNumber = IntPtr.Zero;
            UInt32 serialNumberLength = 0;
            transactionNumber = 0;
            UInt32 signatureValueLength = 0;
            IntPtr signatureValue = IntPtr.Zero;
            signatureValue_string = "";
            Int64 tm = 0;
            DateTime epoch = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);

            signatureCounter = 0;
            signatureValue_string = "";
            public_key = "";
            starttime = DateTime.MaxValue;
            finishtime = DateTime.MinValue;

            int ret = SEApiImpl.startTransaction(clientId, (UInt32)clientId.Length,
                                       processData, (UInt32)processData.Length,
                                       processType, (UInt32)processType.Length,
                                       null, 0,
                                       ref transactionNumber,
                                       ref tm,
                                       ref serialNumber, ref serialNumberLength,
                                       ref signatureCounter, ref signatureValue, ref signatureValueLength);


            starttime = epoch.AddSeconds(tm);

            Console.WriteLine("----------------------------------------------------------------------------------------------------------------------------");
            Console.WriteLine("startTransaction returns: {0}", ret);
            Console.WriteLine("transactionNumber: {0}", transactionNumber);
            Console.WriteLine("signatureCounter: {0}", signatureCounter);
            Console.WriteLine("\nsignatureValueLength: {0}", signatureValueLength);
            Console.WriteLine("\nsignatureValue: {0}", NativeConverter.CharStarStarToB64String(ref signatureValue, signatureValueLength));

            public_key = NativeConverter.CharStarStarToB64String(ref serialNumber, serialNumberLength);


            SEApiImpl.asigntse_free(ref signatureValue);
            SEApiImpl.asigntse_free(ref serialNumber);

            additionalData = new byte[] { };

            ret = SEApiImpl.finishTransaction(clientId, (UInt32)clientId.Length,
                                    transactionNumber,
                                    processData, (UInt32)processData.Length,
                                    processType, (UInt32)processType.Length,
                                    additionalData, (UInt32)additionalData.Length,
                                    ref tm,
                                    ref signatureValue, ref signatureValueLength,
                                    ref signatureCounter);

            finishtime = epoch.AddSeconds(tm);

            Console.WriteLine("finishTransaction returns: {0}", ret);
            Console.WriteLine("signatureCounter: {0}", signatureCounter);
            Console.WriteLine("\nsignatureValue: {0}", NativeConverter.CharStarStarToB64String(ref signatureValue, signatureValueLength));
            Console.WriteLine("----------------------------------------------------------------------------------------------------------------------------");

            signatureValue_string = NativeConverter.CharStarStarToB64String(ref signatureValue, signatureValueLength);
            SEApiImpl.asigntse_free(ref signatureValue);
        }

        private static byte[] GenerateQrCode(string qrCodeContent, int size, string filename)
        {
            try
            {
                QrCodeEncodingOptions options = new QrCodeEncodingOptions();
                options.Height = size;
                options.Width = size;
                options.Margin = 2;
                options.ErrorCorrection = ErrorCorrectionLevel.H;

                BarcodeWriter writer = new BarcodeWriter();
                writer.Format = BarcodeFormat.QR_CODE;
                writer.Options = options;
                Bitmap bmp = writer.Write(qrCodeContent);

                MemoryStream ms = new MemoryStream();

                bmp.Save(filename);
                bmp.Save(ms, System.Drawing.Imaging.ImageFormat.Png);
                byte[] data = ms.ToArray();
                ms.Close();
                return data;
            }
            catch (Exception)
            { }
            return null;
        }
        #endregion
    }
}
