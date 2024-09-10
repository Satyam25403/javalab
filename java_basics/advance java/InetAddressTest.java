import java.net.*;
public class InetAddressTest {
 public static void main(String args[]) throws UnknownHostException
{
 InetAddress Address = InetAddress.getLocalHost();
 System.out.println("Local Host : "+Address);
 
 Address = InetAddress.getByName("www.facebook.com");
 System.out.println("Google Host : "+Address);
 
 InetAddress sw[] = InetAddress.getAllByName("www.google.com");
 for(int i=0;i<sw.length;i++)
 System.out.println("Google Host : "+sw[i]);
 
 }
}