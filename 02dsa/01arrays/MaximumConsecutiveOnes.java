import java.util.Scanner;
//given is an array of 0's and 1's
public class MaximumConsecutiveOnes {
    static void mostOptimalSolution(int a[]){
        //two vars: count(keeping track of successive 1's), max(keeping track of max value attained by count variable till now)
        int countOfConsecutiveOnes=0,maxConsecutiveOnesTillNow=0;
        for(int i=0;i<a.length;i++){
            if(a[i]==1){
                countOfConsecutiveOnes++;
                maxConsecutiveOnesTillNow=Math.max(maxConsecutiveOnesTillNow,countOfConsecutiveOnes);
            }else{
                //if 0 encountered, reset count of continuous 1's
                countOfConsecutiveOnes=0;
            }
        }
        System.out.println("Max consecutive 1's: "+maxConsecutiveOnesTillNow);
    }
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        int a[]=new int[6];
        System.out.println("Enter binary array:");
        for(int i=0;i<6;i++){
            a[i]=sc.nextInt();
        }

        mostOptimalSolution(a);
    }
}
