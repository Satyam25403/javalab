import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;

public class ValidTriangle {

    static boolean isValid(int a,int b,int c){
        //if sum of two sides is greater than third side
        if(a+b> c && b+c>a && c+a>b){
            return true;
        }
        else{
            return false;
        }
    }
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter number:");
        int n=sc.nextInt();
        int[] a=new int[n];
        System.out.println("Enter array:");
        for(int i=0;i<n;i++){
            a[i]=sc.nextInt();
        }
        int count=0;
        
        for(int i=0;i<n;i++){
            for(int j=i+1;j<n;j++){
                for(int k=j+1;k<n;k++){
                    if(isValid(a[i],a[j],a[k])){
                        count++;
                    }
                }
            }
        }
        System.out.println(count);

        
    }
}
