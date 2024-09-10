import java.io.*;
import java.net.*;
import java.util.*;
public class FactorialClient {
    public static void main(String[] args) throws UnknownHostException , IOException {
        Socket s= new Socket("localhost",4445);
        String str="";
        System.out.println("connected to server");
        Scanner sc=new Scanner(System.in);
        DataOutputStream dos=new DataOutputStream(s.getOutputStream());
        DataInputStream dis=new DataInputStream(s.getInputStream());
        while(true){
            System.out.println("Enter an Integer");
            str=sc.nextLine();
            System.out.println("Sent to server ::"+str);
            if(str.equals("exit")||str.equals("stop")){
                System.out.println("Client disconnecting");
                break;
            }
            dos.writeUTF(str);  //to write string in a modified utf-8 format to outputstream
            dos.flush();
            System.out.println("Received from server  ::"+dis.readUTF());
	    }
		s.close();dis.close(); dos.close();sc.close();
	}

}