import java.util.*;
// class MinInRotatedArray{
//     public static void main(String[] args) {
//         Scanner sc=new Scanner(System.in);
//         int n=sc.nextInt();
//         int[] a=new int[n];
//         for(int i=0;i<n;i++){
//             a[i]=sc.nextInt();
//         }

//         //in O(n) complexity
//         int k=a[0];
//         for(int i=0;i<a.length;i++){
//             if(a[i]<k){
//                 k=a[i];
//             }
//         }
//         System.out.println(k);

//         //in O(logn) complexity ??
//     }
// }

public class MinInRotatedArray {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Enter the number of elements in the array");
        int n = sc.nextInt();
        
        int[] arr = new int[n];
        
        System.out.println("Enter the elements of the array (rotated sorted array):");
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }

        int minElement = findMin(arr);

        System.out.println("The minimum element in the rotated sorted array is: " + minElement);

        sc.close(); // Close the scanner after input is done
    }

    // Function to find the minimum element in a rotated sorted array
    public static int findMin(int[] arr) {
        int low = 0;
        int high = arr.length - 1;

        // If the array is not rotated 
        if (arr[low] <= arr[high]) {
            return arr[low]; // First element is the minimum
        }

        // Binary search for the minimum element
        while (low < high) {
            int mid = low + ((high - low) / 2);

            // If mid element is greater than high, minimum is in the right part
            if (arr[mid] > arr[high]) {
                low = mid + 1;
            } else {
                // Minimum is in the left part (including mid)
                high = mid;
            }
        }

        // The loop will exit when low == high, which is the index of the minimum element
        return arr[low];
    }
}