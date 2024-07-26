//functional programming:functionas can be passed as parameters
//functional interface:an interface having only one method
@FunctionalInterface
interface SuperInterface{
    void interfaceMethod();
}
interface Walkable{
    int isWalkable(int steps);
}
public class oops7 {
    SuperInterface si=new SuperInterface() {

        @Override
        public void interfaceMethod() {

        }
        
    };
    //above code can be replaced by: following code

    //in previos example, the objected created above by anonymous class implementing an interface can be created by lambda exp[ression]
    SuperInterface si2=()->{

    };
    //since a single method is present, lambda will not provide any ambiguity to the compliler




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
        //lambda: (parameters with return types)->{implementation}
    }
}
