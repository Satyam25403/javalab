
// - Default values (when declared as class members):
// - boolean: false
// - byte, short, int, long: 0
// - float: 0.0f
// - double: 0.0d
// - char: '\u0000' (null character)

// - Wrapper classes: Each primitive has an object counterpart: 
// | Primitive | Wrapper Class | 
// |---------- |---------------| 
// | int       | Integer       | 
// | char      | Character     | 
// | boolean   | Boolean       | 
// | byte      | Byte          | 
// | short     | Short         | 
// | long      | Long          | 
// | float     | Float         | 
// | double    | Double        |

public class DataTypes{
    public static void main(String[] args) {

        System.out.println("Heloo people");
        //other than "_"  java supports "$" into variable name
        
        boolean $varib$all=true;     //size:1 bit                                        
        byte c=23;                   //size:1 byte         from -128    to   127                           
        short d=3200;                //size:         from  -32768    to 32767     
        int e=89364;                 //size:         from   -2147483648     to   2147483647   
        long f=83964218;             //size:         from    -9223372036854775808    to   9223372036854775807   
        float g=3.14f;              //f is mandatory to specify float  fractional numbers suffecient to store 6 to 7 decimal digits   
        double h=2.0004;             //size:         from    fractionalnumbers  to store 15 decimal digits     
        char i='d';                  //size:         from     single characters/letter /ascii values
        
        long a;           // default value is 0
        long b = 0;       // valid
        long k = 0L;      // also valid, preferred for clarity


        // - Autoboxing & Unboxing:
        Integer x = 5; // autoboxing(wrapper class)
        int y = x;     // unboxing



        //strict rule:  ' 'used for characters ;    " "used for strings

        //for multi line comments /* are used */

    
        //TYPECONVERSIONS: widening(implicit)=smaller to larger ;
        int one=10, two=20;
        long res=one+two;
        System.out.println(res);

        //narrowing(explicit)=larger to smaller type   {casting}        a.k.a lossy conversion like in below example
        byte a1 = 10, b1 = 20;
        byte sum = (byte)(a1 + b1); // a+b promotes to int
        System.out.println(sum);
        //- a and b are both byte types.
        // - When you do a + b, Java automatically promotes both operands to int before performing the addition.
        // - This is part of Java's binary numeric promotion rules.
        // - So a + b becomes an int result (30 in this case).
        // - You’re trying to store that int result into a byte variable (sum), which is not allowed implicitly because it could lose data.
        // - So you need to explicitly cast the result back to byte: byte sum = (byte)(a + b);

        //Why does Java promote to int?
        // Because:
        // - Java promotes all arithmetic involving byte, short, or char to int to avoid overflow and ensure consistent behavior.
        // - Even if both operands are byte, the result is int.
        byte a2 = 100;
        byte b2 = 30;
        byte sum2 = (byte)(a2 + b2); // 130 → overflow → -126

    }

}


//java has 50 keywords use camelcase convention for variable and method names: camelCase
//class names start with capital: CamelCase
