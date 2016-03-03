import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Repraesentiert ein Interface zur An- und Abmeldung von Servern am Load-Balancer
 * 
 * @author Niklas Hohenwarter
 * @author Selina Brinnich
 * @version 2015-01-08
 *
 */
public interface Administration extends Remote{

	public void register(Calculator stub, String name, String ip) throws RemoteException;
	public void unregister(String name) throws RemoteException;
	
}
