public class ArrayMax {
    public static int maxInRange(int[] arr,int lowIndex,int highIndex){
        if(lowIndex==highIndex){
            return arr[lowIndex];
        }
        else{
            int mid=(lowIndex+highIndex)/2;
            int leftMax=maxInRange(arr, lowIndex, mid);
            int rightMax=maxInRange(arr, mid+1, highIndex);
            return Math.max(leftMax,rightMax);
        }
    }
    public static int max(int[] arr){
        return maxInRange(arr,0,arr.length-1);
    }
    public static void main(String args[]){
        int[] arr={3,7,1,9,4,6,8,2,5};
        int maxInRangeResult =maxInRange(arr,0,arr.length-1);
        System.out.println("maximum in the range:"+maxInRangeResult);
        int maxResult=max(arr);
        System.out.println("maximum in the entire array:"+maxResult);
    }
}
