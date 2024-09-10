import java.util.*;
interface Number{
    boolean iszero(int n);
    boolean isNegative(int n);
    boolean isPositive(int n);
    boolean iseven(int n);
    boolean isOdd(int n);
    boolean isPrime(int n);
    boolean isarmstrong(int n);

}
class Verification implements Number{
    public boolean iszero(int n){
        return n==0;
    }
    public boolean iseven(int n){
        return n%2==0;
    }
    public boolean isOdd(int n){
        return n%2!=0;
    }
    public boolean isPositive(int n){
        return n>0;
    }
    public boolean isNegative(int n){
        return n<0;
    }
    public boolean isPrime(int n){
        if(n==0||n==1){
            return false;
        }
        int flag=0;
        for(int i=2;i<=n/2;i++){
            if(n%i==0){
                flag=1;
                break;
            }
        }
        if(flag==0){
            return true;
        }
        return false;
    }
    public boolean isarmstrong(int n){
        int r,sum=0,temp=n;
        String s=Integer.toString(n);
        int l=s.length();
        while(n!=0){
            r=n%10;
            sum+=Math.pow(r,l);
            n/=10;
        }
        return sum==temp;
    }
}
public class numberType {
    public static void main(String args[]){
        Verification obj=new Verification();
        Scanner a=new Scanner(System.in);
        System.out.println("enter a number:");
        int n=a.nextInt();
        if(obj.iszero(n)){
            System.out.println("zero");
        }
        if(obj.isPositive(n)){
            System.out.println("positive");
        }
        if(obj.isNegative(n)){
            System.out.println("negative");
        }
        if(obj.isPrime(n)){
            System.out.println("prime");
        }
        if(obj.isarmstrong(n)){
            System.out.println("armstrong");
        }
        if(obj.isOdd(n)){
            System.out.println("odd");
        }
        if(obj.iseven(n)){
            System.out.println("even");
        }
        a.close();
    }
}
