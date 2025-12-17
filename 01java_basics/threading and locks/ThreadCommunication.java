// without proper communication, threads might end up in ineffecient busy-waiting states, leading to wastage of CPU resources and potential deadlocks
// if threads dont communicate about their progess, their completions etc, CPU will check that for all threads unnecessarily

// some methods for thread communication:
// wait(): tells current thread to release lock and wait until some other thread runs notify()/notifyAll(),
// notify(): wakes up a single thread that is waiting...i.e. tell it to stop waiting,
// notifyAll(): wakes up all threads that are waiting
// these can only be called in synchronized blocks

class SharedResource{
    private int data;
    private boolean hasData;

    public synchronized void produce(int value){
        //if data already there in buffer, wait
        while(hasData){
            try{
                wait();
            }catch(Exception e){
                e.printStackTrace();
                Thread.currentThread().interrupt();     //to restore its state
            }
        }
        data=value;             //set field for consumer to consume later
        hasData=true;           //for the consumer to tell it data has been generated
        System.out.println("Produced: "+value);
        notify();
    }

    public synchronized int consume(){
        while(!hasData){        //if there is no data...wait
            try{
                wait();
            }catch(InterruptedException e){
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        hasData=false;          //data consumed
        notify();
        System.out.println("Consumed: "+data);
        return data;
    }
}
class Producer implements Runnable{
    private SharedResource resource;

    public Producer(SharedResource resource){
        this.resource=resource;
    }

    public void run(){
        for(int i=0; i<10; i++){
            resource.produce(i);
        }
    }
}
class Consumer implements Runnable{
    private SharedResource resource;

    public Consumer(SharedResource resource){
        this.resource=resource;
    }

    public void run(){
        for(int i=0; i<10; i++){
            int value=resource.consume();
        }
    }
}
public class ThreadCommunication {
    public static void main(String[] args) {
        SharedResource resource=new SharedResource();
        Thread producerThread=new Thread(new Producer(resource));
        Thread consumerThread=new Thread(new Consumer(resource));

        producerThread.start();
        consumerThread.start();

        //we want to demonstrate thread communication: we want producer to produce data(but if hasData field is true, it should wait)
        //and consumer to consume data when hasData field is true
        //in this way, producer will communicate to consumer as soon as it produces data and consumer communicates to producer as soon as it has consumed data

        //how notify works?
        //the lock has been acquired on the sharedresource...so when notify runs, the threads that are waiting to access/acquire lock on the shared resource, those threads will get notified
        //that it has finished its work and they can start acquiring....if consumers/waiting threads are more, we can use notifyAll()
    }
}
