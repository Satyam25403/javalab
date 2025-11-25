import java.util.ArrayList;
import java.util.Scanner;

public class IntersectionOfSortedArrays {
    static void bruteForceSolution(int a[],int b[]){
        //for each element in one array, find whether same element exits in other(traverse other)...to keep track of already taken elements use a visited array[] for second array
        //Time complexity:O(n1*n2)
        //Extra space:O(max{n1,n2}) for visited array
        
        int n1=a.length,n2=b.length;
        int[] visited=new int[n2];              //all elements are 0 by default...use Arrays.fill(visited,0) for doing it explicitly
        ArrayList<Integer> list = new ArrayList<>();

        for(int i=0;i<n1;i++){
            for(int j=0;j<n2;j++){ 
                //corresponding element found in second array and is not present in curresnt result set     
                if(a[i]==b[j] && visited[j]==0){
                    list.add(a[i]);
                    visited[j]=1;
                    break;
                }if(b[j]>a[i]){
                    break;              //we will never find same element beyond this in this iteration because array is sorted
                }
            }                 
        }
        
        System.out.println("Intersection: ");
        for(int num:list){
            System.out.print(num);
        }
    }
    static void levelOneOptimization(int a[],int b[]){
        //two pointers, one on each traversing the arrays either of the pointers move each time until one array gets exhausted: O(n1+n2)
        //O(min(n1,n2)) extra space for returning the answer; not for arriving to the solution
        
        int n1=a.length,n2=b.length,i=0,j=0;

        //we are using this because we dont know size of intersection array
        ArrayList<Integer> list = new ArrayList<>();

        while(i<n1 && j<n2){
            //we will take smaller among them but that element should not be in our arraylist already
            if(a[i]<b[j]){
                //no element in a was found to be matching pair for that of b at which ptrs are currently pointing
                i++;
            }else if (b[j] < a[i]){
                //no element in b was found to be matching pair for that of a at which ptrs are currently pointing
                j++;
            }else{ 
                // both equal case(match found): we need to take it into result array even if another match is found(duplicate)
                list.add(a[i]);    
                i++; j++;
            }
        }

        // if one of the arrays exhausted, no need to do anything for the other array, they dont get included in the intersection anyway
        
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
