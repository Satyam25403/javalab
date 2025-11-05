//interface:fully abstract class:contains unimplemented methods(atleast one);like abstract classes, interfaces can't be instantiated: they can just hold the reference to its implemented childclass object
//to use an interface, other class should implement it using the imeplements keyword
//interfaces are another way of achieving abstraction; Used to define contracts that implementing classes must fulfill

//MULTIPLE INHERITANCE is not allowed in java;but this can be overcome by interfaces
//one interface can implement two or more interfaces

interface Animal{
    

    //NOTE:all methods are implicitly public abstract and all fields are implicitly public static final
    int legs=4;
    // - Shared across all implementations.
    // - Cannot be modified (final)
    // - Accessed via interface name: Animal.legs


    void eats();               
    void drinks();

    //default method in java:if we want to add a method to an interface, all classes must be individually modified to override
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

    // - Monkey must implement all abstract methods from both interfaces.
    // - If both interfaces have a method with the same signature (like walk()), the class must override it to resolve ambiguity.

    @Override
    public void eats() {
        System.out.println("Monkey is eating");
    }

    //why overriding walk is mandatory? because walk has a default implementation in Animal interface and is declared as abstract method in Human interface
    //Hence to avoid conflict/ambiguity, implementing class must override this method
    @Override
    public void walk() {            
        System.out.println("Monkey is walking");
    }

    //drinks also is a common method: but has no ambiguity unlike in case of walk() which does has
    public void drinks(){
        System.out.println("Monkey is drinking");
    }
    
}

public class InterfacesAndInheritence {
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
