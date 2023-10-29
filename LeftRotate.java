import java.util.Scanner;
public class LeftRotate {
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        System.out.print("enter number of elements in the array:");
        int n=sc.nextInt();
        int[] a=new int[n];
        System.out.println("enter the elements of the array:");
        for(int i=0;i<n;i++){
            a[i]=sc.nextInt();
        }
        System.out.println("before rotation: ");
        for(int i=0;i<n;i++){
            System.out.print(a[i]+" ");
        }
        System.out.println("enter how many times to rotate the array to left:");
        int nu=sc.nextInt();
         for(int i=0;i<nu;i++){
            int first=a[0];
            for(int j=0;j<a.length-1;j++){
                a[j]=a[j+1];
            }
            a[a.length-1]=first;
        }
        System.out.println("after rotation: ");
        for(int i=0;i<n;i++){
            System.out.print(a[i]+" ");
        }
        sc.close();
    }
}
