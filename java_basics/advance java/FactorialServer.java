import java.io.*;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
public class FactorialServer{
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
		catch(IOException e)
		{
		    e.printStackTrace();
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
		    e.printStackTrace();
		    System.out.println("Connection Error");
		}



        try{
            while(true){
                str=din.readUTF();
                if(str.equals("exit")||str.equals("stop")){
                    System.out.println("Server disconnecting");
                    break;
                }
                System.out.println("Received from client  ::"+str);
                try{
                    str2=Integer.toString(fact(Integer.parseInt(str)));
                }
                catch(NumberFormatException e){
                    str2="Invalid input.Please enter a valid input";
                }
                dout.writeUTF(str2);
                dout.flush();
            }
        }catch(IOException e){
            System.out.println("Server stopped ");
        }
        finally{
            sc.close();din.close();dout.close();s.close();
        }


	}

    static int fact(int n){
        if(n==0){
            return 1;
        }
        else{
            return n*fact(n-1);
        }
    }
}

     

