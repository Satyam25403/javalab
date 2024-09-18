import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPServer {
    public static void main(String[] args) {
        try (DatagramSocket serverSocket = new DatagramSocket(12345)){
            byte[] receiveBuffer = new byte[1024];
            byte[] sendBuffer;
            Scanner scanner = new Scanner(System.in);

            System.out.println("Server is listening on port 12345");

            while (true) {
                // Receive packet and process
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);

                String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Client: " + clientMessage);
                if (clientMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Client disconnected");
                    break;
                }

                //send response to client on its address and port number
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                System.out.print("Server: ");
                String serverMessage = scanner.nextLine();
                sendBuffer = serverMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
                if (serverMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Server disconnected");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

