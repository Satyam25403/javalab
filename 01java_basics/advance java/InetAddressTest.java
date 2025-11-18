import java.net.*;

public class InetAddressTest {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        System.out.println("Local Host: " + address);

        address = InetAddress.getByName("www.facebook.com");
        System.out.println("Facebook Host: " + address);

        InetAddress[] sw = InetAddress.getAllByName("www.google.com");
        for (InetAddress inetAddress : sw) {
            System.out.println("Google Host: " + inetAddress);
        }
    }
}
