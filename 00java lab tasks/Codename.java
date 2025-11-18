import java.util.Scanner;
class CodeChecking{
    String name;
    int len;
    Scanner sc=new Scanner(System.in);
    void Create(){
        System.out.print("enter the code name:");
        name=sc.nextLine();
        len=name.length();//value of global variable which is not local to this function
        Checklen();
        Checkx();
        CheckStart();
        //while checking if any invalid codename is entered, call the create function again
    }
    void Checklen(){
        if(len<=6){
            System.out.println("INVALID CODENAME");
            System.out.println("the length of codename should be greater than 6");
            //then again ask to enter the codename
            Create();
        }
    }
    void Checkx(){
        if(name.charAt(len-1)!='X'&& name.charAt(len-1)!='x'){
            //becomes true when both of them is not satisfied
            System.out.print("invalid codename");
            System.out.println("the codename should end with an x");
            Create();
        }
    }
    void CheckStart(){
        if(!name.startsWith("Agent")&& !name.startsWith("agent")){
            //becomes false when name dont startwith either agent or Agent
            System.out.println("the code name sould start with agent or Agent");
            Create();
        }
    }
    void Checkequal(String x){
        if(name.equals(x))
        //wheter the name declared in this class and the passed argument are same or not
            System.out.println("CODENAME VERIFIED");
        else
            System.out.println("CODENAME MISMATCH");
    }
    //end of class codechecking
}

public class Codename {
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        CodeChecking c1=new CodeChecking();
        c1.Create();
        // takes a string input, checks its length, checks ends with x, and checks whether bit starts with agent
        System.out.println("re-enter your code name for confirmation");
        String s=sc.nextLine();
        c1.Checkequal(s);
        sc.close();
    }
    
}
