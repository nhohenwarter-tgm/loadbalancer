import java.io.IOException;
import java.math.BigDecimal;
import java.net.*;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.Map.Entry;

/**
 * Dient als Load-Balancer fuer Client-Anfragen an Pi-Calculation-Server. Der Load-Balancer wurde mittels Round Robin
 * Algorithmus umgesetzt. Server koennen sich jederzeit am Load-Balancer anmelden bzw. abmelden.
 * 
 * @author Niklas Hohenwarter
 * @author Selina Brinnich
 * @version 2015-01-08
 *
 */
public class CalculatorBalancer implements Calculator, Administration {
	
	private static HashMap<String, Calculator> server;
	private static HashMap<String,String> server_ips;
	private static ArrayList<String> server_weighted;
	private static Iterator<Entry<String,String>> it_server_ips;
	private static Iterator<String> it_current_server_weighted;
	private static Iterator<Entry<String, Calculator>> it_server;
	private static int nextweight;

	private static int algorithm = CalculatorBalancer.ALG_ROUNDROBIN;

	private static final int ALG_ROUNDROBIN = 0;
	private static final int ALG_RESPONSETIME = 1;
	private static final int ALG_WEIGHTEDDISTRIB = 2;
	
	/**
	 * Initialisiert eine neue HashMap die alle angemeldeten Server verwaltet und einen Iterator zur Umsetzung des
	 * Round Robin Algorithmus
	 */
	public CalculatorBalancer(int algorithm) {
		server = new HashMap<String,Calculator>();
		server_ips = new HashMap<String,String>();
		server_weighted = new ArrayList<String>();
		it_server_ips = server_ips.entrySet().iterator();
		it_current_server_weighted = server_weighted.iterator();
		it_server = server.entrySet().iterator();
		nextweight = 1;
		CalculatorBalancer.algorithm = algorithm;
	}
	
	/**
	 * Gibt den naechsten Server in der Liste zurueck
	 * @return den naechsten Server der Liste
	 */
	public synchronized String balance(){
		if(server.size()>0){
			switch(algorithm){
				case CalculatorBalancer.ALG_RESPONSETIME:
					return balance_responsetime();
				case CalculatorBalancer.ALG_WEIGHTEDDISTRIB:
					return balance_weighteddistrib();
				default:
					return balance_roundrobin();
			}
		}else{
			return null;
		}
	}

	/**
	 *
	 */
	public synchronized String balance_roundrobin(){
		if(!it_server.hasNext()){
			it_server = server.entrySet().iterator();
		}
		return it_server.next().getKey();
	}

	/**
	 *
	 */
	public synchronized String balance_responsetime(){
		long least = -1;
		String least_name = null;
		it_server_ips = server_ips.entrySet().iterator();
		while(it_server_ips.hasNext()) {
			Entry<String,String> next = it_server_ips.next();
			String ipAddress = next.getValue();
			try {
				InetAddress inet = InetAddress.getByName(ipAddress);

				long finish = 0;
				long start = System.nanoTime();

				if (inet.isReachable(5000)){
					finish = System.nanoTime();
					long time = finish-start;
					if(time < least || least == -1){
						least = time;
						least_name = next.getKey();
					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return least_name;
	}

	/**
	 *
	 */
	public synchronized String balance_weighteddistrib(){
		if(!it_current_server_weighted.hasNext()) {
			it_current_server_weighted = server_weighted.iterator();
		}
		return it_current_server_weighted.next();
	}


	/**
	 * Sucht sich einen freien Server und laesst diesen Pi berechnen
	 * @param anzahl_nachkommastellen die Anzahl der Nachkommastellen, bis zu denen Pi berechnet werden soll
	 */
	@Override
	public BigDecimal pi(int anzahl_nachkommastellen) throws RemoteException {
		String name = balance();
		if(name == null){
			System.out.println("No server available for client request.");
			return null;
		}else{
			Calculator stub = server.get(name);
			System.out.println("Server \""+name+"\" started calculating pi to "+anzahl_nachkommastellen+" digits.");
			BigDecimal pi = null;
			try{
				pi = stub.pi(anzahl_nachkommastellen);
			}catch(Exception e){
				unregister(name);
				pi(anzahl_nachkommastellen);
			}
			return pi;
		}
	}
	
	/**
	 * Meldet einen neuen Server am Load-Balancer an
	 * @param stub ein exportiertes Objekt des Servers
	 * @param name der Name unter dem der Server gespeichert werden soll
	 */
	@Override
	public synchronized void register(Calculator stub, String name, String ip){
		server.put(name, stub);
		it_server = server.entrySet().iterator();
		server_ips.put(name, ip);
		it_server_ips = server_ips.entrySet().iterator();
		for(int i = 0; i < nextweight; i++)
			server_weighted.add(name);
		it_current_server_weighted = server_weighted.iterator();
		nextweight += 1;
		System.out.println("Registered server \""+name+"\".");
	}

	/**
	 * Meldet einen Server vom Load-Balancer ab
	 * @param name der Name des Servers, der vom Load-Balancer abgemeldet werden soll
	 */
	@Override
	public void unregister(String name) {
		if(server_ips.containsKey(name)){
			server_ips.remove(name);
		}
		if(server_weighted.contains(name)){
			server_weighted.removeAll(Collections.singleton(name));
		}
		if(server.containsKey(name)){
			server.remove(name);
			it_server = server.entrySet().iterator();
			System.out.println("Unregistered server \""+name+"\".");
		}
	}
	
	/**
	 * Gibt die IP-Adresse des Users zurueck
	 * @see http://stackoverflow.com/a/18945245
	 * @return die aktuelle IP-Adresse des Users, der diese Anwendung verwendet, in einem String
	 */
	public static String getIp(){
	    String ipAddress = null;
	    Enumeration<NetworkInterface> net = null;
	    try {
	        net = NetworkInterface.getNetworkInterfaces();
	    } catch (SocketException e) {
	        throw new RuntimeException(e);
	    }

	    while(net.hasMoreElements()){
	        NetworkInterface element = net.nextElement();
	        Enumeration<InetAddress> addresses = element.getInetAddresses();
	        while (addresses.hasMoreElements()){
	            InetAddress ip = addresses.nextElement();
	            if (ip instanceof Inet4Address){
	                if (ip.isSiteLocalAddress()){
	                    ipAddress = ip.getHostAddress();
	                }
	            }
	        }
	    }
	    return ipAddress;
	}
	
	/**
	 * Erstellt einen neuen Load-Balancer, erzeugt eine Registry und registriert darauf ein Calculator-Interface fuer den
	 * Zugriff der Clients und ein Administration-Interface fuer die An- und Abmeldung der Server am Load-Balancer
	 * @param args
	 */
	public static void main(String[] args) {
		int number = -1;
		if(args.length!=1){
			System.err.println("Error! Invalid parameters.\n Syntax: CalculatorBalancer <Algorithm>\nAlgorithms: " +
					"0=Round Robin, 1=Response Time, 2=Weighted Distribution");
			System.exit(1);
		}else{
			try{
				number = Integer.parseInt(args[0]);
			}catch(Exception e){
				System.err.println("Error! Invalid algorithm!");
				System.exit(1);
			}
			if(number < 0 || number > 2){
				System.err.println("Error! Invalid algorithm!");
				System.exit(1);
			}
		}

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}
		
		try {
			Calculator calc = new CalculatorBalancer(number);
			Administration admin = new CalculatorBalancer(number);
			System.setProperty("java.rmi.server.hostname", getIp());
			Calculator stubC = (Calculator) UnicastRemoteObject.exportObject(calc, 0);
			Administration stubA = (Administration) UnicastRemoteObject.exportObject(admin, 0);
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("PiCalculationBalancer-Client", stubC);
			registry.rebind("PiCalculationBalancer-Server", stubA);
            System.out.println("Balancer successfully bound with IP "+getIp());
		} catch (Exception e) {
			System.err.println("Balancer bind failed:");
			e.printStackTrace();
		}
	}

}
