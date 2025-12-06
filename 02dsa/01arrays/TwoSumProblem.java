//1st variation: given an array and target, find whether or not there exist two values distinct a,b such that a+b=target
//2nd variation: given an array and given that there distinct a,b such that a+b=target, find them
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TwoSumProblem {
    static void bruteForceSolution(int a[], int k){
        //take an element and in whole array find an element other than itself which sums to k: O(n^2)
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a.length;j++){
                if(i==j){
                    continue;       //a,b should be distinct
                }

                if(a[i]+a[j]==k){
                    System.out.println("There exist two numbers: "+a[i]+"+"+a[j]+"="+k);
                    return;
                }
            }
        }
        System.out.println("No such pair of elements exist");
    }
    static void betterSolution(int a[], int k){
        //start 2nd loop from i+1: slighlty less than O(n^2)
        for(int i=0;i<a.length;i++){
            for(int j=i+1;j<a.length;j++){          //we dont need to check for same pairs again and again
                if(a[i]+a[j]==k){
                    System.out.println("There exist two numbers: "+a[i]+"+"+a[j]+"="+k);
                    return;
                }
            }
        }
        System.out.println("No such pair of elements exist");
    }
    static void levelOneOptimization(int a[], int k){
        //use hashmap:O(n) for algorithm 

        //for 2nd variety, this solution is most optimal
        Map<Integer,Integer> map=new HashMap<>();           //store(key:element,value:index of element)

        for(int i=0;i<a.length;i++){
            int element=a[i];
            int moreNeededForSumK=k-element;
            if(map.containsKey(moreNeededForSumK)){
                System.out.println("indices: "+map.get(moreNeededForSumK)+","+i+" ,values: "+moreNeededForSumK+","+a[i]);
                return;
            }
            map.put(element,i);
        }
        //whole loop exhausted and we haven't found the pair
        System.out.println("No such pair of elements exist");
    }
    static void mostOptimalSolution(int a[],int k){
        //using 2 pointer greedy approach: sort array first, left and right pointer sum...adjust the pointers to match the sum, is current sum greater, reduce it by moving right ptr to left...if lesser, move left ptr to right
        //time complexity: O(n logn):sorting+O(n) for two pointers covering the array; O(1) extra space

        //for 1st variety where we need to tell whether 2 sum exists or not, this si most optimal
        Arrays.sort(a);
        int left=0,right=a.length-1;
        while(left<right){
            int sum=a[left]+a[right];
            if(sum==k){
                System.out.println("Two sum is possible with values: "+a[left]+" and "+a[right]);
                return;
            }else if(sum>k){
                right--;
            }else{              //sum<k
                left++;         
            }
        }
        System.out.println("No such pair of elements exist");
    }

    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        int a[]=new int[5];
        System.out.println("Enter array:");
        for(int i=0;i<5;i++){
            a[i]=sc.nextInt();
        }
        System.out.println("Enter target sum(k):");
        int k=sc.nextInt();

        
        bruteForceSolution(a,k);
        betterSolution(a, k);
        levelOneOptimization(a, k);
        mostOptimalSolution(a,k);
    }
}
