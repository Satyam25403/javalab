import java.util.*;
public class Check1{
    static String secret,guess;
    static int x,count,leftguess,sum;
    static char y;
    public static void main(String args[]){
        secret="53840";
        Scanner s=new Scanner(System.in);
        leftguess=3;
        sum=0;
        while(leftguess>0){
            System.out.println("enter the guess number:");
            guess=s.next();count=0;
            if(guess.length()!=5){
                throw new ArithmeticException("please enter 5 digits:");
            }
            else{
                for(int i=0;i<5;i++){
                    if(secret.charAt(i)==guess.charAt(i)){
                        y=secret.charAt(i);
                        count++;
                        x=Character.getNumericValue(y);
                        System.out.print(x);
                        sum+=x;
                    }
                }
                System.out.println("are correct  "+count);
                System.out.println("Sum of positions:"+sum);
                if(count==5){
                    System.out.println("congrats");
                    break;
                }
                leftguess--;
                System.out.println("guesses left:"+leftguess);
                if(leftguess==0){
                    throw new RuntimeException("out of guesses ..game over");
                }

            }
        }
    }

}

