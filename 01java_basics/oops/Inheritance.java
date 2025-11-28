package oops;
//the class that is created from an existing(super,parent or base class) is called subclass(child or derived)

//multiple inheritance is not allowed in java ie one class cannot be derived from more than one classes
//reason is:there should be onlynone immediate parent for any class

import Car;

class vehicle{
    int WheelsCount;
    String model;

    vehicle(){
        System.out.println("created instance of vehicle class");
    }
    vehicle(int WheelsCount){
        this.WheelsCount=WheelsCount;
        System.out.println("created vehicle with wheels");
    }

    void start(){
        System.out.println("original start method");
    }

    final void accelerate(){

    }
}

class Car extends vehicle{

    String colour;
    
    Car(){
        super(3);           //to access the parameterized version of the constructor
        //NOTE: we cannot call another constructor in this because constructor call should be the first
        //statement in the constructor
        //super or this must be the first statement in the constructor i.e successively calling this ans super is error
        //ex:super(2)
        //this(3)               will produce error




        
        System.out.println("car is being created");
    }



    //method over riding:same function name,order,number and type of parameters;
    //signature of method should be same
    //method overriding is a runtime polymorphism which is acheived through inheritance
    void start(){



        //we cannot call a constructor in other methods either only one constructor can be called in
        //each constructor like shown above

        super.start();              //access instance of immediate parent(unparameterized version of constructor)
        System.out.println("cars overridden start method");




        //for suppose we want to access breaking method of the scooter class but we want reference of a car
        //this can be done by passing current instance that called the start method i.e. this
        Scooter s=new Scooter();
        s.breaking(this);

    }

    Car braking(){
        return this;                //returning objects from a method
    }


    // void accelerate(){                       we cannot over ride a method declared final

    // }

}

class Lamborghini extends Car{
    void start(){
        super.start();
        System.out.println("Lamorghini's overridden start method");
    }
}


class Scooter{
    void breaking(Car car){
        System.out.println(car.model+" is breaking");
    }
}


public class Inheritance {
    public static void main(String[] args) {
        Car car=new Car();          //even if instance of parent class is not created, when an object is created 
        car.model="1b45z";          //constructor of its parent class called implicitly
        car.WheelsCount=4;
        car.colour="red";
        car.start();

        Lamborghini l=new Lamborghini();
        l.start();


        //SUPER:used to refer to the instance of immediate parent class
        //refer the variables,invoke method or constructor of immediate parent class
        //also super cannot be used in static sense or method(like here in main method)
        
        
    }
    //NOTE: all classes in java are inherited from the object class which is already pre-defined
}


//this vs super

// this is implicit ref variable used to represent current class while super represents the parent class
// this invokes current class methods while super invokes immediate parent class methods(same is case for constructors)
// this refers to instance and static variables of current class while super accessess those for immediate parent class
// this:used to return and pass an argument to methods in context of current class object
// super:used to return and pass argument in context of immediate parent class object

//main difference lies in fact that this is physically an object hence can be passed to methods and be returned
//super cant do so









//final:entities that cannot be modified
//variables declared final cant be reassigned,methods declared final cant be overridden by its subclasses,classes declared final
//cannot be inherited


