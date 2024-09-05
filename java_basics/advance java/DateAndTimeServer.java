//Develop the Date & Time Client-Server application using stream & datagram sockets.
import java.io.*;
import java.net.*;
import java.util.*;
//server: first receive and then send
class DateAndTimeServer {
    private final static int size = 100; // Buffer size for receiving data

    public static void main(String[] args) throws IOException {
        int port = 1932; // Port number the server listens on
        DatagramSocket socket = new DatagramSocket(port); // Create a socket to listen on the specified port
        String sp = " "; // Variable to store received messages

        // Loop to receive and send messages until "stop" is received
        System.out.println("Server is listening>>>>");
        while (!sp.equals("exit")) {
            DatagramPacket packet = new DatagramPacket(new byte[size], size); // Create a packet to receive data
            socket.receive(packet); // Receive a packet from the client
            sp = new String(packet.getData(), 0, packet.getLength()); // Convert the received data to a string
            System.out.println("Message received from ADDRESS:"+packet.getAddress() + " PORT:" + packet.getPort() + " :: " + sp); // Print client's message with address and port

            if (sp.equals("exit")) {
                break; // Break the loop if the client sends "stop"
            }

            Date d = new Date(); // Get the current date and time
            String s = "Date and time: " + d; // Prepare the response message
            byte[] buffer = s.getBytes(); // Convert the response message to bytes
            DatagramPacket clientPacket = new DatagramPacket(buffer, buffer.length, packet.getAddress(), packet.getPort()); // Create a packet to send data to the client
            socket.send(clientPacket); // Send the packet to the client
        }

        socket.close(); // Close the socket
    }
}
