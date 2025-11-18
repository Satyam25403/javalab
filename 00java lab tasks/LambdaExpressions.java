import java.util.function.IntPredicate;
import java.util.Scanner;
public class LambdaExpressions {
    public static IntPredicate isodd(){
        return n->n%2!=0;
    }
    public static IntPredicate isPrime(){
        return n->{
                if(n<2){
                    return false;
                }
                for(int i=2;i<=Math.sqrt(n);i++){
                    if(n%i==0){
                        return false;
                    }
                }
                return true;
        };
    }
    public static IntPredicate isPalindrome(){
        return n->{
            int reversed=0,original=n;
            while(n!=0){
                int digit=n%10;
                reversed=reversed*10+digit;
                n/=10;
            }
            return original==reversed;
        };
    }
    public static void main(String args[]){
        int t,u,v,w,x;
        Scanner s=new Scanner(System.in);
        System.out.println("enter  5 numbers:")
        t=s.nextInt();
        u=s.nextInt();
        v=s.nextInt();
        w=s.nextInt();
        x=s.nextInt();
        System.out.println(isodd().test(t)?"odd":"even");
        System.out.println(isPrime().test(u)?"prime":"composite");
        System.out.println(isPalindrome().test(v)?"palindrome":"not palindrome");
        System.out.println(isodd().test(w)?"odd":"even");
        System.out.println(isPrime().test(x)?"prime":"composite");
    }

}
