import java.io.*;
import java.util.*;
import java.net.*;
public class temp1{
public static void main(String[] args) {
    try(DatagramSocket ds=new DatagramSocket()){
        byte[] receive=new byte[1024];
        byte[] send;
        Scanner sc=new Scanner(System.in);
        InetAddress in=InetAddress.getByName("localhost");

        while(true){
            System.out.print("client: ");
                String cm = sc.nextLine();
                send = cm.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(send, send.length, in, 12345);
                ds.send(sendPacket);
                if (cm.equals("exit")) {
                    System.out.println("Server disconnected");
                    break;
                }DatagramPacket receivePacket = new DatagramPacket(receive, receive.length);
                ds.receive(receivePacket);
                String serverMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Server: " + serverMessage);
                if (serverMessage.equals("exit")) {
                    System.out.println("Server disconnected");
                    break;
                }


        }
    }catch(Exception e){
        e.printStackTrace();
    }
}
}