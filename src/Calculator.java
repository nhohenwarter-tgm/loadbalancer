import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Repraesentiert die Vorlage fuer einen Pi-Calculator
 * 
 * @author Michael Borko
 * @author Niklas Hohenwarter
 * @author Selina Brinnich
 * @version 2016-03-03
 */
public interface Calculator extends Remote{
	/**
	 * Berechnet die Zahl Pi auf eine bestimmte Anzahl an Stellen
	 *
	 * @param anzahl_nachkommastellen anzahl der Stellen
	 * @return berechnetes Pi
	 */
	public BigDecimal pi(int anzahl_nachkommastellen) throws RemoteException;
}