import java.util.ArrayList;
import java.util.Scanner;
/////to modify still
public class OnlineStockSpan {
    private int[] inputArray,stockSpan;
    int size;
    OnlineStockSpan(int[] a,int n){
        inputArray=a;
        size=n;
        stockSpan=calculateStockSpan(a,n);
    }
    int[] calculateStockSpan(int[] arr,int n){
        int[] res=new int[n];
        for(int i=0;i<n;i++){
            int span=1;
            for(int j=i-1;j>=0;j--){
                //if a greater number is encountered
                if(arr[j]>arr[i]){
                    break;
                }
                //else the number countered is lessthan or equal to current number
                span++;
            }
            res[i]=span;
        }
        return res;
    }
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter number of days:");
        int n=sc.nextInt();
        int[] a=new int[n];
        for(int i=0;i<n;i++){
            a[i]=sc.nextInt();
        }
        OnlineStockSpan os=new OnlineStockSpan(a,n);
        for(int i:os.stockSpan){
            System.out.println(i);
        }
        
    }
}
