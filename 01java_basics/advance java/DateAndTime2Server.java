import java.net.*;
import java.util.*;

public class DateAndTime2Server {
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
                if (clientMessage.equals("exit")) {
                    System.out.println("Client disconnected");
                    break;
                }

                //send response to client on its address and port number:response is sent through buffer::getBytes()
                Date d=new Date();
                String serverMessage = "Date and time "+d;
                sendBuffer = serverMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, receivePacket.getAddress(), receivePacket.getPort());
                serverSocket.send(sendPacket);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


