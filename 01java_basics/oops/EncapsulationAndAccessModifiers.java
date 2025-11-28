package oops;
//package is a container that groups related classes, interfaces, enumerations and annotations
//i.e. in simple words Logical grouping of related classes/interfaces.
//java uses file system directories to store packages

//import statements are written directly after the package statement and before class definition

class Person{
    // ✅ Static Variables
    // - Belong to the class, not individual objects.(i.e. changes to this variable reflects accross all objects)
    //  also memory for such variables is allocated at compile time when class is loaded into memory: and not at run time(i.e not at object creation)
    //  static variables can be used in any type of methods: static or non static; 
    //  but non static variables cant be used in static methods
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

    //implementation of data hiding through encapsulation: getters and setters 
    public void setAge(int age){
        //private attribbute can be accessed through this setter method only which is public
        if(canBeChanged && age>0){
            this.age=age;
        }
    }
    public int getAge(){
        if(canBeAccessed)
            return age;         //this allows access after passing specified checks
        return -1;
    }

    void print(){                               //to access this object needs to be created
        System.out.println("hello");
        printHello();                           //static thing in non static method is allowed
    }

    // ✅ Static Methods
    // - Can access only static members.
    // - Cannot use this or super.
    // - Can be called using the class name directly...like Person.printHello()....just like we have Character.getNumericValue(char) 
    //  while .append() method of StringBuilder...etc object methods

    public static void printHello(){            //to access this just class name is suffecient
        System.out.println("hello");
        //print();                                nonstatic method in static entity is not allowed
    }

}

//static block runs when the class is loaded, even before the main method 
public class EncapsulationAndAccessModifiers {
    static{
        System.out.println("FROM STATIC BLOCK");
    }
    public static void main(String[] args) {
        System.out.println("FROM THE MAIN BLOCK");

        //if we want to access class members without creating an instance of class, declare class members as static
        //static variables can be accessed by calling class name of class

        System.out.println(Person.count);           //accessing Static variables
        Person.printHello();

        // Encapsulation:binding fields and methods inside single class and allowing fields modification through allowed methods only;
        //prevents other classes from accessing and changing fields
        //data hiding:restricting access to data members externally like object.fieldname=somevalue
        Person p=new Person();
        p.setAge(12);
        Person p1=new Person();

        System.out.println(Person.no_of_instances);;

        System.out.println(p.count);
        System.out.println(p1.count);

        p.count=50;                 //only modifying count for p

        System.out.println(p.count);
        System.out.println(p1.count);       //count for both has been changed
        System.out.println(Person.count);       //same for all three: recommended static variable access using ClassName and not ObjectName

        //NOTE:hence whenever methods are to be called directly from main method; declare them as static
        //this and super are not used in body of the static method

    }
    static{
        System.out.println("FROM SECOND STATIC BLOCK");
    }
    
}




//access modifiers: set visibility of classes,variables,methods,constructors,data members and setter methods


//                                             same package    same package    different package    different package
// access modifiers            same class       subclass       non-subclass        subclass          non-subclass

// default:(package private)       yes             yes             yes                 no                  no
//i.e. no access modifier provided
// private                         yes             no              no                  no                  no
//(only in that class of that package)
// protected                       yes             yes             yes                 yes                 no
//(accessible to subclasses even in other packages)  
// public                          yes             yes             yes                 yes                 yes
//(every where)