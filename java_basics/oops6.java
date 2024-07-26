//in java it is possible to create a nested class without giving it any name:Anonymous class
//anonymous classes usually extend subclasses or implement interfaces

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

    //METHOD 1:anonymous class extending its super class
    Outer obj=new Outer(){                          //here it may seem that object for parent class is being created but object for child classs is created actually
        //this is anonymous child class of Outer class:this can only be used once
        void sing(){

        }
        public void outerMethod(){

        }
    };      //this is important syntax while writing anonymous classes


    //METHOD 2:anonymous class implementing an interface
    SuperInterface si=new SuperInterface() {

        @Override
        public void interfaceMethod() {

        }
        
    };
}














public class oops6 {
    //inner classes:two types
    //(non-static class within a class:INNER CLASS):need to instantiate outer class to instantiate inner class
    //(static class within other class is called NESTED CLASS):cannot access member variables of outer class
    //we dont needto create instance of outer class
    class InnerClass{
        int price;
    }

    static class Playstation{
        int price;
        //static nested class cannot access member variables of outer class
    }

    public static void main(String[] args) {
        //inner class:non static
        oops6 obj=new oops6();
        InnerClass ic=obj.new InnerClass();
        ic.price=45;

        //nested:or static inner class dont need instance of outer class
        Playstation ps=new oops6.Playstation();
    }
}
