package at.atrust.kassensichv.example;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import at.atrust.asigntse.seapi.DSFinVKUtil;
import at.atrust.asigntse.seapi.DSFinVKUtil.ProcessType;
import at.atrust.asigntse.seapi.SEAPI;
import at.atrust.asigntse.seapi.SEAPIImpl;
import at.atrust.asigntse.seapi.TseLifecycleState;
import at.atrust.asigntse.seapi.TseStatusCode;
import at.atrust.asigntse.seapi.results.AuthenticationResult;
import at.atrust.asigntse.seapi.results.ByteArrayResult;
import at.atrust.asigntse.seapi.results.StartTransactionResult;
import at.atrust.asigntse.seapi.results.StringResult;
import at.atrust.asigntse.seapi.results.TransactionResult;
import at.atrust.kassensichv.example.util.Order;
import at.atrust.kassensichv.example.util.Receipt;

public class CommandLineClient {
	
	private SEAPI seapi = new SEAPIImpl();
	
	private static final String CLIENT_ID = "clientId";
	private static final String PIN = "123456";
	private X509Certificate certificate = null;
	private static final File certificateFile = new File("cert.cer");
	private static final String TSE_ID  = "TSE_ID";
	
	public static void main(String[] args) {

		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		if(args.length == 0) {
			System.out.println("No arguments found. It is required to pass the path of libasigntse.dll as argument, when it is not in classpath ");
			System.exit(-1);
		} else {
			System.setProperty("jna.library.path", args[0]);
		}
		
		CommandLineClient tc = new CommandLineClient();
		tc.init();
		tc.printStammDaten();
		tc.doTest();
		tc.testReceipt();
		tc.testOrder();
		tc.testExport();
	}

	public void init() {

		int status = seapi.at_load();
		if (status != 0) {
			System.err.println("at_load returns error " + status);
			System.exit(-1);
		}

		long lcs = seapi.at_getLifecycleState();
		if (lcs < 0) {
			System.err.println("at_getLifecycleState returns error " + lcs);
		} else if (lcs == TseLifecycleState.NOT_INITIALIZED) {
			initTse();
			initCertificate();
		} else if (lcs == TseLifecycleState.ACTIVE) {
			initCertificate();
		} else {
			System.err.println("Bad state ");
		}
	}

	private void initTse() {

		System.out.println("initialize TSE");

		AuthenticationResult ar = seapi.authenticateUser("admin", PIN.getBytes());
		if (ar.getStatus() != 0) {
			System.err.println("authenticateUser returns error " + ar.getStatus());
			System.exit(-1);
		}

		int status;
		status = seapi.initialize();
		if (status != 0) {
			System.err.println("initialize returns error " + status);
			System.exit(-1);
		}

		status = seapi.at_registerClientId(CLIENT_ID);
		if (status != 0) {
			System.err.println("at_registerClientId returns error " + status);
			System.exit(-1);
		}

		seapi.logOut("admin");
		if (status != 0) {
			System.err.println("logOut returns error " + status);
			System.exit(-1);
		}
	}

	private void initCertificate() {
		if (certificateFile.exists()) {
			try {
				byte[] fileContent = Files.readAllBytes(certificateFile.toPath());
				CertificateFactory certFactory = CertificateFactory.getInstance("X.509", "BC");
				certificate = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(fileContent));
			} catch (IOException | CertificateException | NoSuchProviderException e) {
				e.printStackTrace();
			}
		} else {
			ByteArrayResult res = seapi.at_getCertificate();
			if (res.getStatus() == 0) {
				try {
					CertificateFactory certFactory = CertificateFactory.getInstance("X.509", "BC");
					certificate = (X509Certificate) certFactory
							.generateCertificate(new ByteArrayInputStream(res.getData()));
					Files.write(certificateFile.toPath(), certificate.getEncoded());
				} catch (IOException | CertificateException | NoSuchProviderException e) {
					e.printStackTrace();
				}
			} else {
				System.err.println("Error loading certificate");
			}
		}
	}
	
	private void doTest() {
		ByteArrayResult res = seapi.exportSerialNumbers();
		if(res.getStatus() == 0) {
			writeExportDataResultToFile(res.getData(), "serials.txt");
		}
	}
	
	private void printStammDaten() {
		StringResult resSig = seapi.at_getSignatureAlgorithm();
		StringResult resTime = seapi.at_getLogTimeFormat();
		StringResult serialResult = seapi.at_getSerialNumber();
		
		if(resSig.getStatus() == TseStatusCode.OK && resTime.getStatus() == TseStatusCode.OK && serialResult.getStatus() == TseStatusCode.OK) {
			String serial = serialResult.getData();
			String stammDaten = DSFinVKUtil.getStammTSE("Z_KASSE_ID", "Z_ERSTELLUNG", "Z_NR", TSE_ID, resSig.getData(), resTime.getData(), certificate, serial);
			System.out.println(stammDaten);
		} else {
			System.out.println("Can't load signature algorithm");
		}
		ByteArrayResult res = seapi.exportSerialNumbers();
		if(res.getStatus() == 0) {
			byte[] serials = res.getData();
			writeExportDataResultToFile(serials, "serial.tar");
		}
	}
	
	/**
	 * Tests a "Kassenbeleg" transaction 
	 */
	private void testReceipt() {
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("*** Receipt example ***");
		
		System.out.println("Creating receipt");
		Receipt receipt = new Receipt(7533, 799, 0, 0, 0);
		receipt.addPayment(1000, true);
		receipt.addPayment(500, true, "CHF");
		receipt.addPayment(500, true, "USD");
		receipt.addPayment(6430, false);
		System.out.println("Created receipt:");
		String processData = receipt.getFormatedReceipt(); 
		System.out.println(processData);
		
		System.out.println("Starting receipt signature");
		String receiptResponse = makeTransaction(TSE_ID, CLIENT_ID,  ProcessType.RECEIPT, processData);
		System.out.println("Received response:");
		System.out.println(receiptResponse);

		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
	}
	
	/**
	 * Tests a "Bestellung" transaction 
	 */
	private void testOrder() {
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("*** Order example ***");
		
		System.out.println("Creating order");
		Order order = new Order();
		order.addItem(2, "Eisbecher \"Himbeere\"", 399);
		order.addItem(1, "Eiskaffee", 299);
		System.out.println("Created order:");
		String processData = order.getFormatedOrder();
		System.out.println(processData);
		
		System.out.println("Starting order signature");
		String orderResponse = makeTransaction(TSE_ID, CLIENT_ID, ProcessType.ORDER, processData);
		System.out.println("Received response:");
		System.out.println(orderResponse);
		
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
	}
	

	/**
	 * Tests a data export and writes it to a file
	 */
	private void testExport() {
		String filename = "testExport.tar";
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("*** Export example ***");
		System.out.println("Exporting data");
		byte[] exportBytes= export();
		if(exportBytes != null && exportBytes.length > 0) {
			System.out.println("Received data with size " + exportBytes.length);
			System.out.println("Writing to file");
			writeExportDataResultToFile(exportBytes, filename);
			System.out.println("Wrote to file " + filename);
		} else {
			System.out.println("Error exporting data");
		}
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
	}
	
	private String makeTransaction(String tseId, String clientId, String processType, String processData) {

		String toReturn = null;
		StartTransactionResult startResult = seapi.startTransaction(clientId);
		if(startResult.getStatus() == TseStatusCode.OK) {
			TransactionResult finishResult = seapi.finishTransaction(clientId, startResult.getTransactionNumber(), processData.getBytes(), processType);
			if(finishResult.getStatus() == TseStatusCode.OK) {
				toReturn = DSFinVKUtil.buildTransactionString(tseId, processType, startResult, finishResult);
			}
		}
		return toReturn;
	}
	
	private byte[] export() {
		byte[] toReturn = null;
		ByteArrayResult er = seapi.exportData(0);
		if(er.getStatus() == TseStatusCode.OK) {
			toReturn = er.getData();
		} 
		return toReturn;
	}

	private static void writeExportDataResultToFile(byte[] data, String filename) {
		try (FileOutputStream fos = new FileOutputStream(filename)) {
			fos.write(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}