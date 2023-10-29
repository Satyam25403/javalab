import java.util.*;
public class Frequency {
    public static void main(String args[]){
        Scanner a=new Scanner(System.in);
        int[] fr=new int[]{1,1,2,3,4,5,6,4};
        while(true){
            boolean p=a.nextBoolean();
            if(p==false)
                break;
            else{
                System.out.println("enter the number to count frequency:");
                int n=a.nextInt();
                int count=0;
                for(int i=0;i<fr.length;i++){
                    if(n==fr[i])
                        count++;
                }
                System.out.println("\n"+n+" frequency "+count);
            }
        }
        a.close();
    }
}
