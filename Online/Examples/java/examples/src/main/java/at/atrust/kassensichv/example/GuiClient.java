package at.atrust.kassensichv.example;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import com.google.zxing.WriterException;

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
import at.atrust.kassensichv.example.util.CashRegisterFormater;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GuiClient extends Application {
	
	public static final String CLIENT_ID = "clientId"; 
	private String logTimeFormat = "";

	TextArea outputArea;
	TextField tax1;
	TextField tax2;
	TextField tax3;
	TextField tax4;
	TextField tax5;
	ImageView imageView;
	
	String processData;
	long transactionNumber;
	ZonedDateTime startTime;
	String qrCodeData;

	
	private SEAPI seapi = new SEAPIImpl();
	private X509Certificate certificate = null;
	private static final File certificateFile = new File("cert.cer");
	private static final String PIN = "123456";
	
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
			System.err.println("Bad state " + lcs);
			System.exit(-1);
		}
		
    	StringResult sr = seapi.at_getLogTimeFormat();
    	if(sr.getStatus() == TseStatusCode.OK) {
    		logTimeFormat = sr.getData();
    	} else {
    		System.err.println("Error loading time format");
    		System.exit(-1);
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

	
	public void start(Stage primaryStage) {
		init();
		GridPane grid = generateGrid();
		// grid.setGridLinesVisible(true);
		Scene scene = new Scene(grid, 900, 800);
		primaryStage.setScene(scene);
		primaryStage.setTitle("A-Trust Beispielkasse");
		primaryStage.show();
	}

	private GridPane generateGrid() {
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(2);
		grid.setVgap(2);
		grid.setPadding(new Insets(10, 10, 10, 10));

		addTitle(grid, "Beispielkasse: " + CLIENT_ID , 0, 0);
		tax1 = addTaxInput(grid, "Allgemeiner Steuersatz (19%):", 0, 1);
		tax2 = addTaxInput(grid, "Ermäßigter Steuersatz (7%):", 0, 2);
		tax3 = addTaxInput(grid, "Durchschnittsatz (10.7%):", 0, 3);
		tax4 = addTaxInput(grid, "Durchschnittsatz (5.5%):", 3, 1);
		tax5 = addTaxInput(grid, "0%:", 3, 2);
		
		Label outputLabel = new Label("Output:");
		grid.add(outputLabel, 0, 4);
		
		String text = "";
		outputArea = new TextArea();
		outputArea.setWrapText(true);
		outputArea.setText(text);
		grid.add(outputArea, 0, 5, 6, 15);
		
		addSpacerLabel(grid, 5);
		addButton(grid, "Show public key", 6, 6, getShowPublicKeyHandler());
		addSpacerLabel(grid, 7);
		addButton(grid, "Start Transaction", 6, 8, getStartTransactionHandler());
		addSpacerLabel(grid, 9);
		addButton(grid, "Finish Transaction", 6, 10, getFinishTransactionHandler());
		addSpacerLabel(grid, 11);
		addButton(grid, "Generate QR", 6, 12, getGenerateQRCodeHandler());
		addSpacerLabel(grid, 13);
		addButton(grid, "Reset", 6, 14, getClearHandler());
		addSpacerLabel(grid, 15);
		
	    addPaddedImageView(grid);
		return grid;
	}

	private void addPaddedImageView(GridPane grid) {
	    imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.setX(0);
        imageView.setY(0);
        imageView.setFitWidth(250);
        imageView.setPreserveRatio(true);
		VBox vb = new VBox();
	    vb.setPadding(new Insets(10, 10, 10, 0));
	    vb.setSpacing(10);
	    vb.getChildren().add(imageView);
        grid.add(vb, 0, 18, 6, 23);
	}

	private void addSpacerLabel(GridPane grid, int y) {
		Label dummy1 = new Label("");
		grid.add(dummy1, 0, y);
	}

	private void addTitle(GridPane grid, String title, int x, int y) {
		Text scenetitle = new Text(title);
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0);
	}
	
	private TextField addTaxInput(GridPane grid, String label, int x, int y) {
		Label l1 = new Label(label);
		grid.add(l1, x, y);
		TextField tf1 = new TextField();
		tf1.setText("0");
		grid.add(tf1, x + 1 , y);
		return tf1;
	}

	private Button addButton(GridPane grid, String label, int x, int y, EventHandler<ActionEvent> handler) {
		Button btn = new Button();
		btn.setText(label);
		btn.setOnAction(handler);
		grid.add(btn, x, y);
		return btn;
	}
	
	private EventHandler<ActionEvent> getShowPublicKeyHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clearOutput();
				String publicKey = Base64.getEncoder().encodeToString(certificate.getPublicKey().getEncoded());
				writeOutputLine("public key: " + publicKey);
			}
		};
	}
	
	private EventHandler<ActionEvent> getClearHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tax1.setText("0");
				tax2.setText("0");
				tax3.setText("0");
				tax4.setText("0");
				tax5.setText("0");
				clearOutput();
				imageView.setImage(null);
			}
		};
	}

	private void initProcessData() {
		int t1 = parseTaxValue(tax1.getText()); 
		int t2 = parseTaxValue(tax2.getText());
		int t3 = parseTaxValue(tax3.getText());
		int t4 = parseTaxValue(tax4.getText());
		int t5 = parseTaxValue(tax5.getText());
		int sum = t1 + t2 + t3 + t4 + t5 ;
		processData = "Beleg^";
		processData += CashRegisterFormater.toEuroString(t1) + "_";
		processData += CashRegisterFormater.toEuroString(t2) + "_";
		processData += CashRegisterFormater.toEuroString(t3) + "_";
		processData += CashRegisterFormater.toEuroString(t4) + "_";
		processData += CashRegisterFormater.toEuroString(t5) + "^";
		processData +=  CashRegisterFormater.toEuroString(sum) + ":Bar";
	}
	
	int parseTaxValue(String s) {
		float f = Float.parseFloat(s.replace(",", "."));
		f *= 100;
		return (int) f;
	}
	
	
	private EventHandler<ActionEvent> getStartTransactionHandler() {
		
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clearOutput();
				outputArea.clear();
				writeOutputLine("startTransaction with parameter");
				writeOutputLine("clientID: " + CLIENT_ID);
				StartTransactionResult startTransactionResult = seapi.startTransaction(CLIENT_ID, "X".getBytes(), null, null);
				writeOutputLine("");
				writeOutputLine("startTransaction called with status: " + startTransactionResult.getStatus());
				if(startTransactionResult.getStatus() == 0) {
					transactionNumber = startTransactionResult.getTransactionNumber();
					startTime = startTransactionResult.getLogTime();
					writeOutputLine("transactionNumber: " + transactionNumber);
					writeOutputLine("logTime: " + DSFinVKUtil.formatTime(startTime));
				}
			}
		};
	}
	
	private EventHandler<ActionEvent> getFinishTransactionHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				initProcessData();
				clearOutput();
				writeOutputLine("FinishTransaction with parameter");
				writeOutputLine("clientID: " + CLIENT_ID);
				writeOutputLine("transactionNumber: " + transactionNumber);
				writeOutputLine("processData: " + processData);
				writeOutputLine("processType: " + ProcessType.RECEIPT);
				TransactionResult endTransactionResult = seapi.finishTransaction(CLIENT_ID, transactionNumber, processData.getBytes(), DSFinVKUtil.ProcessType.RECEIPT);
				writeOutputLine("finishTransaction called with status=" + endTransactionResult.getStatus());
				if(endTransactionResult.getStatus() == 0) {
					Long endSignatureCounter = endTransactionResult.getSignatureCounter();
					byte[] endSignature =  endTransactionResult.getSignatureValue();
					ZonedDateTime endTime = endTransactionResult.getLogTime();
					writeOutputLine("logTime: " + DSFinVKUtil.formatTime(endTime));
					writeOutputLine("signatureCounter: " + String.valueOf(endSignatureCounter));
					writeOutputLine("signature: ", endSignature);
					String publicKey = Base64.getEncoder().encodeToString(certificate.getPublicKey().getEncoded());					
					qrCodeData = DSFinVKUtil.generateQrcodeData(CLIENT_ID, ProcessType.RECEIPT, processData, String.valueOf(transactionNumber), String.valueOf(endSignatureCounter),
							DSFinVKUtil.formatTime(startTime), DSFinVKUtil.formatTime(endTime), logTimeFormat, Base64.getEncoder().encodeToString(endSignature), publicKey);
				}
			}
		};
	}
	
	private EventHandler<ActionEvent> getGenerateQRCodeHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clearOutput();
				writeOutputLine("QR code data: " + qrCodeData) ;
				byte[] qrImage = generateQrcode(qrCodeData);
				Image image = new Image(new ByteArrayInputStream(qrImage));
				imageView.setImage(image);
			}
		};
	}

    public byte[] generateQrcode(String qrData) {
    	byte[] toReturn = null;
    	try {
    		toReturn = DSFinVKUtil.getQRCodeImage(qrData, 500, 500);
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return toReturn;
    }
	
    
    private void writeOutputLine(String s) {
		outputArea.appendText(s + "\n");
    }
    
    private void writeOutputLine(String s, byte[] ba) {
		outputArea.appendText(s);
		outputArea.appendText(Base64.getEncoder().encodeToString(ba));
		outputArea.appendText("\n");
    }
    
    private void clearOutput() {
		outputArea.clear();
    }
	
	public static void main(String[] args) {
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		if(args.length == 0) {
			System.out.println("No arguments found. It is required to pass the path of libasigntse.dll as argument, when it is not in classpath ");
			System.exit(-1);
		} else {
			System.setProperty("jna.library.path", args[0]);
		}
		launch(args);
	}
	
}
