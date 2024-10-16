import java.util.Scanner;

public class SearchInRotatedSortedArray  {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Enter the number of elements in the array");
        int n = sc.nextInt();
        
        int[] arr = new int[n];
        
        System.out.println("Enter the elements of the array (rotated sorted array):");
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        
        System.out.println("Enter the target element to search for:");
        int target = sc.nextInt();
        
        System.out.println(search(arr, target));
        sc.close(); // Close the scanner
    }

    // Function to search target in a rotated sorted array using binary search
    public static int search(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;
        
        while (low <= high) {
            int mid = low + (high - low) / 2;
            
            // Check if mid is the target element
            if (arr[mid] == target) {
                return mid;
            }
            
            // Determine which half is sorted
            if (arr[low] <= arr[mid]) {  // Left half is sorted
                if (target >= arr[low] && target < arr[mid]) {
                    high = mid - 1;  // Target is in the left half
                } else {
                    low = mid + 1;  // Target is in the right half
                }
            } else {  // Right half is sorted
                if (target > arr[mid] && target <= arr[high]) {
                    low = mid + 1;  // Target is in the right half
                } else {
                    high = mid - 1;  // Target is in the left half
                }
            }
        }
        
        return -1;  // If element is not found
    }
}
