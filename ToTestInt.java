import java.util.*;
interface Test{
    int square(int n);
}
class Arithmetic implements Test{
    public int square(int num){
        return num*num;
    }
}
public class ToTestInt {
    public static void main(String args[]){
        Arithmetic o=new Arithmetic();
        Scanner a=new Scanner(System.in);
        System.out.println("enter a number:");
        int num=a.nextInt();
        int result=o.square(num);
        System.out.println("square: "+result);
    }
}
