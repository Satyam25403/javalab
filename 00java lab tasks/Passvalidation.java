import java.util.StringTokenizer;
import java.util.Scanner;
class Password{
    boolean Checker(String x){
        char ch;
        boolean checkupal=false;
        boolean checklowal=false;
        boolean checknum=false;
        boolean checkchar=false;
        for(int i=0;i<x.length();i++){
            ch=x.charAt(i);
            if(ch>='0'&&ch<='9')
                checknum=true;
            else if(ch>='A'&& ch<='Z')
                checkupal=true;
            else if(ch>='a'&&ch<='z')
                checklowal=true;
            else if(ch=='$'||ch=='#'||ch=='@')
                checkchar=true;
            if(checknum&&checkupal&&checklowal&&checkchar)
            //at least one of each kind is present
                return true;
        }
        return false;
    }
}
public class Passvalidation {
    public static void main(String args[]){
        Password obj=new Password();
        Scanner sc=new Scanner(System.in);
        System.out.print("enter stream of passwords");
        String pw=sc.nextLine();
        System.out.println("");
        StringTokenizer token=new StringTokenizer(pw,",");
        // The characters in the delim argument are the delimiters for separating tokens. 
        while(token.hasMoreTokens()){
            //tests if there are more tokens available from this tokenizer's string. 
            String s=token.nextToken();
            //Returns the next token from this string tokenizer.
            int len=s.length();
            if(len>=6 && len<=12){
                if(obj.Checker(s))
                    System.out.println(s+" is a valid Password\n");
                else
                    System.out.println(s+"is Invalid\n");
            }
            else
                System.out.println(s+" is Invalid\n");
        }
        sc.close();

    }
}
