import java.util.HashMap;
import java.util.Scanner;
//subarray: contigious part of the array
//subsequence: elements can be in non-contigious

//in this problem we are just required to find the length of longest subarray
public class LongestSubarrayWithSumK {
    static void bruteForceSolution(int a[], int k){
        //generate all subarrays, then find sum of all those sub arrays; among them choose longest array that sums to k
        //nearly O(n^3) time complexity and O(1) extra space
        int longestLengthOfSubarrayWithSumK=0;
        //generate subarrays
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a.length;j++){
                //array part from i to j including both indices is a subarray: find sum of it
                int sum=0;
                for(int idx=i;idx<=j;idx++){
                    sum+=a[idx];
                }

                //if subarray with sum=k found, update longest length
                if(sum==k){
                    longestLengthOfSubarrayWithSumK=Math.max(longestLengthOfSubarrayWithSumK,j-i+1);        //j-i+1 is size of current array
                }

            }
        }
        System.out.println("Longest length of subarray with sum=k is: "+longestLengthOfSubarrayWithSumK);
    }
    static void levelOneOptimization(int a[],int k){
        //we dont actually need to find what subarrays are, each time a continuous element is added while j is iterating, results in a new array
        //so just add the element to find the sum of that new subarray
        //O(n^2) time complexity and O(1) extra space
        int longestLengthOfSubarrayWithSumK=0;
        for(int i=0;i<a.length;i++){
            int sum=0;
            for(int j=i;j<a.length;j++){
                //find sum of new sub array formed by moving j pointer to this index
                sum+=a[j];

                if(sum==k){
                    longestLengthOfSubarrayWithSumK=Math.max(longestLengthOfSubarrayWithSumK,j-i+1);        //j-i+1 is size of current array
                }
            }
        }
        System.out.println("Longest length of subarray with sum=k is: "+longestLengthOfSubarrayWithSumK);
    }
    static void levelTwoOptimization(int a[],int k){
        //a better approach for non-negative elements and most optimal(cant be further optimized) if negative numbers are present in array

        //using hashing: prefix sum approach-> we are keeping prefix sum track...let us say for sum element it came as x....if some where previously, prefixsum came as x-k,
        //then we found a sub array in b/w with sum k=> hence we can generate subarrays with sum=k
        //O(n logn) time complexity for worst case(which is very less likely to occur) : O(n) in average case; and O(n) extra space
        HashMap<Integer, Integer> map = new HashMap<>();            //to store prefix sum till particular index: sum is the key, index is value
        int sum=0;
        int longestLengthOfSubarrayWithSumK=0;
        for (int idx=0;idx<a.length;idx++) {                    //O(n)
            sum+=a[idx];
            if(sum==k){
                longestLengthOfSubarrayWithSumK=Math.max(longestLengthOfSubarrayWithSumK, idx+1);
            }
            int rem = sum - k;
            if (map.containsKey(rem)) {
                int len = idx - map.get(rem);
                longestLengthOfSubarrayWithSumK = Math.max(longestLengthOfSubarrayWithSumK, len);
            }
             
            // Only put sum if it's not already present (to keep earliest index): coz we are looking as left as possible, maximizing length of subarray for the case if 0's exist
            if (!map.containsKey(sum)) {
                map.put(sum, idx);
            }
        }
        System.out.println("Longest length of subarray with sum=k is: "+longestLengthOfSubarrayWithSumK);
    }
    static void mostOptimalSolutionForNonNegatives(int a[],int k){
        //two pointer with greedy approach
    }

    static void mostOptimalSolutionIfNegativesIncluded(int a[],int k){
        //a better approach for non-negative elements and most optimal(cant be further optimized) if negative numbers are present in array
        
        //using hashing: prefix sum approach-> we are keeping prefix sum track...let us say for sum element it came as x....if some where previously, prefixsum came as x-k,
        //then we found a sub array in b/w with sum k=> hence we can generate subarrays with sum=k
        //O(n logn) time complexity for worst case(which is very less likely to occur) : O(n) in average case; and O(n) extra space
        HashMap<Integer, Integer> map = new HashMap<>();            //to store prefix sum till particular index: sum is the key, index is value
        int sum=0;
        int longestLengthOfSubarrayWithSumK=0;
        for (int idx=0;idx<a.length;idx++) {
            sum+=a[idx];
            if(sum==k){
                longestLengthOfSubarrayWithSumK=Math.max(longestLengthOfSubarrayWithSumK, idx+1);
            }
            int rem = sum - k;
            if (map.containsKey(rem)) {
                int len = idx - map.get(rem);
                longestLengthOfSubarrayWithSumK = Math.max(longestLengthOfSubarrayWithSumK, len);
            }
             
            // Only put sum if it's not already present (to keep earliest index): coz we are looking as left as possible, maximizing length of subarray for the case if 0's exist
            if (!map.containsKey(sum)) {
                map.put(sum, idx);
            }
        }
        System.out.println("Longest length of subarray with sum=k is: "+longestLengthOfSubarrayWithSumK);
    }

    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        int a[]=new int[7];
        System.out.println("Enter array:");
        for(int i=0;i<7;i++){
            a[i]=sc.nextInt();
        }
        System.out.println("Enter target sum(k):");
        int k=sc.nextInt();

        //run them individually, array gets changed after each method execution
        bruteForceSolution(a,k);
        levelOneOptimization(a,k);
        levelTwoOptimization(a, k);
    } 
}