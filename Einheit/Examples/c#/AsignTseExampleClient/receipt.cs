using System.Collections.Generic;
using LibAsignTse;

public class Receipt
{
    class Payment
    {

        //  <Betrag>:<Zahlungsart>:<Währung> 
        private static string SEPARATOR = ":";

        private int amount;

        private bool cash;

        private string currency = null;

        public Payment(int amount, bool cash, string currency)
        {
            this.amount = amount;
            this.cash = cash;
            this.currency = currency;
        }

        public Payment(int amount, bool cash)
        {
            this.amount = amount;
            this.cash = cash;
        }

        public string format()
        {
            string result = "";
            result = (result + toEuroString(this.amount));
            result = (result + SEPARATOR);
            if (this.cash)
            {
                result += "Bar";
            }
            else
            {
                result += "Unbar";
            }

            if ((this.currency != null))
            {
                result = (result + SEPARATOR);
                result = (result + this.currency);
            }

            return result;
        }
    }

    private static string SEPARATOR = "^";
    private static string PAYMENT_SEPARATOR = "_";

    private int tax1;
    private int tax2;
    private int tax3;
    private int tax4;
    private int tax5;

    private List<Payment> payments = new List<Payment>();


    /**
     * Constructor 
     * 
     * @param tax1	Allgemeiner Steuersatz (19%) in Cent
     * @param tax2	Ermäßigter Steuersatz (7%) in Cent
     * @param tax3	Durchschnittsatz (§24(1)Nr.3 UStG) (10.7%) in Cent
     * @param tax4	Durchschnittsatz (§24(1)Nr.1 UStG) (5.5%) in Cent
     * @param tax5	0% in Cent
     */
    public Receipt(int tax1, int tax2, int tax3, int tax4, int tax5)
    {
        this.tax1 = tax1;
        this.tax2 = tax2;
        this.tax3 = tax3;
        this.tax4 = tax4;
        this.tax5 = tax5;
    }

    public void addPayment(int amount, bool cash, string currency)
    {
        Payment p = new Payment(amount, cash, currency);
        payments.Add(p);
    }

    public void addPayment(int amount, bool cash)
    {
        Payment p = new Payment(amount, cash);
        payments.Add(p);
    }


    public string getFormatedReceipt()
    {
        // Beleg^0.00_2.55_0.00_0.00_0.00^2.55:Bar
        string result = "";
        result += "Beleg";
        result += SEPARATOR;
        result += formatTaxLine(tax1, tax2, tax3, tax4, tax5);
        result += SEPARATOR;
        foreach (Payment p in payments)
        {
            result += p.format();
            result += PAYMENT_SEPARATOR;

        }
        if (payments.Count != 0)
        {
            result = result.Substring(0, result.Length - PAYMENT_SEPARATOR.Length);
        }
        return result;
    }

    private static string TAX_VALUE_SEPARATOR = "_";

    /**
     * @param tax1	Allgemeiner Steuersatz (19%) in Cent
     * @param tax2	Ermäßigter Steuersatz (7%) in Cent
     * @param tax3	Durchschnittsatz (§24(1)Nr.3 UStG) (10.7%) in Cent
     * @param tax4	Durchschnittsatz (§24(1)Nr.1 UStG) (5.5%) in Cent
     * @param tax5	0% in Cent
     */
    private static string formatTaxLine(int tax1, int tax2, int tax3, int tax4, int tax5)
    {
        // Beleg^0.00_2.55_0.00_0.00_0.00^2.55:Bar
        string toReturn = "";
        toReturn += toEuroString(tax1);
        toReturn += TAX_VALUE_SEPARATOR;
        toReturn += toEuroString(tax2);
        toReturn += TAX_VALUE_SEPARATOR;
        toReturn += toEuroString(tax3);
        toReturn += TAX_VALUE_SEPARATOR;
        toReturn += toEuroString(tax4);
        toReturn += TAX_VALUE_SEPARATOR;
        toReturn += toEuroString(tax5);
        return toReturn;
    }

    private static string toEuroString(int centAmount)
    {
        int euro;
        if ((centAmount == 0))
        {
            euro = 0;
        }
        else
        {
            euro = (centAmount / 100);
        }

        string toReturn = (euro + ".");
        int cent = (centAmount % 100);
        if ((cent < 10))
        {
            toReturn = (toReturn + ("0" + cent));
        }
        else
        {
            toReturn = (toReturn + cent);
        }

        return toReturn;
    }
}