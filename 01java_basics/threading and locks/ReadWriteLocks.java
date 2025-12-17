//synchronise can't distinguish between read and write operations which results in unnecessary blocking of read operations
//this can be improved using ReadWriteLocks which allows multiple threads to read simultaneously as long as no thread is writing
import java.util.concurrent.locks.*;

// - Writers block all readers.
// - Readers can run concurrently when no writer is active.

class SharedResource {
    private int count = 0;
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock(true); // from this we can get read and write locks
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    static int countOfReadLock=0;
    static int countOfWriteLocks=0;

    // Read operation
    public int getCount() {
        readLock.lock(); // acquire read lock: multiple threads can acquire this readLock(but only when
        // no thread has acquired writeLock at that time)
        System.out.println(Thread.currentThread().getName() + " has acquired the read lock");

        try {
            return count;
        } finally {
            System.out.println(Thread.currentThread().getName() + " has released the read lock");
            rwLock.readLock().unlock(); // release read lock
        }
    }

    // Write operation
    public void setCount(int value) {
        writeLock.lock(); // acquire write lock: only one thread can acquire, that too when neither
        // readLock nor writeLock has been acquired
        System.out.println(Thread.currentThread().getName() + " has acquired the write lock");
        try {
            count = value;
            Thread.sleep(2000); // cpu thinks it can give chance to other threads and tries to give...but there
            // is a lock...hence it can allocate to
            // other thread only after the current thread woke and has released lock
            System.out.println(Thread.currentThread().getName() + " Finished writing");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + " has released the write lock");
            rwLock.writeLock().unlock(); // release write lock
        }
    }
}

public class ReadWriteLocks {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        // Reader thread
        Runnable readTask = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + " read: " + resource.getCount());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };

        // Writer thread
        Runnable writerTask = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    int value = i * 10;
                    resource.setCount(value);
                    System.out.println(Thread.currentThread().getName() + " written value : " + value);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };

        Thread reader1 = new Thread(readTask);
        Thread reader2 = new Thread(readTask);
        Thread writer = new Thread(writerTask);

        // Start threads
        writer.start();
        reader1.start();
        reader2.start();

        // Wait for threads to finish
        try {
            writer.join();
            reader1.join();
            reader2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Final count: " + resource.getCount());
    }

    // - Reader prints while writer is sleeping
    // Thread-1 read: 10 appears after the writer acquired the lock.
    // How? Because that reader had already acquired the read lock before the writer
    // got in.
    // - Readers that already hold the read lock are allowed to continue.
    // - The write lock blocks new readers, but doesn’t cancel or preempt existing
    // ones.

    // fair lock ensures writers don’t starve and readers don’t sneak in once a writer is queued...hence u can pass a true: argument in ReentrantReadWriteLock to make it fair
}
