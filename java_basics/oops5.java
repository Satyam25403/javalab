//interface:fully abstract class:contains unimplemented methods;like abstract classes, interfaces cant be instantiated
//to use an interface, other class should implement it using the imeplements keyword
//interfaces are another way of achieving abstraction;

//MULTIPLE INHERITANCE is not allowed in java;but this can be overcome by interfaces
//one interface can implement two or more interfaces

interface Animal{
    

    //NOTE:all methods are implicitly public and all fields are implicitly public static final
    int legs=4;


    void eats();                //dont need to specify as abstract,by default methods in interface are public abstract
    void drinks();

    //default method in java:if we want to add a method to an interface,all classes must be individually modified to override
    //this new method, which can be hectic if large number of classes have implemented the interface
    //hence java allows to provide a default implementation of methods;and implementing classes can override,if they wish to
    default void walk(){
        System.out.println("Animal is walking");
    }
    
}
interface Human{
    void walk();
    void drinks();
}
class Monkey implements Animal,Human{

    @Override
    public void eats() {
        System.out.println("Monkey is eating");
    }

    @Override
    public void walk() {
        System.out.println("Monkey is walking");
    }

    public void drinks(){
        System.out.println("Monkey is drinking");
    }
    
}
public class oops5 {
    public static void main(String[] args) {
        //Animal a=new Animal();              //not allowed
        Monkey m=new Monkey();
        m.drinks();
        m.eats();
        m.walk();
        System.out.println(Animal.legs);
        // Animal.legs=4;           is not allowed as by default fields in interface are public static final
    }
}
