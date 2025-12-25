// Thread pooling: collection of pre-initialised threads that are ready to perform a task, instead of creating threads each time we need to perform an action
// Why?
//1.Resource management: each time creation and destruction of thread for performing each action is an overhead..so we use already initialized threads instead of creating new ones
//2.Response time: since we dont need to create threads each time from scratch, time is saved
//3.Control over thread count: we can keep track of number of threads


//Executors Framework: simplifies development of concurrent applications by abstracting away many of the complexities involved in creating and managing threads
//we dont manually create threads and manage them
//Problems prior to executors framework:
//1.Manual thread management
//2.Resource management
//3.Scalability
//4.Thread reuse
//5.Error Handling

//3 core interfaces of Framework: Executor, ExecutorService, ScheduledExecutorService

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolingAndExecutorsFramework {
    public static void main(String[] args) {
        // 1. analyze time when things go synchronously: one after other factorial computation...sample run:10102 ms(10 secs)
        Long startTime=System.currentTimeMillis();
        for(int i=1;i<=10;i++){
            //let us say we are performing a computationally heavy task for 10 times
            System.out.println(factorial(i));
        }
        System.out.println("Time taken: "+(System.currentTimeMillis()-startTime));

        //2.Use threads: multi threading and analyze time...sample run:1090ms(1 secs)
        Thread[] threads=new Thread[10];
        Long startTime2=System.currentTimeMillis();
        for(int i=1;i<=10;i++){
            int finalI=i;       //so that a lately starting thread dont read the changing value of i in loop
            threads[i-1]=new Thread(
                ()->System.out.println(finalI+" factorial: "+factorial(finalI))
            );
            threads[i-1].start();
        }
        //wait for threads to end execution
        for(Thread thread: threads){
            try{
                thread.join();
            }catch(Exception e){
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Time taken: "+(System.currentTimeMillis()-startTime2));

        //3. Use executors framework: just give what to do(business logic) and reuse threads
        Long startTime3=System.currentTimeMillis();
        ExecutorService executor=Executors.newFixedThreadPool(5);      //means create a pool of this number of threads
        for(int i=1;i<=10;i++){
            int finalI=i;    
            //just give business logic to submit method(takes in runnable)  
            executor.submit(()->System.out.println(finalI+" factorial: "+factorial(finalI)));               //returns Future<?> type

            //submit: takes in either Runnable(no returntype: run() method, exception needs to be caught by try-catch)/Callable(any returntype(int/String etc): call method, throws exception in signature itself) for place of lambda function
            //another signture of submit() allows to return something(custom) in case of successful execution...which will be accessed by future.get()
            //executor.submit() returns a Future<?> type which can be used to:
            // future.get(); future.get(timeout, timeUnit);
            // future.isDone();         //wether completed task or not
            // future.cancel(interrupt running? boolean);         //stop task
            // future.isCancelled();
            //some other methods of executor: executor.shutdownNow(), executor.isShutdown(), executor.isTerminated()...whether assigned tasks were completed after shutdown
            //executor.invokeAll(Collection of callables(tasks i.e. lambdas), timeout: optional): blocks main thread till tasks are completed and returns a collection of Futures 
        }
        executor.shutdown();            //to shutdown the pool...because in future we can use the pool again if needed
        
        //means it stops accepting new tasks submitted...u cant submit new task after this...and it has started orderly shutdown of pool
        //but before it shuts down it makes sure, that previously assigned tasks were completed...but that doesnt mean that lines in main thread after this wont execute till this shutdown happens
        // System.out.println("Time taken: "+(System.currentTimeMillis()-startTime))        //this line after that executor.shutdown() may execute even before the executor shutdown

        //so inorder to make sure the above line runs only after shutdown and not before it, we can do something like
        try {
            //Blocks until all tasks have completed execution after shutdown request or
            //timeout occurs or
            //the current thread is interrupted...whichever happens first so uncomment this line
            // executor.awaitTermination(100, TimeUnit.SECONDS);

            //if you want to wait till shutdown happens no matter upto what time
            while(!executor.awaitTermination(1, TimeUnit.SECONDS)){
                System.out.println("Waiting...");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Time taken: "+(System.currentTimeMillis()-startTime3));
    }

    static long factorial(int n){
        try{
            Thread.sleep(1000);     //to simulate heavy task:taking longer time
        }catch(Exception e){
            Thread.currentThread().interrupt();
        }
        long result=1;
        for(int i=2;i<=n;i++){
            result*=i;
        }
        return result;
    }
}
