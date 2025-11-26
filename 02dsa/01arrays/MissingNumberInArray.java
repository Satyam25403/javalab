import java.util.Scanner;

//array need not to be in sorted order

public class MissingNumberInArray {
    static void bruteForceSolution(int a[], int k){
        //for each number in range, do linear search on array: O(n^2)
        //O(1): extra space
        for(int i=1;i<=k;i++){
            int flag=0;
            for(int j=0;j<a.length;j++){
                if(a[j]==i){
                    //this number exists, search for next number
                    flag=1;
                    break;
                }
            }
            if(flag==0){
                //means flag wasnt changed, i.e. that number wasnt found even after looking whole array
                System.out.println("Missing number: "+i);
                return;
            }
        }
    }
    static void levelOneOptimization(int a[],int k){
        //hashing: marking hasharray if element present as 1 and as 0 if not: O(2n)one for marking hasharray, other for finding 0 in hasharray
        //O(n): extra space complexity for hasharray

        int[] hashArray=new int[k+1];       //we need 1...k positions for marking the range ...so declare k+1 size to get max_index as k

        //marking hasharray
        for(int i=0;i<a.length;i++){
            hashArray[a[i]]=1;
        }
        //searching for unmarked index
        for(int i=1;i<hashArray.length;i++){
            if(hashArray[i]==0){
                System.out.println("Missing Number: "+i);
            }
        }
    }
    static void optimalSolutionBySummation(int a[],int k){
        //sum of k naturals-sum of elements in array: O(n) time complexity and O(1) space complexity

        int sum=0;
        for(int i=0;i<a.length;i++){
            sum+=a[i];
        }

        int sumOfKNaturals=(k*(k+1))/2;
        System.out.println("Missing number: "+(sumOfKNaturals-sum));
    }
    static void optimalSolutionByXOR(int a[],int k){
        //XOR-ing all array elements with all naturals in the range: O(n)
        //XOR-ing a number with itself=0 and, 0 XOR any-number = number-itself; XOR-ing a group of numbers never exceeds the greatest number in the group

        int xorResult=0;
        for(int i=0;i<a.length;i++){
            xorResult^=a[i]^(i+1);                  //natural no's start from 1
        }
        xorResult^=k;           //we need to do this explicitly because natural numbers in given range have one element extra than the array elements
        System.out.println("Missing number: "+xorResult);
    }
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        int a[]=new int[7];
        System.out.println("Enter array:");
        for(int i=0;i<7;i++){
            a[i]=sc.nextInt();
        }
        System.out.println("Enter (k) value:");
        int k=sc.nextInt();

        bruteForceSolution(a,k);
        levelOneOptimization(a,k);
        optimalSolutionBySummation(a, k);
        optimalSolutionByXOR(a, k);
    }
}
