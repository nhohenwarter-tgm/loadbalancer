import java.math.BigDecimal;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Repraesentiert einen RMI Server, welcher Pi auf x Nachkommastellen berechnen kann.
 * 
 * @author Niklas Hohenwarter
 * @author Selina Brinnich
 * @version 2015-01-08
 */
public class Server implements Calculator {
	
	private Calculator calc;
	
	/**
	 * Initialisiert den Calculator am Server
	 */
	public Server(){
		super();
		calc = new CalculatorImpl();
	}
	
	/**
	 * Gibt die Berechnung weiter an die Calculator Klasse. Es wird Pi bis
	 * zu der uebergebenen Stelle berechnet zurueckgegeben.
	 * 
	 * @param precision anzahl der Stellen
	 * @return berechnetes Pi
	 */
	public BigDecimal pi(int precision) throws RemoteException{
		try {
			System.out.println("Calculating pi to "+precision+" digits.");
			return calc.pi(precision);
		} catch (RemoteException e) {
			System.out.println("Error during calculation! Please retry.");
		}
		return null;
	}
	
	/**
	 * Startet den Server und meldet ihn am Load-Balancer an.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length!=2){
			System.err.println("Error! Invalid parameters.\n Syntax: Server <Loadbalancer IP> <Servername>");
			System.exit(1);
		}
		String balancer = args[0];
		String name = args[1];
		
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}
		
		try {
			Calculator server = new Server();
			Registry registry = LocateRegistry.getRegistry(balancer);
			Administration admin = (Administration) registry.lookup("PiCalculationBalancer-Server");
			System.setProperty("java.rmi.server.hostname", CalculatorBalancer.getIp());
			admin.register((Calculator) UnicastRemoteObject.exportObject(server, 0), name, CalculatorBalancer.getIp());
            System.out.println("Server successfully registered with name \""+name+"\"");
		} catch (Exception e) {
			System.err.println("\"" + name + "\" not registered! An Error occured:");
			e.printStackTrace();
		}
	}
}
