import java.io.*;
import java.net.*;
//run with ChatServer.java with split terminals
public class ChatClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345)) {

            //two readers(console, server) and one writer:
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            String clientMessage, serverMessage;
            
            while (true) {

                System.out.print("Client: ");
                clientMessage = consoleInput.readLine();
                output.println(clientMessage);
                //we dont print client message in client console
                if (clientMessage.equals("exit")) {
                    System.out.println("Client disconnected");
                    break;
                }

                serverMessage = input.readLine();
                if (serverMessage.equals("exit")) {
                    System.out.println("Server disconnected");
                    break;
                }
                System.out.println("Server: " + serverMessage);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

