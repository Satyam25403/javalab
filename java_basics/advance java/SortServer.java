import java.io.*;
import java.net.*;
import java.util.Arrays;

public class SortServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server started. Waiting for a client...");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            // Input and output streams for communication
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            
            int[] numbers = new int[10];
            for (int i = 0; i < 10; i++) {
                numbers[i] = in.readInt();
            }
            System.out.println("Received numbers from the client" );

            // Sort the numbers
            Arrays.sort(numbers);

            // Send the sorted numbers back to the client
            for (int number : numbers) {
                out.writeInt(number);
            }

            System.out.println("output sent");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
