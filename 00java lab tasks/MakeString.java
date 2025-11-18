import java.util.*;
public class MakeString {
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        String m,t;
        StringBuilder sb=new StringBuilder(2);//to store characters just befor and after the first occurence
        System.out.println("enter the main string:");
        m=sc.nextLine();
        System.out.print("enter the substring to be searched in the main string:");
        t=sc.nextLine();
        int i=m.indexOf(t);       //to find the index of first occurence of starting of the substring in a string 
        //If no such value of k exists, then -1 is returned.
        if(i>=0){
            sb.append(m.charAt(i-1));
            sb.append(m.charAt(i+t.length()));
            System.out.println("the characters just before and after of the given substring's first occurence is "+sb);
        }
        else{
            //when -1 is returned
            System.out.println("The given substring does not occur in the main string");
        }
        sc.close();
    }
}
