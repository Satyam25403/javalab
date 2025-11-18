import placements.*;
import java.util.Scanner;
class Placementstest{
    public static void main(String args[]){
        Scanner s=new Scanner(System.in);
        int noOfyrs,sum=0;
        System.out.println("enter number of years:");
        noOfyrs=s.nextInt();
        NoOfPlacements[] np=new NoOfPlacements[noOfyrs];
        for(int i=0;i<np.length;i++){
            np[i]=new NoOfPlacements();
            np[i].readdata();
            sum+=np[i].place;
        }
         System.out.println("Total is:"+sum);
         s.close();
    }
}