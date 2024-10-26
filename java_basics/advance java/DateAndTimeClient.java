import java.net.*;
import java.util.*;
//Client:first send and then receive
class DateAndTimeClient {
    private final static int size = 100; // Buffer size for receiving data

    public static void main(String[] args) {
        DatagramSocket socket = null; // Socket for communication
        Scanner sc=null;               //reading user input
        try {
            sc = new Scanner(System.in);
            socket = new DatagramSocket(); // Create a socket for sending and receiving packets
            InetAddress host = InetAddress.getByName("localhost"); // Server address
            int port = 1932;                                            // Server port
            String sp = " ";                                            // Variable to store user input


            // Loop to send and receive messages until "exit" is sent
            while (!sp.equalsIgnoreCase("exit")) {
                System.out.println("Enter a message to send to the server (or 'exit' to quit):");
                sp = sc.nextLine(); 
                byte[] data = sp.getBytes();    // Convert user input to bytes
                DatagramPacket packet = new DatagramPacket(data, data.length, host, port); // Create a packet to send data to the server
                socket.send(packet); // Send the packet to the server

                if (sp.equals("exit")) {
                    break; // Break the loop if the user sends "stop"
                }

                DatagramPacket packet2 = new DatagramPacket(new byte[size], size); // packet to receive data
                socket.receive(packet2);                   //Receive a packet from the server
                System.out.println(":->"+new String(packet2.getData(), 0, packet2.getLength())); // Print the server's message
            }

        }catch (Exception e) {
            System.out.println("Error at client"); // Print an error message if an exception occurs
        }
        
    }
}
