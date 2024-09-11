import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
public class MyBillTotalRemote extends UnicastRemoteObject implements MyBillTotal
{
MyBillTotalRemote() throws RemoteException
{
	super();
}
public long total(int pot,int tom,int on,int spin)
	{
		long sum=0;
		sum=pot*17+tom*8+on*20+spin*12;
		
		return sum;
	}
}
