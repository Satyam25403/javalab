import java.util.Scanner;

public class MoveAllZeroesToEnd {
    static void bruteForceSolution(int a[]){
        //store non-zero elements in an array, copy them from start in original array, fill remaining positions with 0's:
        // O(n)extracting non-zeroes+ O(x)copying nonzeroes to initial positions+ O(n-x)filling last positions with 0=O(2n)
        //Extra space:O(n) for storing non-zeroes

        //store non-zero numbers
        int[] numArr=new int[a.length];
        int j=0;
        for(int i=0;i<a.length;i++){
            if(a[i]!=0){
                numArr[j]=a[i];
                j++;
            }
        }
        //copy stored non-zero nums from beginning to original array
        for(int i=0;i<j;i++){
            a[i]=numArr[i];
        }

        //filling last positions in original array with 0's
        for(int i=j;i<a.length;i++){
            a[i]=0;
        }
        
        System.out.println("Array after shifting zeroes: ");
        for(int num:a){
            System.out.print(num);
        }
    }
    static void mostOptimalSolution(int a[]){
        //2 pointer approach: one pointer keeps pointing to first zero always and another just iterates, the moment 2nd ptr finds non zero number
        // it swaps with first one, j increments to point to swapped 0; i then finds next non-zero number
        //O(x) find first zero+ O(n-x) iterating rest of array=O(n)
        //O(1): extra space 

        //find first zero
        int j=-1;
        for(int i=0;i<a.length;i++){
            if(a[i]==0){
                //zero found
                j=i;
                break;
            }
        }
        if(j==-1){
            System.out.println("No zeroes in array");
            return;
        }

        for(int i=j+1;i<a.length;i++){
            if(a[i]!=0){
                //swap
                int temp=a[i];
                a[i]=a[j];
                a[j]=temp;

                //once swapped, move j to point the swapped 0
                j++;
            }
        }

        System.out.println("Array after shifting zeroes: ");
        for(int num:a){
            System.out.print(num);
        }
    }

    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        int a[]=new int[7];
        System.out.println("Enter array:");
        for(int i=0;i<7;i++){
            a[i]=sc.nextInt();
        }

        //run them individually, array gets changed after each method execution
        // bruteForceSolution(a);
        mostOptimalSolution(a);
    }
}
