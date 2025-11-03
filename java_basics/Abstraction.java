//abstract:dont have definition or body;abstract classes cant be instantiated;but we can create subclasses;
//members of abstract class can be accessed using object of subclass
//if a class has abstract method, all child classes inherited from abstract superclass must provide its implementation
//abstraction:allows to hide unneccessary details(implementation) show only needed information
abstract class Vehicle{
    abstract void accelerate();             //if we try to define a body,it produces an error
    abstract void brake();                  //these mudt be overridden by child class

    //abstract can also contain regular methods: but should have atleast one abstract method
    //alternatively if a method is to be declared abstract, its class must be declared abstract
    void honks(){
        System.out.println("vehicle honks");
    }//this can be ovverridden but not mandatorily

}
class Car extends Vehicle{
    //if this class is inherited; either this is to be declared abstract or should implement all the abstract methods
    //implementation of abstract methods
    @Override                   //this is an annotation generally used to check spelling of above abstract methods
    void accelerate() {
        System.out.println("Car is accelerating");
    }

    @Override
    void brake() {
        System.out.println("Car is breaking");
    }

    //regular method
    void accelerated(){
        System.out.println("hehe");
    }
    
    
}
public class Abstraction {
    public static void main(String[] args) {
        // Vehicle v=new Vehicle();             will produce an error
        Car c=new Car();
        c.accelerate();
        c.accelerated();
        c.brake();

    }
}
