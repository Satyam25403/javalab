import java.net.*;
import java.util.Scanner;

public class UDPChatClient {
    public static void main(String[] args) {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            byte[] sendBuffer;
            byte[] receiveBuffer = new byte[1024];
            Scanner scanner = new Scanner(System.in);

            while (true) {
                // Send message using packet
                System.out.print("Client: ");
                String clientMessage = scanner.nextLine();
                sendBuffer = clientMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, 12345);
                clientSocket.send(sendPacket);
                if (clientMessage.equals("exit")) {
                    System.out.println("Client disconnected");
                    break;
                }

                // Receive response
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                clientSocket.receive(receivePacket);
                String serverMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Server: " + serverMessage);
                if (serverMessage.equals("exit")) {
                    System.out.println("Server disconnected");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
                             