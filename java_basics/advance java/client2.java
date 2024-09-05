import java.io.*;
import java.net.*;
import java.util.*;
public class client2 {
public static void main(String[] args) throws UnknownHostException , IOException {
       Socket s= new Socket("localhost",4445);
       String str="";
       System.out.println("connected to server");
       Scanner sc=new Scanner(System.in);
       DataOutputStream dos=new DataOutputStream(s.getOutputStream());
       DataInputStream dis=new DataInputStream(s.getInputStream());
       while(!str.equals("stop"))
       {
        System.out.println("Enter an String");
         str=sc.nextLine();
	   System.out.println("Sent to server ::"+str);
	   dos.writeUTF(str);
	   dos.flush();
	    System.out.println("Received from server  ::"+dis.readUTF());
	}
		s.close();dis.close(); dos.close();
	}

}