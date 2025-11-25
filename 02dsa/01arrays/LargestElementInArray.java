import java.util.Scanner;
import java.util.Arrays;

//Always import utility classes like Arrays, Collections, or Math explicitly. 
// It avoids ambiguity and makes your code more portable across environments.
//dont use java.util.* always

public class LargestElementInArray {
    static void bruteForceSolution(int a[]){
        //sort and return last element: O(nlogn)
        Arrays.sort(a);
        System.out.println("Largest (brute force): " + a[a.length - 1]);
    }
    static void levelOneOptimization(int a[]){
        //traverse the array and keep updating max: O(n)
        int largest = a[0];
        for (int i=1;i<=a.length-1;i++){
            if(a[i]>largest){
                largest=a[i];
            }
        }
        System.out.println("largest: "+ largest);
    }
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        int a[]=new int[6];
        System.out.println("Enter array:");
        for(int i=0;i<6;i++){
            a[i]=sc.nextInt();
        }

        bruteForceSolution(a);
        levelOneOptimization(a);
    }
}
