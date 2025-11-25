import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

//return unique elements from both arrays

public class UnionOfTwoSortedArrays {
    static void bruteForceSolution(int a[],int b[]){
        //traverse both arrays and add all elements to set: 
        //O(n1 logn)adding elements from first array + O(n2 logn)adding elements from second array+ O(n1+n2) for making elements stored in set into an array for returning
        //O(n1 logn)+O(n2 logn)+O(n1+n2) time complexity
        //O(n1+n2)storing into set+ O(n1+n2) to return answer: space complexity

        Set<Integer> set = new TreeSet<>();     //the elements will be sorted automatically in TreeSet

        
        for(int i=0;i<a.length;i++){
            set.add(a[i]);                  //O(logn)
        }
        for(int i=0;i<b.length;i++){
            set.add(b[i]);                  //O(logn)
        }

        System.out.println("Union: ");
        for(int num:set){
            System.out.print(num);
        }
    }
    static void levelOneOptimization(int a[],int b[]){
        //two pointers, one on each traversing the arrays: O(n1)+O(n2) just traversing both arrays
        //O(n1+n2) extra space for returning the answer
        
        int n1=a.length,n2=b.length,i=0,j=0;

        //we are using this because we dont know size of union array
        ArrayList<Integer> list = new ArrayList<>();

        while(i<n1 && j<n2){
            //we will take smaller among them but that element should not be in our arraylist already
            if(a[i]<b[j]){
                //if list arr is empty or dont have smaller elemnt among both in it, add it and increment corresponding ptr
                if (list.isEmpty() || list.get(list.size()-1) != a[i]){  
                    list.add(a[i]);
                }
                i++;
            }else if (b[j] < a[i]){
                if (list.isEmpty() || list.get(list.size()-1) != b[j]){
                    list.add(b[j]);
                }
                j++;
            }else{ // both equal case: take one of them but increment pointers of both
                if (list.isEmpty() || list.get(list.size()-1) != a[i]){
                    list.add(a[i]);
                }    
                i++; j++;
            }
        }

        // Add remaining elements if one of array traversal completes: make sure not to add duplicates
        while(i < n1){
            if (list.isEmpty() || list.get(list.size()-1) != a[i])
                list.add(a[i]);
            i++;
        }
        while(j < n2){
            if (list.isEmpty() || list.get(list.size()-1) != b[j])
                list.add(b[j]);
            j++;
        }
        
        for(int num:list){
            System.out.print(num);
        }
    }
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        int a[]=new int[6];
        int b[]=new int[6];
        System.out.println("Enter first array:");
        for(int i=0;i<6;i++){
            a[i]=sc.nextInt();
        }
        System.out.println("Enter second array:");
        for(int i=0;i<6;i++){
            b[i]=sc.nextInt();
        }

        bruteForceSolution(a,b);
        System.out.println();
        levelOneOptimization(a,b);
    }
}
