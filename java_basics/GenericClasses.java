//wrapper classes(already present):class whose object wraps primitive datatypes,when an object is created to this class,
//it contains a field and in this field we can store primitive data types
//need or usage:data structures in collection framework store only objects and not primitive types

//AUTOBOXING:automatic conversion of primitive types to object of their corresponding wrapper classes
//UNBOXING:convert an object of wrapper class to its corresponding  primitive  type()


//Generics:parameterized tyepes.it is possible to create classes that work with different data types
//an entity (such as class,interface or method) that operates on a parameterized type is a generic entity
//primitive datatypes are not supported in generic classes only (Integer,String,Character etc wrapper classes are supported)
class GenericClass<T,E >{
    //if we want to use bounded generics for class:
    //class GenericClass<T extends Number,E extends someOtherDatatype>



    
    //note that T,E can be of any types i.e. can be of some custom class types also;
    //we have not restricted the types they hold

    private T id;
    E name;

    GenericClass(T id,E name){
        this.id=id;
        this.name=name;
    }

    public T display(){
        System.out.println(this.name);
        return this.id;
    }

    // GenericClass(T id,E name){
    //     this.id=id;
    //     this.name=name;
    // }
    // void display(){
    //     System.out.println(this.id +" "+this.name);
    // }

}
class Test{

    //similarly we can create generic methods:for creating generic methods we dont need a generic class
    //these can be used with any type of data
    //here the parameter <T> is inserted after the access modifier and before the return type
    public <T> void genericMethod(T data){
        System.out.println(data);
    }

}
//to restrict the types; we use concept of bounded generics
public class GenericClasses {

    public static void main(String[] args) {

        // Integer obj=new Integer(11) this method is depricated for removal
        Integer obj=Integer.valueOf(12);        //recommended;can also convert string into integer

        Integer obj1=12;                            //autoboxing
        System.out.println(obj1);

        int age=obj;                                //unboxing
        System.out.println(age);





        GenericClass<String,String> gc=new GenericClass<>("asdf12","leo");
        System.out.println(gc.display());

        GenericClass<String,Integer> gc1=new GenericClass<>("fries",123);
        System.out.println(gc1.display());

        GenericClass<Integer,String> gc2=new GenericClass<>(1234,"burger");
        System.out.println(gc2.display());
        

        Test t=new Test();
        //generic methods can be called by placing the actual type inside the angular brackets
        t.<String>genericMethod("javaprogramming");
        t.<Integer>genericMethod(12);




        //since doubledata is non static method, an object should be created to access its members
        GenericClasses obj2=new GenericClasses();
        obj2.doubleData("string");
        obj2.doubleData(341);
        obj2.doubleData(t);     //can also take a custom class type: here we have passed a Test type






    }
    <E> void doubleData(E data){
        //Ingeneral type parameter can accept any datatype(except primitive types)

        //BOUNDED GENERIC TYPES:we restrict only for some specific types for which the type should be bounded to
        //use extends keyword: ex-if number is used, only the datatypes that arer children of number are allowed
        System.out.println(data);
    }
    //example of Bounded generics
    <E extends Number> void doubleNumData(E data){
        //Ingeneral type parameter can accept any datatype(except primitive types)

        //BOUNDED GENERIC TYPES:we restrict only for some specific types for which the type should be bounded to
        //use extends keyword: ex-if number is used, only the datatypes that arer children of number are allowed
        System.out.println(data);
    }
}

