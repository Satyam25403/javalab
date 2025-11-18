import java.util.*;
public class OriginalString {
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        System.out.print("enter the string: ");
        String main=sc.nextLine();
        int n=main.length();
        if((main.charAt(0)==main.charAt(n-2))&&(main.charAt(1)==main.charAt(n-1))){
                System.out.println("substring is :"+(main.substring(2,n)));
        }
        else{
            System.out.println("String has no change :"+main);
        }
        sc.close();
    }
}
