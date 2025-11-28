import java.util.Scanner;

public class AllSubstringsOfAGivenString {
    static void allSubstringsOf(String input){
        for(int i=0;i<input.length();i++){
            for(int j=i;j<input.length();j++){
                System.out.print(input.substring(i,j+1)+" "); //Java's substring end index is exclusive To include j, you must use j+1
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String input=sc.next();
        allSubstringsOf(input);
    }
}
