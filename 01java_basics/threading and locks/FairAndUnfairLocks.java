import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class UnfairLocksExample {
    private final Lock unfairLock = new ReentrantLock(); // unfair lock: may cause starvation: threads may wait indefinitely if other threads are continuously acquiring the lock

    public void accessResource() {
        unfairLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " acquired the unfair lock.");
            // Simulate some work with the resource
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt(); // restore the interrupted status/state of the thread
        } finally {
            System.out.println(Thread.currentThread().getName() + " released the unfair lock.");
            unfairLock.unlock();
        }
    }
}

class FairLocksExample {
    private final Lock fairLock = new ReentrantLock(true);      //fair lock: solves problem of starvation: longest waiting thread gets the lock next

    public void accessResource() {
        fairLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " acquired the fair lock.");
            // Simulate some work with the resource
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt(); // restore the interrupted status/state of the thread
        } finally {
            System.out.println(Thread.currentThread().getName() + " released the fair lock.");
            fairLock.unlock();
        }
    }
}

public class FairAndUnfairLocks {
    public static void main(String[] args) throws InterruptedException {
        //unfair lock example
        UnfairLocksExample unfairLocksExample = new UnfairLocksExample();
        Runnable task = new Runnable(){
            @Override
            public void run(){
                unfairLocksExample.accessResource();
            }
        };


        Thread t1=new Thread(task,"Thread-1");      //takes Runnable object as parameter
        Thread t2=new Thread(task,"Thread-2");
        Thread t3=new Thread(task,"Thread-3");
        t1.start();
        t2.start();
        t3.start();
        // //from above example we can see that lock acquisition order is not guaranteed...Thread-2 may acquire the lock before Thread-1 which started first...here we have no control over the order of lock acquisition
        //here we had just made sure that only one thread can access the resource at a time; a sample run output:
        // Thread-1 acquired the unfair lock.
        // Thread-1 released the unfair lock.
        // Thread-3 acquired the unfair lock.
        // Thread-3 released the unfair lock.
        // Thread-2 acquired the unfair lock.
        // Thread-2 released the unfair lock.

        Thread.sleep(5000);
        System.out.println();


        //Fair lock example
        FairLocksExample fairLocksExample = new FairLocksExample();
        Runnable task1 = new Runnable(){
            @Override
            public void run(){
                fairLocksExample.accessResource();
            }
        };
        Thread thread4=new Thread(task1,"Thread-4");      //takes Runnable object as parameter
        Thread thread5=new Thread(task1,"Thread-5");
        Thread thread6=new Thread(task1,"Thread-6");
        thread4.start();
        thread5.start();
        thread6.start();
    }
}
