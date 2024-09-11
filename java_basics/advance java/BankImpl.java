import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BankImpl extends UnicastRemoteObject implements Bank {
    private double balance;

    public BankImpl() throws RemoteException {
        balance = 0.0; // Initial balance
    }

    @Override
    public void deposit(double amount) throws RemoteException {
        balance += amount;
        System.out.println("Deposited: " + amount);
    }

    @Override
    public void withdraw(double amount) throws RemoteException {
        if (amount <= balance) {
            balance -= amount;
            System.out.println("Withdrew: " + amount);
        } else {
            System.out.println("Insufficient balance");
        }
    }

    @Override
    public double getBalance() throws RemoteException {
        return balance;
    }
}