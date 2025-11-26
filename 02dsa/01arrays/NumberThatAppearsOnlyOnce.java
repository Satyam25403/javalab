import java.util.Scanner;
import java.util.HashMap;

//all elements except one, occur twice; find the number that occured only once

public class NumberThatAppearsOnlyOnce {
    static void bruteForceSolution(int a[] ){
        //pick every number and do linear search on array to count its occurences: return element which has count=1: O(n^2)
        //O(1): extra space
        for(int i=0;i<a.length;i++){
            int number=a[i],count=0;
            for(int j=0;j<a.length;j++){
                if(a[j]==number){
                    count++;
                }
            }
            if(count==1){
                System.out.println("Number that occured only once: "+number);
                return;
            }
        }
    }
    //we assume array elements dont have negatives
    static void levelOneOptimization(int a[]){
        //maintaining count of each element in hasharray and returning index where counter=1: 
        // O(n)finding max element+ O(n)filling hash array with count of elements+ O(n)searching index where counter=1
        //O(max_element) : extra space

        //find max element
        int maxElement=a[0];
        for(int i=0;i<a.length;i++){
            maxElement=Math.max(a[i], maxElement);
        }

        int[] hashArray=new int[maxElement+1];       //we need 1...max_number positions for marking ...so declare max_number+1 size to get max_index also

        //marking hasharray
        for(int i=0;i<a.length;i++){
            hashArray[a[i]]++;
        }

        //searching for counter=1
        for(int i=0;i<hashArray.length;i++){
            if(hashArray[i]==1){            //only one occurance found
                System.out.println("Number that only appeared once: "+i);
                return;
            }
        }
    }
    static void levelOneOptimizationForNegativesIncluded(int a[]) {
        //O(n logm)+O(n/2 + 1) time complexity; where m is the size of map=n/2 +1
        //space complexity: O(n) if all elements were distinct; O(n/2 + 1) for this question
        HashMap<Integer, Integer> map = new HashMap<>();            //for bigger numbers, use higher datatype like long as key
        for (int num : a) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        for (int key : map.keySet()) {
            if (map.get(key) == 1) {
                System.out.println("Number that only appeared once: " + key);
                return;
            }
        }
    }
    static void mostOptimalSolution(int a[]){
        //O(n) time complexity; O(1) extra space
        int xorResult=0;
        for(int i=0;i<a.length;i++){
            xorResult^=a[i];                  
        }
        System.out.println("Number that appeared only once: "+xorResult);
    }
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        int a[]=new int[7];
        System.out.println("Enter array:");
        for(int i=0;i<7;i++){
            a[i]=sc.nextInt();
        }
        
        bruteForceSolution(a);
        levelOneOptimization(a);
        mostOptimalSolution(a);
    }
}
