//there can be any number of classes in a file but only one public class
//a class is a template that must be implemented by its objects

class Complex{
    //attributes
    int real,imz;


    //a constructor cannot be abstract(empty without implementation),static(used without creating an instance) or final(cannot be changed)
    //constructor overloading
    public Complex(){
        System.err.println("printing default version of constructor that java provides");
        real=0;imz=0;
    }
    public Complex(int i){
        System.err.println("printing constructor with only real part");
        real=i;imz=0;
    }
    public Complex(int i,int j){
        System.out.println("creating complex number");
        real=i;imz=j;
    }
    //once we have made a constructor,default constructor provided by java no longer is valid, and produces an error
    //ex: if first constructor was not there, Complex c=new Complex(); will generate an error
    //also constructors can be over loaded but not be overridden(when inherited)



    //this keyword:used to differentiate class variables and local variables,used to remove ambiguity;
    //also used to refer to current object inside a method or a constructor;
    //also to invoke methods or constructor of current class; to access attributes of current class

    //use of this keyword and returning objects of the type class
    Complex add(Complex num){
        System.out.println(this);           //print the reference to current object
        this.print();
        return new Complex(this.real+num.real,this.imz+num.imz);
    }

    //methods
    void print(){
        System.out.println(real+"+i"+imz);
    }
}
class MethodOverLoading{

    //same name;different different in type,order and number of parameters;
    //changing return type is not example of method over loading
    void greet(){
        System.out.println("hello good morning");
    }

    void greet(String name){        
        System.out.println("hello "+name);
    }

    void greet(String name,int count){      //type
        for(int i=0;i<count;i++){
            System.out.println("hello "+name);
        }
    }

    void greet(int count,String name){      //order
        for(int i=0;i<count;i++){
            System.out.println("hello ji "+name+" ji");
        }
    }

    //METHOD OVERLOADING is a compile time polymorphism


}

public class oops {
    public static void main(String args[]){

        //NOTE:a class is not allocated memory when it is defined
        //an object is allocated memory when it is created-metaspace stores the metadata(data about data)of classes
        //obhects are allocated memory in heap
        Complex c=new Complex();        //creating an instance of complex-created only when new operator is used
        c.real=3;c.imz=4;
        c.print();


        //constructors are invoked implicitly when we instantiate objects-a consttructor does not have a return type
        //and has same name as the class name
        //when java default constructor is called instance variables are initialized with default values
        Complex num1=new Complex(5,6);
        num1.print();


        Complex res=num1.add(c);            //calls num1's add method 
        System.out.println(num1);           //prints the reference of num1 as class@reference
        res.print();


    }
}
