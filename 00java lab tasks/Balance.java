import java.util.*;
public class Balance {
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter the main string:");
        String m=sc.nextLine();
        int n=m.length();
        for(int i=0;i<n;i++){
            if((m.charAt(i)=='p')&&(m.charAt(i+1)=='q')){
                System.out.println("balanced string");
            }
            else if((m.charAt(i)=='p')&&(m.charAt(i-1)=='q')){
                System.out.println("not a balanced string");
            }
        }
        sc.close();
    }
}
