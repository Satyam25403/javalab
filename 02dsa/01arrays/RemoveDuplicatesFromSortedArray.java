import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

//assume array is sorted
//duplicates removal should be in-place: return the new modified array with count of total distinct elements in the array from start
//(we dont care what the numbers are after {count} no. of elements)

public class RemoveDuplicatesFromSortedArray {
    static void bruteForceSolution(int a[]){
        //overall time complexity: O(n logn):insertion into set + O(n):putting set elements back into array

        //space complexity:O(n)
        Set<Integer> set = new HashSet<>();

        //traverse and keep it into a set(which accepts only unique elements): O(n logn) for insertion  into set
        for(int i=0;i<a.length;i++){
            set.add(a[i]);                  //O(logn)
        }
        
        // System.out.println(set);

        //place distinct elements back into array: O(n) and return distinct elements count and modified array
        int index=0;
        for(int element: set){
            a[index]=element;
            index++;
        }

        System.out.println("modified array with "+set.size()+" distinct elements from start");
        for(int n: a){
            System.out.print(n);
        } 
    }
    static void levelOneOptimization(int a[]){
        //two pointer approach, both moving in same direction: O(n) time complexity and O(1) space complexity
        int lastDistinctElementIndexTillNow=0;          //first distinct element is at 0th position
        for(int j=1;j<a.length;j++){
            if(a[j]!=a[lastDistinctElementIndexTillNow]){
                a[lastDistinctElementIndexTillNow+1]=a[j];
                lastDistinctElementIndexTillNow++;
            }
        }
        int distinctElementsCount=lastDistinctElementIndexTillNow+1;
        System.out.println("No of distinct elements: "+distinctElementsCount);
        for(int num:a){
            System.out.print(num);
        }
    }
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        int a[]=new int[6];
        System.out.println("Enter sorted array:");
        for(int i=0;i<6;i++){
            a[i]=sc.nextInt();
        }

        // bruteForceSolution(a);
        levelOneOptimization(a);
    }
}
