package placements;
import java.util.Scanner;
public class NoOfPlacements {
    Scanner s=new Scanner(System.in);
    public int year,place;
    public void readdata(){
        System.out.println("enter year:");
        year=s.nextInt();
        System.out.println("enter placements:");
        place=s.nextInt();
        return;
    }
}
