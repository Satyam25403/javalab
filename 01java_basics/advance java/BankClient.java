import java.rmi.*;
import java.util.*;

public class BankClient {
    public static void main(String[] args) {
        try {
            // Lookup the Bank object from the RMI registry
            Bank bank = (Bank) Naming.lookup("rmi://localhost/BankService");
            Scanner sc=new Scanner(System.in);
            
            boolean run=true;
            while(run){
                System.out.println("1.deposit\n2.withdrawl\n3.check balance\n4.exit");
                System.out.println("Enter an option:");
                switch(sc.nextInt()){
                    case 1:
                        System.out.println("Enter amount deposit:");
                        bank.deposit(sc.nextInt());
                        break;
                    case 2:
                        System.out.println("Enter amount withdraw:");
                        bank.withdraw(sc.nextInt());
                        break;
                    case 3:
                        System.out.println("Current balance: " + bank.getBalance());
                        break;
                    case 4:
                        run=false;
                        break;
                    default:
                        System.out.println("Invalid option");
                }       
    
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}