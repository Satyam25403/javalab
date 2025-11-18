import java.rmi.Naming;
import java.util.Scanner;
public class MyClient{
public static void main(String args[])
{
try
{
    MyBillTotal stub = (MyBillTotal)Naming.lookup("rmi://localhost/totalbill");
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter number of kgs of potatoes:");
    int pot=sc.nextInt();
    System.out.println("Enter number of kgs of tomatoes :");
    int tom=sc.nextInt();
    System.out.println("Enter number of kgs of onions :");
    int on=sc.nextInt();
    System.out.println("Enter number of kgs of spinach :");
    int spin=sc.nextInt();
    System.out.println("The total cost is "+stub.total(pot,tom,on,spin));
}
catch(Exception e)
{
    System.out.println(e);
}
}
}

