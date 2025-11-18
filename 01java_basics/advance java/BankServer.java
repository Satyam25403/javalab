import java.rmi.*;
import java.rmi.registry.*;

public class BankServer {
    public static void main(String[] args) {
        try {
            // Create the Bank object
            Bank bank = new BankImpl();

            // Start the RMI registry:without need of java 8
            LocateRegistry.createRegistry(1099); // Default RMI registry port

            // Bind the Bank object in the RMI registry
            Naming.rebind("rmi://localhost/BankService", bank);

            System.out.println("Bank Server is ready...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}