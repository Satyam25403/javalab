import java.util.Scanner;

public class PeakElement {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int[]a=new int[n]; 
        for(int i=0;i<n;i++){
            a[i]=sc.nextInt();
        }
        System.out.println("Fist peak element present at index:"+peak(a));

        
    }
    static int peak(int a[]){
        if(a[0]>a[1]){
            return 0;
        }
        for(int i=1;i<=a.length;i++){
            if(isPeak(a[i-1],a[i],a[i+1])){
                return i;
            }
        }
        if(a[a.length-1]>a[a.length-2]){
            return a.length-1;
        }
        return -1;
    }
    static boolean isPeak(int a,int b,int c){
        return (b>a && b>c);
    }
}
