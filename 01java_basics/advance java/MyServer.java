import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
public class MyServer
{
	public static void main(String args[])
	{
		try
		{
			MyBillTotal stub = new MyBillTotalRemote();
			LocateRegistry.createRegistry(1088);
			Naming.rebind("rmi://localhost/totalbill",stub);
			System.out.println("Server is ready............");
			System.out.println("Remote object is ready..........");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
