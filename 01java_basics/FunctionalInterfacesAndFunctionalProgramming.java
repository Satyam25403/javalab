//functional programming:functionas can be passed as parameters

//functional interface:an interface having only one method
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




    public static void main(String[] args) {
        //here we are not instantiating the interface, we are creating anonymous child class for the interface that can be used only once
        Walkable walkable=(steps)->{
            System.out.println("walked "+steps+" steps");
            return steps;
        };
        //we cannot create multiple objects of this anonymous class
        walkable.isWalkable(3);



        
        //creating another object by providing alternate implementation
        Walkable wal=(steps)->2*steps;                  //single expression in lambda

        //NOTE: in lambda expressions curly braces used for multiple statements and then a return statement is required
        //in case of single statement,no need of return statement as above
        //lambda: (parameters)->{implementation}
        // - Enables functional programming style
        // - Used heavily in streams, event handling, and concurrency

    }
}
