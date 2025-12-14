//multithreading is the concurrebt execution of two or more threads to maximize utilization of CPU: capabilities are part of java.lang
//in single thread environment, java's multi-threading is managed by JVM and OS which switch between threads: gives illusion of concurrency
//in multi-core environment, java's multi-threading can take full advantage of available cores: JVM distributes threads across cores allowing 
//true parallel execution of threads

//program is static, process is an instance...multiple instances(processes) can be created for a program....ex: opening chrome 3 times 
//java.lang.Thread class and java.lang.Runnable interface
//one thread at start: main thread responsible for main method execution

//to create a thread: 
// 1.extend Thread class 
class NewThread extends Thread{
    //giving custom name to thread other than Thread-0, Thread-1 etc by using constructor
    public NewThread(String name){
        super(name);
    }

    @Override
    public void run(){
        //infinite loops used to show that threads execute in random order independent of each other and not sequentially
        for(;;){        
            System.out.println("Hello: "+Thread.currentThread().getName());
        }
    }
}
//or 2.implement Runnable interface
class NewThread1 implements Runnable{
    @Override
    public void run(){
        for(;;){        
            System.out.println("World: "+Thread.currentThread().getName());
        }
    }
}
public class MultiThreading {
    public static void main(String[] args) {
        //main thread starts running
        System.out.println(Thread.currentThread().getName());

        //new created thread: method1
        NewThread thread=new NewThread("t1");
        thread.start();     //call the run method of thread: initiates a new thread

        //new thread: method2
        NewThread1 thread1=new NewThread1();
        Thread t1=new Thread(thread1);      //instance of the class i.e. defined needs to be passed
        t1.start();


        //thread methods
        //thread.start(), thread.join(), thread.getState(), thread.sleep(milliseconds), run(overriden method), Thread.currentThread().getName(),  Thread.currentThread().getPriority()
        //thread.setPriority(Thread.MIN_PRIORITY)  MIN_PRIORITY=1, NORM_PRIORITY=5, MAX_PRIORITY=10, thread.interrupt()...whatever the thread was doing, interrupt that task...just giving hint(scheduler may ignore it)
        //thread.yield() to give chance for other threads(ideally give equal chance to all)...indication that current thread is willing to yield its current use of processor to another thread...just giving hint(scheduler may chose to ignore it)

        //USER threads: we create/instantiate them and assign tasks like to perform functionality in the run method
        //DAEMON threads: threads running in background ex:java's garbage collector...main thing is JVM doesnt wait for DAEMON threads to finish execution...it only waits for completion of user threads

        //thread.setDaemon(true)...convert user thread to deamon thread(so that JVM dont wait for it to finish...as soon as user threads terminate, exit the process)

    }
}
