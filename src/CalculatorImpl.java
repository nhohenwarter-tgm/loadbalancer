import java.io.Serializable;
import java.math.*;
import java.rmi.RemoteException;

/**
 * Kann Pi bis zu einer gewissen Nachkommastelle berechnen. Die Methode wird am
 * Server verwendet.
 * 
 * @author Niklas Hohenwarter
 * @author Selina Brinnich
 * 
 * @version 2015-01-08
 *
 * @see http://stackoverflow.com/a/17899007
 *
 */
public class CalculatorImpl implements Calculator, Serializable {
	
	private static final long serialVersionUID = -3107350897306274378L;
	
	private static final BigDecimal TWO = new BigDecimal("2");
	private static final BigDecimal FOUR = new BigDecimal("4");
	private static final BigDecimal FIVE = new BigDecimal("5");
	private static final BigDecimal TWO_THIRTY_NINE = new BigDecimal("239");
	
	public CalculatorImpl() {
		super();
	}

	@Override
	public BigDecimal pi(int anzahl_nachkommastellen) throws RemoteException {
		int calcDigits = anzahl_nachkommastellen + 10;

		return FOUR.multiply(
				(FOUR.multiply(arccot(FIVE, calcDigits))).subtract(arccot(
						TWO_THIRTY_NINE, calcDigits))).setScale(anzahl_nachkommastellen,
				RoundingMode.DOWN);
	}

	private static BigDecimal arccot(BigDecimal x, int numDigits) {

		BigDecimal unity = BigDecimal.ONE
				.setScale(numDigits, RoundingMode.DOWN);
		BigDecimal sum = unity.divide(x, RoundingMode.DOWN);
		BigDecimal xpower = new BigDecimal(sum.toString());
		BigDecimal term = null;

		boolean add = false;

		for (BigDecimal n = new BigDecimal("3"); term == null
				|| term.compareTo(BigDecimal.ZERO) != 0; n = n.add(TWO)) {

			xpower = xpower.divide(x.pow(2), RoundingMode.DOWN);
			term = xpower.divide(n, RoundingMode.DOWN);
			sum = add ? sum.add(term) : sum.subtract(term);
			add = !add;
		}
		return sum;
	}

}
