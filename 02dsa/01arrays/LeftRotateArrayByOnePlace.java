import java.util.Scanner;

public class LeftRotateArrayByOnePlace {
    static void bruteForceSolution(int a[]){
        //copy each element into its left neighbour and move first one to the last: O(n) time complexity and O(1) space complexity(extra space)
        int firstElement=a[0];
        for(int i=1;i<a.length;i++){
            a[i-1]=a[i];
        }
        a[a.length-1]=firstElement;

        System.out.println("Array after rotation: ");
        for(int num:a){
            System.out.print(num);
        }
    }
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        int a[]=new int[6];
        System.out.println("Enter array:");
        for(int i=0;i<6;i++){
            a[i]=sc.nextInt();
        }

        bruteForceSolution(a);
    }
}
