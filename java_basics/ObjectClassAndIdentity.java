//object class (java.long.Object):present in java.lang package.Every class directly or indirectly derived from object class.
//if class dont extend any other class, it is direct child class of Object; if extends another class ,then it is indirectly derived



//Implementing Comparable and Overriding compareTo() method to tell the program how objects should be sorted
public interface Comparable<T>{
    int compareTo(T obj);
}


//Object class acts as root of inheritance hierarchy in any java program
//some methods of Object class: getclass(), for identity(hashCode(), equals()), Representation(toString()), cloning(clone()), finalization(finalize()), Synchronization(wait(), notify(), notifyall())

// cloning:Using clone() — Deep vs Shallow Copy
// Creates a copy of an object. But beware: default clone() is shallow.
// Shallow Copy:
// - Copies primitive fields and references
// - Referenced objects are shared, not duplicated
// Deep Copy:
// - Copies everything, including nested objects
// - Requires manual cloning of referenced fields





class Car implements Comparable<Car>{
    String model;
    int year;
    public Car(String model,int year){
        this.model=model;
        this.year=year;
    }

    //overriding the compareTo() method of comparable interface to define the sorting order of objects
    @Override
    public int compareTo(Car other) {
        return Integer.compare(this.year, other.year); // ascending by year
    }
    // - Return < 0 → this < other
    // - Return 0 → this == other
    // - Return > 0 → this > other
    //we can Use Comparator for flexible sorting (e.g., by model)



    //we can override the toString method and return our own string representation of object
    //default behaviour of toString() is - Returns: ClassName@HexHashCode
    @Override
    public String toString() {
        return model + " (" + year + ")";
    }


    //equals() method: - Compares references and not content
    //use this first by once running normally and once by uncommenting this method
    public boolean equals(Object obj){
        if (this == obj) return true;       // if both references point to the exact same object in memory.

        // - Prevents NullPointerException if obj is null.
        // - Ensures obj is of the exact same class as this.
        //- Type safety: avoids ClassCastException when casting.
        if (obj == null || getClass() != obj.getClass()) return false;
        Car that=(Car)obj;
        return (this.model.equals(that.model) && this.year==that.year);
    }


    //contract between hashCode qand equals method is each object has its unique id;
    //equals method return true if objects are same ie references are of same object;
    //if equals method return true for both objects, their hashCodes must be same
    @Override
    public int hashCode(){
        //a random initial number
        int initialNumber=31;
        initialNumber+=year;
        initialNumber+=model.hashCode();
        return initialNumber;
        //i.e. if year and model are different, their generated hashcodes will be different
    }

    //- Prefer Objects.equals() and Objects.hash() for null safety

}


public class ObjectClassAndIdentity {

    public static void main(String[] args) {
        Car c=new Car("Honda",2022);
        System.out.println(c);          //implicitly call toString method of object class to give string representation of the object if toString wasn't overriden
        Car c1=new Car("Honda",2022);
        System.out.println(c==c1);          //since both have same attributes, objects have to be same ...redundant objects should not be created , just a new reference should be created

        //calls default equals if not overriden
        System.out.println(c.equals(c1));

        //generating hashcodes:default hashcodes will be different,but since we have overridden the method based on object parameters,
        //both will be same
        System.out.println(c.hashCode());
        System.out.println(c1.hashCode());
        //- Always override equals() and hashCode() together to satisfy the contract between .equals() and .hashCode()
        
    }
    
}
