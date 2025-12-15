import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BankAccount{
    private int balance=100;

    private final Lock lock=new ReentrantLock();     
    //LOCK METHODS:
    //lock.lock();   //(try to )acquire the lock...similar to synchronized block/method...keeps waiting until lock is acquired...and hence is not that useful in avoiding deadlocks
    //lock.unlock(); //release the lock
    //lock.tryLock(); //try to acquire the lock (instantaneously), if available and return true, else return false immediately
    //lock.tryLock(long timeout, TimeUnit unit);  TimeUnit.MILLISECONDS //try to acquire the lock within the given time, else return false
    //this method can throw InterruptedException since thread can be interrupted while waiting for the lock
    //lock.lockInterruptibly(); //acquire the lock unless the thread is interrupted
    //this method can throw InterruptedException since thread can be interrupted while waiting for the lock
    public BankAccount(int initialBalance) {
        this.balance = initialBalance;
    }
    
    public synchronized void deposit(int amount) {
        balance += amount;
    }
    
    public void withdraw(int amount) {
        System.out.println(Thread.currentThread().getName() + " is attempting to withdraw " + amount);
        if (lock.tryLock()) {    //try to acquire the lock if available
            System.out.println(Thread.currentThread().getName() + " acquired the lock...proceeding to withdrawl.");
            if(balance>=amount){
                try{
                    //Simulating some processing time
                    Thread.sleep(3000);
                    balance -= amount;
                    System.out.println(Thread.currentThread().getName() + " completed withdrawal. Remaining balance: " + balance);
                }catch(Exception e){
                    e.printStackTrace();
                    //simply logging the exception dont store the information that the thread was interrupted without finishing its work
                    Thread.currentThread().interrupt(); //restore the interrupted status/state of the thread...so that rest of code knows that the thread was interrupted
                    //this is a good practice to follow whenever InterruptedException is caught
                }finally{
                    // Ensure the lock is released even if an exception occurs
                    lock.unlock();
                    System.out.println(Thread.currentThread().getName() + " released the lock.");
                }
                
            } else {
                System.out.println("Insufficient funds");           //lock aquired but insufficient balance
                lock.unlock();   //release the lock
                System.out.println(Thread.currentThread().getName() + " released the lock.");
            }
   
        } else {
            System.out.println(Thread.currentThread().getName() + " could not acquire the lock and is skipping withdrawal.");
        }
    }
    
    public double getBalance() {
        return balance;
    }
}


public class explicitLocksAsSolutionToDeadlocksAndInfiniteWaiting {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(100);
        
        Runnable task=new Runnable() {      //since interface cant be instantiated, we create an anonymous class
            @Override
            public void run() {
                account.withdraw(25);
            }
        };

        Thread t1=new Thread(task,"Thread-1");      //takes Runnable object as parameter
        Thread t2=new Thread(task,"Thread-2");
        t1.start();
        t2.start();
        
    }
}

