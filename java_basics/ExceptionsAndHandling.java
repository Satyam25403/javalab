//polymorphism:single action in different ways,allows to define one interface and multiple implementations

import java.util.Scanner;

//creating custom exceptions
class MyException extends Exception{
    public MyException(String message){
        super(message);
    }
}
public class ExceptionsAndHandling {
    //exception:an unexpected event that occures during program excecution and affects the flow of the program causing program to terminate abnormally
    //causes:invalid user input,device failure,loss of networkconnection,code errors,open unavailable file,physical limitations
    //two types:runtime-due to programming error(unchecked exceptions) checked at runtime(NullPointerException, array index out of bounds, ArithmeticException)
    //io exception(while dealing with secondary memory)-checked exception:checked at compile time and program is prompted to handle these( FileNotFoundException,trying to read past the end of a file)

    //if an exception occurs, 1.either show it in the method signature or 2.use try catch

    //throws:used to specify the possible exceptions that may occur in the method
    static int getNumberFromArray(int a[]) throws ArrayIndexOutOfBoundsException,ArithmeticException{
        return a[8];
    }
    //throw:to explicitly throw an exception contd, creating our own exception
    void divideByZero(){
        throw new ArithmeticException("trying to divide by zero");
    }

    public static void main(String[] args){


        int a[]=new int[5];
        //handling different exceptions seperately
        try{
            System.out.println(a[5]);
            //as soon as an exception occurs, it jumps to corresponding catch block and leaves rest of the statements in try block
            int result=5/0;
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("trying to acces an out of bound element");
        }catch(ArithmeticException e){
            System.out.println(e.getStackTrace());
            System.out.println(e);
            System.out.println(e.getMessage());
        }catch(RuntimeException e){
            //handle other exceptions:RuntimeException which is parent of all exceptions
            //runtimeException extends Exception class: hence to handle any exception use object of Exception class
        }
        System.out.println("Bye guys");





        // if we want to do same thing even irrespective of exception types we do:
        try{
            System.out.println(a[5]);

            int result=5/0;

        }catch(ArrayIndexOutOfBoundsException | ArithmeticException e){
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
                throw new MyException("More than 100 not allowed");
            }
        }catch(Exception e){
            System.out.println(e);
        }

    }




}
