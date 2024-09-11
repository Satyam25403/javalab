import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bank extends Remote {
    void deposit(double amount) throws RemoteException;
    void withdraw(double amount) throws RemoteException;
    double getBalance() throws RemoteException;
}