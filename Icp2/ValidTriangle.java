import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;

public class ValidTriangle {
    //own
    static boolean isValid(int a,int b,int c){
        if(a+b> c && b+c>a && c+a>b){
            return true;
        }
        else{
            return false;
        }
    }
    public static void main(String args[]){
        int count=0;
        int a[]={4,2,3,4};
        int n=a.length;
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
