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
        int floor = 0, ceil = 0;

        // Loop to find the floor and ceil
        for (int i = 0; i < n; i++) {
            if (arr[i] <= target) {
                // If the current element is smaller than or equal to target, it's a potential floor
                if (floor == 0 || arr[i] > floor) {
                    floor = arr[i];
                }
            }

            if (arr[i] >= target) {
                // If the current element is larger than or equal to target, it's a potential ceil
                if (ceil == 0 || arr[i] < ceil) {
                    ceil = arr[i];
                }
            }
        }
        if(ceil!=0)
            System.out.println(ceil);
        else
            System.out.println(-1);
        if(floor!=0)
            System.out.println(floor);
        else
            System.out.println(-1);
        sc.close(); // Close the scanner
    }
}

// int c=0,z=0;

// for(int i=0;i<n;i++)
// {
// if(arr[i]<=x)
// {
//     if(c==0|| arr[i]>c)
//     c=arr[i];
// }
// }
// for(int i=0;i<n;i++)
// {
// if(arr[i]>=x)
// {
// if(z==0 || arr[i]<z)
// z=arr[i];
// }
// }
// if(c!=0)
// System.out.println(c);
// else
// System.out.println(-1);
// if(z!=0)
// System.out.println(z);
// else
// System.out.println(-1);

// }
// }