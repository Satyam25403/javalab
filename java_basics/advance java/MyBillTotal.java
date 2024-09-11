import java.rmi.Remote;
import java.rmi.RemoteException;
public interface MyBillTotal extends Remote{
	public long total(int pot,int tom,int on,int spin) throws RemoteException;
}
