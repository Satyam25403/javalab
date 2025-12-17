// Thread safety:
// code in java is said to be thread-safe if it guarantees that it does not give any unexpected result, any race condition wont occur
// when multiple threads access a particular block of code/that particular object: i.e. data corruption wont happen even if multiple threads are working
class Counter{
    private int count=0;

    //part of program where the shared resource is accessed and modified is called critical section
    public void increment(){
        count++;
    }

    public int getCount(){
        return count;
    }
}

class MyCustomThread extends Thread{
    private Counter counter;

    public MyCustomThread(Counter counter){
        this.counter = counter;
    }

    @Override
    public void run(){
        for(int i=0;i<1000;i++){
            counter.increment();
        }
        System.out.println("Final count: "+counter.getCount());
    }
}



public class Synchronization {
    public static void main(String[] args) {
        //we are taking many threads because On a modern JVM + CPU, the chance of lost updates is very low with just two threads...hence we increase workload
        //2 threads on some old machines may also show lost updates
        Counter counter=new Counter();
        MyCustomThread t1=new MyCustomThread(counter);
        MyCustomThread t2=new MyCustomThread(counter);
        MyCustomThread t3=new MyCustomThread(counter);
        MyCustomThread t4=new MyCustomThread(counter);
        MyCustomThread t5=new MyCustomThread(counter);
        MyCustomThread t6=new MyCustomThread(counter);

        //all threads share the same Counter instance
        t1.start();t2.start();t3.start();t4.start();t5.start();t6.start();

        try{
            //wait for all threads to finish
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
        }catch(Exception e){
            System.out.println(e);
        }

        //ideally final count should be 6000 
        System.out.println("Final count from main upon action of both threads: "+counter.getCount());

        //but due to race condition(sometimes counter being simultaneously updated by multiple threads), it may be less than 2000 
        //as some of threads at a time try to update the count variable simultaneously leading to lost updates
        //race condition occurs when multiple threads access shared data and try to change it at the same time and the final result depends on the order and timing of their execution

        //synchronized keyword makes sure only one thread can execute this method at a time
        //synchronize a method
        // public synchronized void increment(){
        //     count++;
        // }
        //synchronize a block of code
        // public void increment(){
        //     synchronized(this){
        //         count++;                 //only one thread can execute this block at a time(removing race condition): called mutual exclusion
        //     }
        // }

        //as soon as a thread enters a synchronized method/block, it acquires a lock(object level lock in this case) and no other thread can enter any synchronized method/block on the same object until the lock is released(when the thread exits the synchronized method/block)
        //Two types of locks: intrinsic locks (or monitor locks) and explicit locks
        //1.Intrinsic locks are built-in locks provided by Java. we dont see them but they are there.
        //when we use synchronized keyword, we are using intrinsic locks(automatic locks)
        //2.Explicit locks are provided by java.util.concurrent.locks package and give more control over locking mechanism: where we apply and release locks on resources manually
        //we can use ReentrantLock class from java.util.concurrent.locks package for explicit locks

        //by this u can avoid lost updates and final count will always be 6000

        
        //Drawbacks of intrinsic locks:
        //1. We dont know which thread holds the lock at a given time, who is waiting for the lock, who is waiting, who is updating the shared resource, how much duration a thread is aquiring lock on a resource etc.
        //we dont have control on any of these while using synchronized keyword
        //2. Potential for deadlocks: intrinsic locks can lead to deadlocks if not used
        //carefully, especially when multiple locks are involved
        //3. No fairness(starvation): intrinsic locks do not guarantee fairness in lock acquisition,
        //which can lead to thread starvation where some threads may wait indefinitely while others continuously acquire the
        //4. indefinite waiting: if a thread holding a lock is delayed or blocked for an extended period, other threads waiting for the lock may experience indefinite waiting, leading to performance issues.
        //5.No interruptible lock acquisition: threads waiting to acquire an intrinsic lock cannot be interrupted, which can lead to situations where a thread is blocked indefinitely if the lock is not released.
        //6.Read-write locks not supported: intrinsic locks do not support read-write locks, which allow multiple threads to read a resource concurrently while ensuring exclusive access for write operations.
        //synchronize cannot distinguish between read and write operations on a shared resource hence leading to unnecessary blocking.
    }

    //1. Object level lock: when a thread is executing a synchronized instance method/block,
    //   it acquires a lock on the instance(object) and no other thread can execute any synchronized instance method/block on the same object until the lock is released.
    //2. Class level lock: when a thread is executing a synchronized static method/block,
    //   it acquires a lock on the class itself and no other thread can execute any
    //   synchronized static method/block of the same class until the lock is released.
}
