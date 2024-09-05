/*Design a multi threaded Client/Server application with TCP stream sockets.*/
import java.io.*;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
public class MultiClientServer
{
    static final int PORT_NUM = 4445;
	public static void main(String[] args) {
		Socket s=null;
		ServerSocket ss2=null;
		System.out.println("Server is Ready");
		try{
		    ss2=new ServerSocket(PORT_NUM);
		}
		catch(IOException e)
		{
		    e.printStackTrace();
		    System.out.println("Server error");
		}
		while(true)
		{
		    try{
		        s=ss2.accept();
		        System.out.println("Request Accepted");
		        ServerThread st=new ServerThread(s);
		        st.start();
		    }
		    catch(Exception e){
		        e.printStackTrace();
		        System.out.println("Connection Error");
		    }
		}
	}
}
class ServerThread extends Thread {
    String str="",str2="";
    int fa=0;
    Scanner sc=null;
    DataInputStream din =null;
    DataOutputStream dout=null;
    Socket s=null;
    public ServerThread(Socket s){
        this.s=s;
    }
    public void run()
    {
        try{
            din=new DataInputStream(s.getInputStream());
            dout=new DataOutputStream(s.getOutputStream());
            sc=new Scanner(System.in);
        }
        catch(IOException e)
        {
            System.out.println("IO error in server thread");
        }
        try{
            System.out.println("A Thread Created");
            while(!str.equals("stop"))
            {
                str=din.readUTF();
                System.out.println("Received from client  ::"+str);
                str2=Integer.toString(ServerThread.fact(Integer.parseInt(str)));
                dout.writeUTF(str2);
                dout.flush();

            }
        }
        catch(IOException e){
            str=this.getName();
            System.out.println("IO Error/ Client "+str+" terminated abruptly");
        }
        catch(NullPointerException e){
            str=this.getName();
            System.out.println("Client "+str+" Closed");
        }
        finally{
            try{
                System.out.println("Connection Closing..");
                if(din!=null){
                    din.close();
                    System.out.println("Socket Input Stream Closed");
                }
                if(dout!=null){
                    dout.close();
                    System.out.println("Socket out Closed");
                }
                if(s!=null){
                    s.close();
                    System.out.println("Socket Closed");
                }
            }
            catch(IOException ie){
                System.out.println("Socket Close Error");
            }
        }
        
    }
     static int fact(int n)
        {
            if(n==0)
            {
                return 1;
            }
            else 
            {
                return n*fact(n-1);
            }
        }
}
