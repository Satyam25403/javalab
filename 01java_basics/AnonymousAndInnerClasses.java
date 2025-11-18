// Anonymous Classes
// - Unnamed inner classes created on-the-fly(nested class)
// - anonymous classes usually Used to 1.extend subclasses or 2.implement interface for one-time use.
// - Cannot be reused or instantiated again.

class Outer{
    public void outerMethod(){

    }
}//anonymous classes can only be created when subclasses were created


//functional interface:interface having only one method
interface SuperInterface{
    void interfaceMethod();
}

class AnonymousClass{
    // class Inner extends Outer{
    //                                      there is no need to make a child class and name it instead we can do
    // }

    //METHOD 1:anonymous class extending a class
    Outer obj=new Outer(){                          //here it may seem that object for parent class is being created but object for child classs is created actually
        //this is anonymous child class of Outer class:this can only be used once
        void sing(){
            System.out.println("Singing...");
        }
        @Override
        public void outerMethod(){
            System.out.println("Overridden outerMethod");
        }
    };      


    //METHOD 2:anonymous class implementing an interface
    SuperInterface si=new SuperInterface() {
        @Override
        public void interfaceMethod() {
            System.out.println("Implemented anonymously");
        }
    };

    //- Replaced by lambda expressions when using functional interfaces.

}





public class AnonymousAndInnerClasses {
    //inner classes:two types
    //1.(non-static class within a class: INNER CLASS):need to instantiate outer class to instantiate inner class
    // Has access to outer class members
    //2.(static class within other class is called NESTED CLASS):- Can be instantiated independently
    // cannot access instance members of outer class


    int outerPrice = 100; // instance member of outer class

    //non-static: inner class
    // we dont need to create instance of outer class
    class InnerClass{
        int price;
        void showPrice() {
            System.out.println("Accessing outerPrice from InnerClass: " + outerPrice);
        }
    }

    //static: nested class
    static class Playstation{
        int price;
        //static nested class cannot access member variables of outer class
        void showPrice() {
            // System.out.println("Accessing outerPrice from Playstation: " + outerPrice); ❌ Compile-time error
            System.out.println("Static nested class can't access outerPrice directly");
        }
    }

    public static void main(String[] args) {
        //inner class object instantiation preceded by outer class instantiation
        AnonymousAndInnerClasses obj=new AnonymousAndInnerClasses();
        InnerClass ic=obj.new InnerClass();
        ic.price=45;
        ic.showPrice();

        //nested:or static inner class dont need instance of outer class
        Playstation ps=new AnonymousAndInnerClasses.Playstation();      //just need to be instantiated like some fully qualified name
        ps.showPrice();             // Cannot access outerPrice...if tries to access, throws error, hence that line in implementation is commented

    }
}
