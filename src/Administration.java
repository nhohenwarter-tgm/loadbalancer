import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Repraesentiert ein Interface zur An- und Abmeldung von Servern am Load-Balancer
 * 
 * @author Niklas Hohenwarter
 * @author Selina Brinnich
 * @version 2016-03-03
 *
 */
public interface Administration extends Remote{

	/**
	 * Meldet einen neuen Server am Load-Balancer an
	 * @param stub ein exportiertes Objekt des Servers
	 * @param name der Name unter dem der Server gespeichert werden soll
	 * @param ip   die IP, auf der der Server laeuft
	 */
	public void register(Calculator stub, String name, String ip) throws RemoteException;
	/**
	 * Meldet einen Server vom Load-Balancer ab
	 * @param name der Name des Servers, der vom Load-Balancer abgemeldet werden soll
	 */
	public void unregister(String name) throws RemoteException;
	
}
