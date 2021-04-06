package at.atrust.asigntse.seapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import at.atrust.asigntse.seapi.results.StartTransactionResult;
import at.atrust.asigntse.seapi.results.TransactionResult;

public class DSFinVKUtil {

	public class ProcessType {
		public static final String RECEIPT = "Kassenbeleg-V1";
		public static final String ORDER = "Bestellung-V1";
		public static final String OTHER = "SonstigerVorgang";
	}
	
	// XXX what is the separator for a transaction line? it is assumed that it is ',' as it is a .csv file
	private static final String SEPARATOR = ",";
	
	public static String buildTransactionString(String TSE_ID, String processType, StartTransactionResult start, TransactionResult end) {
		return buildTransactionString(TSE_ID, start.getTransactionNumber(), start.getLogTime(), end.getLogTime(), processType, end.getSignatureCounter(), end.getSignatureValue(), "","");
	}
	
	
	/**
	 * Creates a csv line as required for transactions_tse.csv as described in DSFinV-K 2.0 on page 96
	 * 
	 * @param TSE_ID Die ID der für eine Transaktion verwendeten TSE (vgl. Stamm_TSE.csv)
	 * @param TSE_TANR Die Transaktionsnummer der Transaktion
	 * @param TSE_TA_START Die TSE-Log-Time der StartTransaction-Operation. Der Zeitpunkt der Absicherung des Transaktionsstarts (Log time) wird von der TSE bei Signierung zurückgeliefert und ist nach den Vorgaben nach ISO 8601 im Format YYYY-MM-DDThh:mm:ss.fffZ darzu-stellen.
	 * @param TSE_TA_ENDE Die TSE-Log-Time der FinishTransaction-Operation. Der Zeitpunkt der Absicherung des Transaktionsendes (Log time) wird von der TSE bei Signierung zurückgeliefert und ist nach den Vorgaben nach ISO 8601 im Format YYYY-MM-DDThh:mm:ss.fffZ darzu-stellen.
	 * @param TSE_TA_VORGANGSART Der processType der FinishTransaction-Operation (s. Anhang I)
	 * @param TSE_TA_SIGZ Der Signaturzähler der FinishTransaction-Operation
	 * @param TSE_TA_SIG Die Signatur der FinishTransaction-Operation in base64-Codierung
	 * @param TSE_FEHLER Freies Feld für Erläuterungen von Problemen in der Kommunikation zwischen Aufzeichnungssystem und TSE
	 * @param TSE_TA_VORGANGSDATEN Optional können hier die Daten des Vorgangs (processData), die an die TSE übergeben wurden, dargestellt werden. Dies erleichtert die Fehlersuche für die Hersteller und die Finanzverwaltung. Vorgehende und/oder nachfolgende Leerzeichen sind nicht zulässig.
	 * @return 
	 */
	public static String buildTransactionString(String TSE_ID, long TSE_TANR, ZonedDateTime TSE_TA_START, ZonedDateTime TSE_TA_ENDE, String TSE_TA_VORGANGSART, Long TSE_TA_SIGZ, byte[] TSE_TA_SIG, String TSE_FEHLER, String TSE_TA_VORGANGSDATEN) {
		
		String toReturn = "";
		toReturn += TSE_ID;
		toReturn += SEPARATOR;
		toReturn += TSE_TANR;
		toReturn += SEPARATOR;
		toReturn += formatTime(TSE_TA_START);
		toReturn += SEPARATOR;
		toReturn += formatTime(TSE_TA_ENDE);
		toReturn += SEPARATOR;
		toReturn += TSE_TA_VORGANGSART;
		toReturn += SEPARATOR;
		toReturn += TSE_TA_SIGZ;
		toReturn += SEPARATOR;
		toReturn += Base64.getEncoder().encodeToString(TSE_TA_SIG);
		toReturn += SEPARATOR;
		toReturn += TSE_FEHLER;
		toReturn += SEPARATOR;
		toReturn += TSE_TA_VORGANGSDATEN;
		return toReturn;
	}
	
	
	/**
	 * Create a line as required for stamm_tse.csv as described in DSFinV-K 2.0 on page 70
	 * 
	 * @param Z_KASSE_ID ID der (Abschluss-) Kasse
	 * @param Z_ERSTELLUNG Zeitpunkt des Kassenabschlusses
	 * @param Z_NR Nr. des Kassenabschlusses
	 * @param TSE_ID ID der TSE - wird nur zur Referenzierung innerhalb eines Kassenabschlusses verwendet
	 * @param TSE_SIG_ALGO Der von der TSE verwendete Signaturalgorithmus
	 * @param TSE_ZEITFORMAT Das von der TSE verwendete Format für die Log-Time der Absicherung
	 * @param certificate Das initial von der TSE abgefragte Zertifikat
	 * @return  
	 */
	public static String getStammTSE(String Z_KASSE_ID,String  Z_ERSTELLUNG, String  Z_NR, String TSE_ID, String TSE_SIG_ALGO, String TSE_ZEITFORMAT, X509Certificate certificate, String tseSerial) {

		if(certificate == null) {
			return null;
		}
		String b64Cert;
		try {
			b64Cert = Base64.getEncoder().encodeToString(certificate.getEncoded());
		} catch (CertificateEncodingException e) {
			return null;
		}

		// Text-Encoding der ProcessData (UTF-8 oder ASCII)
		String TSE_PD_ENCODING = "UTF-8";
		
		// Öffentlicher Schlüssel ggf. extrahiert aus dem Zertifikat der TSE in base64-Codierung
		String TSE_PUBLIC_KEY = Base64.getEncoder().encodeToString(certificate.getPublicKey().getEncoded()); 
		
		String TSE_ZERTIFIKAT_I = ""; // Erste 1.000 Zeichen des Zertifikats der TSE (in base64-Codierung)
		String TSE_ZERTIFIKAT_II= ""; // Ggf. Rest des Zertifikats (in base64-Codierung)
		if(b64Cert.length() > 1000) {
			TSE_ZERTIFIKAT_I = b64Cert.substring(0, 1000);
			TSE_ZERTIFIKAT_II = b64Cert.substring(1000, b64Cert.length());
		} else {
			TSE_ZERTIFIKAT_I = b64Cert.substring(0, b64Cert.length());
			TSE_ZERTIFIKAT_II = "";
		}
		
		// concatenate all values
		String toReturn = "";
		toReturn += Z_KASSE_ID + SEPARATOR;
		toReturn += Z_ERSTELLUNG + SEPARATOR;
		toReturn += Z_NR + SEPARATOR;
		toReturn += TSE_ID + SEPARATOR;
		toReturn += tseSerial + SEPARATOR;
		toReturn += TSE_SIG_ALGO + SEPARATOR;
		toReturn += TSE_ZEITFORMAT + SEPARATOR;
		toReturn += TSE_PD_ENCODING + SEPARATOR;
		toReturn += TSE_PUBLIC_KEY + SEPARATOR;
		toReturn += TSE_ZERTIFIKAT_I + SEPARATOR;
		toReturn += TSE_ZERTIFIKAT_II;
		return toReturn;
	}
	
	/**
	<kassen-seriennummer> Seriennummer (Client-Id) der Kasse<br/>
	<processType> processType (siehe oben)<br/>
	<processData> processData (siehe oben)<br/>
	<transaktions-nummer> Transaktionsnummer der TSE<br/>
	<signatur-zaehler> Signaturzähler der finishTransaction-Operation der TSE<br/>
	<start-zeit> Log-Time der startTransaction-Operation der TSE im Format YYYY-MM-DDThh:mm:ss.fffZ<br/>
	<log-time> Log-Time der finishTransaction-Operation der TSE im Format YYYY-MM-DDThh:mm:ss.fffZ<br/>
	<sig-alg> Signaturalgorithmus<br/>
	<log-time-format> Log-Time-Format<br/>
	<signatur> Prüfwert / Signatur der finishTransaction-Operation der TSE<br/>
	<public-key>Öffentlicher Schlüssel (base64 codiert)<br/>
 */
	public static String generateQrcodeData(String cashRegisterSerial, String processType, String processData,
			String transactionNumber, String signatureCounter, String startTime, String logTime,
			String logTimeFormat, String signature, String publicKey) {
		String SEP = ";";
		String qrData = "";
		qrData += "V0" + SEP; // <qr-code-version> Versionsnummer des QR-Codes, ist aktuell immer: V0
		qrData += cashRegisterSerial + SEP;
		qrData += processType + SEP;
		qrData += processData + SEP;
		qrData += transactionNumber + SEP;
		qrData += signatureCounter + SEP;
		qrData += startTime + SEP;
		qrData += logTime + SEP;
		qrData += "ecdsa-plain-SHA256" + SEP;
		qrData += logTimeFormat + SEP;
		qrData += signature + SEP;
		qrData += publicKey;
		return qrData;
	}
	

	/**
	 * @param qrData The contents to encode in the QR code
	 * @param width The preferred width in pixels
	 * @param height The preferred height in pixels
	 * @return the QR code as byte array
	 * @throws WriterException
	 * @throws IOException
	 */
	public static byte[] getQRCodeImage(String qrData, int width, int height) throws WriterException, IOException {

		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, width, height);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bos);
		byte[] pngData = bos.toByteArray();
		return pngData;
	}
	
	
	/**
	 * Formats the time as required by DSFinV-K 2.0 on page 96/97 for start and finish transaction time.
	 * <br/><br/>
	 * Die TSE-Log-Time der Start-/FinishTransaction-Operation. Der Zeitpunkt der
	 * Absicherung des Transaktionsstarts (Log time) wird von der TSE bei Signierung
	 * zurückgeliefert und ist nach den Vorgaben nach ISO 8601 im Format
	 * YYYY-MM-DDThh:mm:ss.fffZ darzustellen.
	 * 
	 * @param zonedDateTime the time to format
	 * @return formated time
	 */
	public static String formatTime(ZonedDateTime zonedDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.000");
		String formattedString = zonedDateTime.format(formatter);
		formattedString = formattedString.replace(" ", "T");
		formattedString += "Z";
		return formattedString;
	}

}
