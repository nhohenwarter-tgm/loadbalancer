import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Repraesentiert die Vorlage fuer einen Pi-Calculator
 * 
 * @author Michael Borko
 * @author Niklas Hohenwarter
 * @author Selina Brinnich
 * @version 2014-12-12
 */
public interface Calculator extends Remote{
	public BigDecimal pi(int anzahl_nachkommastellen) throws RemoteException;
}