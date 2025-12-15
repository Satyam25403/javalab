
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

            System.out.println(Thread.currentThread().getName() + " waiting for lock2 in methodA...");
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

            System.out.println(Thread.currentThread().getName() + " waiting for lock1 in methodB...");
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
        ReentrantLockExample2 example2 = new ReentrantLockExample2();
        example2.outerMethod();

    }
}