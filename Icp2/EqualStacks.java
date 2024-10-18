import java.util.*;
import java.util.stream.*;
public class EqualStacks {
    public static int equalStack(int[] a,int[] b,int[] c){
        if(a.length==0||b.length==0||c.length==0){
            return 0;
        }
        int s1=Arrays.stream(a).sum();
        int s2=Arrays.stream(b).sum();
        int s3=Arrays.stream(c).sum();
        int i=0,j=0,k=0;
        while(!(s1==s2 && s2==s3)){
            if(s1>=s2 && s1>=s3){
                s1-=a[i];i++;
            }
            else if(s2>=s1 && s2>=s3){
                s2-=b[j];j++;
            }
            else{
                s3-=c[k];k++;
            }
        }
        return s1;
    }
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter sizes of three stacks");
        int n1=sc.nextInt();
        int n2=sc.nextInt();
        int n3=sc.nextInt();
        int[] a=new int[n1];int[] b=new int[n2];int[] c=new int[n3];
        System.out.println("enter 1st array:");
        for(int i=0;i<n1;i++){
            a[i]=sc.nextInt();
        }
        System.out.println("enter 2nd array:");
        for(int i=0;i<n2;i++){
            b[i]=sc.nextInt();
        }
        System.out.println("enter 3rd array:");
        for(int i=0;i<n3;i++){
            c[i]=sc.nextInt();
        }
        System.out.println("Max size of stack is: "+equalStack(a, b, c));
    }
}
