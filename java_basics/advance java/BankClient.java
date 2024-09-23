import java.rmi.*;
import java.util.*;

public class BankClient {
    public static void main(String[] args) {
        try {
            // Lookup the Bank object from the RMI registry
            Bank bank = (Bank) Naming.lookup("rmi://localhost/BankService");
            Scanner sc=new Scanner(System.in);
            
            // Perform some transactions
            System.out.println("Enter amount deposit:");
            bank.deposit(sc.nextInt());
            System.out.println("Enter amount withdraw:");
            bank.withdraw(sc.nextInt());

            // Get balance
            System.out.println("Current balance: " + bank.getBalance());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}