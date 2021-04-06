using System;
using System.IO;
using System.Text;
using System.Drawing;
using System.Windows.Forms;

using ZXing;
using ZXing.QrCode;
using ZXing.QrCode.Internal;

using LibAsignTse;

namespace AsignTseExampleGUI
{
    public partial class TseGUI : Form
    {
        private Receipt receipt = null;

        public TseGUI()
        {
            InitializeComponent();
            comboBox_procType.SelectedIndex = 0;
        }

        private void do_qrcode()
        {
            try
            {
                if (CheckEntry() == false)
                {
                    ClearForm();
                    return;
                }

                string filename = "QR_Code.png";
                string clientId = textBox_clientID.Text;
                string processType = comboBox_procType.Items[comboBox_procType.SelectedIndex].ToString();
                string processData = textBox_processdata.Text;
                               
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

                string str_qrcode = "V0;" + clientId + ";" + processType + ";" + processData + ";" + transactionNumber + ";" +
                    signatureCounter + ";" + start_time.ToString("yyyy-MM-ddThh:mm:ss.fffZ") + ";" + finish_time.ToString("yyyy-MM-ddThh:mm:ss.fffZ") + ";" +
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
            catch(Exception)
            {
                ClearForm();               
            }     
        }          
                
        private byte[] GenerateQrCode(string qrCodeContent, int size, string filename)
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
                pictureBox.Image = bmp;

                MemoryStream ms = new MemoryStream();       
                bmp.Save(ms, System.Drawing.Imaging.ImageFormat.Png);
                byte[] data = ms.ToArray();
                ms.Close();
                return data;
            }
            catch (Exception)
            { }
            return null;
        }
        
        private void button_genQR_Click(object sender, EventArgs e)
        {
            if(textBox_processdata.Text.Length == 0)
            {
                DialogResult dialogResult = MessageBox.Show(this, "Automatisch generieren?", "Es muss zuerst ein Beleg erstellt werden", MessageBoxButtons.YesNo, MessageBoxIcon.Information);
                if (dialogResult == DialogResult.Yes)
                {
                    autoGenerateReceipt();
                }
                else if (dialogResult == DialogResult.No)
                {
                    return;
                }
            }

            ClearForm();
            do_qrcode();            
        }

        private void autoGenerateReceipt()
        {
            receipt = new Receipt(7533, 799, 0, 0, 0);
            receipt.addPayment(1000, true);
            receipt.addPayment(500, true, "CHF");
            receipt.addPayment(500, true, "USD");
            receipt.addPayment(6430, false);

            string string_receipt = receipt.getFormatedReceipt();
            textBox_processdata.Text = string_receipt;
        }

        private void ClearForm()
        {
            pictureBox.Image = null;           
        }    
    
        private bool CheckEntry()
        {
            if(textBox_clientID.Text.Length == 0)
            {
                MessageBox.Show("Error: clientId empty!");
                return false;
            }
            if (textBox_processdata.Text.Length == 0)
            {
                MessageBox.Show("Error: receipt empty!");
                return false;
            }
            return true;
        }

        private void button_genQR_Click_1(object sender, EventArgs e)
        {
            if(textBox_processdata.Text.Length == 0)
            {
                DialogResult dialogResult = MessageBox.Show("ProcessData autogenerate example?", "Information", MessageBoxButtons.YesNo);
                if (dialogResult == DialogResult.Yes)
                {                    
                    autoGenerateReceipt();
                }
                else if (dialogResult == DialogResult.No)
                {
                    return;
                }                
            }
            ClearForm();
            do_qrcode();
        }
    }
}
