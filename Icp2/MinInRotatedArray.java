import java.util.*;
class MinInRotatedArray{
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int[] a=new int[n];
        for(int i=0;i<n;i++){
            a[i]=sc.nextInt();
        }

        //in O(n) complexity
        int k=a[0];
        for(int i=0;i<a.length;i++){
            if(a[i]<k){
                k=a[i];
            }
        }
        System.out.println(k);

        //in O(logn) complexity ??
    }
}