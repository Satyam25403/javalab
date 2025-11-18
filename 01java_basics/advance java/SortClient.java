import java.io.*;
import java.net.*;
import java.util.*;

public class SortClient {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        try (Socket socket = new Socket("localhost", 12345)) {
            System.out.println("Connected to the server.");

            // Input and output streams for communication
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            System.out.println("Enter 10 numbers:");
            for(int i=0;i<10;i++){
                out.writeInt(sc.nextInt());
            }

            //control now transfers to server
            
            // Receive the sorted numbers from the server
            int[] sortedNumbers = new int[10];
            for (int i = 0; i < 10; i++) {
                sortedNumbers[i] = in.readInt();
            }

            // Print the sorted numbers
            System.out.println("Sorted numbers from server: ");
            for (int number : sortedNumbers) {
                System.out.print(number + " ");
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
