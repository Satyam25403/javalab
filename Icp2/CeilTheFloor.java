import java.util.Scanner;

class CeilTheFloor{
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int[] arr=new int[n];
        for(int i=0;i<n;i++){
            arr[i]=sc.nextInt();
        }
        int target=sc.nextInt();

        int floor=Integer.MIN_VALUE,ceil=Integer.MAX_VALUE;
        //floor:largest element smaller than or equal to x
        //ceil:smallest element greater than or equal to x
        for(int i=0;i<n;i++){
            if(arr[i]<=target && arr[i]>floor)
                floor=arr[i];
            if(arr[i]>=target && arr[i]<ceil)
                ceil=arr[i];
        }
        
        if(floor == Integer.MIN_VALUE){
            System.out.println("No floor value found.");
        }
        if(ceil == Integer.MAX_VALUE){
            System.out.println("No ceil value found.");
        }
        else{
            System.out.print("Floor: " + floor + " "+"Ceil: " + ceil);
        }
    }
}
