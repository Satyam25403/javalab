import java.util.Scanner;
public class HeightOfTreeRepresentedByParentArray {
    public static int findHeight(int[] A){
        int n=A.length;
        int[] depths=new int[n];
        for(int i=0;i<n;i++){
            depths[i]=-1;
        }
        //to calculate depth of a node i:also parallelly find max depth
        int height=0;
        for(int i=0;i<n;i++){
            findDepth(A,depths,i);

            if(depths[i]>height){
                height=depths[i];
            }
        }
        return height;
    }
    public static int findDepth(int[] A,int[] depths,int i){
        if(depths[i]!=-1){
            return depths[i];
        }
        if(A[i]==-1){
            depths[i]=0;
        }else{
            depths[i]=1+findDepth(A, depths, A[i]);
        }
        return depths[i];
    }
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("enter number:");
        int n=sc.nextInt();
        int[] parent=new int[n];
        System.out.println("enter parent array:");
        for(int i=0;i<n;i++){
            parent[i]=sc.nextInt();
        }
        System.out.println(findHeight(parent));
    }
}
