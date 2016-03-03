import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Repraesentiert einen RMI Client, welcher an einen Server die Anzahl der Stellen die von Pi
 * berechnet werden sollen weitergibt und das berechnete Pi zurueckbekommt.
 * 
 * @author Niklas Hohenwarter
 * @author Selina Brinnich
 * @version 2015-01-08
 */
public class Client {
	public static void main(String[] args) {
		String ip;
		int digits = -1;
		if(args.length!=2){
			System.err.println("Error! Invalid parameters.\n Syntax: Client <Loadbalancer IP> <Pi Digits>");
			System.exit(1);
		}else{
			try{
				digits = Integer.parseInt(args[1]);
			}catch(Exception e){
				System.err.println("Error! Invalid number of digits!");
				System.exit(1);
			}
			if(digits <= 0){
				System.err.println("Error! Invalid number of digits!");
				System.exit(1);
			}
		}
		ip = args[0];
		
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
		try {
			String name = "PiCalculationBalancer-Client";
			Registry registry = LocateRegistry.getRegistry(ip);
			Calculator calc = (Calculator) registry.lookup(name);
			boolean breaking = false;
			while(!breaking) {
				BigDecimal pi = calc.pi(digits);
				if (pi != null) {
					System.out.println(pi);
				} else {
					System.out.println("Could not calculate pi! Please try again!");
					breaking = true;
				}
			}
		} catch (Exception e) {
			System.err.println("Calculate-Pi exception:");
			e.printStackTrace();
		}
	}
}
