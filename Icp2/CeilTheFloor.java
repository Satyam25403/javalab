// import java.util.Scanner;

// class CeilTheFloor{
//     public static void main(String[] args){
//         Scanner sc=new Scanner(System.in);
//         int n=sc.nextInt();
//         int[] arr=new int[n];
//         for(int i=0;i<n;i++){
//             arr[i]=sc.nextInt();
//         }
//         int target=sc.nextInt();

//         int floor=-1,ceil=-1;
//         //floor:largest element smaller than or equal to x
//         //ceil:smallest element greater than or equal to x
//         for(int i=0;i<n;i++){
//             if(arr[i]<=target && (arr[i]>floor||floor==-1))
//                 floor=arr[i];
//             if(arr[i]>=target && (arr[i]<ceil||ceil==-1))
//                 ceil=arr[i];
//         }
        
    
//             System.out.print("Floor: " + floor + " "+"Ceil: " + ceil);
       
//     }
// }
import java.util.Scanner;

class CeilTheFloor {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt(); // Number of elements in the array
        int[] arr = new int[n];

        // Reading array elements
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }

        int target = sc.nextInt(); // Target value

        // Initialize floor and ceil to -1 (will change once valid values are found)
        int floor = -1, ceil = -1;

        // Loop to find the floor and ceil
        for (int i = 0; i < n; i++) {
            if (arr[i] <= target) {
                // If the current element is smaller than or equal to target, it's a potential floor
                if (floor == -1 || arr[i] > floor) {
                    floor = arr[i];
                }
            }

            if (arr[i] >= target) {
                // If the current element is larger than or equal to target, it's a potential ceil
                if (ceil == -1 || arr[i] < ceil) {
                    ceil = arr[i];
                }
            }
        }

        // Print the floor and ceil values
        System.out.print("Floor: " + floor + " Ceil: " + ceil);

        sc.close(); // Close the scanner
    }
}
