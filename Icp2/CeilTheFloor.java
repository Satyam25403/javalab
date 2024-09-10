class CeilTheFloor{
    public static void main(String[] args){
        int[] arr={5,6,8,9,6,5,5,6};
        int target=7;
        int floor=Integer.MIN_VALUE,ceil=Integer.MAX_VALUE;
        for(int i=0;i<arr.length;i++){
            if(arr[i]<=target && arr[i]>floor)
                floor=arr[i];
            if(arr[i]>=target && arr[i]<ceil)
                ceil=arr[i];
        }
        
        if(floor == Integer.MIN_VALUE){
            System.out.println("No floor value found.");
        }
        else{
            System.out.print("Floor: " + floor + " ");
        }

        if(ceil == Integer.MAX_VALUE){
            System.out.println("No ceil value found.");
        }
        else{
            System.out.println("Ceil: " + ceil);
        }
    }
}
