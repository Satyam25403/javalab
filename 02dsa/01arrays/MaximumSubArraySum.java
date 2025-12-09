import java.util.Scanner;

//subsequence: can include discontinuous elements
//subarray: only continuous elements of the array

public class MaximumSubArraySum {
    static void bruteForceSolution(int a[]) {
        // by checking all possible sub-arrays: O(n^3) time complexity, O(1) extra space
        int maxSum = Integer.MIN_VALUE;
        for (int i = 0; i < a.length; i++) {
            for (int j = i; j < a.length; j++) {
                int sum = 0;
                for (int k = i; k <= j; k++) {
                    sum += a[k];
                }
                maxSum = Math.max(sum, maxSum);
            }
        }
        System.out.println("Maximum subarray sum: " + maxSum);
    }

    static void levelOneOptimization(int a[]) {
        // just one optimization that we dont actually need subarrays, we just need
        // their sum:
        // hence we add current element to previous subarray sum to get current subarray
        // sum
        // O(n^2) time complexity and O(1) extra space
        int maxSum = Integer.MIN_VALUE;
        for (int i = 0; i < a.length; i++) {
            int sum = 0;
            for (int j = i; j < a.length; j++) {
                sum += a[j]; // covers all possible subarrays starting at index i
                maxSum = Math.max(sum, maxSum);
            }
        }

        System.out.println("Maximum subarray sum: " + maxSum);
    }

    static void mostOptimalSolution(int a[]) {
        // Kadane's algorithm: O(n) time complexity and O(1) extra space

        int maxSum = Integer.MIN_VALUE, maxEndingHere = 0;
        int ansStartIndex = -1, ansEndIndex = -1, start = 0;
        for (int i = 0; i < a.length; i++) {
            if (maxEndingHere == 0) { // a start is always when the sum is 0
                start = i;
            }
            maxEndingHere += a[i];
            if (maxEndingHere > maxSum) {
                maxSum = maxEndingHere;
                ansStartIndex = start;
                ansEndIndex = i;
                // - When you find a new maximum (maxEndingHere > maxSum), you record both
                // ansStartIndex and ansEndIndex.
            }

            if (maxEndingHere < 0) {
                maxEndingHere = 0;
            } // can also be written as...

            // maxEndingHere = Math.max(0, maxEndingHere); //if adding currentelement, still
            // keeps sum positive, carry it further as it will help in increasing the sum...else if<0 drop the sum
            // i.e. extend the previous sum or start fresh if sum negative
        }  
        System.out.println("Maximum subarray sum: " + maxSum);
        System.out.print("Subarray: ");
        for (int i = ansStartIndex; i <= ansEndIndex; i++) {
            System.out.print(a[i] + " ");
        }
        System.out.println();
    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int a[] = new int[7];
        System.out.println("Enter array:");
        for (int i = 0; i < 7; i++) {
            a[i] = sc.nextInt();
        }

        bruteForceSolution(a);
        levelOneOptimization(a);
        mostOptimalSolution(a);
    }
}
