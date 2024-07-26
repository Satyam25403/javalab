//object class:present in java.lang package.Every class directly or indirectly derived from object class.
//if class dont extend any other class, it is direct child class of Object;if extends another class ,then it is indirectly derived
//Object class acts as root of inheritance hierarchy in any java program
//some methods of Object class:getclass(),hashCode(),wait(),toString(),clone(),equals(),finalize(),notify(),notifyall()
class Car{
    String model;
    int year;
    public Car(String model,int year){
        this.model=model;
        this.year=year;
    }
    //we can override the toString method and return our own string representation of object


    //use this first by once running normally and once by uncommenting this method
    // public boolean equals(Object obj){
    //     Car that=(Car)obj;
    //     return (this.model.equals(that.model) && this.year==that.year);
    // }




    //contract between hashCode qand equals method is each object has its unique id;
    //equals method return true if objects are same ie references are of same object;
    //if equals method return true for both objects,their hashCodes must be same

    // @Override
    // public int hashCode(){
    //     //a random initial number
    //     int initialNumber=31;
    //     initialNumber+=year;
    //     initialNumber+=model.hashCode();
    //     return initialNumber;
    //     //i.e. if year and model are different, their generated hashcodes will be different
    // }

}


public class oops9 {
    public static void main(String[] args) {
        Car c=new Car("Honda",2022);
        System.out.println(c);          //implicitly call toString method of object class to give string representation of the object
        Car c1=new Car("Honda",2022);
        System.out.println(c==c1);          //since attributes same objects have to be same ...redundant objects should not be created , just a new reference should be created
        System.out.println(c.equals(c1));

        //override the equals method inside car class uncomment above lines and then run
        System.out.println(c.equals(c1));


        //generating hashcodes:default hashcodes will be different,but since we have overridden the method based on object parameters,
        //both will be same
        System.out.println(c.hashCode());
        System.out.println(c1.hashCode());
        
    }
    
}
