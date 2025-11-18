//polymorphism:single action in different ways,allows to define one interface and multiple implementations


import java.io.IOException;
import java.util.Scanner;
import java.util.logging.*;         //Logger
//exception:an unexpected event that occures during program excecution and affects the flow of the program causing program to terminate abnormally
//causes: invalid user input, device failure, loss of networkconnection, code errors, open unavailable file, physical limitations etc
//two types: 
//1.runtime-due to programming error(unchecked exceptions) ex:(NullPointerException, ArrayIndexOutOfBoundsException, ArithmeticException)
//2.checked exceptions(detected at compile time)(while dealing with secondary memory) program is prompted to handle these( FileNotFoundException,trying to read past the end of a file)


//creating custom exceptions(steps):
//1.Extend Exception (for checked) or RuntimeException (for unchecked)
//2.Call super(message) in constructor

class MyException extends Exception{
    public MyException(String message){
        super(message);
    }
}

public class ExceptionsAndHandling {

    //if an exception occurs, there are 2 ways to handle it: 1.either show it in the method signature or 2.use try catch


    //throws:used to specify the possible exceptions that may occur in the method
    static int getNumberFromArray(int a[]) throws ArrayIndexOutOfBoundsException, ArithmeticException{
        return a[8];
    }
    //throw:to explicitly throw an exception (builtin or custom(creating our own exception))
    void divideByZero(){
        throw new ArithmeticException("trying to divide by zero");
    }

    public static void main(String[] args){


        int a[]=new int[5];
        //handling different exceptions seperately
        //- Always catch specific exceptions first, then general ones (Exception, Throwable)
        try{
            System.out.println(a[5]);
            //as soon as an exception occurs, it jumps to corresponding catch block and leaves rest of the statements in try block
            int result=5/0;
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("trying to access an out of bound element");
        }catch(ArithmeticException e){
            System.out.println(e.getStackTrace());
            System.out.println(e);
            System.out.println(e.getMessage());
        }catch(RuntimeException e){
            //handle other exceptions:RuntimeException which is parent of all exceptions
            //runtimeException extends Exception class: hence to handle any exception use object of Exception class
            System.out.println("Some runtime exception occured");
        }
        System.out.println("Bye guys");





        // if we want to do same thing even irrespective of exception types we do:
        try{
            System.out.println(a[5]);
            int result=5/0;

        }
        //- Use multi-catch (catch (A | B e)) to reduce duplication: i.e. if u want to perform same action upon occurence of different exceptions
        catch(ArrayIndexOutOfBoundsException | ArithmeticException e){
            System.out.println(e.getStackTrace());
            System.out.println(e);
            System.out.println(e.getMessage());
        }catch(Exception e){
            //handle any exception
            System.out.println(e.getStackTrace());
            System.out.println(e);
            System.out.println(e.getMessage());
        }finally{
            //excecute whatever may happen:irrespective whether or not exception occurs 
            //good practice to use finally to write important cleanup code: close a file or connection
            System.out.println("I will run always");
            //some cases when finally doesnt excecute: System.exit() method, an exception occurs in finally block, death of thread
            // - finally always runs unless:
            // - System.exit() is called
            // - JVM crashes
            // - Exception in finally itself
        }




        //for throws block
        try{
            getNumberFromArray(a);
        }catch(Exception e){
            System.out.println("catched the exception "+e.getMessage());
        }



        Scanner sc=new Scanner(System.in);
        System.out.println("Enter age:");
        try{
            int age=sc.nextInt();
            sc.close();
            if(age>100){
                //using custom exceptions
                throw new MyException("More than 100 not allowed");
            }
        }catch(Exception e){
            System.out.println(e);
        }

        testLogger();

        //Chained Exceptions (initCause(), getCause())
        // Used to link one exception to another, preserving the original cause.
        // ✅ Example:
        try {
            throw new IOException("Disk error");
        } catch (IOException e) {
            RuntimeException re = new RuntimeException("Failed to read file");
            re.initCause(e); // chain the original exception
            throw re;
        }
        // - initCause(Throwable cause) → sets the cause
        // - getCause() → retrieves the original exception
        // Use Case: Wrapping low-level exceptions in higher-level domain-specific ones.
    }


    //we avoid System.out.println() in production. Use java.util.logging.Logger or frameworks like Log4j, SLF4J.
    private static final Logger logger = Logger.getLogger(ExceptionsAndHandling.class.getName());
    static void testLogger(){
        try{
            int x = 5/0;
        }catch(ArithmeticException e){
            logger.log(Level.SEVERE, "Division error", e);
        }
    }
    // Benefits:
    // - Structured logs
    // - Configurable output (file, console, remote)
    // - Severity levels: INFO, WARNING, SEVERE, etc.
}
