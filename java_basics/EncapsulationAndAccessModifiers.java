//package is a container that groups related classes, interfaces, enumerations and annotations
//i.e. in simple words Logical grouping of related classes/interfaces.
//java uses file system directories to store packages

//import statements are written directly after the package statement and before class definition

class Person{
    // ✅ Static Variables
    // - Belong to the class, not individual objects.(i.e. changes to this variable reflects accross all objects)
    // - Memory allocated at class loading time, not object creation.

    public static int no_of_instances=0;
    public Person(){
        no_of_instances++;                  //counts no of instances created, each time an object is created, modifies this variable of the classww
        //hence common to all instances of class
    }


    private int age;
    public static final String scientificName="homosapiens";        //generally modification of static variables is considered to be bad programming practice


    static int count=12;

    boolean canBeChanged=false;
    boolean canBeAccessed=true;
    //implementation of data hiding through encapsulation:getter and setter methods
    public void setAge(int age){
        //private attribbute can be accessed through this setter method which is public
        if(canBeChanged && age>0){
            this.age=age;
        }//we now have control on when to let user to set this age based on some condition
    }
    public int getAge(){
        if(canBeAccessed)
            return age;         //this allows access after passing specified checks
        return -1;
    }
    public static void printHello(){            //to access this just class name is suffecient
        System.out.println("hello");
        //print();                                nonstatic method in static entity is not allowed
    }
    void print(){                               //to access this object needs to be created
        System.out.println("hello");
        printHello();                           //static thing in non static method is allowed
    }

}
//static block runs even before the main method i.e. when classes are loaded

public class EncapsulationAndAccessModifiers {
    static{
        System.out.println("FROM STATIC BLOCK");
    }
    public static void main(String[] args) {
        System.out.println("FROM THE MAIN BLOCK");


        System.out.println(Person.count);
        Person.printHello();
        // Encapsulation:binding fields and methods inside single class;prevents other classes from accessing and changing
        // fields and methods;just a way to acheive data hiding
        //data hiding:restricting access to data members by hiding implementation details
        Person p=new Person();
        p.setAge(12);
        Person p1=new Person();


        System.out.println(Person.no_of_instances);;


        //if we want to acess class members without creating an instance of class?declare class members as static
        //static variables can be accessed by calling class name of class
        //static variables are class variables;a single copy is shared among all instances of class
        // instead of allowing each instance of class to have its own copy of this static variable
        System.out.println(p.count);
        System.out.println(p1.count);

        p.count=50;                 //only modifying count for p

        System.out.println(p.count);
        System.out.println(p1.count);       //count for both has been changed:
        //hence static is a class entity and shared commonly for all the instances that will be created

        //also memory for such variables is allocated at compile time when class is loaded into memory: and not at run time
        //static variables can be used in any type of methods:static or non static; 
        //but non static variables cant be used in static methods

        //NOTE:hence whenever methods are to be called directly from main method; declare them as static
        //this and super are not used in body of the static method

    }
    static{
        System.out.println("FROM SECOND STATIC BLOCK");
    }
    
}




//access modifiers: set visibility of classes,variables,methods,constructors,data members and setter methods


//                                             same package    same package    different package    same package
// access modifiers            same class       subclass       non-subclass        subclass          non-subclass

// default:(package private)       yes             yes             yes                 no                  no
//i.e. no access modifier provided
// private                         yes             no              no                  no                  no
//(only in that class of that package)
// protected                       yes             yes             yes                 yes                 no
//(accessible to subclasses even in other packages)  
// public                          yes             yes             yes                 yes                 yes
//(every where)