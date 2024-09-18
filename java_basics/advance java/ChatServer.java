import java.io.*;
import java.net.*;
import java.util.*;
//run with ChatClient.java with split terminals
public class ChatServer {
    static final int PORT_NUM = 4445;
	public static void main(String[] args) throws IOException{
		Socket s=null;
		ServerSocket ss2=null;
        String str="",str2="";
        Scanner sc=null;
        DataInputStream din =null;
        DataOutputStream dout=null;
		System.out.println("Server is Ready");
        //creating socket at server
		try{
		    ss2=new ServerSocket(PORT_NUM);
            sc=new Scanner(System.in);
		}
		catch(Exception e){
		    System.out.println("Server error");
		}


        //accept connection from client
		try{
		    s=ss2.accept();
		    System.out.println("Request Accepted");
            din=new DataInputStream(s.getInputStream());
            dout=new DataOutputStream(s.getOutputStream());
		}
		catch(Exception e){
		    System.out.println("Connection Error");
		}



        try{
            while(true){
                str=din.readUTF();
                System.out.println("Received from client  ::"+str);
                if(str.equals("exit")||str.equals("stop")){
                    System.out.println("Server disconnecting");
                    break;
                }
                
                
                
                System.out.println("Server:");
                str2=sc.nextLine();
                System.out.println("Sent to server ::"+str2);
                if(str2.equals("exit")||str2.equals("stop")){
                    System.out.println("Client disconnecting");
                    break;
                }
                dout.writeUTF(str2);
                dout.flush();
            }
        }catch(Exception e){
            System.out.println("Server stopped ");
        }
        finally{
            sc.close();din.close();dout.close();s.close();
        }


	}

    
}
