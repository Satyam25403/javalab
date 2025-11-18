import java.util.*;

public class Arrays {
    public static void main(String[] args) {
        
        int intArray[];         //array declaration
        intArray=new int[20];       //allocating memory:to do both in same line int intArray[]=new int[20]
        //uninitialized locations are set to 0 if only memory is allocated
        //if a[0]=3 and a[1]=4 and a[2]not initialized ,a[2]=0

        // - Default values: 0 for int, null for String, false for boolean
        // - Accessing out-of-bound index → ArrayIndexOutOfBoundsException

        int marks[]={98,23,45,2,12};        //declararion with initialization


        String names[]={"ram","harish","gopi","karan"};
        int n=names.length;

        //for iterable things like arrays for each loop is used
        for(String a:names){
            System.out.println(a);
        }

        //multiple dimensional arrays
        int a[][]=new int[3][4];
        int scores[][]={{1,2,3},{4,5,6},{7,8,9}};


        // Q: “Can arrays hold mixed types?”
        // A: No. Arrays are type-specific. Use Object[] for mixed types


        //copying an arrya:
        int[] copy = Arrays.copyOf(marks, marks.length);
    }
}

// some classic problems:

// 🔁 Core Array Subproblems
// 1. Find max/min element
// - Traverse once, track max or min
// - Variants: second max, index of max, max in rotated array
// 2. Reverse an array
// - Two-pointer swap from ends
// - In-place vs extra space
// 3. Sum of elements / prefix sum
// - Running total
// - Prefix sum array for range queries
// 4. Search element
// - Linear search
// - Binary search (sorted arrays)
// 5. Count frequency of elements
// - Use HashMap or frequency array
// - Variants: most frequent, first non-repeating
// 6. Sort array
// - Built-in: Arrays.sort()
// - Interview: implement Bubble, Selection, Merge, Quick
// 7. Remove duplicates
// - Use Set or two-pointer for sorted arrays
// 8. Move zeros to end
// - Two-pointer swap or overwrite
// - Maintain relative order
// 9. Rotate array
// - Naive shift vs reversal method
// - Variants: rotate left/right by k
// 10. Find missing number
// - XOR trick or sum formula
// - Works for 1 to n range

// 🧠 Intermediate Patterns
// 11. Kadane’s Algorithm
// - Max subarray sum
// - Handles negatives
// 12. Sliding Window
// - Fixed or variable window size
// - Use for max/min/sum in subarrays
// 13. Two Pointer Technique
// - Sorted arrays: pair sum, remove duplicates
// - Unsorted: careful with edge cases
// 14. Dutch National Flag
// - Sort 0s, 1s, 2s
// - Three-way partitioning
// 15. Prefix XOR / Prefix Product
// - For range queries or bit tricks

// 🚀 Advanced Interview Patterns
// 16. Binary Search Variants
// - First/last occurrence
// - Search in rotated array
// - Peak element
// 17. Merge Intervals
// - Sort + merge overlapping
// - Used in calendar, range problems
// 18. Matrix Traversal
// - Spiral, zigzag, boundary
// - DFS/BFS for 2D grids
// 19. Subarray with Given Sum
// - HashMap + prefix sum
// - Sliding window for positive numbers
// 20. Partitioning Arrays
// - Quickselect for kth largest
// - Rearranging based on condition

// 🔧 Utility Methods to Build
// - reverse(int[] arr)
// - rotate(int[] arr, int k)
// - findMax(int[] arr)
// - countFrequency(int[] arr)
// - isSorted(int[] arr)
// - binarySearch(int[] arr, int target)

