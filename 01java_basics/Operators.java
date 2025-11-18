
import java.util.Scanner;
//operators:arithmetic(+,-,*,/,%),assignment(=,+=,-=,*=,/=,%=)

//relational(==,!=,>,<,<=,>=)returning bool values; logical(&&,||,!=); unary(++,--,!)

//bitwise(~,<<,>>,>>>{unsigned right shift},&,^); ternary(_?_:_)

public class Operators {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);

        //sc.nextLine() for  whole line;  sc.next() for string input
        //sc.nextInt(), sc.nextFloat(), sc.nextDouble(), sc.nextLong()
        int a=sc.nextInt();
        int b=sc.nextInt();

        double res=a/b;                 //fractional part set to 0
        double res1=(double)a/b;        //contains fractional part as a is converted to double before division


        //conditional statements:can be nested
        if(res==0){
            System.out.println("zero");
        }
        else if(res>0){
            System.out.println("positive");
        }
        else{
            System.out.println("negative");
        }


        //switch only works on integers, strings or enums in argument
        //all cases succeeding a particular case will be excecuted by default
        switch((int)res){
            case (1):
                System.out.println("one");
            case 2: case 3:                     //works for both 2 and 3
                System.out.println("two and three");
            default:
                System.err.println("default");
        }
        //case values can be only constants not variables or expressions


        
        System.out.println("res is"+res1);
        sc.close();                     //this is recommended after the use to avoid memory leakage

        // 🔁 Convert Character '1' → Integer 1
        // Method 1: Subtract ASCII value of '0'
        // char ch = '1';
        // int num = ch - '0'; // gives 1 because u know java converts them to int and then evaluates

        // - '1' has ASCII value 49
        // - '0' has ASCII value 48
        // - So: 49 - 48 = 1    
        // fast and safe for '0' to '9'.

        // Method 2: Use Character.getNumericValue()
        // int num = Character.getNumericValue('1'); // gives 1
        // - Works for digits and even hex characters like 'A' → 10    //like for hexadecimal number system 9->9 10->A atc



        // 🔁 Convert Integer 1 → Character '1'
        // Method 1: Add ASCII value of '0'
        // int num = 1;
        // char ch = (char)(num + '0'); // gives '1'


        // Method 2: Use Integer.toString() and .charAt(0)
        // char ch = Integer.toString(1).charAt(0); // gives '1'




        // Q: “What happens if you do 'A' - '0'?”
        // A: 'A' is 65, '0' is 48 → result is 17, but it’s not a valid digit. Use Character.isDigit() to check first.


        // - Character.isDigit(ch) and Character.isLetter(ch)
        char ch = '5';
        System.out.println(Character.isDigit(ch)); // true
        System.out.println(Character.isLetter(ch)); // false

        // - Parsing strings: "123" → int Use Integer.parseInt() or Integer.valueOf():
        // - parseInt() returns a primitive int
        // - valueOf() returns an Integer object
        String s = "123";
        int num = Integer.parseInt(s); // returns 123
        // Throws NumberFormatException if the string contains non-digit characters.


        // - ASCII table tricks: upper/lower case conversion
        //  Convert lowercase to uppercase:
        char lower = 'g';
        char upper = (char)(lower - 32); // 'G'
        // ✅ Convert uppercase to lowercase:
        char upper1 = 'D';
        char lower1 = (char)(upper1 + 32); // 'd'
        // - ASCII values: 'A' = 65, 'a' = 97 → all small and capital case letters have constant difference of 32
        //safer way
        Character.toUpperCase(ch);
        Character.toLowerCase(ch);

        // - Unicode: has characters of all languages vs ASCII: english only
        // java uses unicode for char by default

    }
}
