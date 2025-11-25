//to check if it is in non-descending order

import java.util.*;

public class CheckIfSortedArray {
    static boolean bruteForceSolution(int a[]){
        //traverse: O(n)
        for(int i=1;i<a.length;i++){
            if(a[i]>=a[i-1]){

            }else{
                System.out.println("It is not in non-descending order");
                return false;
            }
        }
        //if whole array traversal is complete and we still dont find problematic part, the array is in non-descending order
        System.out.println("It is in non-descending order");
        return true;
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
