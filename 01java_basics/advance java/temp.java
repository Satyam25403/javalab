import java.io.*;
import java.util.*;
import java.net.*;
public class temp {
    public static void main(String[] args) {
        try(DatagramSocket ds=new DatagramSocket(12345)){
            byte[] receive=new byte[1024];
            byte[] sendBuff;
            Scanner sc=new Scanner(System.in);


            while(true){
                DatagramPacket rec=new DatagramPacket(receive, receive.length);
                ds.receive(rec);
                String s=new String(rec.getData(),0,rec.getLength());
                System.out.println("Client:"+s);
                if(s.equals("exit")){
                    System.out.println("client stopped");
                    break;
                }


                InetAddress add=rec.getAddress();
                int port =rec.getPort();
                System.out.println("Server:");
                String gs2=sc.nextLine();
                sendBuff=gs2.getBytes();
                DatagramPacket dout=new DatagramPacket(sendBuff,sendBuff.length,add,port);
                ds.send(dout);
                if(gs2.equals("exit")){
                    System.out.println("server stopped");
                    break;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}