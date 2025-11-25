import java.util.Scanner;

//rotating arr.length positions is same as not rotating the array, rotating by arr.length+1 positions is same as rotating 1 time and so on i.e. effective no. of rotations=k%arr.length

public class LeftRotateArrayByKPositions {
    static int[] reverseArray(int a[],int start,int end){
        while(start<end){
            int temp=a[start];
            a[start]=a[end];
            a[end]=temp;
            start++;
            end--;
        }
        return a;
    }
    static void bruteForceSolution(int a[], int k){
        //store k elements in an array, move rest all elements to left, then copy stored elements at last: O(k)copying elements+O(n-k) shifting elements to left+O(k)copying at last= O(n+k)time complexity and O(k)space complexity
        int effectiveRotations=k%a.length;
        int numberOfElementsToStoreFromStart=effectiveRotations;
        int[] numArray=new int[numberOfElementsToStoreFromStart];

        //store elements from start
        for(int i=0;i<numberOfElementsToStoreFromStart;i++){
            numArray[i]=a[i];
        }

        //moving elements to left
        for(int i=numberOfElementsToStoreFromStart;i<a.length;i++){
            a[i-numberOfElementsToStoreFromStart]=a[i];
        }

        //copying stored elements
        int j=0;
        for(int i=a.length-numberOfElementsToStoreFromStart;i<a.length;i++){
            a[i]=numArray[j];
            j++;
        }

        //advanced version of copying
        // for(int i=a.length-numberOfElementsToStoreFromStart;i<a.length;i++){
        //     a[i]=numArray[i-(a.length-numberOfElementsToStoreFromStart)];
        // }

        System.out.println("Array after rotation: ");
        for(int num:a){
            System.out.print(num);
        }
    }
    static void mostOptimalSolution(int a[],int k){
        //reverse the both parts of array individually, and again reverse the array as whole: O(k)+O(n-k)+O(n)=O(2n) 
        // time complexity slightly increases, but without using extra space i.e. o(1) extra space 
        k=k%a.length;           //effectiveRotations

        //reverse first k elements
        a=reverseArray(a, 0, k-1);
        //reverse remaining:last n-k elements
        a=reverseArray(a, k, a.length-1);
        //reverse whole array
        a=reverseArray(a, 0, a.length-1);

        System.out.println("Array after rotation: ");
        for(int num:a){
            System.out.print(num);
        }

        // The 3-step reverse technique can describe a right rotation if someone interprets the partitions differently.
        // The reverse pattern is identical for both left and right rotation:
        // reverse(part A)
        // reverse(part B)
        // reverse(whole)

        // The only difference is:
        // Left rotation → A = first k elements
        // Right rotation → A = first (n − k) elements
    }

    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        int a[]=new int[7];
        System.out.println("Enter array:");
        for(int i=0;i<7;i++){
            a[i]=sc.nextInt();
        }
        System.out.println("Enter num of positions to rotate by(k):");
        int k=sc.nextInt();

        //run them individually, array gets changed after each method execution
        bruteForceSolution(a,k);
        mostOptimalSolution(a,k);
    }
}
