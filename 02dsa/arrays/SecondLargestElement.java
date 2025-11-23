import java.util.Scanner;
import java.util.Arrays;

//similar is the approach for finding second smallest element in array

public class SecondLargestElement {
    static void bruteForceSolution(int a[]){
        //sort and return the element from last which is not equal to last element: O(nlogn)
        Arrays.sort(a);
        int largest=a[a.length-1],secondLargest=Integer.MIN_VALUE;
        for(int i=a.length-2;i>=0;i--){
            if(largest!=a[i]){
                secondLargest=a[i];
                break;
            }
        }

        //if all elements are same, second largest dont exist:return -1
        if(largest==secondLargest){
            secondLargest=-1;
        }

        System.out.println("Second Largest(bruteforce): " + secondLargest);
    }

    static void levelOneOptimization(int a[]){
        //traverse the array twice(first for largest and second time for second largest): O(2n)
        int largest = a[0];
        for (int i=1;i<a.length;i++){
            if(a[i]>largest){
                largest=a[i];
            }
        }
        
        int secondLargest=Integer.MIN_VALUE;
        for(int i=0;i<a.length;i++){
            if(a[i]>secondLargest && a[i]!=largest){
                secondLargest=a[i];
            }
        }
        
        if (secondLargest == Integer.MIN_VALUE) {
            System.out.println("Second Largest does not exist.");
        } else {
            System.out.println("Second Largest(Level one): " + secondLargest);
        }
    }

    static void levelTwoOptimization(int a[]){
        //maintain two vars one for biggest and one for second largest: O(n)
        int largest = a[0], secondLargest=Integer.MIN_VALUE;
        for (int i=1;i<a.length;i++){
            if(a[i]>largest){
                secondLargest=largest;
                largest=a[i];
            }else if (a[i] > secondLargest && a[i] != largest) {
                secondLargest = a[i];
            }
            //NOTE: if an element is equal to largest don't do anything, it doesnt matter
        }

        if (secondLargest == Integer.MIN_VALUE) {
            System.out.println("Second Largest does not exist.");
        } else {
            System.out.println("Second Largest(Level two): " + secondLargest);
        }
    }
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        int a[]=new int[6];
        System.out.println("Enter array:");
        for(int i=0;i<6;i++){
            a[i]=sc.nextInt();
        }

        bruteForceSolution(a);
        levelOneOptimization(a);
        levelTwoOptimization(a);
    }
}


