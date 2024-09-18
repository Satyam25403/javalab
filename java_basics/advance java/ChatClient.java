import java.io.*;
import java.net.*;
import java.util.*;
//run with ChatServer.java with split terminals
public class ChatClient {
    public static void main(String[] args) throws UnknownHostException , IOException {
        Socket s= new Socket("localhost",4445);
        String str="";
        System.out.println("connected to server");
        Scanner sc=new Scanner(System.in);
        DataOutputStream dos=new DataOutputStream(s.getOutputStream());
        DataInputStream dis=new DataInputStream(s.getInputStream());
        while(true){
            System.out.println("Client:");
            str=sc.nextLine();
            System.out.println("Sent to server ::"+str);
            if(str.equals("exit")||str.equals("stop")){
                System.out.println("Client disconnecting");
                break;
            }
            dos.writeUTF(str);  //to write string in a modified utf-8 format to outputstream
            dos.flush();


            
            String str2=dis.readUTF();
            System.out.println("Received from server  ::str2");
            if(str2.equals("exit")||str2.equals("stop")){
                System.out.println("Client disconnecting");
                break;
            }
	    }
		s.close();dis.close(); dos.close();sc.close();
	
    }

    
}
