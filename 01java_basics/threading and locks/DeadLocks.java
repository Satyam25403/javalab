//Deadlocks occur when 4 conditions are finally met:
//1.Mutual exclusion: Only one thread can access a resource at a time
//2.Hold and wait:A thread holding at least 1 resource is waiting to acquire additional resources held by other threads
//3.No Preemption: Resources cannot be forcibly taken from threads holding them
//4.Circular wait: A set of threads is waiting for each other in a circular chain

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class SharedResource {
    private final Lock lock1 = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();

    public void methodA() {
        lock1.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " acquired lock1 in methodA");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + " waiting to acquire lock2 in methodA...");
            lock2.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " acquired lock2 in methodA");
            } finally {
                lock2.unlock();
                System.out.println(Thread.currentThread().getName() + " released lock2 in methodA");
            }
        } finally {
            lock1.unlock();
            System.out.println(Thread.currentThread().getName() + " released lock1 in methodA");
        }
    }

    public void methodB() {
        lock2.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " acquired lock2 in methodB");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + " waiting to acquire lock1 in methodB...");
            lock1.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " acquired lock1 in methodB");
            } finally {
                lock1.unlock();
                System.out.println(Thread.currentThread().getName() + " released lock1 in methodB");
            }
        } finally {
            lock2.unlock();
            System.out.println(Thread.currentThread().getName() + " released lock2 in methodB");
        }
    }
}
class ReentrantLockExample2{
    private final Lock lock = new ReentrantLock();

    public void outerMethod() {
        lock.lock();
        try {
            System.out.println("Outer method acquired the lock.");
            innerMethod();
        } finally {
            lock.unlock();
            System.out.println("Outer method released the lock.");
        }
    }
    public void innerMethod() {
        lock.lock();
        try {
            System.out.println("Inner method acquired the lock.");
        } finally {
            lock.unlock();
            System.out.println("Inner method released the lock.");
        }
    }
}

public class DeadLocks {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource(); // just an object/resource shared between threads

        Thread t1 = new Thread(() -> resource.methodA(), "Thread-1");
        Thread t2 = new Thread(() -> resource.methodB(), "Thread-2");

        t1.start();
        t2.start();

        //example2: demonstrate that each lock should be paired with an unlock otherwise the lock will not be released.
        //multiple locks on same resource by the same thread is allowed with ReentrantLock: one lock acquired followed by another lock acquisition and so on.
        // ReentrantLockExample2 example2 = new ReentrantLockExample2();
        // example2.outerMethod();



        //Two resources and two one acquired by each thread and each waiting for other to release
        // Pen pen=new Pen();
        // Paper paper=new Paper();

        // Thread thread1=new Thread(new Task1(pen, paper),"<First thread>");
        // Thread thread2=new Thread(new Task2(pen, paper),"<Second thread>");

        // thread1.start();
        // thread2.start();

    }
}
class Pen{
    public synchronized void writeWithPenAndPaper(Paper paper){
        System.out.println(Thread.currentThread().getName()+" is using pen "+this+" and trying to get hold of paper");
        paper.finishWriting();
    }
    public synchronized void finishWriting(){
        System.out.println(Thread.currentThread().getName()+" finished using pen "+this);
    }
}
class Paper{
    public synchronized void writeWithPaperAndPen(Pen pen){
        System.out.println(Thread.currentThread().getName()+" is using paper "+this+" and trying to get hold of pen");
        pen.finishWriting();
    }
    public synchronized void finishWriting(){
        System.out.println(Thread.currentThread().getName()+" finished using paper "+this);
    }
}
class Task1 implements Runnable{
    private Pen pen;
    private Paper paper;

    public Task1(Pen pen,Paper paper){
        this.pen=pen;
        this.paper=paper;
    }
    @Override
    public void run(){
        pen.writeWithPenAndPaper(paper);
    }
}
class Task2 implements Runnable{
    private Pen pen;
    private Paper paper;

    public Task2(Pen pen,Paper paper){
        this.pen=pen;
        this.paper=paper;
    }
    @Override
    public void run(){
        paper.writeWithPaperAndPen(pen);
    }
}