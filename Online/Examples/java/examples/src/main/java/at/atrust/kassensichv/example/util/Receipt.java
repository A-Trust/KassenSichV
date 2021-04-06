package at.atrust.kassensichv.example.util;

import java.util.ArrayList;
import java.util.List;

public class Receipt {

	class Payment {
		// <Betrag>:<Zahlungsart>:<Währung> 
		private static final String SEPARATOR = ":";
		private int amount;
		private boolean cash;
		private String currency = null;
		
		public Payment(int amount, boolean cash, String currency) {
			this.amount = amount;
			this.cash = cash;
			this.currency = currency;
		}

		public Payment(int amount, boolean cash) {
			this.amount = amount;
			this.cash = cash;
		}
		
		String format() {
			String result = "";
			result += CashRegisterFormater.toEuroString(amount);
			result += SEPARATOR;
			if(cash) {
				result += "Bar";
			} else {
				result += "Unbar";
			}
			if(currency != null) {
				result += SEPARATOR;
				result += currency;
			}
			return result;
			
		}
	}
	
	private static final String SEPARATOR = "^";
	private static final String PAYMENT_SEPARATOR = "_";
	
	private int tax1;
	private int tax2;
	private int tax3;
	private int tax4;
	private int tax5;
	
	private List<Payment> payments = new ArrayList<>();
	
	/**
	 * Constructor 
	 * 
	 * @param tax1	Allgemeiner Steuersatz (19%) in Cent
	 * @param tax2	Ermäßigter Steuersatz (7%) in Cent
	 * @param tax3	Durchschnittsatz (§24(1)Nr.3 UStG) (10.7%) in Cent
	 * @param tax4	Durchschnittsatz (§24(1)Nr.1 UStG) (5.5%) in Cent
	 * @param tax5	0% in Cent
	 */
	public Receipt(int tax1, int tax2, int tax3, int tax4, int tax5) {
		this.tax1 = tax1;
		this.tax2 = tax2;
		this.tax3 = tax3;
		this.tax4 = tax4;
		this.tax5 = tax5;
	}
	
	public void addPayment(int amount, boolean cash, String currency) {
		Payment p = new Payment(amount, cash, currency);
		payments.add(p);
	}
	
	public void addPayment(int amount, boolean cash) {
		Payment p = new Payment(amount, cash);
		payments.add(p);
	}
	
	
	public String getFormatedReceipt() {
		// Beleg^0.00_2.55_0.00_0.00_0.00^2.55:Bar
		String result = "";
		result += "Beleg";
		result += SEPARATOR;
		result += formatTaxLine(tax1, tax2, tax3, tax4, tax5);
		result += SEPARATOR;
		for(Payment p : payments) {
			result += p.format();
			result += PAYMENT_SEPARATOR;
		}
		if(payments.size() != 0) {
			result = result.substring(0, result.length() - PAYMENT_SEPARATOR.length());
		}
		return result;
	}
	
	private static final String TAX_VALUE_SEPARATOR = "_";
	
	/**
	 * @param tax1	Allgemeiner Steuersatz (19%) in Cent
	 * @param tax2	Ermäßigter Steuersatz (7%) in Cent
	 * @param tax3	Durchschnittsatz (§24(1)Nr.3 UStG) (10.7%) in Cent
	 * @param tax4	Durchschnittsatz (§24(1)Nr.1 UStG) (5.5%) in Cent
	 * @param tax5	0% in Cent
	 */
	private static String formatTaxLine(int tax1, int tax2, int tax3, int tax4, int tax5) {
		// Beleg^0.00_2.55_0.00_0.00_0.00^2.55:Bar
		String toReturn = "";
		toReturn += CashRegisterFormater.toEuroString(tax1);
		toReturn += TAX_VALUE_SEPARATOR;
		toReturn += CashRegisterFormater.toEuroString(tax2);
		toReturn += TAX_VALUE_SEPARATOR;
		toReturn += CashRegisterFormater.toEuroString(tax3);
		toReturn += TAX_VALUE_SEPARATOR;
		toReturn += CashRegisterFormater.toEuroString(tax4);
		toReturn += TAX_VALUE_SEPARATOR;
		toReturn += CashRegisterFormater.toEuroString(tax5);
		return toReturn;
	}
}
