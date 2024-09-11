import java.io.*;
import java.net.*;
//run with ChatClient.java with split terminals
public class ChatServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is listening on port 12345:");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected to server");

            //two readers(console, client) and one writer:
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
            String clientMessage, serverMessage;

            while (true) {
                clientMessage = input.readLine();
                if (clientMessage.equals("exit")) {
                    System.out.println("Client disconnected");
                    break;
                }
                System.out.println("Client: " + clientMessage);

                System.out.print("Server: ");
                serverMessage = consoleInput.readLine();
                output.println(serverMessage);
                //we dont print server message in server console
                if (serverMessage.equals("exit")) {
                    System.out.println("Server disconnected");
                    break;
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
