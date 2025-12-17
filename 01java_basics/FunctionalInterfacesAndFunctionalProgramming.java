//functional programming:functions can be passed as parameters

//functional interface:an interface having only one abstract method(which doesnt have a definition) ... ex: Runnable interface(has only run method that needs to be overriden by the class that implements it)
// - Can still have default or static methods.


@FunctionalInterface
interface SuperInterface{
    void interfaceMethod();
}

@FunctionalInterface
interface Walkable{
    int isWalkable(int steps);
}

public class FunctionalInterfacesAndFunctionalProgramming {
    public static void main(String[] args) {
        SuperInterface si=new SuperInterface() {

            @Override
            public void interfaceMethod() {
                //implementation
            }
        
        };
        //above code can be replaced by: following code

        //in previous example, the object that is created above by anonymous class implementing an interface can be created by lambda expression
        SuperInterface si2=()->{

        };

        //since a single method is present in functional interface, lambda will not provide any ambiguity to the compliler...it just implements the declared function
        //NOTE: lambda expression is an anonymous function...it has no name 
        // in lambda expressions curly braces used for multiple statements and then a return statement is required
        //in case of single statement,no need of return statement as above
        //lambda: (parameters)->{implementation}
        // - Enables functional programming style
        // - Used heavily in streams, event handling, and concurrency

        //example2:
        Runnable runnable=()->{
            System.out.println("Thread is running");
        };
        Thread t1=new Thread(runnable);
        t1.start();
        //so we have 3 ways to instantiate objects for a class implementing an interface:
        //1.on RHS create an object of the implementation class that implements the interface
        //2.create an anonymous inner class...like new Interface name(){
        //    //  logic
        // } 
        //3.directly use lambda expression (only if it has one abstract method) like the above example of Runnable
        //pass arguments into lambda, if the abstract method takes in parameters

        //more consise way is to omit the temporary reference of Runnable: since it is a functional interface
        //since lambda expression can be assigned to reference of any functional interfcae
        Thread t2=new Thread(()->System.out.println("Thread is running"));





    
        //here we are not instantiating the interface, we are creating anonymous child class for the interface that can be used only once
        Walkable walkable=(steps)->{
            System.out.println("walked "+steps+" steps");
            return steps;
        };
        //we cannot create multiple objects of this anonymous class
        walkable.isWalkable(3);



        
        //creating another object by providing alternate implementation
        Walkable wal=(steps)->2*steps;                  //single expression in lambda

        

    }
}
