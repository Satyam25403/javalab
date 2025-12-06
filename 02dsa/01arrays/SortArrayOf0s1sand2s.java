import java.util.HashMap;
import java.util.Scanner;

//Dutch national flag problem

public class SortArrayOf0s1sand2s {
    static void bruteForceSolution(int a[]) {
        // use any sorting: merge sort O(n logn) time complexity and O(n) extra space
    }

    static void levelOneOptimization(int a[]) {
        // count 0s, 1s and 2s and override them manually in the original array

        int[] freqArr = new int[3];
        for (int i = 0; i < a.length; i++) {
            freqArr[a[i]]++;
        }

        int i = 0;
        for (int count = 0; count < freqArr[0]; count++) {
            a[i++] = 0; // fill and then increment
        }
        for (int count = 0; count < freqArr[1]; count++) {
            a[i++] = 1;
        }
        for (int count = 0; count < freqArr[2]; count++) {
            a[i++] = 2;
        }
        // for (int i = 0; i < freqArr[0]; i++) {
        // a[i] = 0;
        // }
        // for (int i= freqArr[0]; i< freqArr[0]+freqArr[1];i++) {
        // a[i] = 1;
        // }
        // for (int i=freqArr[0]+freqArr[1]; i< freqArr[0]+freqArr[1]+freqArr[2]; i++) {
        // a[i] = 2;
        // }

        for (int num : a) {
            System.out.print(num + " ");
        }
    }

    static void mostOptimalSolution(int a[]) {
        // Dutch national flag algorithm:
        // positions from 0 to low-1 has 0s, low to mid-1 has 1s and mid to high has
        // unsorted random numbers and hight+1 to n-1 has 2s
        // mid to high has unsorted elements rest portions are sorted
        // so at starting index 0=mid and index n-1=high because this section has
        // unsorted numbers which in initial case when array isn't sorted is same
        // indices from start to n-1
        // also index 0=low is valid because 0 to low-1 initially, if low=0, dont
        // contain any element, so we can say, it has sorted 0's which are 0 in count


        //time complexity:O(n) and O(1) extra space
        int low = 0, mid = 0, high = a.length - 1;
        while (mid <= high) { // till mid pasts high
            if (a[mid] == 0) {
                // swap mid and low, increment low and mid
                int temp = a[mid];
                a[mid] = a[low];
                a[low] = temp;

                low++;
                mid++;
            } else if (a[mid] == 1) {
                // increment mid
                mid++;
            } else {
                // mid has element 2:swap high and mid and decrement high
                int temp = a[mid];
                a[mid] = a[high];
                a[high] = temp;

                high--;
            }
        }

        for (int num : a) {
            System.out.print(num + " ");
        }

    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int a[] = new int[7];
        System.out.println("Enter array:");
        for (int i = 0; i < 7; i++) {
            a[i] = sc.nextInt();
        }

        // bruteForceSolution(a);
        levelOneOptimization(a);
        System.out.println();
        mostOptimalSolution(a);
    }
}
