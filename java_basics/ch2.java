//binary system,operators,user input,conditionals
import java.util.Scanner;
//operators:arithmetic(+,-,*,/,%),assignment(=,+=,-=,*=,/=,%=)

//relational(==,!=,>,<,<=,>=)returning bool values,logical(&&,||,!),unary(++,--)

//bitwise(~,<<,>>,>>>{unsigned right shift},&,^),ternary(_?_:_)

public class ch2 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);

        //sc.nextLine() for line  ;sc.next() for string input
        //sc.nextInt() ,sc.nextFloat() ,sc.nextDouble(),sc.nextLong()
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


        //switch only works on integers,strings or enums in argument
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


    }
}
