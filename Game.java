import java.util.*;
public class Game {
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        int secretNumber=(int)(Math.random()*10)+1;
        //random function->Returns a double value with a positive sign, greater than or equal to 0.0 and less than 1.0
        int attempts=0;
        System.out.println("welcome to the number guessing game!");
        System.out.println("try to guess the secret number between 1 and 10");
        while(true){// an infinite loop till users makes orrect guess
            System.out.println("enter the number you guessed:");
            int userGuess=sc.nextInt();
            attempts++;
            if(userGuess<secretNumber)
                System.out.println("the number is too low");
            else if(userGuess>secretNumber)
                System.out.println("the number is too high");
            else{
                System.out.println("you win");
                System.out.println("Number of attempts :"+attempts);
                break;
            }
        }
        sc.close();
    }

}
