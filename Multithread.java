import java.io.*;
class NewThread implements Runnable{
    String name,fname;
    Thread t;
    public NewThread(String n,String fn){
        name=n;
        fname=fn;
        //thread constructor
        t=new Thread(this,name);
    }
    public void run(){
        try{
            try{
                BufferedReader br=new BufferedReader(new FileReader(fname));
                String s=br.readLine();
                while(s!=null){
                    System.out.println(name+":"+s);
                    s=br.readLine();
                    Thread.sleep(1000);
                }
            }
            catch(IOException e){
                System.out.println("io error");
            }
        }
        catch(InterruptedException e){
            System.out.println("Interrupted");
        }
    }
}public class Multithread {
    public static void main(String args[]){
        NewThread n=new NewThread("thread1", "personal record.txt");
        NewThread n1=new NewThread("thread2", "Academic record.txt");
        n.t.start();
        n1.t.start();
    }
}
