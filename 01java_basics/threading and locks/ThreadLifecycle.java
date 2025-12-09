
//Lifecycle of thread:
//1.New: thread created but not started     ex:NewThread thread=new NewThread();
//2.Runnable: After start method is called, thread becomes runnable and is waiting for CPU time         ex:thread.start();
//3.Running: Thread during execution
//4.Blocked/waiting: waiting for a resource/ waiting for another thread to perform an action
//5.Terminated: finished execution

//threadInstance.getState() returns State type having values in enum:(NEW,RUNNABLE,BLOCKED,WAITING,TIMED_WAITING(when a thread sleeps..it goes into this state),TERMINATED)

public class ThreadLifecycle extends Thread{
    @Override
    public void run(){
        System.out.println("RUNNING");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        ThreadLifecycle thread=new ThreadLifecycle();
        System.out.println(thread.getState());
        thread.start();
        System.out.println(thread.getState());


        Thread.sleep(100);      //give execution to other thread stop main thread for a while
        System.out.println(thread.getState());


        try {
            thread.join();          //since main thread is executing the code,...this is a way of telling the main thread to wait till execution of called thread 
            // and then resume/execute itself: main method waits for thread to finish its task
        } catch (InterruptedException e) {
            e.printStackTrace();
        }     
        //by here thread has done its work 
        System.out.println(thread.getState());
    }
}
