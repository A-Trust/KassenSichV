package at.atrust.kassensichv.example.util;

public class CashRegisterFormater {

	public static String toEuroString(int centAmount) {
		int euro;
		if (centAmount == 0) {
			euro = 0;
		} else {
			euro = centAmount / 100;
		}
		String toReturn = euro + ".";
		int cent = Math.abs(centAmount % 100);
		if (cent < 10) {
			toReturn = toReturn + "0" + cent;
		} else {
			toReturn += cent;
		}
		return toReturn;
	}

}
