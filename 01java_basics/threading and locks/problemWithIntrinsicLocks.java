class BankAccount{
    private int balance=100;
    
    public BankAccount(int initialBalance) {
        this.balance = initialBalance;
    }
    
    public synchronized void deposit(int amount) {
        balance += amount;
    }
    
    public synchronized void withdraw(int amount) {
        System.out.println(Thread.currentThread().getName() + " is attempting to withdraw " + amount);
        if (amount <= balance) {
            System.out.println(Thread.currentThread().getName() + " is withdrawing " + amount);

            try {
                Thread.sleep(3000);    //if the costly operation fails/takes longer time here, the lock will be held for long time(maybe causing deadlocks) other threads may wait indefinitely...
                // we dont have control while using synchronized keyword
            } catch (InterruptedException e) {
                e.printStackTrace();
            } // Simulate delay for some db operations

            balance -= amount;
            System.out.println(Thread.currentThread().getName() + " completed withdrawal. Remaining balance: " + balance);
        } else {
            System.out.println("Insufficient funds");
        }
    }
    
    public double getBalance() {
        return balance;
    }
}


public class problemWithIntrinsicLocks {
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
